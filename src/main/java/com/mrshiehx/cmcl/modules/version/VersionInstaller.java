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
package com.mrshiehx.cmcl.modules.version;

import com.mrshiehx.cmcl.ConsoleMinecraftLauncher;
import com.mrshiehx.cmcl.api.download.DownloadSource;
import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.interfaces.Void;
import com.mrshiehx.cmcl.modules.MinecraftLauncher;
import com.mrshiehx.cmcl.modules.modLoaders.fabric.FabricMerger;
import com.mrshiehx.cmcl.modules.modLoaders.forge.ForgeMerger;
import com.mrshiehx.cmcl.utils.*;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.List;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.isEmpty;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.downloadFile;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.gameDir;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.versionsDir;

public class VersionInstaller {
    public static void start(String versionName,
                             String storage,
                             JSONArray versions,
                             boolean installAssets,
                             boolean installNatives,
                             boolean installLibraries,
                             InstallForgeOrFabric installForgeOrFabric,
                             int threadCount) {
        try {
            start(versionName,
                    storage,
                    versions,
                    installAssets,
                    installNatives,
                    installLibraries,
                    installForgeOrFabric,
                    threadCount,
                    (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                        Pair<Boolean, List<JSONObject>> a = new FabricMerger().merge(minecraftVersion, headJSONObject, minecraftJarFile, askContinue);
                        if (askContinue && a != null && !a.getKey()) {
                            Utils.deleteDirectory(minecraftJarFile.getParentFile());
                        }
                        return a;
                    },
                    (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                        Pair<Boolean, List<JSONObject>> a = new ForgeMerger().merge(minecraftVersion, headJSONObject, minecraftJarFile, askContinue);
                        if (askContinue && a != null && !a.getKey()) {
                            Utils.deleteDirectory(minecraftJarFile.getParentFile());
                        }
                        return a;
                    },
                    () -> System.out.println(getString("MESSAGE_INSTALLED_NEW_VERSION")));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    ;

    public static void start(String versionName,
                             String storage,
                             JSONArray versions,
                             boolean installAssets,
                             boolean installNatives,
                             boolean installLibraries,
                             InstallForgeOrFabric installForgeOrFabric,
                             int threadCount,
                             Merger fabricMerger,
                             Merger forgeMerger,
                             @Nullable Void onFinished) throws Exception {
        if (!checkNetwork(DownloadSource.getProvider().versionJSON())) {
            throw new Exception(getString("MESSAGE_FAILED_TO_CONNECT_TO_URL", DownloadSource.getProvider().versionJSON()));
        }
        File cmcl = new File("cmcl");
        //File versionsFile = new File(cmcl, "versions.json");
        cmcl.mkdirs();
        if (versions == null || versions.length() == 0) {
            throw new Exception(getString("MESSAGE_VERSIONS_LIST_IS_EMPTY"));
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
            throw new Exception(getString("EXCEPTION_VERSION_NOT_FOUND", versionName));
        }
        url = url.replace("https://launchermeta.mojang.com/", DownloadSource.getProvider().versionJSON());
        File versionDir = new File(versionsDir, storage);
        versionDir.mkdirs();
        File jsonFile = new File(versionDir, storage + ".json");
        File jarFile = new File(versionDir, storage + ".jar");
        if (jsonFile.exists()) jsonFile.delete();
        try {
            byte[] versionJSONBytes = ConsoleMinecraftLauncher.downloadBytes(url);
            //downloadFile(url, jsonFile);
            JSONObject headVersionFile = new JSONObject(new String(versionJSONBytes));
            headVersionFile.put("gameVersion", headVersionFile.optString("id"));
            headVersionFile.put("id", storage);


            if (installForgeOrFabric == InstallForgeOrFabric.FABRIC) {
                if (!fabricMerger.merge(versionName, headVersionFile, jarFile, true).getKey()) {
                    return;
                }
            }

            JSONObject downloadsJo = headVersionFile.optJSONObject("downloads");
            JSONObject clientJo = downloadsJo != null ? downloadsJo.optJSONObject("client") : null;
            if (downloadsJo == null) {
                throw new Exception(getString("MESSAGE_INSTALL_NOT_FOUND_JAR_FILE_DOWNLOAD_INFO"));
            }
            if (clientJo == null) {
                throw new Exception(getString("MESSAGE_INSTALL_NOT_FOUND_JAR_FILE_DOWNLOAD_INFO"));
            }
            String urlClient = clientJo.optString("url").replace("https://launcher.mojang.com/", DownloadSource.getProvider().versionClient());
            if (isEmpty(urlClient)) {
                throw new Exception(getString("MESSAGE_INSTALL_JAR_FILE_DOWNLOAD_URL_EMPTY"));
            }

            try {
                System.out.print(getString("MESSAGE_INSTALL_DOWNLOADING_JAR_FILE"));

                jarFile.createNewFile();
                try {
                    downloadFile(urlClient, jarFile, new PercentageTextProgress());
                } catch (Exception e) {
                    Utils.deleteDirectory(versionDir);
                    throw e;
                }
                System.out.println(getString("MESSAGE_INSTALL_DOWNLOADED_JAR_FILE"));


                if (installForgeOrFabric == InstallForgeOrFabric.FORGE) {
                    System.out.println(getString("MESSAGE_START_INSTALLING_FORGE"));
                    if (!forgeMerger.merge(versionName, headVersionFile, jarFile, true).getKey()) {
                        return;
                    }
                    System.out.println(getString("MESSAGE_INSTALLED_FORGE"));
                }
                jsonFile.createNewFile();
                try {
                    Utils.writeFile(jsonFile, headVersionFile.toString(2), false);
                } catch (Exception e) {
                    Utils.deleteDirectory(versionDir);
                    throw e;
                }

                File librariesDir = new File(gameDir, "libraries");
                File nativesDir = new File(versionDir, Utils.getNativesDirName());
                File tempNatives = new File(cmcl, "temp_natives");
                //tempNatives.mkdirs();
                //librariesDir.mkdirs();
                //nativesDir.mkdirs();

                if (!installAssets) {
                    if (installLibraries || installNatives) {
                        downloadLibrariesAndNatives(installNatives, installLibraries, tempNatives, librariesDir, nativesDir, headVersionFile);
                    }
                    ConsoleMinecraftLauncher.createLauncherProfiles();
                    if (onFinished != null) onFinished.execute();
                    return;
                }
                System.out.println(getString("MESSAGE_INSTALL_DOWNLOADING_ASSETS"));
                //downloadFile(url, jarFile, progressBar);

                File assetsDir = Utils.getAssets(Utils.getConfig().optString("assetsPath"), gameDir);
                File indexesDir = new File(assetsDir, "indexes");
                File objectsDir = new File(assetsDir, "objects");
                assetsDir.mkdirs();
                indexesDir.mkdirs();
                objectsDir.mkdirs();
                String assetsIndex = headVersionFile.optString("assets");
                if (isEmpty(assetsIndex)) {
                    throw new Exception(getString("MESSAGE_INSTALL_DOWNLOAD_ASSETS_NO_INDEX"));
                }
                File assetsIndexFile = new File(indexesDir, assetsIndex + ".json");
                JSONObject assetIndexObject = headVersionFile.optJSONObject("assetIndex");
                String assetIndexUrl = assetIndexObject != null ? assetIndexObject.optString("url").replace("https://launchermeta.mojang.com/", DownloadSource.getProvider().versionAssetsIndex()) : null;

                if (isEmpty(assetIndexUrl)) {
                    throw new Exception(getString("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_ASSETS", getString("MESSAGE_EXCEPTION_DETAIL_NOT_FOUND_URL")));
                }
                try {
                    downloadFile(assetIndexUrl, assetsIndexFile);
                    JSONObject assetsJo = new JSONObject(Utils.readFileContent(assetsIndexFile));
                    JSONObject objectsJo = assetsJo.optJSONObject("objects");

                    Map<String, Object> map = objectsJo.toMap();
                    List<String> nameList = new ArrayList<>(map.keySet());
                    JSONArray names = new JSONArray(nameList);
                    JSONArray objectsJa = objectsJo.toJSONArray(names);
                    List<Pair<String, File>> list = new LinkedList<>();
                    for (int i = 0; i < objectsJa.length(); i++) {
                        JSONObject object = objectsJa.optJSONObject(i);
                        if (object == null) {
                            continue;
                        }
                        String hash = object.optString("hash");
                        try {
                            if (isEmpty(hash)) continue;
                            File file;
                            if (!assetsIndex.equals("legacy")) {
                                File dir = new File(objectsDir, hash.substring(0, 2));
                                dir.mkdirs();
                                file = new File(dir, hash);
                            } else {
                                file = new File(assetsDir, "virtual/legacy/" + nameList.get(i));
                                file.getParentFile().mkdirs();
                            }


                            if (!file.exists()) {
                                file.createNewFile();
                            }
                            if (file.length() == 0)
                                list.add(new Pair<>(DownloadSource.getProvider().assets() + hash.substring(0, 2) + "/" + hash, file));

                        } catch (Exception e1) {
                            throw new Exception(getString("MESSAGE_FAILED_DOWNLOAD_FILE", hash));
                        }

                    }
                    ThreadsDownloader threadsDownloader = new ThreadsDownloader(list, () -> {
                        System.out.println(getString("MESSAGE_INSTALL_DOWNLOADED_ASSETS"));
                        if (installLibraries || installNatives) {
                            downloadLibrariesAndNatives(installNatives, installLibraries, tempNatives, librariesDir, nativesDir, headVersionFile);
                        }
                        ConsoleMinecraftLauncher.createLauncherProfiles();
                        if (onFinished != null) onFinished.execute();
                    }, threadCount > 0 ? threadCount : Constants.DEFAULT_DOWNLOAD_THREAD_COUNT);
                    threadsDownloader.start();
                } catch (Exception e1) {
                    throw new Exception(getString("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_ASSETS", e1));
                }

            } catch (Exception ex) {
                throw new Exception(getString("MESSAGE_FAILED_TO_INSTALL_NEW_VERSION", ex));
            }


        } catch (IOException e) {
            throw new Exception(getString("MESSAGE_FAILED_TO_CONTROL_VERSION_JSON_FILE", e));
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
                        Pair<String, String> pair = Utils.getLibraryDownloadURLAndStoragePath(jsonObject);

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
                                        downloadFile(url1, file, new PercentageTextProgress());
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
                    JSONObject nativesJo = classifiersJo.optJSONObject(nativesNamesJO.optString(OperatingSystem.CURRENT_OS.getCheckedName().replace("${arch}", Utils.getArchInt())));

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
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public enum InstallForgeOrFabric {
        FORGE, FABRIC
    }

    public static interface Merger {
        Pair<Boolean, List<JSONObject>> merge(String minecraftVersion, JSONObject headJSONObject, File minecraftJarFile, boolean askContinue);
    }
}
