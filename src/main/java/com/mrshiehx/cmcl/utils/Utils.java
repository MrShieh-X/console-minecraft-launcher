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
package com.mrshiehx.cmcl.utils;

import com.mrshiehx.cmcl.ConsoleMinecraftLauncher;
import com.mrshiehx.cmcl.api.download.DownloadSource;
import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.exceptions.NotSelectedException;
import com.sun.management.OperatingSystemMXBean;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Utils {
    private static final String[] linuxBrowsers = {
            "xdg-open",
            "google-chrome",
            "firefox",
            "microsoft-edge",
            "opera",
            "konqueror",
            "mozilla"
    };

    public static void openLink(String link) {
        if (link == null)
            return;

        if (java.awt.Desktop.isDesktopSupported()) {
            new Thread(() -> {
                if (OperatingSystem.CURRENT_OS == OperatingSystem.LINUX) {
                    for (String browser : linuxBrowsers) {
                        try (final InputStream is = Runtime.getRuntime().exec(new String[]{"which", browser}).getInputStream()) {
                            if (is.read() != -1) {
                                Runtime.getRuntime().exec(new String[]{browser, link});
                                return;
                            }
                        } catch (Throwable ignored) {
                        }
                        //Logging.LOG.log(Level.WARNING, "No known browser found");
                    }
                }
                try {
                    java.awt.Desktop.getDesktop().browse(new URI(link));
                } catch (Throwable e) {
                    if (OperatingSystem.CURRENT_OS == OperatingSystem.OSX)
                        try {
                            Runtime.getRuntime().exec(new String[]{"/usr/bin/open", link});
                        } catch (IOException ex) {
                            //Logging.LOG.log(Level.WARNING, "Unable to open link: " + link, ex);
                        }
                    //Logging.LOG.log(Level.WARNING, "Failed to open link: " + link, e);
                }
            }).start();

        }
    }

    public static String getString(String name) {
        return ConsoleMinecraftLauncher.getString(name);
    }

    public static String getString(String name, Object... objects) {
        return ConsoleMinecraftLauncher.getString(name, objects);
    }

    public static <K, V> Map<K, V> mapOf(Iterable<Pair<K, V>> pairs) {
        Map<K, V> map = new LinkedHashMap<>();
        for (Pair<K, V> pair : pairs)
            map.put(pair.getKey(), pair.getValue());
        return map;
    }

    public static byte[] inputStream2ByteArray(InputStream resourceAsStream) throws IOException {
        if (resourceAsStream != null) {
        /*byte[] bytes = new byte[0];
        bytes = new byte[resourceAsStream.available()];
        resourceAsStream.read(bytes);
        return new String(bytes);*/
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = resourceAsStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        } else return null;
    }


    public static boolean isEmpty(CharSequence c) {
        return c == null || c.length() == 0;
    }


    public static String post(String url, String content) throws IOException {
        return post(url, content, "application/json", null);
    }

    public static String post(String url, String content, String contentType, String accept) throws IOException {
        URL ConnectUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) ConnectUrl.openConnection();
        //here is your code above
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", contentType);
        if (!Utils.isEmpty(accept)) connection.setRequestProperty("Accept", accept);
        OutputStream wrt = ((connection.getOutputStream()));

        if (content != null) wrt.write(content.getBytes());
        String s = Utils.httpURLConnection2String(connection);
        wrt.close();
        return (s);
    }

    public static String getWithToken(String url, String tokenType, String token) throws IOException {
        return getWithToken(url, tokenType, token, "application/json", "application/json");
    }

    public static String getWithToken(String url, String tokenType, String token, String contentType, String accept) throws IOException {
        URL ConnectUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) ConnectUrl.openConnection();
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Authorization", tokenType + " " + token);
        if (!Utils.isEmpty(accept)) connection.setRequestProperty("Accept", accept);
        return (Utils.httpURLConnection2String(connection));
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
        return (Utils.httpURLConnection2String(connection));
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
        return (Utils.httpURLConnection2String(connection));
    }

    public static String getLibraryName(String path) {
        if (Utils.isEmpty(path)) return "";
        String splitter = File.separator;
        if (!path.contains(splitter)) return path;
        path = path.replace(File.separatorChar, '/');
        splitter = "/";
        String[] strings = path.split(splitter);
        if (strings.length < 4) return path;

        return strings[strings.length - 3];
    }

    public static String getNativeLibraryName(String path) {
        if (Utils.isEmpty(path)) return "";
        String splitter = File.separator;
        if (!path.contains(splitter) && !path.contains("\\") && !path.contains("/")) return path;
        path = path.replace(File.separatorChar, '/');
        splitter = "/";
        String[] strings = path.split(splitter);
        if (strings.length < 3) return path;

        return strings[strings.length - 3];
    }


    public static void deleteDirectory(File directory) {
        if (directory != null) {
            if (directory.exists()) {
                if (directory.isDirectory()) {
                    File[] files = directory.listFiles();
                    if (files != null) {
                        if (files.length != 0) {
                            for (File file : files) {
                                if (file.isFile()) {
                                    file.delete();
                                } else {
                                    deleteDirectory(file);
                                }
                            }
                        }
                    }
                }
                directory.delete();

            }
        }
    }


    public static void copyDirectory(File from, String toWillNewDirNameIsAtFromName, String afterThatName) throws IOException {
        if (from != null && !isEmpty(toWillNewDirNameIsAtFromName) && from.exists()) {
            if (from.isFile()) {
                Utils.copyFile(from, new File(toWillNewDirNameIsAtFromName, afterThatName));
                return;
            }
            File toWillNewDirNameIsAtFrom = new File(toWillNewDirNameIsAtFromName);
            File to = new File(toWillNewDirNameIsAtFrom, afterThatName);
            if (!to.exists()) to.mkdirs();
            for (File file : from.listFiles()) {
                if (file.isFile()) {
                    Utils.copyFile(file, new File(to, file.getName()));
                } else {
                    copyDirectory(file, to.getAbsolutePath(), file.getName());
                }
            }
        }
    }

    public static File createFile(File file) throws IOException {
        return createFile(file, true);
    }

    public static File createFile(File file, boolean delete) throws IOException {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        if (delete && file.exists()) file.delete();
        if (!file.exists()) file.createNewFile();
        return file;
    }

    public static void copyFile(File source, File to)
            throws IOException {
        if (null == source) return;
        if (source.isDirectory()) copyDirectory(source, to.getParent(), to.getName());
        createFile(to, true);
        InputStream input = new FileInputStream(source);
        OutputStream output = new FileOutputStream(to);
        byte[] buf = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buf)) != -1) {
            output.write(buf, 0, bytesRead);
        }
        input.close();
        output.close();
    }

    public static String valueOf(Object value) {
        return value == null ? "" : value.toString();
    }

    public static boolean isWindows() {
        /*return File.separator.equals("\\")
                || File.separatorChar == '\\'
                ||*//*AccessController.doPrivileged(OSInfo.getOSTypeAction()) == OSInfo.OSType.WINDOWS*//*System.getProperty("os.name").toLowerCase().contains("windows");*/
        return OperatingSystem.CURRENT_OS == OperatingSystem.WINDOWS;
    }

    public static String getDefaultJavaPath() {
        String s = System.getProperty("java.home");
        return isEmpty(s) ? "" : new File(s, isWindows() ? "bin\\java.exe" : "bin/java").getAbsolutePath();
    }

    public static void saveConfig(JSONObject jsonObject) {
        ConsoleMinecraftLauncher.configContent = jsonObject;
        try {
            if (!Constants.configFile.exists()) Constants.configFile.createNewFile();
            FileWriter writer = new FileWriter(Constants.configFile, false);
            writer.write(jsonObject.toString(com.mrshiehx.cmcl.constants.Constants.INDENT_FACTOR));
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static JSONObject getConfig() {
        /*JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(ConsoleMinecraftLauncher.configContent);
        } catch (Exception e3) {
            e3.printStackTrace();
            jsonObject = new JSONObject();
        }
        return jsonObject;*/
        if (ConsoleMinecraftLauncher.configContent != null)
            return ConsoleMinecraftLauncher.configContent;
        return ConsoleMinecraftLauncher.initConfig();
    }

    public static File getAssets(String assetsDirPath, File gameDir) {
        return !isEmpty(assetsDirPath) ? new File(assetsDirPath) : new File(gameDir, "assets");
    }

    public static int getJavaVersion(String javaFile) {
        try {
            String version = null;
            Process process = new ProcessBuilder(javaFile, "-XshowSettings:properties", "-version").start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream(), OperatingSystem.NATIVE_CHARSET))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    Matcher m = Pattern.compile("java\\.version = (?<version>.*)").matcher(line);
                    if (m.find()) {
                        version = m.group("version");
                        break;
                    }
                }
            }

            if (version == null) {
                process = new ProcessBuilder(javaFile, "-version").start();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream(), OperatingSystem.NATIVE_CHARSET))) {
                    for (String line; (line = reader.readLine()) != null; ) {
                        Matcher m = Pattern.compile("version \"(?<version>(.*?))\"").matcher(line);
                        if (m.find()) {
                            version = m.group("version");
                            break;
                        }
                    }
                }
            }
            if (version != null) {
                if (version.startsWith("1.")) {
                    version = version.substring(2, 3);
                } else {
                    int dot = version.indexOf(".");
                    if (dot != -1) {
                        version = version.substring(0, dot);
                    }
                }
                return Integer.parseInt(version);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getJavaVersion() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if (dot != -1) {
                version = version.substring(0, dot);
            }
        }
        return Integer.parseInt(version);
    }

    public static String getNativesDirName() {
        return "natives-" + OperatingSystem.CURRENT_OS.getCheckedName();
    }

    public static String readFileContent(File file) throws IOException {
        BufferedReader reader;
        StringBuilder sbf = new StringBuilder();
        reader = new BufferedReader(new FileReader(file));
        String tempStr;
        while ((tempStr = reader.readLine()) != null) {
            sbf.append(tempStr);
        }
        reader.close();
        return sbf.toString();
    }

    public static String clearAllSpaces(String s) {
        StringBuilder stringBuffer = new StringBuilder();
        for (char a : s.toCharArray()) {
            if (a != ' ') {
                stringBuffer.append(a);
            }
        }
        return stringBuffer.toString();
    }

    public static <X> List<X> removeDuplicate(List<X> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (list.get(j).equals(list.get(i))) {
                    list.remove(j);
                }
            }
        }
        return list;
    }

    public static String[] split(String src) {
        List<String> list = new ArrayList<>();
        /*List<Boolean> yinyongs=new ArrayList<>();
        String[]split=src.split(String.valueOf(symbol));*/
        boolean yinyong = false;
        for (int i = 0; i < src.length(); i++) {
            char str = src.charAt(i);
            boolean modifiedYinyong = false;
            if (str == '\"') {
                yinyong = !yinyong;
                modifiedYinyong = true;
            }
            if (!yinyong) {
                if (!modifiedYinyong) {
                    if (str != ' ') {
                        if (i == 0) list.add(String.valueOf(str));
                        else list.set(list.size() - 1, list.get(list.size() - 1) + str);
                    } else {
                        list.add("");
                    }
                }
            } else {
                if (!modifiedYinyong) {
                    if (list.size() > 0) {
                        list.set(list.size() - 1, list.get(list.size() - 1) + str);
                    } else {
                        list.add("" + str);
                    }
                }
            }
        }
        return list.toArray(new String[0]);
    }

    public static String clearRedundantSpaces(String string) {
        char[] sourceChars = string.toCharArray();
        Object space = new Object();
        Object[] objects = new Object[string.length()];
        boolean yinyong = false;
        for (int i = 0; i < sourceChars.length; i++) {
            char cha = sourceChars[i];
            if (cha == '\"') {
                yinyong = !yinyong;
            }
            objects[i] = !yinyong && cha == ' ' ? space : cha;
        }
        List<Character> list = new ArrayList<>();
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            if (object == space) {
                list.add(' ');
                for (int j = i; j < objects.length; j++) {
                    if (objects[j] != space) {
                        i = j - 1;
                        break;
                    }
                }

            } else if (object instanceof Character) {
                list.add((Character) object);
            }
        }

        char[] chars = new char[list.size()];
        for (int i = 0; i < list.size(); i++) {
            chars[i] = list.get(i);
        }
        return new String(chars);
    }

    public static void printfln(String str, Object... object) {
        System.out.printf(str, object);
        System.out.println();
    }

    public static void printflnErr(String str, Object... object) {
        System.out.printf(str, object);
        System.out.println();
    }

    public static boolean versionContain(String name) {
        return new File(ConsoleMinecraftLauncher.versionsDir, name + "/" + name + ".json").exists() && new File(ConsoleMinecraftLauncher.versionsDir, name + "/" + name + ".json").exists();
    }

    public static File getNativesDir(File versionFile) {
        String defa = Utils.getNativesDirName();
        File defaul = new File(versionFile, defa);
        if (defaul.exists()) {
            File[] files = defaul.listFiles(File::isFile);
            if (files != null && files.length > 0) return defaul;
        }
        File[] files = versionFile.listFiles(pathname -> pathname.isDirectory() && pathname.getName().startsWith(defa));
        if (files != null && files.length > 0) {
            for (File file : files) {
                File[] files2 = file.listFiles(File::isFile);
                if (files2 != null && files2.length > 0) return file;
            }
        }
        return defaul;
    }


    public static String httpURLConnection2String(HttpURLConnection con) throws IOException {
        try {
            try (InputStream stdout = con.getInputStream()) {
                return inputStream2String(stdout);
            }
        } catch (IOException e) {
            try (InputStream stderr = con.getErrorStream()) {
                if (stderr == null) throw e;
                return inputStream2String(stderr);
            }
        }
    }

    public static String inputStream2String(InputStream stream) throws IOException {
        try (InputStream is = stream) {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            while (true) {
                int len = is.read(buf);
                if (len == -1)
                    break;
                result.write(buf, 0, len);
            }
            return result.toString(UTF_8.name());
        }
    }

    public static String inputStream2String(InputStream stream, int length) throws IOException {
        try (InputStream is = stream) {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buf = new byte[length];

            int len = is.read(buf, 0, length);
            result.write(buf, 0, len);
            return result.toString(UTF_8.name());
        }
    }

    public static File downloadVersionsFile() throws IOException {
        File cmcl = new File("cmcl");
        cmcl.mkdirs();
        File versionsFile = new File(cmcl, "versions.json");
        createFile(versionsFile, true);
        ConsoleMinecraftLauncher.downloadFile(DownloadSource.getProvider().versionManifest(), versionsFile);
        return versionsFile;
    }

    public static String getTypeText(String simpleName) {
        if ("String".equals(simpleName)) {
            return getString("DATATYPE_STRING");
        } else if ("Boolean".equals(simpleName)) {
            return getString("DATATYPE_BOOLEAN");
        } else if ("Integer".equals(simpleName)) {
            return getString("DATATYPE_INTEGER");
        } else if ("Double".equals(simpleName) || "Float".equals(simpleName) || "BigDecimal".equals(simpleName)) {
            return getString("DATATYPE_FRACTION");
        } else if ("ArrayList".equals(simpleName)) {
            return getString("DATATYPE_JSON_ARRAY");
        } else if ("HashMap".equals(simpleName)) {
            return getString("DATATYPE_JSON_OBJECT");
        } else return simpleName;
    }

    public static List<String> parseJVMArgs(JSONArray jvmArgs) {
        List<Object> objs;
        List<String> result = new LinkedList<>();
        if (jvmArgs == null) return result;
        else {
            objs = jvmArgs.toList();
        }
        for (Object obj : objs) {
            String v = valueOf(obj);
            if (!isEmpty(v)) {
                if (!v.contains("-Dminecraft.launcher.brand") && !v.contains("-Dminecraft.launcher.version") && !result.contains(v)) {
                    result.add(v);
                }
            }
        }
        return result;
    }

    public static Map<String, String> parseGameArgs(JSONObject gameArgs) {
        Map<String, Object> map;
        Map<String, String> result = new HashMap<>();
        if (gameArgs == null) {
            return result;
        } else {
            map = gameArgs.toMap();
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String va = valueOf(entry.getValue());
            String key = entry.getKey();
            if (!isEmpty(key) && !entry.getKey().equals("version") && !entry.getKey().equals("versionType") && !result.containsKey(key)) {
                result.put(key, va);
            }
        }
        return result;
    }

    public static int parse(String value) throws Exception {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println(getString("CONSOLE_UNSUPPORTED_VALUE", value));
            throw new Exception();
        }
    }

    public static JSONObject getSelectedAccount() throws NotSelectedException {
        return getSelectedAccount(getConfig());
    }

    public static JSONObject getSelectedAccount(JSONObject config) throws NotSelectedException {
        JSONArray accounts = config.optJSONArray("accounts");
        if (accounts == null || accounts.length() == 0) {
            System.out.println(getString("NOT_SELECTED_AN_ACCOUNT"));
            throw new NotSelectedException();
        }
        for (Object o : accounts) {
            if (o instanceof JSONObject) {
                JSONObject jsonObject1 = (JSONObject) o;
                if (jsonObject1.optBoolean("selected")) {
                    return jsonObject1;
                }
            }
        }
        System.out.println(getString("NOT_SELECTED_AN_ACCOUNT"));
        throw new NotSelectedException();
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

    /**
     * @return 如果jsonObjectString是一个JSONObject，就返回被解析的JSONObject，否则返回null
     **/
    public static JSONObject parseJSONObject(String jsonObjectString) {
        if (Utils.isEmpty(jsonObjectString)) return null;
        try {
            return new JSONObject(jsonObjectString);
        } catch (Throwable ignored) {
        }
        return null;
    }

    /**
     * @return 如果jsonObjectString是一个JSONObject，就返回被解析的JSONObject，否则返回null
     **/
    public static JSONArray parseJSONArray(String jsonArrayString) {
        if (Utils.isEmpty(jsonArrayString)) return null;
        try {
            return new JSONArray(jsonArrayString);
        } catch (Throwable ignored) {
        }
        return null;
    }


    public static String bytesToString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        if (bytes != null && bytes.length > 0) {
            for (byte aByte : bytes) {
                builder.append(String.format("%02X", aByte));
            }
        }
        return builder.toString().toUpperCase();
    }

    public static String getInputStreamHashSHA256String(InputStream inputStream) throws Exception {
        return bytesToString(getInputStreamHashSHA256(inputStream)).toLowerCase();
    }

    public static byte[] getInputStreamHashSHA256(InputStream inputStream) throws Exception {

        BufferedInputStream in = new BufferedInputStream(inputStream);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        int bufferSize = 16384;
        byte[] buffer = new byte[bufferSize];
        int sizeRead;
        while ((sizeRead = in.read(buffer)) != -1) {
            digest.update(buffer, 0, sizeRead);
        }
        in.close();
        return digest.digest();
    }

    public static String getBytesHashSHA256String(byte[] bytes) throws Exception {
        return bytesToString(getBytesHashSHA256(bytes)).toLowerCase();
    }

    public static byte[] getBytesHashSHA256(byte[] bytes) throws Exception {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        digest.update(bytes, 0, bytes.length);
        return digest.digest();
    }

    public static int getDefaultMemory() {
        return (int) ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize() / (4 * 1024 * 1024);
    }

    public static byte[] getBytes(File file) throws IOException {
        return FileUtils.toByteArray(file);
    }

    public static List<String> addDoubleQuotationMark(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).contains(" ")) {
                list.set(i, "\"" + list.get(i) + "\"");
            }
        }
        return list;
    }

    public static String randomUUIDNoSymbol() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getUUIDByName(String playerName) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(StandardCharsets.UTF_8)).toString().replace("-", "");
    }

    public static void setProxy(String host, String port, String userName, String password) {
        if (Utils.isEmpty(host)) return;
        System.setProperty("java.net.useSystemProxies", "true");
        System.setProperty("https.proxyHost", host);
        System.setProperty("http.proxyHost", host);
        if (!isEmpty(port)) {
            System.setProperty("https.proxyPort", port);
            System.setProperty("http.proxyPort", port);
        }
        if (!isEmpty(userName)) {
            System.setProperty("http.proxyUserName", userName);
            System.setProperty("https.proxyUserName", userName);
            if (!isEmpty(password)) {
                System.setProperty("http.proxyPassword", password);
                System.setProperty("https.proxyPassword", password);
            }

        }
    }

    public static void writeFile(File file, String content, boolean append) throws IOException {
        createFile(file, false);
        FileWriter writer = new FileWriter(file, append);
        writer.write(content);
        writer.close();
    }

    /**
     * @return KEY is downloadURL and VALUE is storage path
     **/
    public static Pair<String, String> getLibraryDownloadURLAndStoragePath(JSONObject library) {
        JSONObject downloads = library.optJSONObject("downloads");
        if (downloads != null) {
            JSONObject artifactJo = downloads.optJSONObject("artifact");
            if (artifactJo != null) {
                String path = artifactJo.optString("path");
                String url = artifactJo.optString("url");
                if (!isEmpty(path) && !isEmpty(url)) {
                    url = url.replace("https://libraries.minecraft.net/", DownloadSource.getProvider().libraries());
                    return new Pair<>(url, path);
                }
            }
        } else {
            String name = library.optString("name");
            String url = library.optString("url", DownloadSource.getProvider().libraries());
            if (url.equals("https://maven.fabricmc.net/") || url.equals("https://maven.fabricmc.net")) {
                url = DownloadSource.getProvider().fabricMaven();
            } else if (url.equals("http://repo.maven.apache.org/maven2/") || url.equals("http://repo.maven.apache.org/maven2")) {
                url = "https://repo.maven.apache.org/maven2/";
            }
            if (isEmpty(name)) return null;
            String[] nameSplit = name.split(":");
            if (nameSplit.length < 3) return null;
            String fileName = nameSplit[1] + "-" + nameSplit[2] + ".jar";
            String path = nameSplit[0].replace(".", "/") + "/" + nameSplit[1] + "/" + nameSplit[2] + "/" + fileName;
            return new Pair<>(addSlashIfMissing(url) + path, path);
        }
        return null;
    }

    public static List<JSONObject> jsonArrayToJSONObjectList(JSONArray jsonArray) {
        List<JSONObject> list = new LinkedList<>();
        if (jsonArray == null || jsonArray.length() == 0) return list;
        for (Object object : jsonArray) {
            if (object instanceof JSONObject) {
                list.add((JSONObject) object);
            }
        }
        return list;
    }

    public static String getTimezoneName() {
        return TimeZone.getDefault().getDisplayName();
    }
}
