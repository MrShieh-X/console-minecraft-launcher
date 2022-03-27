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
package com.mrshiehx.cmcl.modules.account.skin;

import com.mrshiehx.cmcl.ConsoleMinecraftLauncher;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.authlib.AuthlibInjectorProvider;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.*;

public class SkinDownloader {
    public static void start(File file, JSONObject account) {
        if (file != null) {
            String uuid = account.optString("uuid");
            if (!isEmpty(uuid)) {
                try {
                    URL url = new URL(account.optInt("loginMethod") == 2 ? ("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid) : new AuthlibInjectorProvider(account.optString("url")).getProfilePropertiesURL(uuid));
                    JSONObject result = new JSONObject(Utils.httpURLConnection2String((HttpURLConnection) url.openConnection()));
                    JSONArray properties = result.getJSONArray("properties");
                    for (int i = 0; i < properties.length(); i++) {
                        JSONObject jsonObject1 = properties.optJSONObject(i);
                        if (jsonObject1 != null) {
                            if (jsonObject1.optString("name").equals("textures")) {
                                JSONObject jsonObject2 = new JSONObject(new String(Base64.getDecoder().decode(jsonObject1.optString("value"))));
                                JSONObject var = jsonObject2.optJSONObject("textures");
                                if (var.has("SKIN")) {
                                    ConsoleMinecraftLauncher.downloadFile(var.optJSONObject("SKIN").optString("url"), file);
                                } else {
                                    System.out.println(getString("MESSAGE_DOWNLOAD_SKIN_FILE_NOT_SET_TEXT"));
                                }
                                break;
                            }
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    System.out.println(getString("CONSOLE_FAILED_OPERATE") + exception);
                }
            } else {
                System.out.println(getString("CONSOLE_FAILED_OPERATE") + getString("MESSAGE_UUID_ACCESSTOKEN_EMPTY"));
            }
        }
    }
}
