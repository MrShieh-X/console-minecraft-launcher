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
 */
package com.mrshiehx.cmcl.modules.version;

import com.mrshiehx.cmcl.CMCL;
import com.mrshiehx.cmcl.api.download.DownloadSource;
import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.exceptions.DescriptionException;
import com.mrshiehx.cmcl.interfaces.Void;
import com.mrshiehx.cmcl.modules.MinecraftLauncher;
import com.mrshiehx.cmcl.modules.version.downloaders.AssetsDownloader;
import com.mrshiehx.cmcl.modules.version.downloaders.NativesDownloader;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.cmcl.version.VersionLibraryUtils;
import com.mrshiehx.cmcl.utils.cmcl.version.VersionUtils;
import com.mrshiehx.cmcl.utils.console.PercentageTextProgress;
import com.mrshiehx.cmcl.utils.internet.DownloadUtils;
import com.mrshiehx.cmcl.utils.json.JSONUtils;
import com.mrshiehx.cmcl.utils.json.XJSONObject;
import com.mrshiehx.cmcl.utils.system.OperatingSystem;
import com.mrshiehx.cmcl.utils.system.SystemUtils;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mrshiehx.cmcl.CMCL.*;

public class VersionInstaller {
    public static void start(String versionName,
                             String storage,
                             JSONArray versions,
                             boolean installAssets,
                             boolean installNatives,
                             boolean installLibraries,
                             InstallForgeOrFabricOrQuilt installForgeOrFabricOrQuilt,
                             int threadCount,
                             Merger fabricMerger,
                             Merger forgeMerger,
                             Merger quiltMerger,
                             Merger liteLoaderMerger,
                             Merger optiFineMerger,
                             @Nullable Void onFinished,
                             @Nullable VersionJSONMerger versionJSONMerger,
                             JSONArray librariesToBeMerged) throws DescriptionException {
        /*if (!checkNetwork(DownloadSource.getProvider().versionJSON())) {
            throw new DescriptionException(getString("MESSAGE_FAILED_TO_CONNECT_TO_URL", DownloadSource.getProvider().versionJSON()));
        }*/
        File cmcl = CMCL.getCMCLWorkingDirectory();
        //File versionsFile = new File(cmcl, "versions.json");
        cmcl.mkdirs();
        if (versions == null || versions.length() == 0) {
            throw new DescriptionException(getString("MESSAGE_VERSIONS_LIST_IS_EMPTY"));
        }
        String url = null;
        for (Object o : versions) {
            if (o instanceof JSONObject) {
                JSONObject j = ((JSONObject) o);
                if (Objects.equals(versionName, j.optString("id"))) {
                    url = j.optString("url");
                    break;
                }
            }
        }

        if (isEmpty(url)) {
            throw new DescriptionException(getString("EXCEPTION_VERSION_NOT_FOUND", versionName));
        }
        url = url.replace("https://launchermeta.mojang.com/", DownloadSource.getProvider().versionJSON()).replace("https://piston-meta.mojang.com/", DownloadSource.getProvider().versionJSON());
        File versionDir = new File(versionsDir, storage);
        versionDir.mkdirs();
        File jsonFile = new File(versionDir, storage + ".json");
        File jarFile = new File(versionDir, storage + ".jar");
        if (jsonFile.exists()) jsonFile.delete();
        XJSONObject headVersionFile;
        try {
            byte[] versionJSONBytes = DownloadUtils.downloadBytes(url);
            //downloadFile(url, jsonFile);
            headVersionFile = new com.mrshiehx.cmcl.utils.json.XJSONObject(new String(versionJSONBytes));
            headVersionFile.put("gameVersion", headVersionFile.optString("id"));
            headVersionFile.put("id", storage);

        } catch (IOException e) {
            throw new DescriptionException(getString("MESSAGE_FAILED_TO_CONTROL_VERSION_JSON_FILE", e));
        }

        if (installForgeOrFabricOrQuilt == InstallForgeOrFabricOrQuilt.FABRIC) {
            if (!fabricMerger.merge(versionName, headVersionFile, jarFile, true).getKey()) {
                return;
            }
        } else if (installForgeOrFabricOrQuilt == InstallForgeOrFabricOrQuilt.QUILT) {
            if (!quiltMerger.merge(versionName, headVersionFile, jarFile, true).getKey()) {
                return;
            }
        }

        if (versionJSONMerger != null) {
            versionJSONMerger.merge(headVersionFile);
        }

        JSONObject downloadsJo = headVersionFile.optJSONObject("downloads");
        JSONObject clientJo = downloadsJo != null ? downloadsJo.optJSONObject("client") : null;
        if (downloadsJo == null) {
            throw new DescriptionException(getString("MESSAGE_INSTALL_NOT_FOUND_JAR_FILE_DOWNLOAD_INFO"));
        }
        if (clientJo == null) {
            throw new DescriptionException(getString("MESSAGE_INSTALL_NOT_FOUND_JAR_FILE_DOWNLOAD_INFO"));
        }
        String urlClient = clientJo.optString("url").replace("https://launcher.mojang.com/", DownloadSource.getProvider().versionClient());
        if (isEmpty(urlClient)) {
            throw new DescriptionException(getString("MESSAGE_INSTALL_JAR_FILE_DOWNLOAD_URL_EMPTY"));
        }

        try {
            System.out.print(getString("MESSAGE_INSTALL_DOWNLOADING_JAR_FILE"));

            jarFile.createNewFile();
            try {
                DownloadUtils.downloadFile(urlClient, jarFile, new PercentageTextProgress());
            } catch (Exception e) {
                FileUtils.deleteDirectory(versionDir);
                throw new com.mrshiehx.cmcl.exceptions.DescriptionException(getString("MESSAGE_FAILED_DOWNLOAD_FILE", urlClient));
            }
            System.out.println(getString("MESSAGE_INSTALL_DOWNLOADED_JAR_FILE"));

            /*安装顺序不可乱*/
            if (installForgeOrFabricOrQuilt == InstallForgeOrFabricOrQuilt.FORGE) {
                System.out.println(getString("MESSAGE_START_INSTALLING_FORGE"));
                if (!forgeMerger.merge(versionName, headVersionFile, jarFile, true).getKey()) {
                    return;
                }
                System.out.println(getString("MESSAGE_INSTALLED_FORGE"));
            }
            if (liteLoaderMerger != null) {
                System.out.println(getString("MESSAGE_START_INSTALLING_LITELOADER"));
                if (!liteLoaderMerger.merge(versionName, headVersionFile, jarFile, true).getKey()) {
                    return;
                }
                System.out.println(getString("MESSAGE_INSTALLED_LITELOADER"));
            }
            if (optiFineMerger != null) {
                System.out.println(getString("MESSAGE_START_INSTALLING_OPTIFINE"));
                if (!optiFineMerger.merge(versionName, headVersionFile, jarFile, true).getKey()) {
                    return;
                }
                System.out.println(getString("MESSAGE_INSTALLED_OPTIFINE"));
            }
            jsonFile.createNewFile();

            List<JSONObject> toBeMerged = JSONUtils.jsonArrayToJSONObjectList(librariesToBeMerged);
            if (toBeMerged.size() > 0) {
                headVersionFile.put("libraries",
                        VersionLibraryUtils.mergeLibraries(
                                JSONUtils.jsonArrayToJSONObjectList(headVersionFile.optJSONArray("libraries")),
                                toBeMerged)
                );
            }
            try {
                FileUtils.writeFile(jsonFile, headVersionFile.toString(2), false);
            } catch (Exception e) {
                FileUtils.deleteDirectory(versionDir);
                throw e;
            }

            File librariesDir = new File(gameDir, "libraries");
            File nativesDir = new File(versionDir, VersionUtils.getNativesDirName());
            File tempNatives = new File(cmcl, "temp_natives");
            //tempNatives.mkdirs();
            //librariesDir.mkdirs();
            //nativesDir.mkdirs();

            CMCL.createLauncherProfiles();
            if (installLibraries || installNatives) {
                downloadLibrariesAndNatives(installNatives, installLibraries, tempNatives, librariesDir, nativesDir, headVersionFile);
            }

            if (installAssets) {
                System.out.println(getString("MESSAGE_INSTALL_DOWNLOADING_ASSETS"));
                AssetsDownloader.start(headVersionFile, threadCount, () -> {
                    System.out.println(getString("MESSAGE_INSTALL_DOWNLOADED_ASSETS"));
                    if (onFinished != null)
                        onFinished.execute();
                });
            } else {
                if (onFinished != null)
                    onFinished.execute();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DescriptionException(getString("MESSAGE_FAILED_TO_INSTALL_NEW_VERSION", ex));
        }


    }

    private static void downloadLibrariesAndNatives(boolean installNatives, boolean installLibraries, File tempNatives, File librariesDir, File nativesDir, JSONObject headVersionFile) {
        System.out.println(getString("MESSAGE_INSTALL_DOWNLOADING_LIBRARIES"));
        try {
            JSONArray librariesJa = headVersionFile.optJSONArray("libraries");
            if (librariesJa == null || librariesJa.length() == 0) {
                System.out.println(getString("MESSAGE_INSTALL_LIBRARIES_LIST_EMPTY"));
                return;
            }
            List<String> nativesNames = new ArrayList<>();
            for (int i = 0; i < librariesJa.length(); i++) {
                JSONObject jsonObject = librariesJa.optJSONObject(i);
                if (jsonObject != null) {
                    boolean meet = true;
                    JSONArray rules = jsonObject.optJSONArray("rules");
                    if (rules != null) {
                        meet = MinecraftLauncher.isMeetConditions(rules, false, false);
                    }
                    //System.out.println(meet);

                    if (!meet) continue;
                    if (installLibraries) {
                        Pair<String, String> pair = VersionLibraryUtils.getLibraryDownloadURLAndStoragePath(jsonObject);

                        if (pair != null) {
                            String path = pair.getValue();
                            String url1 = pair.getKey();
                            if (!isEmpty(url1)) {
                                File file = new File(librariesDir, path);
                                try {
                                    file.getParentFile().mkdirs();
                                    if (!file.exists()) {
                                        file.createNewFile();
                                    }
                                    if (file.length() == 0) {

                                        System.out.print(getString("MESSAGE_DOWNLOADING_FILE", url1.substring(url1.lastIndexOf("/") + 1)));
                                        DownloadUtils.downloadFile(url1, file, new PercentageTextProgress());
                                    }
                                } catch (Exception e1) {
                                    //e1.printStackTrace();
                                    Utils.downloadFileFailed(url1, file, e1);
                                    //System.out.println(getString("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARY", url1, e1));
                                }
                            } else {
                                if (!jsonObject.optString("name").startsWith("net.minecraftforge:forge:"))
                                    System.out.println(getString("MESSAGE_NOT_FOUND_LIBRARY_DOWNLOAD_URL", jsonObject.optString("name")));
                            }
                        }

                    }

                    if (!installNatives) continue;
                    JSONObject downloadsJo1 = jsonObject.optJSONObject("downloads");
                    if (downloadsJo1 == null) continue;
                    JSONObject classifiersJo = downloadsJo1.optJSONObject("classifiers");
                    if (classifiersJo == null) continue;
                    JSONObject nativesNamesJO = jsonObject.optJSONObject("natives");

                    if (nativesNamesJO == null) continue;

                    //String osName = System.getProperty("os.name");
                    JSONObject nativesJo = classifiersJo.optJSONObject(nativesNamesJO.optString(OperatingSystem.CURRENT_OS.getCheckedName().replace("${arch}", SystemUtils.getArchInt())));

                    if (nativesJo == null) continue;

                    NativesDownloader.downloadSingleNative(tempNatives, nativesJo, nativesNames);


                }
            }
            System.out.println(getString("MESSAGE_INSTALL_DOWNLOADED_LIBRARIES"));
        } catch (Exception e1) {
            System.out.println(getString("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARIES", e1));
        }

        if (installNatives) {
            NativesDownloader.unzip(tempNatives, nativesDir);
        }


    }


    private static boolean checkNetwork(String urla) {
        try {
            URL url = new URL(urla);
            try {
                URLConnection co = url.openConnection();
                co.setConnectTimeout(12000);
                co.connect();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public enum InstallForgeOrFabricOrQuilt {
        FORGE, FABRIC, QUILT
    }

    public interface Merger {
        Pair<Boolean, List<JSONObject>> merge(String minecraftVersion, JSONObject headJSONObject, File minecraftJarFile, boolean askContinue);
    }

    public interface VersionJSONMerger {
        void merge(JSONObject headJSONObject);
    }
}
