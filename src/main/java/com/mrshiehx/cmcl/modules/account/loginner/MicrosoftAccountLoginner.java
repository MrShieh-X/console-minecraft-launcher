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

import com.mrshiehx.cmcl.server.MicrosoftAuthenticationServer;
import com.mrshiehx.cmcl.utils.ConsoleUtils;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.Waiter;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;
import java.util.UUID;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.CLIENT_ID;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;
import static com.mrshiehx.cmcl.utils.Utils.post;

public class MicrosoftAccountLoginner {
    public static void loginMicrosoftAccount(JSONObject config, boolean select) {
        System.out.println(Utils.getString("CONSOLE_LOGIN_MICROSOFT_WAIT_FOR_RESPONSE"));

        //new Thread(()->{
        Waiter w = new Waiter();
        MicrosoftAuthenticationServer server = null;
        MicrosoftAuthenticationServer.OnGotCode o = (code, rUrl) -> {
            try {
                String secret = System.currentTimeMillis() + UUID.randomUUID().toString();
                String ACCESS_TOKEN_URL = "https://login.live.com/oauth20_token.srf" +
                        "?grant_type=authorization_code" +
                        "&scope=XboxLive.signin+offline_access" +
                        "&client_secret=" + secret +
                        "&client_id=" + CLIENT_ID +
                        "&redirect_uri=" + rUrl + "" +
                        "&code=" + code;
                String secondString = ("client_id=" + CLIENT_ID +
                        "&grant_type=authorization_code" +
                        "&scope=XboxLive.signin+offline_access" +
                        "&client_id=" + CLIENT_ID +
                        "&redirect_uri=" + rUrl + "" +
                        "&code=" + code);
                String first = post(ACCESS_TOKEN_URL, secondString, "application/x-www-form-urlencoded", null);
                JSONObject result = Utils.parseJSONObject(first);
                if (result != null) {
                    if (result.has("error") || result.has("error_description")) {
                        String var = result.optString("error_description");
                        System.out.println(getString("ERROR_WITH_MESSAGE", result.optString("error"), var));
                    } else {
                        String tokenType = result.optString("token_type");
                        //String scope=result.optString("scope");
                        //String access_token=result.optString("access_token");
                        String refresh_token = result.optString("refresh_token");
                        //String user_id=result.optString("user_id");
                        //long expires_in=result.optLong("expires_in");

                        String secondFirst = "https://login.live.com/oauth20_token.srf" +
                                "?client_id=" + CLIENT_ID +
                                "&client_secret=" + secret +
                                "&refresh_token=" + refresh_token +
                                "&grant_type=refresh_token" +
                                "&redirect_uri=" + rUrl;
                        String secondSecond =
                                "client_id=" + CLIENT_ID +
                                        "&refresh_token=" + refresh_token +
                                        "&grant_type=refresh_token" +
                                        "&redirect_uri=" + rUrl;
                        String second = post(secondFirst, secondSecond, "application/x-www-form-urlencoded", null);
                        JSONObject result2 = Utils.parseJSONObject(second);


                        if (result2 != null) {
                            if (result2.has("error") || result2.has("error_description")) {
                                String var = result2.optString("error_description");
                                System.out.println(getString("ERROR_WITH_MESSAGE", result2.optString("error"), var));
                            } else {
                                String xboxLive = post("https://user.auth.xboxlive.com/user/authenticate", "{\"Properties\":{\"AuthMethod\":\"RPS\",\"SiteName\":\"user.auth.xboxlive.com\",\"RpsTicket\":\"d=" + result2.optString("access_token") + "\"},\"RelyingParty\":\"http://auth.xboxlive.com\",\"TokenType\":\"JWT\"}", "application/json", "application/json");

                                JSONObject xboxLiveFirst = Utils.parseJSONObject(xboxLive);
                                if (xboxLiveFirst != null && !xboxLiveFirst.has("error")) {
                                    String Token = xboxLiveFirst.optString("Token");
                                    String uhs = "";
                                    JSONObject DisplayClaims = xboxLiveFirst.optJSONObject("DisplayClaims");
                                    if (DisplayClaims != null) {
                                        JSONArray xui = DisplayClaims.optJSONArray("xui");
                                        if (xui != null && xui.length() > 0) {
                                            JSONObject firsta = xui.optJSONObject(0);
                                            if (firsta != null) uhs = firsta.optString("uhs");
                                        }
                                    }

                                    JSONObject xstsResult = Utils.parseJSONObject(post("https://xsts.auth.xboxlive.com/xsts/authorize", "{\"Properties\":{\"SandboxId\":\"RETAIL\",\"UserTokens\":[\"" + Token + "\"]},\"RelyingParty\":\"rp://api.minecraftservices.com/\",\"TokenType\":\"JWT\"}", "application/json", "application/json"));

                                    if (xstsResult != null && !xstsResult.has("error")) {
                                        String xstsToken = xstsResult.optString("Token");
                                        JSONObject mcFirst = Utils.parseJSONObject(post("https://api.minecraftservices.com/authentication/login_with_xbox", "{\"identityToken\":\"XBL3.0 x=" + uhs + ";" + xstsToken + "\"}", "application/json", "application/json"));

                                        if (mcFirst != null && !mcFirst.has("error")) {
                                            String access_token = mcFirst.optString("access_token");
                                            JSONObject mcSecond = Utils.parseJSONObject(Utils.getWithToken("https://api.minecraftservices.com/minecraft/profile", tokenType, access_token));

                                            if (mcSecond != null) {

                                                /*System.out.println("microsoft_first : " + first);
                                                System.out.println("microsoft_second: " + second);
                                                System.out.println("xboxLive first  : " + xboxLive);
                                                System.out.println("xstsResult first: " + xstsResult);
                                                System.out.println("mc         first: " + mcFirst);
                                                System.out.println("mc        second: " + mcSecond);*/
                                                if (mcSecond.has("error") || mcSecond.has("errorMessage")) {
                                                    String var = mcSecond.optString("errorMessage");
                                                    System.out.println(getString("ERROR_WITH_MESSAGE", mcSecond.optString("error"), var));
                                                } else {


                                                    String accountID = mcFirst.optString("username");

                                                    JSONArray accounts = config.optJSONArray("accounts");
                                                    int indexOf = -1;
                                                    if (accounts != null) {
                                                        for (int i = 0; i < accounts.length(); i++) {
                                                            JSONObject jsonObject1 = accounts.optJSONObject(i);
                                                            if (jsonObject1 != null) {
                                                                if (jsonObject1.optInt("loginMethod") == 2 && Objects.equals(accountID, jsonObject1.optString("id")) && Utils.isValidAccount(jsonObject1)) {
                                                                    indexOf = i;
                                                                    break;
                                                                } else {
                                                                    if (select) jsonObject1.put("selected", false);
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        accounts = new JSONArray();
                                                    }

                                                    JSONObject account = new JSONObject();
                                                    account.put("selected", select);
                                                    account.put("id", accountID);
                                                    account.put("loginMethod", 2);
                                                    account.put("accessToken", access_token);
                                                    account.put("tokenType", tokenType);
                                                    account.put("uuid", mcSecond.optString("id"));
                                                    account.put("playerName", mcSecond.optString("name"));


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

                                                }
                                            } else {
                                                System.out.println(getString("MESSAGE_OFFICIAL_LOGIN_FAILED_TITLE"));
                                            }


                                        } else {
                                            System.out.println(getString("MESSAGE_OFFICIAL_LOGIN_FAILED_MESSAGE"));
                                        }
                                    } else {
                                        System.out.println(getString("MESSAGE_OFFICIAL_LOGIN_FAILED_TITLE"));
                                    }

                                } else {
                                    System.out.println(getString("MESSAGE_OFFICIAL_LOGIN_FAILED_TITLE"));
                                }
                            }
                        } else {
                            System.out.println(getString("MESSAGE_OFFICIAL_LOGIN_FAILED_TITLE"));
                        }
                    }
                } else {
                    System.out.println(getString("MESSAGE_OFFICIAL_LOGIN_FAILED_TITLE"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(getString("MESSAGE_OFFICIAL_LOGIN_FAILED_TITLE"));
            }
            w.stop();
        };
        try {
            server = getServer(o);
        } catch (Exception e) {
            //e.printStackTrace();
        }

        if (server != null) {
            String url = "";
            try {
                url = "https://login.live.com/oauth20_authorize.srf" +
                        "?client_id=" + CLIENT_ID +
                        "&response_type=code" +
                        "&scope=XboxLive.signin+offline_access" +
                        "&prompt=select_account" +
                        "&redirect_uri=" + (server.getRedirectURI());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Utils.openLink(url);
            w.startWait();

        } else {
            System.out.println(getString("MESSAGE_UNABLE_TO_LOGIN_MICROSOFT"));
        }
        //}).start();

    }

    private static MicrosoftAuthenticationServer getServer(MicrosoftAuthenticationServer.OnGotCode o) throws Exception {
        MicrosoftAuthenticationServer server;
        Exception exception = null;
        for (int port : new int[]{29116, 29117, 29118, 29119, 29120, 29121, 29122, 29123, 29124, 29125, 29126}) {
            ////stem.ut.println(port);
            try {
                server = new MicrosoftAuthenticationServer(port, o);
                server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, true);
                return server;
            } catch (Exception e) {
                //e.printStackTrace();
                exception = e;
            }
        }
        throw exception;
    }
}
