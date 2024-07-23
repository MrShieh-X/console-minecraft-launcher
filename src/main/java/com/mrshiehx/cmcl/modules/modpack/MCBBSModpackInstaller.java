/*
 * Console Minecraft Launcher
 * Copyright (C) 2021-2024  MrShiehX
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
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.exceptions.MissingElementException;
import com.mrshiehx.cmcl.functions.mod.ModpackFunction;
import com.mrshiehx.cmcl.interfaces.Void;
import com.mrshiehx.cmcl.modules.extra.fabric.FabricMerger;
import com.mrshiehx.cmcl.modules.extra.forge.ForgeMerger;
import com.mrshiehx.cmcl.modules.extra.liteloader.LiteloaderMerger;
import com.mrshiehx.cmcl.modules.extra.optifine.OptiFineMerger;
import com.mrshiehx.cmcl.modules.extra.quilt.QuiltMerger;
import com.mrshiehx.cmcl.modules.version.VersionInstaller;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.console.TextProgress;
import com.mrshiehx.cmcl.utils.internet.NetworkUtils;
import com.mrshiehx.cmcl.utils.internet.ThreadsDownloader;
import com.mrshiehx.cmcl.utils.json.JSONUtils;
import com.mrshiehx.cmcl.utils.json.XJSONObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.isEmpty;

public class MCBBSModpackInstaller {
    public static int tryToInstallMCBBSModpack(ZipFile zipFile, File modpackFile, File versionDir, boolean keepFile, boolean installAssets, boolean installNatives, boolean installLibraries, int threadCount) throws ModpackFunction.NotValidModPackFormat {
        ZipEntry entry = zipFile.getEntry("mcbbs.packmeta");
        //FileUtils.inputStream2File(zipFile.getInputStream(entry),new File(versionDir,"modpack.json"));
        if (entry == null) {
            entry = zipFile.getEntry("manifest.json");
            if (entry == null)
                throw new ModpackFunction.NotValidModPackFormat("not a MCBBS modpack");
        }

        JSONObject mcbbsPackMeta;
        try {
            InputStream i = zipFile.getInputStream(entry);
            mcbbsPackMeta = new XJSONObject(Utils.inputStream2String(i));
        } catch (Exception e) {
            if (Constants.isDebug()) e.printStackTrace();
            throw new ModpackFunction.NotValidModPackFormat(e.getMessage());
        }

        JSONArray addonsJsonArray = mcbbsPackMeta.optJSONArray("addons");
        if (addonsJsonArray == null) {
            throw new ModpackFunction.NotValidModPackFormat(new MissingElementException("addons", "JSONArray").getMessage());
        }
        List<JSONObject> addons = JSONUtils.jsonArrayToJSONObjectList(addonsJsonArray);

        String gameVersion = null;
        String forgeVersion = null;
        String liteloaderVersion = null;
        String fabricVersion = null;
        String quiltVersion = null;//not sure
        String optifineVersion = null;
        String neoforgeVersion = null;
        for (JSONObject addon : addons) {
            String id = addon.optString("id");
            String version = addon.optString("version");
            switch (id) {
                case "game":
                    gameVersion = version;
                    break;
                case "forge":
                    forgeVersion = version;
                    break;
                case "liteloader":
                    liteloaderVersion = version;
                    break;
                case "fabric":
                    fabricVersion = version;
                    break;
                case "quilt":
                    quiltVersion = version;
                    break;
                case "optifine":
                    optifineVersion = version;
                    break;
                case "neoforge":
                    neoforgeVersion = version;
                    break;
            }
        }

        if (isEmpty(gameVersion)) {
            throw new ModpackFunction.NotValidModPackFormat(getString("MESSAGE_INSTALL_MODPACK_NOT_FOUND_GAME_VERSION"));
        }
        if (!isEmpty(neoforgeVersion)) {
            System.out.println(getString("MESSAGE_INSTALL_MODPACK_NOT_SUPPORTED_NEOFORGE"));
            return -1;
        }
        if (!isEmpty(forgeVersion) && !isEmpty(fabricVersion)) {
            System.out.println(getString("MESSAGE_INSTALL_MODPACK_COEXIST", "Forge", "Fabric"));
            return -1;
        }
        if (!isEmpty(liteloaderVersion) && !isEmpty(fabricVersion)) {
            System.out.println(getString("MESSAGE_INSTALL_MODPACK_COEXIST", "LiteLoader", "Fabric"));
            return -1;
        }
        if (!isEmpty(optifineVersion) && !isEmpty(fabricVersion)) {
            System.out.println(getString("MESSAGE_INSTALL_MODPACK_COEXIST", "OptiFine", "Fabric"));
            return -1;
        }
        if (!isEmpty(forgeVersion) && !isEmpty(quiltVersion)) {
            System.out.println(getString("MESSAGE_INSTALL_MODPACK_COEXIST", "Forge", "Quilt"));
            return -1;
        }
        if (!isEmpty(liteloaderVersion) && !isEmpty(quiltVersion)) {
            System.out.println(getString("MESSAGE_INSTALL_MODPACK_COEXIST", "LiteLoader", "Quilt"));
            return -1;
        }
        if (!isEmpty(optifineVersion) && !isEmpty(quiltVersion)) {
            System.out.println(getString("MESSAGE_INSTALL_MODPACK_COEXIST", "OptiFine", "Quilt"));
            return -1;
        }
        if (!isEmpty(fabricVersion) && !isEmpty(quiltVersion)) {
            System.out.println(getString("MESSAGE_INSTALL_MODPACK_COEXIST", "Fabric", "Quilt"));
            return -1;
        }


        VersionInstaller.InstallForgeOrFabricOrQuilt installForgeOrFabricOrQuilt = null;
        String modLoaderVersion;
        VersionInstaller.Merger mergerForFabric = null;
        VersionInstaller.Merger mergerForForge = null;
        VersionInstaller.Merger mergerForQuilt = null;
        VersionInstaller.Merger mergerForLiteLoader = null;
        VersionInstaller.Merger mergerForOptiFine = null;
        if (!isEmpty(forgeVersion)) {
            installForgeOrFabricOrQuilt = VersionInstaller.InstallForgeOrFabricOrQuilt.FORGE;
            modLoaderVersion = forgeVersion;
            String finalModLoaderVersion1 = modLoaderVersion;
            mergerForForge = (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                Map<String, JSONObject> forges;

                try {
                    forges = ForgeMerger.getInstallableForges(minecraftVersion);
                } catch (Exception e) {
                    System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e.getMessage()));
                    FileUtils.deleteDirectory(versionDir);
                    return new Pair<>(false, null);
                }
                JSONObject forge = forges.get(finalModLoaderVersion1);
                if (forge == null) {
                    System.out.println(getString("EXCEPTION_INSTALL_MODPACK", getString("INSTALL_MODLOADER_FAILED_NOT_FOUND_TARGET_VERSION", finalModLoaderVersion1).replace("${NAME}", "Forge")));
                    FileUtils.deleteDirectory(versionDir);
                    return new Pair<>(false, null);
                }
                try {
                    //System.out.println(getString("MESSAGE_START_INSTALLING_FORGE"));
                    return ForgeMerger.installInternal(forge, headJSONObject, minecraftVersion, minecraftJarFile);
                } catch (Exception e) {
                    System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e.getMessage()));
                    FileUtils.deleteDirectory(versionDir);
                    return new Pair<>(false, null);
                }
            };
        } else if (!isEmpty(fabricVersion)) {
            installForgeOrFabricOrQuilt = VersionInstaller.InstallForgeOrFabricOrQuilt.FABRIC;
            modLoaderVersion = fabricVersion;
            String finalModLoaderVersion = modLoaderVersion;
            mergerForFabric = (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                try {
                    return new FabricMerger().installInternal(minecraftVersion, finalModLoaderVersion, headJSONObject);
                } catch (Exception e) {
                    System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e.getMessage()));
                    FileUtils.deleteDirectory(versionDir);
                    return new Pair<>(false, null);
                }
            };
        } else if (!isEmpty(quiltVersion)) {
            installForgeOrFabricOrQuilt = VersionInstaller.InstallForgeOrFabricOrQuilt.QUILT;
            modLoaderVersion = quiltVersion;
            String finalModLoaderVersion = modLoaderVersion;
            mergerForQuilt = (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                try {
                    return new QuiltMerger().installInternal(minecraftVersion, finalModLoaderVersion, headJSONObject);
                } catch (Exception e) {
                    System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e.getMessage()));
                    FileUtils.deleteDirectory(versionDir);
                    return new Pair<>(false, null);
                }
            };
        }

        if (!isEmpty(liteloaderVersion)) {
            modLoaderVersion = liteloaderVersion;
            String finalModLoaderVersion = modLoaderVersion;
            mergerForLiteLoader = (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                try {
                    return LiteloaderMerger.installInternal(minecraftVersion, finalModLoaderVersion, headJSONObject);
                } catch (Exception e) {
                    System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e.getMessage()));
                    FileUtils.deleteDirectory(versionDir);
                    return new Pair<>(false, null);
                }
            };
        }

        if (!isEmpty(optifineVersion)) {
            modLoaderVersion = optifineVersion;
            String finalModLoaderVersion = modLoaderVersion;
            mergerForOptiFine = (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                try {
                    return OptiFineMerger.installInternal(minecraftVersion, finalModLoaderVersion, headJSONObject, minecraftJarFile);
                } catch (Exception e) {
                    System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e.getMessage()));
                    FileUtils.deleteDirectory(versionDir);
                    return new Pair<>(false, null);
                }
            };
        }

        zipFile.stream().forEach((Consumer<ZipEntry>) (zipEntry -> {
            String overrides = "overrides";
            if (!zipEntry.getName().startsWith(overrides)) return;
            File to = new File(versionDir, zipEntry.getName().substring(9));
            if (zipEntry.isDirectory()) {
                to.mkdirs();
            } else {
                try {
                    FileUtils.inputStream2File(zipFile.getInputStream(zipEntry), to);
                } catch (IOException e) {
                    if (Constants.isDebug()) e.printStackTrace();
                    System.out.println(getString("MESSAGE_FAILED_TO_DECOMPRESS_FILE", zipEntry.getName(), e));
                }
            }
        }));


        JSONArray librariesToBeMerged = mcbbsPackMeta.optJSONArray("libraries");
        Void onFinished = () -> {

            /*from CurseForgeModpackInstaller*/
            JSONArray filesArray = mcbbsPackMeta.optJSONArray("files");
            List<Pair<String, File>> files = new LinkedList<>();
            if (filesArray != null && filesArray.length() > 0) {
                System.out.print(getString("INSTALL_MODPACK_EACH_MOD_GET_URL"));
                TextProgress textProgress = new TextProgress();
                textProgress.setMaximum(filesArray.length());
                for (int i = 0; i < filesArray.length(); i++) {
                    Object o = filesArray.get(i);
                    if (!(o instanceof JSONObject)) continue;
                    JSONObject file = (JSONObject) o;
                    int fileID = file.optInt("fileID", -1);
                    int projectID = file.optInt("projectID", -1);
                    if (fileID == -1 || projectID == -1) {
                        textProgress.setValue(i + 1);
                        continue;
                    }
                    try {
                        String fileName;
                        String fileDownloadUrl;
                        try {
                            String url = "https://api.curseforge.com/v1/mods/" + projectID + "/files/" + fileID;
                            JSONObject jsonObject = new JSONObject(NetworkUtils.curseForgeGet(url)).optJSONObject("data");
                            fileName = jsonObject.optString("fileName");
                            fileDownloadUrl = jsonObject.optString("downloadUrl");
                            if (isEmpty(fileDownloadUrl)) {
                                fileDownloadUrl = String.format("https://edge.forgecdn.net/files/%d/%d/%s", fileID / 1000, fileID % 1000, fileName);
                            }
                        } catch (Exception e2) {
                            System.out.println(getString("INSTALL_MODPACK_FAILED_DOWNLOAD_MOD", projectID, e2));
                            continue;
                        }
                        file.put("fileName", fileName);
                        file.put("url", fileDownloadUrl);
                        files.add(new Pair<>(fileDownloadUrl, new File(versionDir, "mods/" + fileName)));
                        textProgress.setValue(i + 1);

                    } catch (Exception e) {
                        System.out.println(getString("INSTALL_MODPACK_FAILED_DOWNLOAD_MOD", projectID, e));
                    }
                }

                textProgress.setValue(filesArray.length());
            }
            try {
                FileUtils.writeFile(new File(versionDir, "modpack.json"), mcbbsPackMeta.toString(2), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Void onDownloaded = () -> {
                if (!keepFile) {
                    modpackFile.delete();
                }
                System.out.println(getString("INSTALL_MODPACK_COMPLETE"));
            };
            Utils.close(zipFile);
            if (files.size() > 0) {
                ThreadsDownloader threadsDownloader = new ThreadsDownloader(files, onDownloaded, true);
                threadsDownloader.start();
            } else {
                onDownloaded.execute();
            }
        };
        VersionInstaller.VersionJSONMerger versionJSONMerger = headJSONObject -> {
            JSONObject launchInfo = mcbbsPackMeta.optJSONObject("launchInfo", new JSONObject());
            JSONArray launchArgument = launchInfo.optJSONArray("launchArgument");
            JSONArray javaArgument = launchInfo.optJSONArray("javaArgument");
            JSONObject arguments = headJSONObject.optJSONObject("arguments");
            if (arguments != null) {
                if (launchArgument != null && launchArgument.length() > 0) {
                    arguments.put("game", Optional.of(arguments.optJSONArray("game")).orElse(new JSONArray()).putAll(launchArgument));
                }
                if (javaArgument != null && javaArgument.length() > 0) {
                    arguments.put("jvm", Optional.of(arguments.optJSONArray("jvm")).orElse(new JSONArray()).putAll(javaArgument));
                }
            } else {
                if (launchArgument != null && launchArgument.length() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (Object o : launchArgument) {
                        sb.append(o).append(' ');
                    }
                    String minecraftArguments = headJSONObject.optString("minecraftArguments");
                    if (minecraftArguments.isEmpty()) {
                        headJSONObject.put("minecraftArguments", sb.substring(0, sb.length() - 1));
                    } else {
                        headJSONObject.put("minecraftArguments", minecraftArguments + " " + sb.substring(0, sb.length() - 1));
                    }
                }
            }
        };
        try {
            File versionsFile = Utils.downloadVersionsFile();
            JSONArray versions = new JSONObject(FileUtils.readFileContent(versionsFile)).optJSONArray("versions");
            VersionInstaller.start(
                    gameVersion,
                    versionDir.getName(),
                    versions,
                    installAssets,
                    installNatives,
                    installLibraries,
                    installForgeOrFabricOrQuilt,
                    threadCount,
                    mergerForFabric,
                    mergerForForge,
                    mergerForQuilt,
                    mergerForLiteLoader,
                    mergerForOptiFine,
                    onFinished,
                    versionJSONMerger,
                    librariesToBeMerged);
        } catch (Exception e) {
            System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e));
            FileUtils.deleteDirectory(versionDir);
            return -1;
        }
        return 0;
    }

}
