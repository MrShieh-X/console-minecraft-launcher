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
package com.mrshiehx.cmcl.modules.account.skin;

import com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.authlib.AuthlibInjectorApiProvider;
import com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.nide8auth.Nide8AuthApiProvider;
import com.mrshiehx.cmcl.utils.internet.DownloadUtils;
import com.mrshiehx.cmcl.utils.internet.NetworkUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.isEmpty;

public class SkinDownloader {
    public static void start(File file, JSONObject account) {
        String uuid = account.optString("uuid");
        if (isEmpty(uuid)) {
            System.out.println(getString("CONSOLE_FAILED_OPERATE") + getString("MESSAGE_UUID_ACCESSTOKEN_EMPTY"));
            return;
        }
        try {
            String urlString = "";
            switch (account.optInt("loginMethod")) {
                case 1:
                    urlString = new AuthlibInjectorApiProvider(account.optString("url")).getProfilePropertiesURL(uuid);
                    break;
                case 2:
                    urlString = "https://sessionserver.mojang.com/session/minecraft/profile/" + uuid;
                    break;
                case 3:
                    urlString = new Nide8AuthApiProvider(account.optString("serverId")).getProfilePropertiesURL(uuid);
                    break;
            }

            URL url = new URL(urlString);
            JSONObject result = new JSONObject(NetworkUtils.httpURLConnection2String((HttpURLConnection) url.openConnection()));
            JSONArray properties = result.getJSONArray("properties");
            for (int i = 0; i < properties.length(); i++) {
                JSONObject property = properties.optJSONObject(i);
                if (property == null || !property.optString("name").equals("textures")) continue;
                JSONObject value = new JSONObject(new String(Base64.getDecoder().decode(property.optString("value"))));
                JSONObject textures = value.optJSONObject("textures");
                if (textures.has("SKIN")) {
                    DownloadUtils.downloadFile(textures.optJSONObject("SKIN").optString("url"), file);
                } else {
                    System.out.println(getString("MESSAGE_DOWNLOAD_SKIN_FILE_NOT_SET_TEXT"));
                }
                return;
            }
            System.out.println(getString("MESSAGE_DOWNLOAD_SKIN_FILE_NOT_SET_TEXT"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(getString("CONSOLE_FAILED_OPERATE") + e);
        }
    }
}
