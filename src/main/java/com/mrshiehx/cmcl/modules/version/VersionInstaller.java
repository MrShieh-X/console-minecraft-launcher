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

import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.modules.MinecraftLauncher;
import com.mrshiehx.cmcl.utils.OperatingSystem;
import com.mrshiehx.cmcl.utils.ThreadsDownloader;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.XProgressBar;
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
    public static void showDialog(String versionName, String storage, JSONArray versions, boolean installAssets, boolean installNatives, boolean installLibraries, int threadCount) {
        if (checkNetwork()) {
            File cmcl = new File("cmcl");
            //File versionsFile = new File(cmcl, "versions.json");
            cmcl.mkdirs();
            /*try {
                if (versionsFile.exists()) versionsFile.delete();
                versionsFile.createNewFile();
                downloadFile("https://launchermeta.mojang.com/mc/game/version_manifest.json", versionsFile);
            } catch (IOException exception) {
                exception.printStackTrace();
                return;
            }*/
            if (versions != null) {
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

                if (!isEmpty(url)) {
                    if (!isEmpty(storage)) {
                        File versionDir = new File(versionsDir, storage);
                        versionDir.mkdirs();
                        File jsonFile = new File(versionDir, storage + ".json");
                        File jarFile = new File(versionDir, storage + ".jar");
                        if (jsonFile.exists()) jsonFile.delete();
                        try {
                            jsonFile.createNewFile();
                            jarFile.createNewFile();
                            downloadFile(url, jsonFile);
                            JSONObject headVersionFile = new JSONObject(Utils.readFileContent(jsonFile));
                            JSONObject downloadsJo = headVersionFile.optJSONObject("downloads");
                            JSONObject clientJo = downloadsJo != null ? downloadsJo.optJSONObject("client") : null;
                            if (downloadsJo != null) {
                                if (clientJo != null) {
                                    String urlClient = clientJo.optString("url");
                                    if (!isEmpty(urlClient)) {

                                        try {
                                            String text = getString("MESSAGE_INSTALL_DOWNLOADING_JAR_FILE");
                                            System.out.print(text);
                                            downloadFile(urlClient, jarFile, new XProgressBar());
                                            System.out.println(getString("MESSAGE_INSTALL_DOWNLOADED_JAR_FILE"));

                                            File librariesDir = new File(gameDir, "libraries");
                                            File nativesDir = new File(versionDir, Utils.getNativesDirName());
                                            File tempNatives = new File(cmcl, "temp_natives");
                                            tempNatives.mkdirs();
                                            librariesDir.mkdirs();
                                            nativesDir.mkdirs();

                                            if (installAssets) {
                                                System.out.println(getString("MESSAGE_INSTALL_DOWNLOADING_ASSETS"));
                                                //downloadFile(url, jarFile, progressBar);

                                                File assetsDir = Utils.getAssets(Utils.getConfig().optString("assetsPath"), gameDir);
                                                File indexesDir = new File(assetsDir, "indexes");
                                                File objectsDir = new File(assetsDir, "objects");
                                                assetsDir.mkdirs();
                                                indexesDir.mkdirs();
                                                objectsDir.mkdirs();
                                                String assetsIndex = headVersionFile.optString("assets");
                                                if (!isEmpty(assetsIndex)) {
                                                    File assetsIndexFile = new File(indexesDir, assetsIndex + ".json");
                                                    JSONObject assetIndexObject = headVersionFile.optJSONObject("assetIndex");
                                                    String assetIndexUrl = assetIndexObject != null ? assetIndexObject.optString("url") : null;

                                                    if (!isEmpty(assetIndexUrl)) {
                                                        try {
                                                            downloadFile(assetIndexUrl, assetsIndexFile);
                                                            JSONObject assetsJo = new JSONObject(Utils.readFileContent(assetsIndexFile));
                                                            JSONObject objectsJo = assetsJo.optJSONObject("objects");

                                                            Map<String, Object> map = objectsJo.toMap();
                                                            List<String> nameList = new ArrayList<>(map.keySet());
                                                            JSONArray names = new JSONArray(nameList);
                                                            JSONArray objectsJa = objectsJo.toJSONArray(names);
                                                            //ThreadsDownloader threadsDownloader=new ThreadsDownloader(/*10*/null);
                                                            List<Pair<String, File>> list = new LinkedList<>();
                                                            for (int i = 0; i < objectsJa.length(); i++) {
                                                                JSONObject object = objectsJa.optJSONObject(i);
                                                                if (object != null) {
                                                                    String hash = object.optString("hash");
                                                                    try {
                                                                        if (!isEmpty(hash)) {
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
                                                                                list.add(new Pair<>("https://resources.download.minecraft.net/" + hash.substring(0, 2) + "/" + hash, file));
                                                                                /*String text2 = String.format(getString("MESSAGE_DOWNLOADING_FILE"), hash);
                                                                                System.out.println(text2);
                                                                                downloadFile("https://resources.download.minecraft.net/" + hash.substring(0, 2) + "/" + hash, file,new XProgressBar());*/
                                                                                //threadsDownloader.add("https://resources.download.minecraft.net/" + hash.substring(0, 2) + "/" + hash, file);
                                                                                /*if(i==0){
                                                                                    threadsDownloader.start();
                                                                                }*/
                                                                            }
                                                                        }
                                                                    } catch (Exception e1) {
                                                                        e1.printStackTrace();
                                                                        String textx = String.format(getString("MESSAGE_FAILED_DOWNLOAD_FILE"), hash);
                                                                        System.out.println(textx);
                                                                    }
                                                                }
                                                            }
                                                            ThreadsDownloader threadsDownloader = new ThreadsDownloader(list, () -> {
                                                                System.out.println(getString("MESSAGE_INSTALL_DOWNLOADED_ASSETS"));
                                                                method(installNatives, installLibraries, tempNatives, librariesDir, nativesDir, headVersionFile);

                                                            }, threadCount > 0 ? threadCount : 10);
                                                            threadsDownloader.start();
                                                        } catch (Exception e1) {
                                                            e1.printStackTrace();
                                                            System.out.println(String.format(getString("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_ASSETS"), e1));
                                                        }
                                                    } else {
                                                        System.out.println(String.format(getString("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_ASSETS"), getString("MESSAGE_EXCEPTION_DETAIL_NOT_FOUND_URL")));
                                                    }
                                                } else {
                                                    System.out.println(getString("MESSAGE_INSTALL_DOWNLOAD_ASSETS_NO_INDEX"));
                                                }
                                            }


                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                            System.out.println(String.format(getString("MESSAGE_FAILED_TO_INSTALL_NEW_VERSION"), ex));
                                        }

                                    } else {
                                        System.out.println(getString("MESSAGE_INSTALL_JAR_FILE_DOWNLOAD_URL_EMPTY"));
                                    }
                                } else {
                                    System.out.println(getString("MESSAGE_INSTALL_NOT_FOUND_JAR_FILE_DOWNLOAD_INFO"));
                                }
                            } else {
                                System.out.println(getString("MESSAGE_INSTALL_NOT_FOUND_JAR_FILE_DOWNLOAD_INFO"));
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println(String.format(getString("MESSAGE_FAILED_TO_CONTROL_VERSION_JSON_FILE"), e));
                        }
                    }


                } else {
                    System.out.println(getString("EXCEPTION_VERSION_NOT_FOUND"));
                }
            } else {
                System.out.println(getString("MESSAGE_VERSIONS_LIST_IS_EMPTY"));
            }
        } else {
            System.out.println(getString("MESSAGE_FAILED_TO_CONNECT_TO_LAUNCHERMETA"));
        }
    }

    private static void method(boolean installNatives, boolean installLibraries, File tempNatives, File librariesDir, File nativesDir, JSONObject headVersionFile) {
        if (installLibraries) {
            String textx = getString("MESSAGE_INSTALL_DOWNLOADING_LIBRARIES");
            System.out.println(textx);
            try {
                JSONArray librariesJa = headVersionFile.optJSONArray("libraries");
                if (librariesJa != null) {
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

                            JSONObject downloadsJo1 = jsonObject.optJSONObject("downloads");
                            if (meet && downloadsJo1 != null) {
                                JSONObject artifactJo = downloadsJo1.optJSONObject("artifact");
                                if (artifactJo != null) {
                                    String path = artifactJo.optString("path");
                                    String url1 = artifactJo.optString("url");
                                    if (!isEmpty(path) && !isEmpty(url1)) {
                                        try {
                                            File file = new File(librariesDir, path);
                                            file.getParentFile().mkdirs();
                                            if (!file.exists()) {
                                                file.createNewFile();
                                            }
                                            if (file.length() == 0) {
                                                String textxx = String.format(getString("MESSAGE_DOWNLOADING_FILE"), url1.substring(url1.lastIndexOf("/") + 1));
                                                //print(VersionInstaller.class,textxx);
                                                System.out.print(textxx);
                                                downloadFile(url1, file, new XProgressBar());
                                            }
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                            String textxx = String.format(getString("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARY"), url1, e1);
                                            //print(VersionInstaller.class,textxx);
                                            System.out.println(textxx);
                                        }
                                    }
                                }


                                if (installNatives) {
                                    JSONObject classifiersJo = downloadsJo1.optJSONObject("classifiers");
                                    if (classifiersJo != null) {
                                        JSONObject nativesNamesJO = jsonObject.optJSONObject("natives");

                                        if (nativesNamesJO != null) {

                                            //String osName = System.getProperty("os.name");
                                            JSONObject nativesJo = classifiersJo.optJSONObject(nativesNamesJO.optString(OperatingSystem.CURRENT_OS.getCheckedName()));

                                            if (nativesJo != null) {
                                                String name12 = Utils.getNativeLibraryName(nativesJo.optString("path"));
                                                //System.out.println(Arrays.toString(nativesNames.toArray())+","+name );
                                                if (!nativesNames.contains(name12)) {
                                                    String url1 = nativesJo.optString("url");
                                                    try {
                                                        if (!isEmpty(url1)) {
                                                            File nativeFile = new File(tempNatives, url1.substring(url1.lastIndexOf("/") + 1));
                                                            //if(!nativeFile.exists()) {
                                                            nativeFile.createNewFile();
                                                            String textxx = String.format(getString("MESSAGE_DOWNLOADING_FILE"), url1.substring(url1.lastIndexOf("/") + 1));
                                                            //print(VersionInstaller.class,textxx);
                                                            System.out.print(textxx);
                                                            downloadFile(url1, nativeFile, new XProgressBar());
                                                            nativesNames.add(name12);
                                                            //}
                                                        }
                                                    } catch (Exception e1) {
                                                        e1.printStackTrace();
                                                        String textxx = String.format(getString("MESSAGE_FAILED_DOWNLOAD_FILE"), url1);
                                                        //print(VersionInstaller.class,textxx);
                                                        System.out.println(textxx);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                    System.out.println(getString("MESSAGE_INSTALL_DOWNLOADED_LIBRARIES"));
                } else {
                    System.out.println(getString("MESSAGE_INSTALL_LIBRARIES_LIST_EMPTY"));
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                System.out.println(String.format(getString("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARIES"), e1));
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
                        String textxxx = String.format(getString("MESSAGE_UNZIPPING_FILE"), file.getName());
                        //print(VersionInstaller.class,text);
                        System.out.print(textxxx);
                        unZip(file, dir, new XProgressBar());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        System.out.println(String.format(getString("MESSAGE_FAILED_TO_DECOMPRESS_FILE"), file.getAbsolutePath(), e1));
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
                    String textxxxxxx = String.format(getString("MESSAGE_COPYING_FILE"), file.getName(), to.getPath());
                    //print(VersionInstaller.class,text);
                    System.out.println(textxxxxxx);
                    Utils.copyFile(file, to);
                } catch (IOException e1) {
                    e1.printStackTrace();
                    System.out.println(String.format(getString("MESSAGE_FAILED_TO_COPY_FILE"), file.getAbsolutePath(), to.getAbsolutePath(), e1));
                }
            }

            Utils.deleteDirectory(tempNatives);
            System.out.println(getString("MESSAGE_INSTALL_COPIED_NATIVE_LIBRARIES"));
        }
        System.out.println(getString("MESSAGE_INSTALLED_NEW_VERSION"));
    }


    private static boolean checkNetwork() {
        try {
            URL url = new URL("https://launchermeta.mojang.com");
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
