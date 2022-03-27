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

package com.mrshiehx.cmcl.modules.fabric;

import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.modules.version.DownloadLackLibraries;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.*;

/**
 * 将 Fabric 安装到本地版本（前提是目标版本没有安装 Fabric）
 **/
public class FabricInstaller {
    public static void install(File jsonFile) {
        String fileContent;
        try {
            fileContent = Utils.readFileContent(jsonFile);
        } catch (Exception e) {
            System.out.println(Utils.getString("EXCEPTION_READ_FILE_WITH_PATH", jsonFile.getAbsoluteFile()));
            return;
        }
        JSONObject gameJSON;
        try {
            gameJSON = new JSONObject(fileContent);
        } catch (Exception e) {
            System.out.println(Utils.getString("EXCEPTION_PARSE_FILE_WITH_PATH", jsonFile.getAbsoluteFile()));
            return;
        }
        JSONObject fabric = gameJSON.optJSONObject("fabric");
        if (fabric != null && !Utils.isEmpty(fabric.optString("version"))) {
            System.out.println(getString("INSTALL_FABRIC_ALREADY_INSTALL"));
            return;
        }
        String mcVersion = gameJSON.optString("id");
        if (isEmpty(mcVersion)) {
            System.out.println(getString("INSTALL_FABRIC_EMPTY_MC_VERSION"));
            return;
        }
        Pair<Boolean, List<JSONObject>> pair = FabricMerger.merge(mcVersion, gameJSON, false);
        if (pair.getKey()) {
            List<JSONObject> list = pair.getValue();
            if (list != null && list.size() > 0) {
                for (JSONObject library : list) {
                    DownloadLackLibraries.downloadSingleLibrary(library);
                }
            }
            try {
                Utils.writeFile(jsonFile, gameJSON.toString(), false);
                System.out.println(getString("INSTALLED_FABRIC"));
            } catch (Exception e) {
                System.out.println(getString("INSTALL_FABRIC_FAILED_WITH_REASON", getString("EXCEPTION_WRITE_FILE_WITH_PATH", jsonFile.getAbsolutePath())));
            }
        }
    }
}
