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

package com.mrshiehx.cmcl.utils.authlib;

import com.mrshiehx.cmcl.ConsoleMinecraftLauncher;
import com.mrshiehx.cmcl.api.download.DownloadSource;
import com.mrshiehx.cmcl.utils.PercentageTextProgress;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONObject;

import java.io.File;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;

public class AuthlibUtils {

    public static File getAuthlibInjector() throws Exception {
        File cmcl = new File("cmcl");
        cmcl.mkdirs();
        File file = new File(cmcl, "authlib-injector.jar");
        if (file.exists() && file.isFile() && file.length() > 0) return file;
        JSONObject latest = new JSONObject(Utils.getWithToken(DownloadSource.getProvider().authlibInjector()));
        System.out.print(getString("MESSAGE_DOWNLOADING_FILE", "authlib-injector.jar"));
        ConsoleMinecraftLauncher.downloadFile(latest.optString("download_url"), file, new PercentageTextProgress());
        return file;
    }


    public static String addHttpsIfMissing(String url) {
        String lower = url.toLowerCase();
        if (!lower.startsWith("https://") && !lower.startsWith("http://")) {
            url = "https://" + url;
        }
        return url;
    }

    public static boolean urlEqualsIgnoreSlash(String a, String b) {
        if (!a.endsWith("/"))
            a += "/";
        if (!b.endsWith("/"))
            b += "/";
        return a.equals(b);
    }
}
