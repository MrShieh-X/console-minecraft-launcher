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

package com.mrshiehx.cmcl.modules.account.loginner;

import com.mrshiehx.cmcl.utils.ConsoleUtils;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.authlib.AuthlibInjectorProvider;
import com.mrshiehx.cmcl.utils.authlib.AuthlibUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;

public class AuthlibAccountLoginner {
    public static JSONObject login(String address, String username, String password, boolean selected, JSONObject config) throws Exception {
        String url = AuthlibUtils.addHttpsIfMissing(address);
        /*serverName*/
        String serverName = "AuthlibServer";
        /*metadata存储时变成base64，启动时直接拿来用*/
        String metadata;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

            String ali = conn.getHeaderField("x-authlib-injector-api-location");
            if (ali != null) {
                URL absoluteAli = new URL(conn.getURL(), ali);
                if (!AuthlibUtils.urlEqualsIgnoreSlash(url, absoluteAli.toString())) {
                    conn.disconnect();
                    url = absoluteAli.toString();
                    conn = (HttpURLConnection) absoluteAli.openConnection();
                }
            }
            if (!url.endsWith("/"))
                url += "/";

            JSONObject firstRequest = new JSONObject(metadata = Utils.httpURLConnection2String(conn));
            JSONObject meta = firstRequest.optJSONObject("meta");
            if (meta != null) {
                serverName = meta.optString("serverName", serverName);
            }
        } catch (Exception e) {
            throw new Exception(getString("FAILED_TO_LOGIN_OTHER_AUTHENTICATION_ACCOUNT_UNAVAILABLE_SERVER"));
        }


        try {
            AuthlibInjectorProvider provider = new AuthlibInjectorProvider(url);
            String authenticationURL = provider.getAuthenticationURL();
            JSONObject request = new JSONObject();
            request.put("agent", new JSONObject().put("name", "Minecraft").put("version", 1));
            request.put("username", username);
            request.put("password", password);
            request.put("clientToken", UUID.randomUUID().toString().replace("-", ""));
            request.put("requestUser", true);
            JSONObject firstResponse = Utils.parseJSONObject(Utils.post(authenticationURL, request.toString()));
            if (firstResponse == null) {
                System.out.println(getString("CONSOLE_FAILED_REFRESH_OFFICIAL_NO_RESPONSE"));
                return null;
            }

            if (firstResponse.has("error")) {
                if (firstResponse.has("errorMessage")) {
                    Utils.printfln(getString("FAILED_TO_LOGIN_OTHER_AUTHENTICATION_ACCOUNT"), "\n" + firstResponse.optString("error") + ": " + firstResponse.optString("errorMessage"));
                } else {
                    Utils.printfln(getString("FAILED_TO_LOGIN_OTHER_AUTHENTICATION_ACCOUNT"), firstResponse.optString("error"));
                }
                return null;
            }
            //System.out.println(firstResponse);
            /*clientToken*/
            String clientToken = firstResponse.optString("clientToken");

            /*playerName*/
            String playerName;
            /*uuid*/
            String uuid;


            JSONObject selectedProfile = firstResponse.optJSONObject("selectedProfile");
            if (selectedProfile != null) {
                playerName = selectedProfile.optString("name");
                uuid = selectedProfile.optString("id");
            } else {
                List<JSONObject> availableProfiles = Utils.jsonArrayToJSONObjectList(firstResponse.optJSONArray("availableProfiles"));
                if (availableProfiles.size() > 0) {
                    for (int i = 0; i < availableProfiles.size(); i++) {
                        JSONObject jsonObject = availableProfiles.get(i);
                        System.out.println((i + 1) + "." + jsonObject.optString("name"));
                    }
                    int number = ConsoleUtils.inputInt(Utils.getString("MESSAGE_AUTHLIB_INJECTOR_LOGIN_SELECT_PROFILE", 1, availableProfiles.size()), 1, availableProfiles.size());

                    if (number != Integer.MAX_VALUE) {
                        JSONObject profile = availableProfiles.get(number - 1);
                        playerName = profile.optString("name");
                        uuid = profile.optString("id");
                    } else {
                        return null;
                    }
                } else {
                    System.out.println(getString("FAILED_TO_LOGIN_OAA_NO_SELECTED_CHARACTER"));
                    return null;
                }
            }

            JSONObject user = firstResponse.optJSONObject("user");
            boolean did = false;
            JSONObject propertiesJO = new JSONObject();
            if (user != null) {
                JSONObject properties = user.optJSONObject("properties");
                if (properties != null) {
                    for (Map.Entry<String, Object> entry : properties.toMap().entrySet()) {
                        did = true;
                        propertiesJO.put(entry.getKey(), new JSONArray().put(entry));
                    }
                }
            }





            /*accessToken*/
            String accessToken = firstResponse.optString("accessToken");

            JSONArray accounts = config.optJSONArray("accounts");
            int indexOf = -1;
            if (accounts != null) {
                for (int i = 0; i < accounts.length(); i++) {
                    JSONObject jsonObject1 = accounts.optJSONObject(i);
                    if (jsonObject1 != null) {
                        if (jsonObject1.optInt("loginMethod") == 1 &&
                                AuthlibUtils.urlEqualsIgnoreSlash(url, jsonObject1.optString("url")) &&
                                Objects.equals(uuid, jsonObject1.optString("uuid")) && Utils.isValidAccount(jsonObject1)) {
                            indexOf = i;
                            break;
                        } else {
                            if (selected) jsonObject1.put("selected", false);
                        }
                    }
                }
            } else {
                config.put("accounts", accounts = new JSONArray());
            }

            JSONObject account = new JSONObject();
            account.put("selected", selected);
            account.put("loginMethod", 1);

            account.put("uuid", uuid);
            account.put("accessToken", accessToken);
            account.put("playerName", playerName);

            account.put("serverName", serverName);
            account.put("metadataEncoded", Base64.getEncoder().encodeToString(metadata.getBytes(StandardCharsets.UTF_8)));
            account.put("clientToken", clientToken);
            account.put("url", url);


            if (did) {
                account.put("properties", propertiesJO);
            }


            if (indexOf < 0) {
                accounts.put(account);
                config.put("accounts", accounts);
                Utils.saveConfig(config);
                System.out.println(getString("MESSAGE_LOGINED_TITLE"));
            } else {
                if (ConsoleUtils.yesOrNo(String.format(getString("CONSOLE_REPLACE_LOGGED_ACCOUNT"), indexOf))) {
                    accounts.put(indexOf, account);
                    config.put("accounts", accounts);
                    Utils.saveConfig(config);
                    System.out.println(getString("MESSAGE_LOGINED_TITLE"));
                }
            }
            return account;

        } catch (Exception e) {
            throw new Exception(getString("FAILED_TO_LOGIN_OTHER_AUTHENTICATION_ACCOUNT_FAILED_AUTHENTICATE"));
        }
    }
}