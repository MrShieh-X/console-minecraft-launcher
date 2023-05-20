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

package com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.authlib;

import com.mrshiehx.cmcl.CMCL;
import com.mrshiehx.cmcl.api.download.DownloadSource;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.exceptions.DescriptionException;
import com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.YggdrasilAuthentication;
import com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.YggdrasilAuthenticationApiProvider;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.cmcl.AccountUtils;
import com.mrshiehx.cmcl.utils.console.PercentageTextProgress;
import com.mrshiehx.cmcl.utils.internet.DownloadUtils;
import com.mrshiehx.cmcl.utils.internet.NetworkUtils;
import com.mrshiehx.cmcl.utils.json.JSONUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.isEmpty;

public class AuthlibInjectorAuthentication {
    public static JSONObject authlibInjectorLogin(String address, String username, boolean select) throws DescriptionException {
        if (Utils.isEmpty(username)) {
            System.out.print(getString("INPUT_ACCOUNT"));
            try {
                username = new Scanner(System.in).nextLine();
            } catch (NoSuchElementException ignore) {
                return null;
            }
        }
        Console console = System.console();
        String password;
        if (console != null) {
            System.out.print(getString("INPUT_PASSWORD"));
            char[] input = console.readPassword();
            password = input != null ? new String(input) : "";
        } else {
            System.out.println(getString("WARNING_SHOWING_PASSWORD"));
            System.out.print(getString("INPUT_PASSWORD"));
            try {
                password = new Scanner(System.in).nextLine();
            } catch (NoSuchElementException ignore) {
                return null;
            }
        }
        return AuthlibInjectorAuthentication.login(address, username, password, select);
    }

    public static JSONObject login(String address, String username, String password, boolean selected) throws DescriptionException {
        String url = NetworkUtils.addHttpsIfMissing(address);
        String serverName = "AuthlibServer";
        /*metadata存储时变成base64，启动时直接拿来用*/
        String metadata;
        try {
            HttpURLConnection conn;
            try {
                conn = (HttpURLConnection) new URL(url).openConnection();

                String ali = conn.getHeaderField("x-authlib-injector-api-location");
                if (ali != null) {
                    URL absoluteAli = new URL(conn.getURL(), ali);
                    if (!NetworkUtils.urlEqualsIgnoreSlash(url, absoluteAli.toString())) {
                        conn.disconnect();
                        url = absoluteAli.toString();
                        conn = (HttpURLConnection) absoluteAli.openConnection();
                    }
                }
            } catch (MalformedURLException e) {
                throw e;
            } catch (IOException e) {
                if (Utils.getConfig().optBoolean("proxyEnabled"))
                    System.err.println(Utils.getString("EXCEPTION_NETWORK_WRONG_PLEASE_CHECK_PROXY"));
                throw e;
            }


            if (!url.endsWith("/"))
                url += "/";

            JSONObject firstRequest = new JSONObject(metadata = NetworkUtils.httpURLConnection2String(conn));
            JSONObject meta = firstRequest.optJSONObject("meta");
            if (meta != null) {
                serverName = meta.optString("serverName", serverName);
            }
        } catch (Exception e) {
            throw new com.mrshiehx.cmcl.exceptions.DescriptionException(getString("FAILED_TO_LOGIN_YGGDRASIL_ACCOUNT_UNAVAILABLE_SERVER"));
        }


        AuthlibInjectorApiProvider provider = new AuthlibInjectorApiProvider(url);
        String authenticationURL = provider.getAuthenticationURL();
        JSONObject request = new JSONObject();
        request.put("agent", new JSONObject().put("name", "Minecraft").put("version", 1));
        request.put("username", username);
        request.put("password", password);
        request.put("clientToken", UUID.randomUUID().toString().replace("-", ""));
        JSONObject firstResponse;
        try {
            firstResponse = JSONUtils.parseJSONObject(NetworkUtils.post(authenticationURL, request.toString()));
        } catch (IOException e) {
            throw new DescriptionException(getString("EXCEPTION_OF_NETWORK_WITH_URL", authenticationURL, e));
        }
        if (firstResponse == null) {
            System.out.println(getString("CONSOLE_FAILED_REFRESH_OFFICIAL_NO_RESPONSE"));
            return null;
        }

        if (firstResponse.has("error")) {
            if (firstResponse.has("errorMessage")) {
                Utils.printfln(getString("FAILED_TO_LOGIN_OTHER_AUTHENTICATION_ACCOUNT"), "\n" + (!firstResponse.optString("error").isEmpty() ? firstResponse.optString("error") : "Error") + ": " + firstResponse.optString("errorMessage"));
            } else {
                Utils.printfln(getString("FAILED_TO_LOGIN_OTHER_AUTHENTICATION_ACCOUNT"), firstResponse.optString("error"));
            }
            return null;
        }
        String clientToken = firstResponse.optString("clientToken");

        String playerName;
        String uuid;

        JSONObject selectedProfile = firstResponse.optJSONObject("selectedProfile");
        if (selectedProfile != null) {
            playerName = selectedProfile.optString("name");
            uuid = selectedProfile.optString("id");
        } else {
            List<JSONObject> availableProfiles = JSONUtils.jsonArrayToJSONObjectList(firstResponse.optJSONArray("availableProfiles"));
            if (availableProfiles.size() > 0) {
                JSONObject profile = YggdrasilAuthentication.selectCharacter(availableProfiles);
                if (profile == null) return null;
                playerName = profile.optString("name");
                uuid = profile.optString("id");
            } else {
                System.out.println(getString("FAILED_TO_LOGIN_OAA_NO_SELECTED_CHARACTER"));
                return null;
            }
        }

        String accessToken = firstResponse.optString("accessToken");

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
        account.put("username", username);
        return account;
    }

