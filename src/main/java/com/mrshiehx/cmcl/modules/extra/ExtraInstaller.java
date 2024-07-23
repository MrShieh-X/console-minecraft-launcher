/*
 * Console Minecraft Launcher
 * Copyright (C) 2021-2024  MrShiehX
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

package com.mrshiehx.cmcl.modules.extra;

import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.modules.version.downloaders.LibrariesDownloader;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.cmcl.version.VersionUtils;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.isEmpty;

public abstract class ExtraInstaller {

    protected abstract String getExtraName();

    protected abstract ExtraMerger getExtraMerger();

    protected abstract boolean checkInstallable(JSONObject gameJSON);

    public boolean install(File jsonFile, File jarFile, @Nullable String extraVersion) {
        String fileContent;
        try {
            fileContent = FileUtils.readFileContent(jsonFile);
        } catch (Exception e) {
            if (Constants.isDebug()) e.printStackTrace();
            System.out.println(Utils.getString("EXCEPTION_READ_FILE_WITH_PATH", jsonFile.getAbsoluteFile()));
            return false;
        }
        JSONObject gameJSON;
        try {
            gameJSON = new com.mrshiehx.cmcl.utils.json.XJSONObject(fileContent);
        } catch (Exception e) {
            if (Constants.isDebug()) e.printStackTrace();
            System.out.println(Utils.getString("EXCEPTION_PARSE_FILE_WITH_PATH", jsonFile.getAbsoluteFile()));
            return false;
        }

        if (!checkInstallable(gameJSON))
            return false;


        String mcVersion = VersionUtils.getGameVersion(gameJSON, jarFile).id;
        if (isEmpty(mcVersion)) {
            System.out.println(getString("INSTALL_MODLOADER_EMPTY_MC_VERSION", getExtraName()));
            return false;
        }

        Pair<Boolean, List<JSONObject>> pair = getExtraMerger().merge(mcVersion, gameJSON, jarFile, false, extraVersion);
        if (pair.getKey()) {
            List<JSONObject> list = pair.getValue();
            if (list != null && list.size() > 0) {
                for (JSONObject library : list) {
                    LibrariesDownloader.downloadSingleLibrary(library);
                }
            }
            try {
                FileUtils.writeFile(jsonFile, gameJSON.toString(2), false);
                System.out.println(getString("INSTALLED_MODLOADER", getExtraName()));
                return true;
            } catch (Exception e) {
                if (Constants.isDebug()) e.printStackTrace();
                System.out.println(getString("INSTALL_MODLOADER_FAILED_WITH_REASON", getExtraName(), getString("EXCEPTION_WRITE_FILE_WITH_PATH", jsonFile.getAbsolutePath())));
                return false;
            }
        } else return false;
    }
}
