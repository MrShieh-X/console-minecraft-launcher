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
 *
 */

package com.mrshiehx.cmcl.bean;

import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONObject;

public class VersionInfo {
    public static final String SIGN_WORKING_DIRECTORY_IN_VERSION_DIR = "&*/\\$%";
    public final String workingDirectory;

    public VersionInfo(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public static VersionInfo valueOf(JSONObject origin) {
        return new VersionInfo(origin.optString("workingDirectory"));
    }

    public static VersionInfo valueOfHMCL(JSONObject origin) {
        //workingDirectory
        String workingDirectory = null;
        {
            int gameDirType = origin.optInt("gameDirType");
            if (gameDirType == 1) {
                workingDirectory = SIGN_WORKING_DIRECTORY_IN_VERSION_DIR;
            } else if (gameDirType == 2) {
                workingDirectory = origin.optString("gameDir");
            }
        }
        return new VersionInfo(workingDirectory);
    }

    public VersionInfo merge(VersionInfo versionInfo) {
        return new VersionInfo(!Utils.isEmpty(versionInfo.workingDirectory) ? versionInfo.workingDirectory : workingDirectory);
    }
}