    public static boolean refresh(JSONObject selectedAccount, JSONArray accounts) throws DescriptionException {
        String accessToken = selectedAccount.optString("accessToken");
        String clientToken = selectedAccount.optString("clientToken");
        String url = selectedAccount.optString("url");
        if (Utils.isEmpty((accessToken)) || Utils.isEmpty(clientToken) || Utils.isEmpty(url)) {
            throw new DescriptionException(getString("MESSAGE_AUTHLIB_ACCOUNT_INCOMPLETE"));
        }
        YggdrasilAuthenticationApiProvider provider = new AuthlibInjectorApiProvider(url);
        JSONObject validate;
        try {
            validate = YggdrasilAuthentication.validate(provider, accessToken, clientToken);
        } catch (IOException e) {
            if (Constants.isDebug()) e.printStackTrace();
            throw new DescriptionException(getString("MESSAGE_ACCOUNT_FAILED_TO_VALIDATE", e));
        }
        if (validate == null) {
            //无需刷新
            String uuid = selectedAccount.optString("uuid");
            if (!uuid.isEmpty()) {
                try {
                    String newPlayerName = new JSONObject(NetworkUtils.get(provider.getProfilePropertiesURL(uuid))).optString("name");
                    if (!isEmpty(newPlayerName)) selectedAccount.put("playerName", newPlayerName);
                    return true;
                } catch (Exception e) {
                    if (Constants.isDebug()) e.printStackTrace();
                    System.out.println(getString("EXCEPTION_GET_USER_PROPERTIES", e));
                }
            }
            return false;
        }
        if (!validate.optString("error").equals("ForbiddenOperationException")) {
            throw new DescriptionException(getString("MESSAGE_ACCOUNT_FAILED_TO_VALIDATE", "\n" + (!validate.optString("error").isEmpty() ? validate.optString("error") : "Error") + ": " + validate.optString("errorMessage")));
        }
        JSONObject refreshResponse;
        try {
            refreshResponse = YggdrasilAuthentication.refresh(new AuthlibInjectorApiProvider(url), accessToken, clientToken);
        } catch (IOException e) {
            if (Constants.isDebug()) e.printStackTrace();
            throw new DescriptionException(getString("MESSAGE_FAILED_REFRESH_TITLE") + ": " + e);
        }
        if (refreshResponse.optString("error").equals("ForbiddenOperationException")) {
            System.out.println(getString("MESSAGE_ACCOUNT_INFO_EXPIRED_NEED_RELOGIN"));
            JSONObject accountNew = AuthlibInjectorAuthentication.authlibInjectorLogin(url, selectedAccount.optString("username"), true);
            if (accountNew == null) {
                throw new DescriptionException(null);
            }
            for (int i = 0; i < accounts.length(); i++) {
                if (!AccountUtils.isValidAccount(accounts.opt(i))) continue;
                JSONObject accountInFor = (JSONObject) accounts.opt(i);
                if (accountInFor.optBoolean("selected")) {
                    accounts.put(i, accountNew);
                    break;
                }
            }
            return true;
        } else if (!refreshResponse.optString("error").isEmpty()) {
            //報錯
            throw new DescriptionException(getString("ERROR_WITH_MESSAGE", (!refreshResponse.optString("error").isEmpty() ? refreshResponse.optString("error") : "Error"), refreshResponse.optString("errorMessage")));
        }
        //真刷新
        String newAccessToken = refreshResponse.optString("accessToken");
        String newClientToken = refreshResponse.optString("clientToken");
        JSONArray availableProfiles = refreshResponse.optJSONArray("availableProfiles");

        List<JSONObject> availableProfilesList = JSONUtils.jsonArrayToJSONObjectList(availableProfiles);

        if (availableProfilesList.size() > 0) {
            Optional<JSONObject> profileConsistent = availableProfilesList.stream().filter(profile -> profile.optString("id").equals(selectedAccount.optString("uuid"))).findFirst();
            if (profileConsistent.isPresent()) {
                selectedAccount.put("playerName", profileConsistent.get().optString("name"));
            } else {
                System.out.println(getString("MESSAGE_YGGDRASIL_ACCOUNT_REFRESH_OLD_CHARACTER_DELETED"));
                JSONObject selectProfile = YggdrasilAuthentication.selectCharacter(availableProfilesList);
                if (selectProfile == null)
                    return false;
                selectedAccount.put("uuid", selectProfile.optString("id"));
                selectedAccount.put("playerName", selectProfile.optString("name"));
            }

        } else {
            //提示此角色已被删除
            System.out.println(getString("MESSAGE_AUTHLIB_ACCOUNT_REFRESH_NO_CHARACTERS"));
        }
        selectedAccount.put("accessToken", newAccessToken);
        selectedAccount.put("clientToken", newClientToken);
        return true;
    }


    public static File getAuthlibInjectorFile() throws IOException {
        File cmcl = CMCL.getCMCLWorkingDirectory();
        cmcl.mkdirs();
        File file = new File(cmcl, "authlib-injector.jar");
        if (file.exists() && file.isFile() && file.length() > 0) return file;
        JSONObject latest = new JSONObject(NetworkUtils.getWithToken(DownloadSource.getProvider().authlibInjectorFile()));
        System.out.print(getString("MESSAGE_DOWNLOADING_FILE", "authlib-injector.jar"));
        DownloadUtils.downloadFile(latest.optString("download_url"), file, new PercentageTextProgress());
        return file;
    }
}