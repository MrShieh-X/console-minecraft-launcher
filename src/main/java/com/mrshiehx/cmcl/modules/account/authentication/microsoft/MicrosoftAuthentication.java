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
package com.mrshiehx.cmcl.modules.account.authentication.microsoft;

import com.mrshiehx.cmcl.exceptions.ExceptionWithDescription;
import com.mrshiehx.cmcl.server.MicrosoftAuthenticationServer;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.cmcl.AccountUtils;
import com.mrshiehx.cmcl.utils.internet.NetworkUtils;
import com.mrshiehx.cmcl.utils.json.JSONUtils;
import com.mrshiehx.cmcl.utils.system.SystemUtils;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.isEmpty;
import static com.mrshiehx.cmcl.constants.Constants.CLIENT_ID;
import static com.mrshiehx.cmcl.utils.internet.NetworkUtils.post;

public class MicrosoftAuthentication {
    public static final String ACCESS_TOKEN_URL = "https://login.live.com/oauth20_token.srf";

    public static JSONObject loginMicrosoftAccount() {
        System.out.println(Utils.getString("CONSOLE_LOGIN_MICROSOFT_WAIT_FOR_RESPONSE"));

        MicrosoftAuthenticationServer server = null;
        try {
            server = getServer();
        } catch (IOException ignore) {
        }

        if (server != null) {
            String url = "https://login.live.com/oauth20_authorize.srf?client_id=" + CLIENT_ID + "&response_type=code&scope=XboxLive.signin+offline_access&prompt=select_account&redirect_uri=" + server.getRedirectURI();
            SystemUtils.openLink(url);
            try {
                return onGotCode(server.getCode(), server.getRedirectURI());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println(getString("MESSAGE_UNABLE_TO_LOGIN_MICROSOFT"));
        }
        return null;

    }

    private static JSONObject onGotCode(String code, String rUrl) {
        try {
            String secondString = ("client_id=" + CLIENT_ID + "&grant_type=authorization_code" + "&scope=XboxLive.signin+offline_access" + "&client_id=" + CLIENT_ID + "&redirect_uri=" + rUrl + "&code=" + code);
            String first = post(ACCESS_TOKEN_URL, secondString, "application/x-www-form-urlencoded", null);
            JSONObject result = JSONUtils.parseJSONObject(first);
            if (result == null) {
                System.out.println(getString("MESSAGE_OFFICIAL_LOGIN_FAILED_TITLE"));
                return null;
            }
            if (result.has("error") || result.has("error_description")) {
                String var = result.optString("error_description");
                System.out.println(getString("ERROR_WITH_MESSAGE", result.optString("error"), var));
                return null;
            }

            String tokenType = result.optString("token_type");
            String refreshToken = result.optString("refresh_token");


            JSONObject mcFirst = continueAuthentication(refreshToken);
            if (mcFirst == null) return null;

            long expiresIn = mcFirst.optInt("expires_in") * 1000L + System.currentTimeMillis();
            String accessToken = mcFirst.optString("access_token");
            JSONObject profile = getProfile(tokenType, accessToken);
            if (profile == null) {
                System.out.println(getString("MESSAGE_OFFICIAL_LOGIN_FAILED_TITLE"));
                return null;
            }

            if (profile.has("error") || profile.has("errorMessage")) {
                String var = profile.optString("errorMessage");
                System.out.println(getString("ERROR_WITH_MESSAGE", profile.optString("error"), var));
                return null;
            }

            String accountID = mcFirst.optString("username");


            JSONObject account = new JSONObject();
            account.put("id", accountID);
            account.put("loginMethod", 2);
            account.put("accessToken", accessToken);
            account.put("refreshToken", refreshToken);//in 2.0
            account.put("expiresIn", expiresIn);//in 2.0
            account.put("tokenType", tokenType);
            account.put("uuid", profile.optString("id"));
            account.put("playerName", profile.optString("name"));
            return account;
        } catch (Exception e) {
            System.out.println(getString("MESSAGE_OFFICIAL_LOGIN_FAILED_TITLE") + ": " + e);
        }
        return null;
    }

    public static JSONObject continueAuthentication(String refreshToken) throws IOException {
        String secondSecond = "client_id=" + CLIENT_ID + "&refresh_token=" + refreshToken + "&grant_type=refresh_token"/* + "&redirect_uri=" + rUrl*/;
        String second = post(ACCESS_TOKEN_URL, secondSecond, "application/x-www-form-urlencoded", null);
        JSONObject result2 = JSONUtils.parseJSONObject(second);
        if (result2 == null) {
            System.out.println(getString("MESSAGE_OFFICIAL_LOGIN_FAILED_TITLE"));
            return null;
        }
        if (result2.has("error") || result2.has("error_description")) {
            String var = result2.optString("error_description");
            System.out.println(getString("ERROR_WITH_MESSAGE", result2.optString("error"), var));
            return null;
        }
        String xboxLive = post("https://user.auth.xboxlive.com/user/authenticate", "{\"Properties\":{\"AuthMethod\":\"RPS\",\"SiteName\":\"user.auth.xboxlive.com\",\"RpsTicket\":\"d=" + result2.optString("access_token") + "\"},\"RelyingParty\":\"http://auth.xboxlive.com\",\"TokenType\":\"JWT\"}", "application/json", "application/json");

        JSONObject xboxLiveFirst = JSONUtils.parseJSONObject(xboxLive);
        if (xboxLiveFirst == null || xboxLiveFirst.has("error")) {
            System.out.println(getString("MESSAGE_OFFICIAL_LOGIN_FAILED_TITLE"));
            return null;
        }
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

        JSONObject xstsResult = JSONUtils.parseJSONObject(post("https://xsts.auth.xboxlive.com/xsts/authorize", "{\"Properties\":{\"SandboxId\":\"RETAIL\",\"UserTokens\":[\"" + Token + "\"]},\"RelyingParty\":\"rp://api.minecraftservices.com/\",\"TokenType\":\"JWT\"}", "application/json", "application/json"));

        if (xstsResult == null || xstsResult.has("error")) {
            System.out.println(getString("MESSAGE_OFFICIAL_LOGIN_FAILED_TITLE"));
            return null;

        }
        String xstsToken = xstsResult.optString("Token");
        JSONObject mcFirst = JSONUtils.parseJSONObject(post("https://api.minecraftservices.com/authentication/login_with_xbox", "{\"identityToken\":\"XBL3.0 x=" + uhs + ";" + xstsToken + "\"}", "application/json", "application/json"));

        if (mcFirst == null || mcFirst.has("error")) {
            System.out.println(getString("MESSAGE_OFFICIAL_LOGIN_FAILED_MESSAGE"));
            return null;
        }
        return mcFirst;
    }

    public static JSONObject getProfile(String tokenType, String accessToken) throws IOException {
        return JSONUtils.parseJSONObject(NetworkUtils.getWithToken("https://api.minecraftservices.com/minecraft/profile", !isEmpty(tokenType) ? tokenType : "Bearer", accessToken));
    }

    private static MicrosoftAuthenticationServer getServer() throws IOException {
        MicrosoftAuthenticationServer server;
        IOException exception = null;
        for (int port : new int[]{29116, 29117, 29118, 29119, 29120, 29121, 29122, 29123, 29124, 29125, 29126}) {
            ////stem.ut.println(port);
            try {
                server = new MicrosoftAuthenticationServer(port);
                server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, true);
                return server;
            } catch (IOException e) {
                //e.printStackTrace();
                exception = e;
            }
        }
        throw exception;
    }

