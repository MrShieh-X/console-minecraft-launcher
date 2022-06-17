/*
 * Console Minecraft Launcher
 * Copyright (C) 2021-2022  MrShiehX <3553413882@qq.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.mrshiehx.cmcl.modules.modpack;

import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.interfaces.Void;
import com.mrshiehx.cmcl.modules.extra.fabric.FabricMerger;
import com.mrshiehx.cmcl.modules.extra.forge.ForgeMerger;
import com.mrshiehx.cmcl.modules.extra.liteloader.LiteloaderMerger;
import com.mrshiehx.cmcl.modules.extra.quilt.QuiltMerger;
import com.mrshiehx.cmcl.modules.version.VersionInstaller;
import com.mrshiehx.cmcl.options.ModpackOption;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.json.XJSONObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.isEmpty;

public class MultiMCModpackInstaller {
    public static int tryToInstallMultiMCModpack(ZipFile zipFile, File file, File versionDir, boolean keepFile, boolean installAssets, boolean installNatives, boolean installLibraries, int threadCount) throws ModpackOption.NotValidModPackFormat {
        String instanceFileName = "instance.cfg";

        ZipEntry instanceCFGEntry;
        String modpackName = null;
        if ((instanceCFGEntry = zipFile.getEntry(instanceFileName)) != null)
            modpackName = "";
        else {
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                Object entry = entries.nextElement();
                if (entry instanceof ZipEntry) {
                    ZipEntry a = ((ZipEntry) entry);
                    String entryName = a.getName();

                    int idx = entryName.indexOf('/');
                    if (idx >= 0 && entryName.length() == idx + instanceFileName.length() + 1 && entryName.startsWith(instanceFileName, idx + 1)) {
                        modpackName = entryName.substring(0, idx + 1);
                        instanceCFGEntry = a;
                        break;
                    }
                }
            }
        }
        if (modpackName == null)
            throw new ModpackOption.NotValidModPackFormat("not a MultiMC modpack");

        ZipEntry mmcPack = zipFile.getEntry(modpackName + "mmc-pack.json");
        if (mmcPack == null)
            throw new ModpackOption.NotValidModPackFormat("not a MultiMC modpack");

        JSONObject mmcPackJSON;
        try {
            mmcPackJSON = new JSONObject(Utils.inputStream2String(zipFile.getInputStream(mmcPack)));
        } catch (Exception e) {
            /*System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e));
            Utils.deleteDirectory(versionDir);
            return -1;*/
            throw new ModpackOption.NotValidModPackFormat(e.getMessage());
        }
        Properties instanceCFG = new Properties();
        try {
            instanceCFG.load(new InputStreamReader(zipFile.getInputStream(instanceCFGEntry), StandardCharsets.UTF_8));
        } catch (Exception e) {
            /*System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e));
            Utils.deleteDirectory(versionDir);
            return -1;*/
        }

        JSONArray components = mmcPackJSON.optJSONArray("components");

        String gameVersion = null;
        String forgeVersion = null;
        String liteloaderVersion = null;
        String fabricVersion = null;
        String quiltVersion = null;

        if (components != null) {
            for (Object o : components) {
                if (o instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) o;
                    String version = jsonObject.optString("version");
                    if (!isEmpty(version)) {
                        switch (jsonObject.optString("uid")) {
                            case "net.minecraft":
                                gameVersion = version;
                                break;
                            case "net.minecraftforge":
                                forgeVersion = version;
                                break;
                            case "com.mumfrey.liteloader":
                                liteloaderVersion = version;
                                break;
                            case "net.fabricmc.fabric-loader":
                                fabricVersion = version;
                                break;
                            case "org.quiltmc.quilt-loader":
                                quiltVersion = version;
                                break;
                        }
                    }
                }
            }
        }

        if (isEmpty(gameVersion)) {
            /*System.out.println(getString("MESSAGE_INSTALL_MODPACK_NOT_FOUND_GAME_VERSION"));
            return -1;*/
            throw new ModpackOption.NotValidModPackFormat(getString("MESSAGE_INSTALL_MODPACK_NOT_FOUND_GAME_VERSION"));
        }
        if (!isEmpty(forgeVersion) && !isEmpty(fabricVersion)) {
            System.out.println(getString("MESSAGE_INSTALL_MODPACK_COEXIST", "Forge", "Fabric"));
            return -1;
        }
        if (!isEmpty(liteloaderVersion) && !isEmpty(fabricVersion)) {
            System.out.println(getString("MESSAGE_INSTALL_MODPACK_COEXIST", "LiteLoader", "Fabric"));
            return -1;
        }

        //Quilt
        if (!isEmpty(forgeVersion) && !isEmpty(quiltVersion)) {
            System.out.println(getString("MESSAGE_INSTALL_MODPACK_COEXIST", "Forge", "Quilt"));
            return -1;
        }
        if (!isEmpty(liteloaderVersion) && !isEmpty(quiltVersion)) {
            System.out.println(getString("MESSAGE_INSTALL_MODPACK_COEXIST", "LiteLoader", "Quilt"));
            return -1;
        }
        if (!isEmpty(fabricVersion) && !isEmpty(quiltVersion)) {
            System.out.println(getString("MESSAGE_INSTALL_MODPACK_COEXIST", "Fabric", "Quilt"));
            return -1;
        }


        String finalModpackName = modpackName;
        zipFile.stream().forEach((Consumer<ZipEntry>) zipEntry -> {
            int start;
            if (zipEntry.getName().startsWith(finalModpackName + ".minecraft/")) {
                start = (finalModpackName + ".minecraft/").length();
            } else if (zipEntry.getName().startsWith(finalModpackName + "minecraft/")) {
                start = (finalModpackName + "minecraft/").length();
            } else {
                return;
            }
            File to = new File(versionDir, zipEntry.getName().substring(start));
            if (zipEntry.isDirectory()) {
                to.mkdirs();
            } else {
                try {
                    FileUtils.inputStream2File(zipFile.getInputStream(zipEntry), to);
                } catch (IOException e) {
                    System.out.println(getString("MESSAGE_FAILED_TO_DECOMPRESS_FILE", zipEntry.getName(), e));
                }
            }
        });


        VersionInstaller.InstallForgeOrFabric installForgeOrFabric = null;
        VersionInstaller.Merger mergerForFabric = null;
        VersionInstaller.Merger mergerForForge = null;
        VersionInstaller.Merger mergerForQuilt = null;
        VersionInstaller.Merger mergerForLiteLoader = null;
        if (!isEmpty(forgeVersion)) {
            installForgeOrFabric = VersionInstaller.InstallForgeOrFabric.FORGE;
            String finalForgeVersion = forgeVersion;
            mergerForForge = (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                Map<String, JSONObject> forges;

                try {
                    forges = ForgeMerger.getForges(minecraftVersion);
                } catch (Exception e) {
                    System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e.getMessage()));
                    Utils.deleteDirectory(versionDir);
                    return new Pair<>(false, null);
                }
                JSONObject forge = forges.get(finalForgeVersion);
                if (forge == null) {
                    System.out.println(getString("EXCEPTION_INSTALL_MODPACK", getString("INSTALL_MODLOADER_FAILED_NOT_FOUND_TARGET_VERSION", finalForgeVersion).replace("${NAME}", "Forge")));
                    Utils.deleteDirectory(versionDir);
                    return new Pair<>(false, null);
                }
                try {
                    //System.out.println(getString("MESSAGE_START_INSTALLING_FORGE"));
                    return ForgeMerger.installInternal(forge, headJSONObject, minecraftVersion, minecraftJarFile);
                } catch (Exception e) {
                    System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e.getMessage()));
                    Utils.deleteDirectory(versionDir);
                    return new Pair<>(false, null);
                }
            };
        } else if (!isEmpty(fabricVersion)) {
            installForgeOrFabric = VersionInstaller.InstallForgeOrFabric.FABRIC;
            String finalModLoaderVersion = fabricVersion;
            mergerForFabric = (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                try {
                    return new FabricMerger().installInternal(minecraftVersion, finalModLoaderVersion, headJSONObject);
                } catch (Exception e) {
                    System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e.getMessage()));
                    Utils.deleteDirectory(versionDir);
                    return new Pair<>(false, null);
                }
            };
        } else if (!isEmpty(quiltVersion)) {
            installForgeOrFabric = VersionInstaller.InstallForgeOrFabric.QUILT;
            String finalModLoaderVersion = quiltVersion;
            mergerForQuilt = (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                try {
                    return new QuiltMerger().installInternal(minecraftVersion, finalModLoaderVersion, headJSONObject);
                } catch (Exception e) {
                    System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e.getMessage()));
                    Utils.deleteDirectory(versionDir);
                    return new Pair<>(false, null);
                }
            };
        }
        if (!isEmpty(liteloaderVersion)) {
            String finalModLoaderVersion = liteloaderVersion;
            mergerForLiteLoader = (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                try {
                    return LiteloaderMerger.installInternal(minecraftVersion, finalModLoaderVersion, headJSONObject);
                } catch (Exception e) {
                    System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e.getMessage()));
                    Utils.deleteDirectory(versionDir);
                    return new Pair<>(false, null);
                }
            };
        }

        Void onFinished = () -> {
            try {
                Utils.writeFile(new File(versionDir, "modpack.json"), new XJSONObject().put("type", "MultiMC").put("mmcPack", mmcPackJSON).put("instanceConfig", instanceCFG).toString(2), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!keepFile) {
                Utils.close(zipFile);
                file.delete();
            }
            System.out.println(getString("INSTALL_MODPACK_COMPLETE"));
        };

        try {
            File versionsFile = Utils.downloadVersionsFile();
            JSONArray versions = new JSONObject(Utils.readFileContent(versionsFile)).optJSONArray("versions");
            VersionInstaller.start(
                    gameVersion,
                    versionDir.getName(),
                    versions,
                    installAssets,
                    installNatives,
                    installLibraries,
                    installForgeOrFabric,
                    threadCount,
                    mergerForFabric,
                    mergerForForge,
                    mergerForQuilt,
                    mergerForLiteLoader,
                    null,
                    onFinished, null, null);
        } catch (Exception e) {
            System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e));
            Utils.deleteDirectory(versionDir);
            return -1;
        }
        return 0;
    }
}
