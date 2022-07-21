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
package com.mrshiehx.cmcl.constants;

import com.mrshiehx.cmcl.utils.ManifestUtils;
import com.mrshiehx.cmcl.utils.Utils;

import java.io.File;

public class Constants {
    public static final File configFile = new File("cmcl.json");
    public static final String CMCL_VERSION = "1.4.1";
    public static final String CLIENT_ID = getMicrosoftAuthenticationClientID();
    public static final String COPYRIGHT = "Copyright (C) 2021-2022  MrShiehX";
    public static final int INDENT_FACTOR = 2;//JsonObject转String的间隔
    public static final String SOURCE_CODE = "https://www.github.com/MrShieh-X/console-minecraft-launcher";
    public static boolean ECHO_OPEN_FOR_IMMERSIVE = true;
    public static final int DEFAULT_DOWNLOAD_THREAD_COUNT = 64;

    public static String getCurseForgeApiKey() {
        String s = System.getProperty("cmcl.curseforge.apikey");
        return !Utils.isEmpty(s) ? s : ManifestUtils.getString("CurseForge-ApiKey");
    }

    public static String getMicrosoftAuthenticationClientID() {
        String s = System.getProperty("cmcl.authentication.clientId");
        return !Utils.isEmpty(s) ? s : ManifestUtils.getString("Microsoft-Authentication-ClientID");
    }
}
