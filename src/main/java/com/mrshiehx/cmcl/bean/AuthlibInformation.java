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
package com.mrshiehx.cmcl.bean;

import com.mrshiehx.cmcl.ConsoleMinecraftLauncher;
import com.mrshiehx.cmcl.options.AccountOption;
import com.mrshiehx.cmcl.server.OfflineSkinServer;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.authlib.AuthlibUtils;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;

public class AuthlibInformation {
    public final String serverAddress;
    public final String serverName;
    public final String playerName;
    public final String token;
    public final String uuid;
    public final String metadataEncoded;

    public boolean forOfflineSkin;

    public AuthlibInformation(String serverAddress, String serverName, String playerName, String token, String uuid, String metadataEncoded) {
        serverAddress = AuthlibUtils.addHttpsIfMissing(serverAddress);
        if (!serverAddress.endsWith("/"))
            serverAddress += "/";
        this.serverAddress = serverAddress;
        this.serverName = serverName;
        this.playerName = playerName;
        this.token = token;
        this.uuid = uuid;
        this.metadataEncoded = metadataEncoded;
    }

    public boolean isEmpty() {
        return Utils.isEmpty(serverAddress) ||
                Utils.isEmpty(uuid) ||
                Utils.isEmpty(token);
    }

    public static AuthlibInformation valuesOf(JSONObject account, String token, String uuid, boolean allowOfflineSkin) {
        if (account.optInt("loginMethod") == 1) {
            return new AuthlibInformation(
                    account.optString("url"),
                    account.optString("serverName"),
                    account.optString("playerName", "XPlayer"),
                    account.optString("accessToken"),
                    account.optString("uuid"),
                    account.optString("metadataEncoded"));
        } else if (account.optInt("loginMethod") == 0 && allowOfflineSkin) {
            if (account.has("offlineSkin") || account.has("providedSkin") || account.has("cape")) {
                byte[] cape = null;
                int capeLength = 0;
                String capeHash = null;

                String capeString = account.optString("cape");
                if (!Utils.isEmpty(capeString)) {
                    File file = new File(capeString);
                    if (file.exists()) {
                        try {
                            cape = Utils.getBytes(file);
                            capeLength = cape.length;
                            capeHash = Utils.getBytesHashSHA256String(cape);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Utils.printflnErr(Utils.getString("CAPE_FILE_FAILED_LOAD"), file.getAbsolutePath());
                        }
                    } else {
                        Utils.printflnErr(Utils.getString("CAPE_FILE_NOT_FOUND"), file.getAbsolutePath());
                    }
                }

                boolean slim = false;

                byte[] skin = null;
                int skinLength = 0;
                String skinHash = null;
                if (account.has("offlineSkin") || account.has("providedSkin")) {
                    String providedSkin = account.optString("providedSkin");
                    Pair<byte[], Pair<Boolean, String>> pair = getSkin(account, providedSkin, capeString);
                    if (pair != null) {
                        skin = pair.getKey();
                        slim = pair.getValue().getKey();
                        skinLength = skin.length;
                        try {
                            skinHash = Utils.getBytesHashSHA256String(skin);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Utils.printflnErr(pair.getValue().getValue());
                            return null;
                        }
                    }
                }

                try {
                    OfflineSkinServer offlineSkinServer = new OfflineSkinServer(
                            0,
                            uuid,
                            account.optString("playerName", "XPlayer"),
                            skin,
                            skinLength,
                            skinHash,
                            cape,
                            capeLength,
                            capeHash,
                            slim);
                    offlineSkinServer.start(NanoHTTPD.SOCKET_READ_TIMEOUT, true);
                    AuthlibInformation authlibInformation = new AuthlibInformation(
                            offlineSkinServer.getRootUrl(),
                            "CMCL",
                            account.optString("playerName", "XPlayer"),
                            token,
                            uuid,
                            null);
                    authlibInformation.forOfflineSkin = true;
                    return authlibInformation;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(getString("UNABLE_TO_START_OFFLINE_SKIN_SERVER"));
                }
            }
        }
        return null;
    }

    private static Pair<byte[], Pair<Boolean, String>> getSkin(JSONObject account, String providedSkin, String capeString) {
        byte[] skin;
        if ("steve".equals(providedSkin)) {
            try {
                InputStream is = AccountOption.class.getResourceAsStream("/skin/steve.png");
                if (is == null) {
                    System.out.println(getString("UNABLE_OFFLINE_CUSTOM_SKIN_STEVE_NOT_FOUND"));
                    return null;
                }
                skin = Utils.inputStream2ByteArray(is);
                return new Pair<>(skin, new Pair<>(false, getString("UNABLE_OFFLINE_CUSTOM_SKIN_STEVE_UNABLE_LOAD")));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(getString("UNABLE_OFFLINE_CUSTOM_SKIN_STEVE_UNABLE_LOAD"));
                return null;
            }
        } else if ("alex".equals(providedSkin)) {
            try {
                InputStream is = AccountOption.class.getResourceAsStream("/skin/alex.png");
                if (is == null) {
                    System.out.println(getString("UNABLE_OFFLINE_CUSTOM_SKIN_ALEX_NOT_FOUND"));
                    return null;
                }
                skin = Utils.inputStream2ByteArray(is);
                return new Pair<>(skin, new Pair<>(true, getString("UNABLE_OFFLINE_CUSTOM_SKIN_ALEX_UNABLE_LOAD")));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(getString("UNABLE_OFFLINE_CUSTOM_SKIN_ALEX_UNABLE_LOAD"));
                return null;
            }
        } else {
            String offlineSkin = account.optString("offlineSkin");
            if (ConsoleMinecraftLauncher.isEmpty(offlineSkin)) {
                return null;
            }
            File file = new File(offlineSkin);
            if (!file.exists()) {
                Utils.printflnErr(getString("UNABLE_OFFLINE_CUSTOM_SKIN_FILE_NOT_FOUND"), file.getAbsolutePath());
                return null;
            }

            try {
                skin = Utils.getBytes(file);
                return new Pair<>(skin, new Pair<>(account.optBoolean("slim"), String.format(getString("UNABLE_OFFLINE_CUSTOM_SKIN_FILE_FAILED_LOAD"), file.getAbsolutePath())));
            } catch (Exception e) {
                e.printStackTrace();
                Utils.printflnErr(getString("UNABLE_OFFLINE_CUSTOM_SKIN_FILE_FAILED_LOAD"), file.getAbsolutePath());
                return null;
            }
        }
    }
}
