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

package com.mrshiehx.cmcl.modules.modLoaders;

import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.modules.version.LibrariesDownloader;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.*;

public abstract class ModLoaderInstaller {

    protected abstract String getModLoaderName();

    protected abstract ModLoaderMerger getModLoaderMerger();

    protected abstract boolean checkInstalled(JSONObject gameJSON);

    public void install(File jsonFile, File jarFile) {
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

        if (!checkInstalled(gameJSON)) return;


        String mcVersion = Utils.getGameVersion(gameJSON, jarFile).id;
        if (isEmpty(mcVersion)) {
            System.out.println(getString("INSTALL_MODLOADER_EMPTY_MC_VERSION", getModLoaderName()));
            return;
        }

        Pair<Boolean, List<JSONObject>> pair = getModLoaderMerger().merge(mcVersion, gameJSON, jarFile, false);
        if (pair.getKey()) {
            List<JSONObject> list = pair.getValue();
            if (list != null && list.size() > 0) {
                for (JSONObject library : list) {
                    LibrariesDownloader.downloadSingleLibrary(library);
                }
            }
            try {
                Utils.writeFile(jsonFile, gameJSON.toString(2), false);
                System.out.println(getString("INSTALLED_MODLOADER", getModLoaderName()));
            } catch (Exception e) {
                System.out.println(getString("INSTALL_MODLOADER_FAILED_WITH_REASON", getModLoaderName(), getString("EXCEPTION_WRITE_FILE_WITH_PATH", jsonFile.getAbsolutePath())));
            }
        }
    }
}
