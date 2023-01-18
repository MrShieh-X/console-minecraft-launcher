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
package com.mrshiehx.cmcl.modules.version.downloaders;

import com.mrshiehx.cmcl.api.download.DownloadSource;
import com.mrshiehx.cmcl.modules.MinecraftLauncher;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.cmcl.version.VersionUtils;
import com.mrshiehx.cmcl.utils.console.PercentageTextProgress;
import com.mrshiehx.cmcl.utils.internet.DownloadUtils;
import com.mrshiehx.cmcl.utils.system.OperatingSystem;
import com.mrshiehx.cmcl.utils.system.SystemUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.isEmpty;

public class NativesDownloader {
    public static void download(File versionDir, List<JSONObject> librariesJa) {
        File tempNatives = new File(".cmcl", "temp_natives");
        tempNatives.mkdirs();

        try {
            if (librariesJa != null) {
                List<String> nativesNames = new ArrayList<>();
                for (JSONObject jsonObject : librariesJa) {
                    boolean meet = true;
                    JSONArray rules = jsonObject.optJSONArray("rules");
                    if (rules != null) {
                        meet = MinecraftLauncher.isMeetConditions(rules, false, false);
                    }

                    JSONObject downloadsJo1 = jsonObject.optJSONObject("downloads");
                    if (meet && downloadsJo1 != null) {

                        JSONObject classifiersJo = downloadsJo1.optJSONObject("classifiers");
                        if (classifiersJo != null) {
                            JSONObject nativesNamesJO = jsonObject.optJSONObject("natives");

                            if (nativesNamesJO != null) {
                                JSONObject nativesJo = classifiersJo.optJSONObject(nativesNamesJO.optString(OperatingSystem.CURRENT_OS.getCheckedName()).replace("${arch}", SystemUtils.getArchInt()));

                                downloadSingleNative(tempNatives, nativesJo, nativesNames);
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
            System.out.println(getString("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARIES", e1));
        }


        File nativesDir = new File(versionDir, VersionUtils.getNativesDirName());
        unzip(tempNatives, nativesDir);
    }

    public static void unzip(File tempNatives, File nativesDir) {
        File[] natives = tempNatives.listFiles((dir, name1) -> name1.endsWith(".jar"));
        if (natives == null || natives.length == 0) {
            System.out.println(getString("MESSAGE_INSTALL_NATIVES_EMPTY_JAR"));
            return;
        }
        System.out.println(getString("MESSAGE_INSTALL_DECOMPRESSING_NATIVE_LIBRARIES"));
        nativesDir.mkdirs();
        for (File file : natives) {
            try {
                //File dir = new File(tempNatives, file.getName().substring(0, file.getName().lastIndexOf(".")));
                //dir.mkdirs();

                System.out.print(getString("MESSAGE_UNZIPPING_FILE", file.getName()));
                FileUtils.unZip(file, nativesDir, new PercentageTextProgress(), string -> {
                    String s = Utils.getExtension(string);
                    return isEmpty(s) || (!isEmpty(s) && !s.equals("sha1") && !s.equals("git"));
                });
            } catch (Exception e1) {
                e1.printStackTrace();
                System.out.println(getString("MESSAGE_FAILED_TO_DECOMPRESS_FILE", file.getAbsolutePath(), e1));
            }
        }
        System.out.println(getString("MESSAGE_INSTALL_DECOMPRESSED_NATIVE_LIBRARIES"));

        /*List<File> libFiles = new ArrayList<>();
        String houzhui = ".so";

        String osName = System.getProperty("os.name");

        if (osName.toLowerCase().contains("windows")) {
            houzhui = ".dll";
        } else if (osName.toLowerCase().contains("mac")) {
            houzhui = ".dylib";
        }

        File[] var4 = tempNatives.listFiles((dir, name12) -> dir.exists() && dir.isDirectory());

        if (var4 == null || var4.length == 0) {
            System.out.println(getString("MESSAGE_INSTALL_NATIVES_EMPTY_NATIVE_FILE"));
            return;

        }

        for (File file : var4) {
            if (file != null && file.isDirectory()) {
                String finalHouzhui = houzhui;
                File[] files = file.listFiles((dir, name12) -> name12.toLowerCase().endsWith(finalHouzhui) || name12.toLowerCase().endsWith(".jnilib"));
                libFiles.addAll(Arrays.asList(files));
            }
        }

        for (File file : libFiles) {
            File to = new File(nativesDir, file.getName());
            try {

                System.out.println(getString("MESSAGE_COPYING_FILE", file.getName(), to.getPath()));
                if (to.exists()) to.delete();
                Utils.copyFile(file, to);
            } catch (IOException e1) {
                e1.printStackTrace();
                System.out.println(getString("MESSAGE_FAILED_TO_COPY_FILE", file.getAbsolutePath(), to.getAbsolutePath(), e1));
            }
        }

        System.out.println(getString("MESSAGE_INSTALL_COPIED_NATIVE_LIBRARIES"));*/
        FileUtils.deleteDirectory(tempNatives);
    }

    public static void downloadSingleNative(File tempNatives, JSONObject nativesJo, List<String> nativesNames) {
        if (nativesJo != null) {
            String name12 = VersionUtils.getNativeLibraryName(nativesJo.optString("path"));
            if (!nativesNames.contains(name12)) {
                String url1 = nativesJo.optString("url");
                if (!isEmpty(url1)) {
                    url1 = url1.replace("https://libraries.minecraft.net/", DownloadSource.getProvider().libraries());
                    File nativeFile = new File(tempNatives, url1.substring(url1.lastIndexOf("/") + 1));
                    try {
                        //if(!nativeFile.exists()) {
                        //nativeFile.createNewFile();

                        System.out.print(getString("MESSAGE_DOWNLOADING_FILE", url1.substring(url1.lastIndexOf("/") + 1)));
                        DownloadUtils.downloadFile(url1, nativeFile, new PercentageTextProgress());
                        nativesNames.add(name12);
                        //}
                    } catch (Exception e1) {
                        //Utils.downloadFileFailed(url1, nativeFile, e1);
                        System.out.println(getString("MESSAGE_FAILED_DOWNLOAD_FILE_WITH_REASON", url1, e1));
                    }
                }
            }
        }
    }
}
