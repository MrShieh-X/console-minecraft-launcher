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

import com.mrshiehx.cmcl.bean.Library;
import com.mrshiehx.cmcl.modules.MinecraftLauncher;
import com.mrshiehx.cmcl.utils.XProgressBar;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.*;

public class DownloadLackLibraries {
    public static void downloadLackLibraries(List<Library> list) {
        try {
            //downloadFile(url, jarFile, progressBar);
            File librariesDir = new File(gameDir, "libraries");
            //System.out.println(assetsDir.getAbsolutePath());
            //System.out.println(assetsDir.getAbsolutePath());
            librariesDir.mkdirs();

            System.out.println(getString("MESSAGE_INSTALL_DOWNLOADING_LIBRARIES"));
            if (list != null) {
                for (Library library : list) {
                    JSONObject jsonObject = library.libraryJSONObject;
                    if (jsonObject != null) {
                        boolean meet = true;
                        JSONArray rules = jsonObject.optJSONArray("rules");
                        if (rules != null) {
                            meet = MinecraftLauncher.isMeetConditions(rules, false, false);
                        }
                        //System.out.println(meet);

                        JSONObject downloadsJo = jsonObject.optJSONObject("downloads");
                        if (meet && downloadsJo != null) {
                            JSONObject artifactJo = downloadsJo.optJSONObject("artifact");
                            if (artifactJo != null) {
                                String path = artifactJo.optString("path");
                                String url = artifactJo.optString("url");
                                if (!isEmpty(path) && !isEmpty(url)) {
                                    try {
                                        File file = new File(librariesDir, path);
                                        file.getParentFile().mkdirs();
                                        if (!file.exists()) {
                                            file.createNewFile();
                                        }
                                        if (file.length() == 0) {
                                            String text = String.format(getString("MESSAGE_DOWNLOADING_FILE"), url.substring(url.lastIndexOf("/") + 1));
                                            System.out.print(text);
                                            downloadFile(url, file, new XProgressBar());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        System.out.println(String.format(getString("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARY"), url, e));
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

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(String.format(getString("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARIES"), ex));
        }
    }
}
