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

import com.mrshiehx.cmcl.modules.MinecraftLauncher;
import com.mrshiehx.cmcl.utils.OperatingSystem;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.XProgressBar;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.*;

public class NativesReDownloader {
    public static void reDownload(File versionDir, JSONArray librariesJa) {
        File tempNatives = new File("cmcl", "temp_natives");
        tempNatives.mkdirs();

        System.out.println(getString("MESSAGE_INSTALL_DOWNLOADING_LIBRARIES"));
        try {
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

                            JSONObject classifiersJo = downloadsJo1.optJSONObject("classifiers");
                            if (classifiersJo != null) {
                                JSONObject nativesNamesJO = jsonObject.optJSONObject("natives");

                                if (nativesNamesJO != null) {

                                    //String osName = System.getProperty("os.name");
                                    JSONObject nativesJo = classifiersJo.optJSONObject(nativesNamesJO.optString(OperatingSystem.CURRENT_OS.getCheckedName()));
                                    ;


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
                System.out.println(getString("MESSAGE_INSTALL_DOWNLOADED_LIBRARIES"));
            } else {
                System.out.println(getString("MESSAGE_INSTALL_LIBRARIES_LIST_EMPTY"));
                return;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            System.out.println(String.format(getString("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARIES"), e1));
        }


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

        File nativesDir = new File(versionDir, Utils.getNativesDirName());
        nativesDir.mkdirs();
        for (File file : libFiles) {
            File to = new File(nativesDir, file.getName());
            try {
                String textxxxxxx = String.format(getString("MESSAGE_COPYING_FILE"), file.getName(), to.getPath());
                //print(VersionInstaller.class,text);
                System.out.println(textxxxxxx);
                if (to.exists()) to.delete();
                Utils.copyFile(file, to);
            } catch (IOException e1) {
                e1.printStackTrace();
                System.out.println(String.format(getString("MESSAGE_FAILED_TO_COPY_FILE"), file.getAbsolutePath(), to.getAbsolutePath(), e1));
            }
        }

        Utils.deleteDirectory(tempNatives);
        System.out.println(getString("MESSAGE_INSTALL_COPIED_NATIVE_LIBRARIES"));
        System.out.println(getString("MESSAGE_REDOWNLOADED_NATIVES"));
    }
}
