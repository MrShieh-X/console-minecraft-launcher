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
 *
 */

package com.mrshiehx.cmcl.utils.internet;

import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NetworkUtils {
    public static final String PARAMETER_SEPARATOR = "&";
    public static final String NAME_VALUE_SEPARATOR = "=";

    public static List<Pair<String, String>> parseQuery(URI uri) {
        return parseQuery(uri.getRawQuery());
    }

    public static List<Pair<String, String>> parseQuery(String queryParameterString) {
        List<Pair<String, String>> result = new ArrayList<>();

        if (Utils.isEmpty(queryParameterString)) return result;
        try (Scanner scanner = new Scanner(queryParameterString)) {
            scanner.useDelimiter("&");
            while (scanner.hasNext()) {
                String[] nameValue = scanner.next().split(NAME_VALUE_SEPARATOR);
                if (nameValue.length == 0 || nameValue.length > 2) {
                    throw new IllegalArgumentException("bad query string");
                }

                String name = URLDecoder.decode(nameValue[0], "UTF-8");
                String value = nameValue.length == 2 ? URLDecoder.decode(nameValue[1], "UTF-8") : null;
                result.add(new Pair<>(name, value));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static String post(String url, String content) throws IOException {
        return post(url, content, "application/json", null);
    }

    public static String post(String url, String content, String contentType, String accept) throws IOException {
        URL connectUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) connectUrl.openConnection();
        //here is your code above
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", contentType);
        if (!Utils.isEmpty(accept)) connection.setRequestProperty("Accept", accept);
        OutputStream wrt = connection.getOutputStream();

        if (content != null) wrt.write(content.getBytes());
        String s = httpURLConnection2String(connection);
        wrt.close();
        return s;
    }

    public static String getWithToken(String url, String tokenType, String token) throws IOException {
        return getWithToken(url, tokenType, token, "application/json", "application/json");
    }

    public static String getWithToken(String url, String tokenType, String token, String contentType, String accept) throws IOException {
        URL connectUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) connectUrl.openConnection();
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Authorization", tokenType + " " + token);
        if (!Utils.isEmpty(accept)) connection.setRequestProperty("Accept", accept);
        return (httpURLConnection2String(connection));
    }

    public static String get(String url) throws IOException {
        return get(url, "application/json", "application/json");
    }

    public static String get(String url, String contentType, String accept) throws IOException {
        URL ConnectUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) ConnectUrl.openConnection();
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", contentType);
        if (!Utils.isEmpty(accept)) connection.setRequestProperty("Accept", accept);
        return (httpURLConnection2String(connection));
    }

    public static String curseForgeGet(String url) throws IOException {
        return curseForgeGet(url, "application/json", "application/json");
    }

    public static String curseForgeGet(String url, String contentType, String accept) throws IOException {
        URL ConnectUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) ConnectUrl.openConnection();
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("x-api-key", Constants.getCurseForgeApiKey());
        if (!Utils.isEmpty(accept)) connection.setRequestProperty("Accept", accept);
        return (httpURLConnection2String(connection));
    }

    public static String delete(String url, String tokenType, String token) throws IOException {
        return delete(url, tokenType, token, "application/json", "application/json");
    }

    public static String delete(String url, String tokenType, String token, String contentType, String accept) throws IOException {
        URL ConnectUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) ConnectUrl.openConnection();
        //here is your code above
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Authorization", tokenType + " " + token);
        //System.out.println(tokenType);
        //System.out.println(token);
        if (!Utils.isEmpty(accept)) connection.setRequestProperty("Accept", accept);
        //connection.getOutputStream();//.write("\"publicCreateProfileDTO\":\"45\"".getBytes(UTF_8));
        return (httpURLConnection2String(connection));
    }

    public static String httpURLConnection2String(HttpURLConnection con) throws IOException {
        try {
            try (InputStream stdout = con.getInputStream()) {
                return Utils.inputStream2String(stdout);
            }
        } catch (IOException e) {
            try (InputStream stderr = con.getErrorStream()) {
                if (stderr == null) throw e;
                return Utils.inputStream2String(stderr);
            }
        }
    }

    public static byte[] httpURLConnection2Bytes(HttpURLConnection con) throws IOException {
        try {
            try (InputStream stdout = con.getInputStream()) {
                return FileUtils.inputStream2ByteArray(stdout);
            }
        } catch (IOException e) {
            try (InputStream stderr = con.getErrorStream()) {
                if (stderr == null) throw e;
                return FileUtils.inputStream2ByteArray(stderr);
            }
        }
    }

    public static void setProxy(String host, String port, String userName, String password) {
        if (Utils.isEmpty(host) || Utils.isEmpty(port)) return;
        System.setProperty("java.net.useSystemProxies", "true");
        System.setProperty("https.proxyHost", host);
        System.setProperty("http.proxyHost", host);
        if (!Utils.isEmpty(port)) {
            System.setProperty("https.proxyPort", port);
            System.setProperty("http.proxyPort", port);
        }
        if (!Utils.isEmpty(userName)) {
            System.setProperty("http.proxyUserName", userName);
            System.setProperty("https.proxyUserName", userName);
            if (!Utils.isEmpty(password)) {
                System.setProperty("http.proxyPassword", password);
                System.setProperty("https.proxyPassword", password);
            }

        }
    }

    public static String getWithToken(String url) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        byte[] dataBuffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            byteOutputStream.write(dataBuffer, 0, bytesRead);
        }
        byteOutputStream.close();
        in.close();
        return byteOutputStream.toString();
    }

    public static String addSlashIfMissing(String url) {
        if (!url.endsWith("/")) url = url + "/";
        return url;
    }

    public static String encodeURL(String url) {
        StringBuilder sb = new StringBuilder();
        boolean left = true;
        for (char c : url.toCharArray()) {
            if (c == ' ') {
                sb.append(left ? "%20" : '+');
                continue;
            }
            if (c == '?') left = false;
            try {
                sb.append(c >= 0x80 ? URLDecoder.decode(Character.toString(c), "UTF-8") : c);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        return sb.toString();
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
