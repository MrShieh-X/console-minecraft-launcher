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

import com.mrshiehx.cmcl.microsoft.MicrosoftAuthenticationServer;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.Waiter;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.UUID;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.CLIENT_ID;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;
import static com.mrshiehx.cmcl.utils.Utils.post;

public class MicrosoftAccountLoginner {
    public static void loginMicrosoftAccount(JSONObject jsonObject) {
        System.out.println(Utils.getString("CONSOLE_LOGIN_MICROSOFT_WAIT_FOR_RESPONSE"));

        //new Thread(()->{
        Waiter w = new Waiter();
        MicrosoftAuthenticationServer server = null;
        MicrosoftAuthenticationServer.OnGotCode o = (code, rUrl) -> {
            //if (dialog.isVisible()) {
            //stem.ut.println(code);
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
                        System.out.println(String.format(String.format(getString("DIALOG_OFFICIAL_LOGIN_FAILED_TEXT"), result.optString("error"), var)));
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
                                System.out.println(String.format(getString("DIALOG_OFFICIAL_LOGIN_FAILED_TEXT"), result2.optString("error"), var));
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
                                            JSONObject mcSecond = Utils.parseJSONObject(Utils.get("https://api.minecraftservices.com/minecraft/profile", tokenType, access_token));

                                            if (mcSecond != null) {

                                                //stem.ut.println("microsoft_first : " + first);
                                                //stem.ut.println("microsoft_second: " + second);
                                                //stem.ut.println("xboxLive first  : " + xboxLive);
                                                //stem.ut.println("xstsResult first: " + xstsResult);
                                                //stem.ut.println("mc         first: " + mcFirst);
                                                //stem.ut.println("mc        second: " + mcSecond);
                                                if (mcSecond.has("error") || mcSecond.has("errorMessage")) {
                                                    String var = mcSecond.optString("errorMessage");
                                                    System.out.println(String.format(getString("DIALOG_OFFICIAL_LOGIN_FAILED_TEXT"), mcSecond.optString("error"), var));
                                                } else {
                                                    jsonObject.put("loginMethod", 2);
                                                    jsonObject.put("accessToken", access_token);
                                                    jsonObject.put("tokenType", tokenType);
                                                    jsonObject.put("uuid", mcSecond.optString("id"));
                                                    jsonObject.put("playerName", mcSecond.optString("name"));
                                                    Utils.saveConfig(jsonObject);
                                                    //((JLabel) playerName).setText(selectedProfileJo.optString("name"));
                                                    //dialog.setVisible(false);
                                                    //dialog.dispose();
                                                    System.out.println(getString("DIALOG_OFFICIAL_LOGINED_TITLE"));

                                                }
                                            } else {
                                                //stem.ut.println(849);
                                                System.out.println(getString("DIALOG_OFFICIAL_LOGIN_FAILED_TITLE"));
                                            }


                                        } else {
                                            //stem.ut.println(855);
                                            System.out.println(getString("DIALOG_OFFICIAL_LOGIN_FAILED_MESSAGE"));
                                        }
                                    } else {
                                        System.out.println(getString("DIALOG_OFFICIAL_LOGIN_FAILED_TITLE"));
                                        //stem.ut.println(859);
                                    }

                                } else {
                                    //stem.ut.println(864);
                                    System.out.println(getString("DIALOG_OFFICIAL_LOGIN_FAILED_TITLE"));
                                }
                            }
                        } else {
                            //stem.ut.println(869);
                            System.out.println(getString("DIALOG_OFFICIAL_LOGIN_FAILED_TITLE"));
                        }
                    }
                } else {
                    //stem.ut.println(874);
                    System.out.println(getString("DIALOG_OFFICIAL_LOGIN_FAILED_TITLE"));
                }
            } catch (Exception e) {
                //stem.ut.println(878);
                e.printStackTrace();
                System.out.println(getString("DIALOG_OFFICIAL_LOGIN_FAILED_TITLE"));
            }
            //dialog.setVisible(false);
            //dialog.dispose();
            //}
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


            //dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            //dialog.setAlwaysOnTop(true);
            //dialog.setModal(false);
            //dialog.setVisible(true);
        } else {
            System.out.println(getString("DIALOG_UNABLE_TO_LOGIN_MICROSOFT"));
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
                e.printStackTrace();
                exception = e;
            }
        }
        throw exception;
    }
}
