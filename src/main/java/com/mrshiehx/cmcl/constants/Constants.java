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
package com.mrshiehx.cmcl.constants;

import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.cmcl.ManifestUtils;

import java.io.File;
import java.util.Objects;

public class Constants {
    public static final File DEFAULT_CONFIG_FILE = new File("cmcl.json");
    public static final String CMCL_VERSION_NAME = "2.1.1";
    public static final int CMCL_VERSION_CODE = 16;
    public static final String CLIENT_ID = getMicrosoftAuthenticationClientID();
    public static final String COPYRIGHT = "Copyright (C) 2021-2023  MrShiehX";
    public static final int INDENT_FACTOR = 2;//JsonObject转String的间隔
    public static final String SOURCE_CODE = "https://www.github.com/MrShieh-X/console-minecraft-launcher";
    public static boolean ECHO_OPEN_FOR_IMMERSIVE = true;
    public static final int DEFAULT_DOWNLOAD_THREAD_COUNT = 64;
    public static final String[] CHECK_FOR_UPDATES_INFORMATION_URLS = {
            "https://gitee.com/MrShiehX/console-minecraft-launcher/raw/master/new_version.json",
            "https://raw.githubusercontent.com/MrShieh-X/console-minecraft-launcher/master/new_version.json"
    };

    public static String getCurseForgeApiKey() {
        String s = System.getProperty("cmcl.curseforge.apikey");
        return !Utils.isEmpty(s) ? s : ManifestUtils.getString("CurseForge-ApiKey");
    }

    public static String getMicrosoftAuthenticationClientID() {
        String s = System.getProperty("cmcl.authentication.clientId");
        return !Utils.isEmpty(s) ? s : ManifestUtils.getString("Microsoft-Authentication-ClientID");
    }

    public static boolean isRelease() {
        return "true".equals(ManifestUtils.getString("Is-Release"));
    }

    public static boolean isDebug() {
        return !isRelease() || Objects.equals(System.getProperty("debug"), "true");
    }
}