    public static boolean refresh(JSONObject selectedAccount, JSONArray accounts) throws ExceptionWithDescription {
        boolean needRefresh;
        JSONObject profile = null;
        try {
            profile = MicrosoftAuthentication.getProfile(selectedAccount.optString("tokenType", "Bearer"), selectedAccount.optString("accessToken"));
        } catch (IOException ignored) {
        }
        needRefresh = selectedAccount.optString("accessToken").isEmpty()
                || selectedAccount.optString("uuid").isEmpty()
                || selectedAccount.optString("playerName").isEmpty()
                || selectedAccount.optString("id").isEmpty()
                || System.currentTimeMillis() > selectedAccount.optLong("expiresIn")
                || profile == null
                || profile.has("error")
                || profile.has("errorMessage");
        if (!needRefresh) {
            selectedAccount.put("uuid", profile.optString("id"));
            selectedAccount.put("playerName", profile.optString("name"));
            return true;
        }

        String refreshToken = selectedAccount.optString("refreshToken");
        boolean needReLogin = refreshToken.isEmpty();

        if (needReLogin) {
            System.out.println(getString("MESSAGE_ACCOUNT_INFO_MISSING_NEED_RELOGIN"));
            JSONObject accountNew = MicrosoftAuthentication.loginMicrosoftAccount();
            if (accountNew == null) {
                throw new ExceptionWithDescription(null);
            }
            if (!accountNew.optString("id").equals(selectedAccount.optString("id"))) {
                throw new ExceptionWithDescription(getString("ACCOUNT_MICROSOFT_REFRESH_NOT_SAME"));
            }
            accountNew.put("selected", true);
            for (int i = 0; i < accounts.length(); i++) {
                JSONObject accountInFor = accounts.optJSONObject(i);
                if (!AccountUtils.isValidAccount(accountInFor)) continue;
                if (accountInFor.optBoolean("selected")) {
                    accounts.put(i, accountNew);
                    break;
                }
            }
            return true;
        }
        try {
            JSONObject auth = MicrosoftAuthentication.continueAuthentication(refreshToken);
            if (auth == null)
                throw new ExceptionWithDescription(null);
            long expiresIn = auth.optInt("expires_in") * 1000L + System.currentTimeMillis();
            String accessToken = auth.optString("access_token");
            JSONObject newProfile = MicrosoftAuthentication.getProfile(selectedAccount.optString("tokenType", "Bearer"), accessToken);
            if (newProfile == null) {
                throw new ExceptionWithDescription(getString("MESSAGE_OFFICIAL_LOGIN_FAILED_TITLE"));
            }
            if (newProfile.has("error") || newProfile.has("errorMessage")) {
                String var = newProfile.optString("errorMessage");
                throw new ExceptionWithDescription(getString("ERROR_WITH_MESSAGE", newProfile.optString("error"), var));
            }

            String accountID = auth.optString("username");
            selectedAccount.put("id", accountID);
            selectedAccount.put("accessToken", accessToken);
            selectedAccount.put("expiresIn", expiresIn);//in 2.0
            selectedAccount.put("uuid", newProfile.optString("id"));
            selectedAccount.put("playerName", newProfile.optString("name"));
            return true;
        } catch (Exception e) {
            //if(Constants.isDebug())e.printStackTrace();
            throw new ExceptionWithDescription(getString("MESSAGE_FAILED_REFRESH_TITLE") + (!Utils.isEmpty(Utils.valueOf(e)) ? (": " + e) : ""));
        }
    }

    /*public static boolean validate(long expiresIn, String tokenType, String accessToken) throws IOException {
        if (System.currentTimeMillis() > expiresIn) {
            return false;
        }
        JSONObject profile = getProfile(tokenType, accessToken);
        return profile != null && !profile.has("error") && !profile.has("errorMessage");
    }*/
}
