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
import com.mrshiehx.cmcl.exceptions.MissingElementException;
import com.mrshiehx.cmcl.interfaces.Void;
import com.mrshiehx.cmcl.modules.extra.fabric.FabricMerger;
import com.mrshiehx.cmcl.modules.extra.forge.ForgeMerger;
import com.mrshiehx.cmcl.modules.extra.quilt.QuiltMerger;
import com.mrshiehx.cmcl.modules.version.VersionInstaller;
import com.mrshiehx.cmcl.options.ModpackOption;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.ThreadsDownloader;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.json.XJSONObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.isEmpty;

public class ModrinthModpackInstaller {
    public static int installModrinthModpack(JSONObject manifest, ZipFile zipFile, File modPackFile, File versionDir, boolean keepFile, boolean installAssets, boolean installNatives, boolean installLibraries, int threadCount) throws Exception {
        zipFile.stream().forEach((Consumer<ZipEntry>) zipEntry -> {
            String overrides;
            if (zipEntry.getName().startsWith("overrides")) {
                overrides = "overrides";
            } else if (zipEntry.getName().startsWith("client-overrides")) {
                overrides = "client-overrides";
            } else
                return;
            File to = new File(versionDir, zipEntry.getName().substring(overrides.length()));
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
        JSONObject dependencies = manifest.optJSONObject("dependencies");
        if (dependencies == null)
            throw new ModpackOption.NotValidModPackFormat(new MissingElementException("dependencies", "JSONObject").getMessage());

        String gameVersion = null/*=dependencies.optString("minecraft")*/;
        String forgeVersion = null/*=dependencies.optString("forge")*/;
        String fabricVersion = null/*=dependencies.optString("fabric-loader")*/;
        String quiltVersion = null;

        for (Map.Entry<String, Object> entry : dependencies.toMap().entrySet()) {
            if (entry.getValue() instanceof String) {
                String key = entry.getKey();
                String value = (String) entry.getValue();
                switch (key) {
                    case "minecraft":
                        gameVersion = value;
                        break;
                    case "forge":
                        forgeVersion = value;
                        break;
                    case "fabric-loader":
                        fabricVersion = value;
                        break;
                    case "quilt-loader":
                        quiltVersion = value;
                        break;
                    default:
                        System.out.println(getString("INSTALL_MODPACK_MODRINTH_UNKNOWN_MODLOADER", key));
                        break;
                }
            }
        }

        if (isEmpty(gameVersion))
            throw new ModpackOption.NotValidModPackFormat(getString("MESSAGE_INSTALL_MODPACK_NOT_FOUND_GAME_VERSION"));


        if (!isEmpty(forgeVersion) && !isEmpty(fabricVersion)) {
            System.out.println(getString("MESSAGE_INSTALL_MODPACK_COEXIST", "Forge", "Fabric"));
            return -1;
        }

        //quilt
        if (!isEmpty(quiltVersion) && !isEmpty(fabricVersion)) {
            System.out.println(getString("MESSAGE_INSTALL_MODPACK_COEXIST", "Quilt", "Fabric"));
            return -1;
        }
        if (!isEmpty(forgeVersion) && !isEmpty(quiltVersion)) {
            System.out.println(getString("MESSAGE_INSTALL_MODPACK_COEXIST", "Forge", "Quilt"));
            return -1;
        }


        VersionInstaller.InstallForgeOrFabric installForgeOrFabric = null;
        String modLoaderVersion;
        VersionInstaller.Merger mergerForFabric = null;
        VersionInstaller.Merger mergerForForge = null;
        VersionInstaller.Merger mergerForQuilt = null;
        if (!isEmpty(forgeVersion)) {
            installForgeOrFabric = VersionInstaller.InstallForgeOrFabric.FORGE;
            modLoaderVersion = forgeVersion;
            String finalModLoaderVersion1 = modLoaderVersion;
            mergerForForge = (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                Map<String, JSONObject> forges;

                try {
                    forges = ForgeMerger.getForges(minecraftVersion);
                } catch (Exception e) {
                    System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e.getMessage()));
                    Utils.deleteDirectory(versionDir);
                    return new Pair<>(false, null);
                }
                JSONObject forge = forges.get(finalModLoaderVersion1);
                if (forge == null) {
                    System.out.println(getString("EXCEPTION_INSTALL_MODPACK", getString("INSTALL_MODLOADER_FAILED_NOT_FOUND_TARGET_VERSION", finalModLoaderVersion1).replace("${NAME}", "Forge")));
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
            modLoaderVersion = fabricVersion;
            String finalModLoaderVersion = modLoaderVersion;
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
            modLoaderVersion = quiltVersion;
            String finalModLoaderVersion = modLoaderVersion;
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

        Void onFinished = () -> {
            JSONArray files = manifest.optJSONArray("files");
            if (files != null && files.length() > 0) {
                List<Pair<String, File>> filess = new LinkedList<>();
                for (int i = 0; i < files.length(); i++) {
                    Object o = files.get(i);
                    if (o instanceof JSONObject) {
                        JSONObject file = (JSONObject) o;
                        String path = file.optString("path");
                        JSONArray downloads = file.optJSONArray("downloads");
                        String url = null;
                        if (downloads != null) {
                            for (Object download : downloads) {
                                if (download instanceof String) {
                                    url = (String) download;
                                }
                            }
                        }
                        if (isEmpty(url) || isEmpty(path)) {
                            continue;
                        }
                        filess.add(new Pair<>(url, new File(versionDir, path)));
                    }
                }

                if (filess.size() > 0) {
                    ThreadsDownloader threadsDownloader = new ThreadsDownloader(filess);
                    threadsDownloader.start();
                }
            }
            try {
                Utils.writeFile(new File(versionDir, "modpack.json"), manifest.toString(2), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!keepFile) {
                Utils.close(zipFile);
                modPackFile.delete();
            }
            System.out.println(getString("INSTALL_MODPACK_COMPLETE"));
        };
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
                null,
                null,
                onFinished, null, null);

        return 0;

    }

    public static int tryToInstallModrinthModpack(ZipFile zipFile, File file, File versionDir, boolean keepFile, boolean installAssets, boolean installNatives, boolean installLibraries, int threadCount) throws ModpackOption.NotValidModPackFormat {
        ZipEntry entry = zipFile.getEntry("modrinth.index.json");
        //FileUtils.inputStream2File(zipFile.getInputStream(entry),new File(versionDir,"modpack.json"));
        if (entry == null)
            throw new ModpackOption.NotValidModPackFormat("not a Modrinth modpack");
        try {
            InputStream i = zipFile.getInputStream(entry);
            JSONObject manifest = new XJSONObject(Utils.inputStream2String(i));
            return installModrinthModpack(manifest, zipFile, file, versionDir, keepFile, installAssets, installNatives, installLibraries, threadCount);
        } catch (ModpackOption.NotValidModPackFormat e) {
            Utils.deleteDirectory(versionDir);
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e));
            Utils.deleteDirectory(versionDir);
        }
        return 0;
    }
}
