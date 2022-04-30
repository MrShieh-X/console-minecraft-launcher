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
 */

package com.mrshiehx.cmcl.options;

import com.mrshiehx.cmcl.ConsoleMinecraftLauncher;
import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.bean.arguments.ValueArgument;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.curseforge.CFManager;
import com.mrshiehx.cmcl.curseforge.ModpackManager;
import com.mrshiehx.cmcl.exceptions.MissingElementException;
import com.mrshiehx.cmcl.interfaces.Void;
import com.mrshiehx.cmcl.interfaces.filters.StringFilter;
import com.mrshiehx.cmcl.modules.modLoaders.fabric.FabricMerger;
import com.mrshiehx.cmcl.modules.modLoaders.forge.ForgeMerger;
import com.mrshiehx.cmcl.modules.version.VersionInstaller;
import com.mrshiehx.cmcl.utils.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.*;

public class ModpackOption implements Option {
    @Override
    public void execute(Arguments arguments) {
        Argument subOption = arguments.opt(1);
        if (subOption == null) {
            System.out.println(getString("CONSOLE_GET_USAGE"));
            return;
        }
        String key = subOption.key;
        String lowerCase = key.toLowerCase();
        if (!"i".equals(lowerCase) && !"s".equals(lowerCase) && !"l".equals(lowerCase)) {
            System.out.println(getString("CONSOLE_UNKNOWN_OPTION", key));
            return;
        }

        JSONObject modpack;
        CFManager modManager = new ModpackManager();
        if ((subOption instanceof ValueArgument) && ("i".equals(subOption.key) || "s".equals(subOption.key))) {

            ValueArgument valueArgument = (ValueArgument) subOption;

            String value = valueArgument.value;


            //mod=null;
            modpack = modManager.search(value);
            //mod = selectMod(value);
            if (modpack == null) return;

        } else if (arguments.optInt("c", Integer.MIN_VALUE) != Integer.MIN_VALUE) {
            int modpackId = arguments.optInt("c");
            try {
                modpack = modManager.getByID(String.valueOf(modpackId));
            } catch (CFManager.NotMinecraftAddon e) {
                System.out.println(getString("CF_GET_BY_ID_NOT_OF_MC", e.gameId).replace("${NAME}", getString("CF_BESEARCHED_MODPACK_ALC")));
                return;
            } catch (CFManager.IncorrectCategoryAddon e) {
                System.out.println(getString("CF_GET_BY_ID_INCORRECT_CATEGORY", e.gameCategoryId).replace("${NAME}", getString("CF_BESEARCHED_MODPACK_ALC")));
                return;
            } catch (Exception e) {
                System.out.println(getString("CF_GET_BY_ID_FAILED", e).replace("${NAME}", getString("CF_BESEARCHED_MODPACK_ALC")));
                return;
            }
        } else {
            Argument arg = arguments.optArgument("l");
            if (!(arg instanceof ValueArgument)) {
                System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                return;
            }
            File file = new File(((ValueArgument) arg).value);
            if (!file.exists() || file.isDirectory()) {
                System.out.println(getString("FILE_NOT_FOUND_OR_IS_A_DIRECTORY"));
                return;
            }
            String versionName = ConsoleUtils.inputStringInFilter(getString("MESSAGE_INPUT_VERSION_NAME"), getString("MESSAGE_INSTALL_INPUT_NAME_EXISTS"), new StringFilter() {
                @Override
                public boolean accept(String string) {
                    return !isEmpty(string) && !Utils.versionExists(string);
                }
            });

            if (isEmpty(versionName)) return;

            int threadCount = arguments.optInt("t");
            File versionDir = new File(versionsDir, versionName);
            try {

                ZipFile zipFile = new ZipFile(file);
                ZipEntry entry = zipFile.getEntry("manifest.json");
                //FileUtils.inputStream2File(zipFile.getInputStream(entry),new File(versionDir,"modpack.json"));
                JSONObject manifest = new JSONObject(Utils.inputStream2String(zipFile.getInputStream(entry)));
                installCurseForgeModpack(manifest, zipFile, file, versionDir, true, !arguments.contains("na"), !arguments.contains("nn"), !arguments.contains("nl"), threadCount > 0 ? threadCount : Constants.DEFAULT_DOWNLOAD_THREAD_COUNT);
            } catch (Exception e) {
                System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e));
                Utils.deleteDirectory(versionDir);
            }
            return;
        }
        switch (lowerCase) {
            case "i":

                /*System.out.println(getString("CF_SUPPORTED_GAME_VERSION",modName));
                for (int i = 0; i < mod.getJSONArray("gameVersionLatestFiles").length(); i++) {
                    System.out.println("(" + (i + 1) + ")" + mod.getJSONArray("gameVersionLatestFiles").getJSONObject(i).optString("gameVersion"));
                }*/

            {
                String modpackName = modpack.optString("name");
                int modId = modpack.optInt("id");


                String modpackDownloadLink = modManager.getDownloadLink(modId, modpackName);
                if (isEmpty(modpackDownloadLink)) return;
                downloadModpack(modpackDownloadLink, arguments);
            }
            break;

            case "s": {
                String modpackName = modpack.optString("name");
                modManager.printInformation(modpack, modpackName);
            }
            break;

            default:
                System.out.println(getString("CONSOLE_UNKNOWN_OPTION", key));
                break;
        }
    }

    private static File askStorage(File last) {
        System.out.print(getString("CF_STORAGE_FILE_EXISTS", last.getAbsolutePath()).replace("${NAME}", getString("CF_BESEARCHED_MODPACK_ALC")));
        String path;
        try {
            path = new Scanner(System.in).nextLine();
        } catch (NoSuchElementException e) {
            return null;
        }
        if (isEmpty(path)) return askStorage(last);
        File file = new File(path, last.getName());
        if (!file.exists()) return file;
        return askStorage(file);
    }

    @Override
    public String getUsageName() {
        return "MODPACK";
    }


    private static void downloadModpack(String modDownloadLink, Arguments arguments) {
        File mods = new File("cmcl", "modpacks");
        mods.mkdirs();
        String fileName = modDownloadLink.substring(modDownloadLink.lastIndexOf('/') + 1);
        File modpackFile = new File(mods, fileName);
        if (modpackFile.exists()) {
            File file = askStorage(modpackFile);
            if (file != null) {
                modpackFile = file;
                mods = file.getParentFile();
            } else {
                return;
            }
        }
        try {
            System.out.print(getString("MESSAGE_DOWNLOADING_FILE_TO", fileName, mods.getAbsolutePath()));
            ConsoleMinecraftLauncher.downloadFile(modDownloadLink, modpackFile, new PercentageTextProgress());
        } catch (Exception e) {
            System.out.println(getString("MESSAGE_FAILED_DOWNLOAD_FILE_WITH_REASON", fileName, e));
            return;
        }

        String versionName = ConsoleUtils.inputStringInFilter(getString("MESSAGE_INPUT_VERSION_NAME"), getString("MESSAGE_INSTALL_INPUT_NAME_EXISTS"), new StringFilter() {
            @Override
            public boolean accept(String string) {
                return !isEmpty(string) && !Utils.versionExists(string);
            }
        });
        ;

        if (isEmpty(versionName)) return;

        int threadCount = arguments.optInt("t");
        File versionDir = new File(versionsDir, versionName);

        try {
            ZipFile zipFile = new ZipFile(modpackFile);
            ZipEntry entry = zipFile.getEntry("manifest.json");
            //FileUtils.inputStream2File(zipFile.getInputStream(entry),new File(versionDir,"modpack.json"));
            JSONObject manifest = new JSONObject(Utils.inputStream2String(zipFile.getInputStream(entry)));
            installCurseForgeModpack(manifest, zipFile, modpackFile, versionDir, arguments.contains("k"), !arguments.contains("na"), !arguments.contains("nn"), !arguments.contains("nl"), threadCount > 0 ? threadCount : Constants.DEFAULT_DOWNLOAD_THREAD_COUNT);
        } catch (Exception e) {
            System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e));
            Utils.deleteDirectory(versionDir);
        }
    }

    private static void installCurseForgeModpack(JSONObject manifest, ZipFile zipFile, File modPackFile, File versionDir, boolean keepFile, boolean installAssets, boolean installNatives, boolean installLibraries, int threadCount) throws Exception {
        String overrides = Utils.addSlashIfMissing(manifest.optString("overrides"));
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
            throw new MissingElementException("minecraft", "JSONObject");
        }
        String minecraftVersion = minecraft.optString("version");
        if (isEmpty(minecraftVersion)) {
            throw new MissingElementException("version", "String");
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
        VersionInstaller.InstallForgeOrFabric installForgeOrFabric = null;
        String modLoaderVersion = null;
        VersionInstaller.Merger mergerForFabric = null;
        VersionInstaller.Merger mergerForForge = null;
        if (!isEmpty(modLoader)) {
            if (modLoader.startsWith("forge-") && modLoader.length() > 6) {
                installForgeOrFabric = VersionInstaller.InstallForgeOrFabric.FORGE;
                modLoaderVersion = modLoader.substring(6);
                String finalModLoaderVersion1 = modLoaderVersion;
                mergerForForge = new VersionInstaller.Merger() {
                    @Override
                    public Pair<Boolean, List<JSONObject>> merge(String minecraftVersion, JSONObject headJSONObject, File minecraftJarFile, boolean askContinue) {
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
                    }
                };
            } else if (modLoader.startsWith("fabric-") && modLoader.length() > 7) {
                installForgeOrFabric = VersionInstaller.InstallForgeOrFabric.FABRIC;
                modLoaderVersion = modLoader.substring(7);
                String finalModLoaderVersion = modLoaderVersion;
                mergerForFabric = new VersionInstaller.Merger() {
                    @Override
                    public Pair<Boolean, List<JSONObject>> merge(String minecraftVersion, JSONObject headJSONObject, File minecraftJarFile, boolean askContinue) {
                        try {
                            return FabricMerger.installInternal(minecraftVersion, finalModLoaderVersion, headJSONObject);
                        } catch (Exception e) {
                            System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e.getMessage()));
                            Utils.deleteDirectory(versionDir);
                            return new Pair<>(false, null);
                        }
                    }
                };
            }
        }

        Void onFinished = () -> {
            JSONArray files = manifest.optJSONArray("files");
            if (files != null && files.length() > 0) {
                List<Pair<String, File>> filess = new LinkedList<>();
                System.out.print(getString("INSTALL_MODPACK_EACH_MOD_GET_URL"));
                TextProgress textProgress = new TextProgress();
                textProgress.setMaximum(files.length());
                for (int i = 0; i < files.length(); i++) {
                    Object o = files.get(i);
                    if (o instanceof JSONObject) {
                        JSONObject file = (JSONObject) o;
                        int fileID = file.optInt("fileID");
                        int projectID = file.optInt("projectID");
                        if (fileID == 0 || projectID == 0) continue;
                        try {
                            String url1 = "https://cursemeta.dries007.net/" + projectID + "/" + fileID + ".json";
                            String fileName;
                            String fileDownloadUrl;
                            try {
                                JSONObject jsonObject = new JSONObject(Utils.get(url1));
                                fileName = jsonObject.optString("FileNameOnDisk");
                                fileDownloadUrl = jsonObject.optString("DownloadURL");
                            } catch (Exception e) {
                                try {
                                    String url2 = "https://addons-ecs.forgesvc.net/api/v2/addon/" + projectID + "/file/" + fileID;
                                    JSONObject jsonObject = new JSONObject(Utils.get(url2));
                                    fileName = jsonObject.optString("fileName");
                                    fileDownloadUrl = jsonObject.optString("downloadUrl");
                                } catch (Exception e2) {
                                    System.out.println(getString("INSTALL_MODPACK_FAILED_DOWNLOAD_MOD", projectID, e2));
                                    continue;
                                }
                            }
                            file.put("fileName", fileName);
                            file.put("url", fileDownloadUrl);
                            //System.out.print(getString("MESSAGE_DOWNLOADING_FILE", fileName));
                            //ConsoleMinecraftLauncher.downloadFile(fileDownloadUrl,new File(versionDir,"mods/"+fileName),new PercentageTextProgress());
                            filess.add(new Pair<>(fileDownloadUrl, new File(versionDir, "mods/" + fileName)));
                            textProgress.setValue(i + 1);

                        } catch (Exception e) {
                            System.out.println(getString("INSTALL_MODPACK_FAILED_DOWNLOAD_MOD", projectID, e));
                            continue;
                        }

                    }
                }

                textProgress.setValue(files.length());
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
                minecraftVersion,
                versionDir.getName(),
                versions,
                installAssets,
                installNatives,
                installLibraries,
                installForgeOrFabric,
                threadCount,
                mergerForFabric,
                mergerForForge,
                onFinished);


    }
}