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

package com.mrshiehx.cmcl.server;

import com.mrshiehx.cmcl.utils.NetworkUtils;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mrshiehx.cmcl.server.ServerUtils.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class OfflineSkinServer extends HttpServer {
    public static final KeyPair keyPair;
    public final String uuid;
    public final String playerName;
    public final byte[] skin;
    public final String skinHash;
    public final byte[] cape;
    public final String capeHash;
    public final boolean isSlim;
    public final int skinLength;
    public final int capeLength;

    static {
        KeyPair keyPair1;
        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(4096, new SecureRandom());
            keyPair1 = gen.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            keyPair1 = null;
        }
        keyPair = keyPair1;
    }

    public OfflineSkinServer(int port, String uuid, String playerName, byte[] skin, int skinLength, String skinHash, byte[] cape, int capeLength, String capeHash, boolean isSlim) {
        super(port);
        this.uuid = uuid;
        this.playerName = playerName;
        this.skin = skin;
        this.skinHash = skinHash;
        this.cape = cape;
        this.capeHash = capeHash;
        this.isSlim = isSlim;
        this.skinLength = skinLength;
        this.capeLength = capeLength;
        if (Utils.isEmpty(uuid)) {
            throw new RuntimeException(Utils.getString("EMPTY_UUID"));
        }
        if (Utils.isEmpty(playerName)) {
            throw new RuntimeException(Utils.getString("EMPTY_PLAYERNAME"));
        }
        /*if (skin == null) {
            throw new RuntimeException(Utils.getString("EMPTY_SKIN"));
        }*/
    }

    @Override
    public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        String uri = session.getUri();
        if (method.equals(Method.GET)) {
            Matcher matcherForProfile = Pattern.compile("/sessionserver/session/minecraft/profile/(?<uuid>[a-f0-9]{32})").matcher(uri);
            Matcher matcherForTextures = Pattern.compile("/textures/(?<hash>[a-f0-9]{64})").matcher(uri);
            if (Pattern.compile("^/$").matcher(uri).find()) {
                return root(session);
            } else if (Pattern.compile("/status").matcher(uri).find()) {
                return status(session);

            } else if (Pattern.compile("/sessionserver/session/minecraft/hasJoined").matcher(uri).find()) {
                Map<String, String> query = Utils.mapOf(NetworkUtils.parseQuery(session.getQueryParameterString()));
                return hasJoined(session, query);

            } else if (matcherForProfile.find()) {
                return profile(session, matcherForProfile);

            } else if (matcherForTextures.find()) {
                return textures(session, matcherForTextures);
            } else if (Pattern.compile("/sessionserver/session/minecraft/join").matcher(uri).find()) {
                return badRequest();
            } else if (Pattern.compile("/api/profiles/minecraft").matcher(uri).find()) {
                return badRequest();
            }
        }
        if (method.equals(Method.POST)) {
            if (Pattern.compile("/sessionserver/session/minecraft/join").matcher(uri).find()) {
                return ServerUtils.noContent();
            } else if (Pattern.compile("/api/profiles/minecraft").matcher(uri).find()) {
                try {
                    return profiles(session);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return notFound();
    }

    private Response profiles(IHTTPSession session) throws IOException {
        InputStream i = session.getInputStream();
        String s = Utils.inputStream2String(i, i.available());
        JSONArray jsonArray = Utils.parseJSONArray(s);
        if (jsonArray == null) {
            return badRequest();
        }
        boolean contains = false;
        for (Object obj : jsonArray) {
            if (Objects.equals(obj, playerName)) {
                contains = true;
                break;
            }
        }
        JSONArray result = new JSONArray();
        if (contains) {
            result.put(new JSONObject().put("id", uuid).put("name", playerName));
        }
        return ok(result);
    }

    private Response textures(IHTTPSession session, Matcher matcher) {
        String hash = matcher.group("hash");
        InputStream inputStream = null;
        int inputStreamLength = 0;
        if (Objects.equals(hash, skinHash) && skin != null) {
            inputStream = new ByteArrayInputStream(skin);
            inputStreamLength = skinLength;
        } else if (Objects.equals(hash, capeHash) && cape != null) {
            inputStream = new ByteArrayInputStream(cape);
            inputStreamLength = capeLength;
        }
        if (inputStream != null && inputStreamLength > 0) {

            Response response = newFixedLengthResponse(Response.Status.OK, "image/png", inputStream, inputStreamLength);
            response.addHeader("Etag", String.format("\"%s\"", hash));
            response.addHeader("Cache-Control", "max-age=2592000, public");
            return response;
        }

        return notFound();
    }

    private Response profile(IHTTPSession session, Matcher matcher) {
        String uuid = matcher.group("uuid");
        if (Objects.equals(this.uuid, uuid)) {
            return getProfile();
        }

        return noContent();
    }

    private Response getProfile() {
        JSONObject textures = new JSONObject();

        if (skin != null && !Utils.isEmpty(skinHash)) {
            JSONObject skinJSONObject = new JSONObject().put("url", getRootUrl() + "textures/" + skinHash);
            if (isSlim) {
                skinJSONObject.put("metadata", new JSONObject().put("model", "slim"));
            }
            textures.put("SKIN", skinJSONObject);
        }

        if (cape != null && !Utils.isEmpty(capeHash)) {
            textures.put("CAPE", new JSONObject().put("url", getRootUrl() + "textures/" + capeHash));
        }

        JSONObject texturesBase64 = new JSONObject();
        texturesBase64.put("timestamp", System.currentTimeMillis());
        texturesBase64.put("profileId", uuid);
        texturesBase64.put("profileName", playerName);
        texturesBase64.put("textures", textures);

        String value = Base64.getEncoder().encodeToString(texturesBase64.toString(2).getBytes(StandardCharsets.UTF_8));
        JSONArray properties = new JSONArray();
        JSONObject texturesProperty = new JSONObject();
        texturesProperty.put("name", "textures");
        texturesProperty.put("value", value);
        texturesProperty.put("signature", sign(value));
        properties.put(texturesProperty);

        JSONObject main = new JSONObject();
        main.put("id", uuid);
        main.put("name", playerName);
        main.put("properties", properties);
        return ok(main);
    }

    private static String sign(String data) {
        try {
            Signature signature = Signature.getInstance("SHA1withRSA");
            signature.initSign(keyPair.getPrivate(), new SecureRandom());
            signature.update(data.getBytes(UTF_8));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Response hasJoined(IHTTPSession session, Map<String, String> query) {
        String username = query.get("username");
        if (Utils.isEmpty(username)) {
            return badRequest();
        }
        if (Objects.equals(playerName, username)) {
            return getProfile();
        }

        return noContent();
    }

    private Response status(IHTTPSession session) {
        JSONObject main = new JSONObject();
        main.put("user.count", 1/*角色数量*/);
        main.put("token.count", 0);
        main.put("pendingAuthentication.count", 0);
        return ok(main);
    }

    private Response root(IHTTPSession session) {
        if (keyPair == null) return internalError();

        byte[] encoded = keyPair.getPublic().getEncoded();
        String key = "-----BEGIN PUBLIC KEY-----\n" + Base64.getMimeEncoder(76, new byte[]{'\n'}).encodeToString(encoded) + "\n-----END PUBLIC KEY-----\n";
        JSONObject main = new JSONObject();
        main.put("signaturePublickey", key);
        JSONObject meta = new JSONObject();
        meta.put("serverName", "CMCL");
        meta.put("implementationName", "CMCL");
        meta.put("implementationVersion", "1.0");
        meta.put("feature.non_email_login", true);
        main.put("meta", meta);
        JSONArray skinDomains = new JSONArray();
        skinDomains.put("127.0.0.1");
        skinDomains.put("localhost");
        main.put("skinDomains", skinDomains);
        return ok(main);
    }

}
