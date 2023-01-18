/*
 * Console Minecraft Launcher
 * Copyright (C) 2021-2023  MrShiehX <3553413882@qq.com>
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
import com.mrshiehx.cmcl.exceptions.DescriptionException;
import com.mrshiehx.cmcl.exceptions.MissingElementException;
import com.mrshiehx.cmcl.functions.mod.ModpackFunction;
import com.mrshiehx.cmcl.interfaces.Void;
import com.mrshiehx.cmcl.modules.extra.fabric.FabricMerger;
import com.mrshiehx.cmcl.modules.extra.forge.ForgeMerger;
import com.mrshiehx.cmcl.modules.version.VersionInstaller;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.console.TextProgress;
import com.mrshiehx.cmcl.utils.internet.NetworkUtils;
import com.mrshiehx.cmcl.utils.internet.ThreadsDownloader;
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

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.isEmpty;

public class CurseForgeModpackInstaller {
    public static void installCurseForgeModpack(JSONObject manifest,
                                                ZipFile zipFile,
                                                File modPackFile,
                                                File versionDir,
                                                boolean keepFile,
                                                boolean installAssets,
                                                boolean installNatives,
                                                boolean installLibraries,
                                                int threadCount) throws DescriptionException, IOException, ModpackFunction.NotValidModPackFormat {
        String overrides = NetworkUtils.addSlashIfMissing(manifest.optString("overrides"));
        zipFile.stream().forEach((Consumer<ZipEntry>) zipEntry -> {
            if (!zipEntry.getName().startsWith(overrides)) return;
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
        JSONObject minecraft = manifest.optJSONObject("minecraft");
        if (minecraft == null) {
            throw new ModpackFunction.NotValidModPackFormat(new MissingElementException("minecraft", "JSONObject").getMessage());
        }
        String minecraftVersion = minecraft.optString("version");
        if (isEmpty(minecraftVersion)) {
            throw new ModpackFunction.NotValidModPackFormat(new MissingElementException("version", "String").getMessage());
        }
        String modLoader = null;
        JSONArray modLoaders = minecraft.optJSONArray("modLoaders");
        if (modLoaders != null && modLoaders.length() > 0) {
            for (Object m : modLoaders) {
                if (m instanceof JSONObject) {
                    modLoader = ((JSONObject) m).optString("id");
                }
            }
        }
        VersionInstaller.InstallForgeOrFabricOrQuilt installForgeOrFabricOrQuilt = null;
        String modLoaderVersion;
        VersionInstaller.Merger mergerForFabric = null;
        VersionInstaller.Merger mergerForForge = null;
        if (!isEmpty(modLoader)) {
            if (modLoader.startsWith("forge-") && modLoader.length() > 6) {
                installForgeOrFabricOrQuilt = VersionInstaller.InstallForgeOrFabricOrQuilt.FORGE;
                modLoaderVersion = modLoader.substring(6);
                String finalModLoaderVersion1 = modLoaderVersion;
                mergerForForge = (minecraftVersion12, headJSONObject, minecraftJarFile, askContinue) -> {
                    Map<String, JSONObject> forges;

                    try {
                        forges = ForgeMerger.getForges(minecraftVersion12);
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
                        return ForgeMerger.installInternal(forge, headJSONObject, minecraftVersion12, minecraftJarFile);
                    } catch (Exception e) {
                        System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e.getMessage()));
                        FileUtils.deleteDirectory(versionDir);
                        return new Pair<>(false, null);
                    }
                };
            } else if (modLoader.startsWith("fabric-") && modLoader.length() > 7) {
                installForgeOrFabricOrQuilt = VersionInstaller.InstallForgeOrFabricOrQuilt.FABRIC;
                modLoaderVersion = modLoader.substring(7);
                String finalModLoaderVersion = modLoaderVersion;
                mergerForFabric = (minecraftVersion1, headJSONObject, minecraftJarFile, askContinue) -> {
                    try {
                        return new FabricMerger().installInternal(minecraftVersion1, finalModLoaderVersion, headJSONObject);
                    } catch (Exception e) {
                        System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e.getMessage()));
                        FileUtils.deleteDirectory(versionDir);
                        return new Pair<>(false, null);
                    }
                };
            }
        }

        Void onFinished = () -> {
            JSONArray filesArray = manifest.optJSONArray("files");
            List<Pair<String, File>> files = new LinkedList<>();
            if (filesArray != null && filesArray.length() > 0) {
                System.out.print(getString("INSTALL_MODPACK_EACH_MOD_GET_URL"));
                TextProgress textProgress = new TextProgress();
                textProgress.setMaximum(filesArray.length());
                for (int i = 0; i < filesArray.length(); i++) {
                    Object o = filesArray.get(i);
                    if (!(o instanceof JSONObject)) continue;
                    JSONObject file = (JSONObject) o;
                    int fileID = file.optInt("fileID");
                    int projectID = file.optInt("projectID");
                    if (fileID == 0 || projectID == 0) continue;
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
                FileUtils.writeFile(new File(versionDir, "modpack.json"), manifest.toString(2), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Void onDownloaded = () -> {
                if (!keepFile) {
                    modPackFile.delete();
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
        File versionsFile = Utils.downloadVersionsFile();
        JSONArray versions = new JSONObject(FileUtils.readFileContent(versionsFile)).optJSONArray("versions");
        VersionInstaller.start(
                minecraftVersion,
                versionDir.getName(),
                versions,
                installAssets,
                installNatives,
                installLibraries,
                installForgeOrFabricOrQuilt,
                threadCount,
                mergerForFabric,
                mergerForForge,
                null,
                null,
                null,
                onFinished, null, null);


    }


    public static int tryToInstallCurseForgeModpack(ZipFile zipFile, File file, File versionDir, boolean keepFile, boolean installAssets, boolean installNatives, boolean installLibraries, int threadCount) throws ModpackFunction.NotValidModPackFormat {
        ZipEntry entry = zipFile.getEntry("manifest.json");
        //FileUtils.inputStream2File(zipFile.getInputStream(entry),new File(versionDir,"modpack.json"));
        if (entry == null)
            throw new ModpackFunction.NotValidModPackFormat("not a CurseForge modpack");
        try {
            InputStream i = zipFile.getInputStream(entry);
            JSONObject manifest = new XJSONObject(Utils.inputStream2String(i));
            installCurseForgeModpack(manifest, zipFile, file, versionDir, keepFile, installAssets, installNatives, installLibraries, threadCount);
        } catch (ModpackFunction.NotValidModPackFormat e) {
            FileUtils.deleteDirectory(versionDir);
            throw e;
        } catch (Exception e) {
            System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e));
            FileUtils.deleteDirectory(versionDir);
        }
        return 0;
    }
}
