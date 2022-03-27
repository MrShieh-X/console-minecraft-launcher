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
import com.mrshiehx.cmcl.modules.MinecraftLauncher;
import com.mrshiehx.cmcl.modules.fabric.FabricMerger;
import com.mrshiehx.cmcl.utils.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.List;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.isEmpty;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.downloadFile;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.unZip;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.gameDir;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.versionsDir;

public class VersionInstaller {
    public static void start(String versionName, String storage, JSONArray versions, boolean installAssets, boolean installNatives, boolean installLibraries, boolean installFabric, int threadCount) {
        if (!checkNetwork(DownloadSource.getProvider().versionJSON())) {
            System.out.println(getString("MESSAGE_FAILED_TO_CONNECT_TO_URL", DownloadSource.getProvider().versionJSON()));
            return;
        }
        File cmcl = new File("cmcl");
        //File versionsFile = new File(cmcl, "versions.json");
        cmcl.mkdirs();
        if (versions == null) {
            System.out.println(getString("MESSAGE_VERSIONS_LIST_IS_EMPTY"));
            return;
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
            System.out.println(getString("EXCEPTION_VERSION_NOT_FOUND"));
            return;
        }
        url = url.replace("https://launchermeta.mojang.com/", DownloadSource.getProvider().versionJSON());
        if (isEmpty(storage)) {
            return;
        }
        File versionDir = new File(versionsDir, storage);
        versionDir.mkdirs();
        File jsonFile = new File(versionDir, storage + ".json");
        File jarFile = new File(versionDir, storage + ".jar");
        if (jsonFile.exists()) jsonFile.delete();
        try {
            byte[] versionJSONBytes = ConsoleMinecraftLauncher.downloadBytes(url);
            //downloadFile(url, jsonFile);
            JSONObject headVersionFile = new JSONObject(new String(versionJSONBytes));


            if (installFabric) {
                if (!FabricMerger.merge(versionName, headVersionFile, true).getKey())
                    return;
            }
            jsonFile.createNewFile();
            try {
                Utils.writeFile(jsonFile, headVersionFile.toString(), false);
            } catch (Exception e) {
                Utils.deleteDirectory(versionDir);
                throw e;
            }

            JSONObject downloadsJo = headVersionFile.optJSONObject("downloads");
            JSONObject clientJo = downloadsJo != null ? downloadsJo.optJSONObject("client") : null;
            if (downloadsJo == null) {
                System.out.println(getString("MESSAGE_INSTALL_NOT_FOUND_JAR_FILE_DOWNLOAD_INFO"));
                return;
            }
            if (clientJo == null) {
                System.out.println(getString("MESSAGE_INSTALL_NOT_FOUND_JAR_FILE_DOWNLOAD_INFO"));
                return;
            }
            String urlClient = clientJo.optString("url").replace("https://launcher.mojang.com/", DownloadSource.getProvider().versionClient());
            if (isEmpty(urlClient)) {
                System.out.println(getString("MESSAGE_INSTALL_JAR_FILE_DOWNLOAD_URL_EMPTY"));
                return;
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

                File librariesDir = new File(gameDir, "libraries");
                File nativesDir = new File(versionDir, Utils.getNativesDirName());
                File tempNatives = new File(cmcl, "temp_natives");
                tempNatives.mkdirs();
                librariesDir.mkdirs();
                nativesDir.mkdirs();

                if (!installAssets) {
                    downloadLibrariesAndNatives(installNatives, installLibraries, tempNatives, librariesDir, nativesDir, headVersionFile);
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
                    System.out.println(getString("MESSAGE_INSTALL_DOWNLOAD_ASSETS_NO_INDEX"));
                    return;
                }
                File assetsIndexFile = new File(indexesDir, assetsIndex + ".json");
                JSONObject assetIndexObject = headVersionFile.optJSONObject("assetIndex");
                String assetIndexUrl = assetIndexObject != null ? assetIndexObject.optString("url").replace("https://launchermeta.mojang.com/", DownloadSource.getProvider().versionAssetsIndex()) : null;

                if (isEmpty(assetIndexUrl)) {
                    System.out.println(getString("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_ASSETS", getString("MESSAGE_EXCEPTION_DETAIL_NOT_FOUND_URL")));
                    return;
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
                            e1.printStackTrace();
                            System.out.println(getString("MESSAGE_FAILED_DOWNLOAD_FILE", hash));
                        }

                    }
                    ThreadsDownloader threadsDownloader = new ThreadsDownloader(list, () -> {
                        System.out.println(getString("MESSAGE_INSTALL_DOWNLOADED_ASSETS"));
                        downloadLibrariesAndNatives(installNatives, installLibraries, tempNatives, librariesDir, nativesDir, headVersionFile);
                    }, threadCount > 0 ? threadCount : 64);
                    threadsDownloader.start();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    System.out.println(getString("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_ASSETS", e1));
                }

            } catch (Exception ex) {
                //ex.printStackTrace();
                System.out.println(getString("MESSAGE_FAILED_TO_INSTALL_NEW_VERSION", ex));
            }


        } catch (IOException e) {
            System.out.println(getString("MESSAGE_FAILED_TO_CONTROL_VERSION_JSON_FILE", e));
        }


    }

    private static void downloadLibrariesAndNatives(boolean installNatives, boolean installLibraries, File tempNatives, File librariesDir, File nativesDir, JSONObject headVersionFile) {
        if (installLibraries) {
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
                        Pair<String, String> pair = Utils.getLibraryDownloadURLAndStoragePath(jsonObject);
                        ;
                        if (pair != null) {
                            String path = pair.getValue();
                            String url1 = pair.getKey();
                            try {
                                File file = new File(librariesDir, path);
                                file.getParentFile().mkdirs();
                                if (!file.exists()) {
                                    file.createNewFile();
                                }
                                if (file.length() == 0) {

                                    System.out.print(getString("MESSAGE_DOWNLOADING_FILE", url1.substring(url1.lastIndexOf("/") + 1)));
                                    downloadFile(url1, file, new PercentageTextProgress());
                                }
                            } catch (Exception e1) {
                                e1.printStackTrace();
                                System.out.println(getString("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARY", url1, e1));
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
                        JSONObject nativesJo = classifiersJo.optJSONObject(nativesNamesJO.optString(OperatingSystem.CURRENT_OS.getCheckedName()));

                        if (nativesJo == null) continue;
                        String name12 = Utils.getNativeLibraryName(nativesJo.optString("path"));
                        //System.out.println(Arrays.toString(nativesNames.toArray())+","+name );
                        if (!nativesNames.contains(name12)) {
                            String url2 = nativesJo.optString("url");
                            try {
                                if (isEmpty(url2)) continue;
                                url2 = url2.replace("https://libraries.minecraft.net/", DownloadSource.getProvider().libraries());
                                File nativeFile = new File(tempNatives, url2.substring(url2.lastIndexOf("/") + 1));
                                //if(!nativeFile.exists()) {
                                nativeFile.createNewFile();

                                System.out.print(getString("MESSAGE_DOWNLOADING_FILE", url2.substring(url2.lastIndexOf("/") + 1)));
                                downloadFile(url2, nativeFile, new PercentageTextProgress());
                                nativesNames.add(name12);
                                //}

                            } catch (Exception e1) {
                                e1.printStackTrace();

                                System.out.println(getString("MESSAGE_FAILED_DOWNLOAD_FILE", url2));
                            }
                        }


                    }
                }
                System.out.println(getString("MESSAGE_INSTALL_DOWNLOADED_LIBRARIES"));
            } catch (Exception e1) {
                e1.printStackTrace();
                System.out.println(getString("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARIES", e1));
            }
        }


        if (installNatives) {
            File[] natives = tempNatives.listFiles((dir, name1) -> name1.endsWith(".jar"));
            if (natives != null && natives.length != 0) {
                System.out.println(getString("MESSAGE_INSTALL_DECOMPRESSING_NATIVE_LIBRARIES"));
                for (File file : natives) {
                    try {
                        File dir = new File(tempNatives, file.getName().substring(0, file.getName().lastIndexOf(".")));
                        dir.mkdirs();

                        System.out.print(getString("MESSAGE_UNZIPPING_FILE", file.getName()));
                        unZip(file, dir, new PercentageTextProgress());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        System.out.println(getString("MESSAGE_FAILED_TO_DECOMPRESS_FILE", file.getAbsolutePath(), e1));
                    }
                }
            }
            System.out.println(getString("MESSAGE_INSTALL_DECOMPRESSED_NATIVE_LIBRARIES"));


            List<File> libFiles = new ArrayList<>();
            String houzhui = ".so";

            String osName = System.getProperty("os.name");

            if (osName.toLowerCase().contains("windows")) {
                houzhui = ".dll";
            } else if (osName.toLowerCase().contains("mac")) {
                houzhui = ".dylib";
            }

            File[] var4 = tempNatives.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name12) {
                    return dir.exists() && dir.isDirectory();
                }
            });

            for (File file : var4) {
                if (file != null && file.isDirectory()) {
                    String finalHouzhui = houzhui;
                    File[] files = file.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name12) {
                            return name12.toLowerCase().endsWith(finalHouzhui) || name12.toLowerCase().endsWith(".jnilib");
                        }
                    });
                    libFiles.addAll(Arrays.asList(files));
                }
            }

            for (File file : libFiles) {
                File to = new File(nativesDir, file.getName());
                try {

                    System.out.println(getString("MESSAGE_COPYING_FILE", file.getName(), to.getPath()));
                    Utils.copyFile(file, to);
                } catch (IOException e1) {
                    e1.printStackTrace();
                    System.out.println(getString("MESSAGE_FAILED_TO_COPY_FILE", file.getAbsolutePath(), to.getAbsolutePath(), e1));
                }
            }

            Utils.deleteDirectory(tempNatives);
            System.out.println(getString("MESSAGE_INSTALL_COPIED_NATIVE_LIBRARIES"));
        }
        System.out.println(getString("MESSAGE_INSTALLED_NEW_VERSION"));
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
}
