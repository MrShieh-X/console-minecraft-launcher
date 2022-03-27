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
import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.modules.MinecraftLauncher;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.PercentageTextProgress;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.*;

public class DownloadLackLibraries {
    public static void downloadLackLibraries(List<Library> list) {
        try {
            System.out.println(getString("MESSAGE_INSTALL_DOWNLOADING_LIBRARIES"));
            if (list != null) {
                for (Library library : list) {
                    JSONObject jsonObject = library.libraryJSONObject;
                    downloadSingleLibrary(jsonObject);
                }
                System.out.println(getString("MESSAGE_INSTALL_DOWNLOADED_LIBRARIES"));
            } else {
                System.out.println(getString("MESSAGE_INSTALL_LIBRARIES_LIST_EMPTY"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(getString("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARIES", ex));
        }
    }

    public static void downloadSingleLibrary(JSONObject library) {
        if (library == null) return;
        boolean meet = true;
        JSONArray rules = library.optJSONArray("rules");
        if (rules != null) {
            meet = MinecraftLauncher.isMeetConditions(rules, false, false);
        }

        if (!meet) return;
        Pair<String, String> pair = Utils.getLibraryDownloadURLAndStoragePath(library);

        if (pair == null) return;
        String pairValue = pair.getValue();
        try {
            File file = new File(librariesDir, pairValue);
            Utils.createFile(file, false);
            if (file.length() == 0) {
                System.out.print(getString("MESSAGE_DOWNLOADING_FILE", pairValue.substring(pairValue.lastIndexOf("/") + 1)));
                downloadFile(pair.getKey(), file, new PercentageTextProgress());
            }
        } catch (Exception e) {
            System.out.println(getString("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_LIBRARY", pair.getKey(), e));
        }
    }
}

