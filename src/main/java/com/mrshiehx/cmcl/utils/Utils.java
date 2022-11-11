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
import com.mrshiehx.cmcl.api.download.DefaultApiProvider;
import com.mrshiehx.cmcl.api.download.DownloadSource;
import com.mrshiehx.cmcl.bean.GameVersion;
import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.bean.SplitLibraryName;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.exceptions.NotSelectedException;
import com.mrshiehx.cmcl.interfaces.filters.JSONObjectFilter;
import com.mrshiehx.cmcl.modules.account.loginner.MicrosoftAccountLoginner;
import com.mrshiehx.cmcl.options.AccountOption;
import com.sun.management.OperatingSystemMXBean;
import org.jenkinsci.constant_pool_scanner.ConstantPool;
import org.jenkinsci.constant_pool_scanner.ConstantType;
import org.jenkinsci.constant_pool_scanner.StringConstant;
import org.jetbrains.annotations.NotNull;
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
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

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
    public static final Comparator<String> VERSION_COMPARATOR = (o1, o2) -> {
        o1 = o1.replace("3D-Shareware-v1.34", "3D-Shareware-v1-34")
                .replace("3D Shareware v1.34", "3D-Shareware-v1-34")
                .replace(" Pre-Release ", "-pre");
        o2 = o2.replace("3D-Shareware-v1.34", "3D-Shareware-v1-34")
                .replace("3D Shareware v1.34", "3D-Shareware-v1-34")
                .replace(" Pre-Release ", "-pre");

        String[] o1s = Utils.xsplit(o1, "\\.");
        String[] o2s = Utils.xsplit(o2, "\\.");

        int ONE = -1;
        int NONE = 1;
        if (o1s.length == 0 || o2s.length == 0) {
            if (o1s.length == 0 && o2s.length == 0) {
                if ("20w14infinite".equals(o1)) {
                    o1 = "20w13c";
                }
                if ("20w14infinite".equals(o2)) {
                    o2 = "20w13c";
                }
                if ("22w13oneblockatatime".equals(o1)) {
                    o1 = "22w13b";
                }
                if ("22w13oneblockatatime".equals(o2)) {
                    o2 = "22w13b";
                }
                if ("3D-Shareware-v1-34".equals(o1)) {
                    o1 = "19w13c";
                }
                if ("3D-Shareware-v1-34".equals(o2)) {
                    o2 = "19w13c";
                }
                if ("1.RV-Pre1".equals(o1)) {
                    o1 = "16w13a";
                }
                if ("1.RV-Pre1".equals(o2)) {
                    o2 = "16w13a";
                }
                int[] o1i = new int[]{Integer.parseInt(o1.substring(0, 2)), Integer.parseInt(o1.substring(3, 5)), (int) o1.charAt(5)};
                int[] o2i = new int[]{Integer.parseInt(o2.substring(0, 2)), Integer.parseInt(o2.substring(3, 5)), (int) o2.charAt(5)};


                for (int i = 0; i < 3; i++) {
                    int o1ii = o1i[i];
                    int o2ii = o2i[i];
                    if (o1ii > o2ii) {
                        return NONE;
                    } else if (o1ii < o2ii) {
                        return ONE;
                    }
                }
                return 0;
            } else if (o1s.length == 0) {
                return ONE;
            } else {
                return NONE;
            }
        }

        int bigger = Math.max(o1s.length, o2s.length);

        int[] o1i = new int[bigger + 2];
        int[] o2i = new int[bigger + 2];

        for (int i = 0; i < bigger; i++) {
            if (i < o1s.length) {
                String o1String = o1s[i];
                if (o1String.contains("-pre")) {
                    o1i[i + 2] = Integer.parseInt(o1String.substring(0, o1String.indexOf("-pre")));
                    o1i[1] = Integer.parseInt(o1String.substring(o1String.indexOf("-pre") + 4));
                } else if (o1String.contains("-rc")) {
                    o1i[i + 2] = Integer.parseInt(o1String.substring(0, o1String.indexOf("-rc")));
                    o1i[0] = Integer.parseInt(o1String.substring(o1String.indexOf("-rc") + 3));
                } else {
                    o1i[i + 2] = Integer.parseInt(o1String);
                    o1i[0] = -1;
                    o1i[1] = -1;
                }
            }
        }
        for (int i = 0; i < bigger; i++) {
            if (i < o2s.length) {
                String o2String = o2s[i];
                if (o2String.contains("-pre")) {
                    o2i[i + 2] = Integer.parseInt(o2String.substring(0, o2String.indexOf("-pre")));
                    o2i[1] = Integer.parseInt(o2String.substring(o2String.indexOf("-pre") + 4));
                } else if (o2String.contains("-rc")) {
                    o2i[i + 2] = Integer.parseInt(o2String.substring(0, o2String.indexOf("-rc")));
                    o2i[0] = Integer.parseInt(o2String.substring(o2String.indexOf("-rc") + 3));
                } else {
                    o2i[i + 2] = Integer.parseInt(o2String);
                    o2i[0] = -1;
                    o2i[1] = -1;
                }
            }
        }


        for (int i = 2; i < bigger + 2; i++) {
            int o1ii = o1i[i];
            int o2ii = o2i[i];
            if (o1ii > o2ii) {
                return NONE;
            } else if (o1ii < o2ii) {
                return ONE;
            } else {
                if (i + 1 == bigger + 2) {
                    for (int j = 0; j < 2; j++) {
                        int o1rp = o1i[j];
                        int o2rp = o2i[j];
                        if (o1rp > o2rp) {
                            return NONE;
                        } else if (o1rp < o2rp) {
                            return ONE;
                        }
                    }
                }
            }
        }

        return 0;
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

    public static String getCF(String url) throws IOException {
        return getCF(url, "application/json", "application/json");
    }

    public static String getCF(String url, String contentType, String accept) throws IOException {
        URL ConnectUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) ConnectUrl.openConnection();
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("x-api-key", Constants.getCurseForgeApiKey());
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
        File configFile = ConsoleMinecraftLauncher.getConfigFile();
        try {
            if (!configFile.exists())
                Utils.createFile(configFile, false);
            FileWriter writer = new FileWriter(configFile, false);
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
        return "natives-" + OperatingSystem.CURRENT_OS.getCheckedName() + "-" + getArchCheckedName();
    }

    public static String readFileContent(File file) throws IOException {
        BufferedReader reader;
        StringBuilder sbf = new StringBuilder();
        FileReader fr = new FileReader(file);
        reader = new BufferedReader(fr);
        String tempStr;
        while ((tempStr = reader.readLine()) != null) {
            sbf.append(tempStr).append('\n');
        }
        fr.close();
        reader.close();
        return sbf.substring(0, sbf.length() - 1);
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

    public static List<String> splitCommand(String src) {
        List<String> list = new ArrayList<>();
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
        return list;
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
        System.out.printf(str, object);//legal
        System.out.println();
    }

    public static void printflnErr(String str, Object... object) {
        System.out.printf(str, object);//legal
        System.out.println();
    }

    public static boolean versionExists(String name) {
        return new File(ConsoleMinecraftLauncher.versionsDir, name + "/" + name + ".json").exists()
                /*&& new File(ConsoleMinecraftLauncher.versionsDir, name + "/" + name + ".jar").exists()*/;
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

    public static byte[] httpURLConnection2Bytes(HttpURLConnection con) throws IOException {
        try {
            try (InputStream stdout = con.getInputStream()) {
                return inputStream2ByteArray(stdout);
            }
        } catch (IOException e) {
            try (InputStream stderr = con.getErrorStream()) {
                if (stderr == null) throw e;
                return inputStream2ByteArray(stderr);
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

    public static List<String> parseJVMArgs(String[] jvmArgs) {
        List<String> result = new LinkedList<>();
        if (jvmArgs == null || jvmArgs.length == 0)
            return result;
        for (String v : jvmArgs) {
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
        Map<String, String> result = new LinkedHashMap<>();
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

    public static JSONObject getSelectedAccount(JSONObject config, boolean prompt) throws NotSelectedException {
        JSONArray accounts = config.optJSONArray("accounts");
        if (accounts == null || accounts.length() == 0) {
            if (prompt) System.out.println(getString("NOT_SELECTED_AN_ACCOUNT"));
            throw new NotSelectedException();
        }
        for (Object o : accounts) {
            if (o instanceof JSONObject) {
                JSONObject jsonObject1 = (JSONObject) o;
                if (jsonObject1.optBoolean("selected") && Utils.isValidAccount(jsonObject1)) {
                    return jsonObject1;
                }
            }
        }
        if (prompt) System.out.println(getString("NOT_SELECTED_AN_ACCOUNT"));
        throw new NotSelectedException();
    }

    public static JSONObject getSelectedAccountIfNotLoginNow(JSONObject config) {
        JSONArray accounts = config.optJSONArray("accounts");
        int valid = 0;
        if (accounts != null) {
            for (Object o : accounts) {
                if (o instanceof JSONObject) {
                    JSONObject jsonObject1 = (JSONObject) o;
                    if (Utils.isValidAccount(jsonObject1)) {
                        valid++;
                        if (jsonObject1.optBoolean("selected")) {
                            return jsonObject1;
                        }
                    }
                }
            }
        }
        if (accounts != null && valid > 0) {
            AccountOption.printAllAccounts(accounts);
            int order = ConsoleUtils.inputInt(getString("MESSAGE_SELECT_ACCOUNT", 0, accounts.length() - 1), 0, accounts.length() - 1);
            JSONObject account = accounts.optJSONObject(order);

            if (!Utils.isValidAccount(account)) {
                Utils.printfln(getString("ACCOUNT_INVALID"), order);
                return null;
            }

            account.put("selected", true);
            for (int i = 0; i < accounts.length(); i++) {
                if (i != order) {
                    JSONObject acc = accounts.optJSONObject(i);
                    if (acc != null) {
                        acc.put("selected", false);
                    }
                }
            }
            config.put("accounts", accounts);
            Utils.saveConfig(config);
            return account;
        } else {
            System.out.println("[0]" + Utils.getString("ACCOUNT_TYPE_OFFLINE"));
            System.out.println("[1]" + Utils.getString("ACCOUNT_TYPE_MICROSOFT"));
            System.out.println("[2]" + Utils.getString("ACCOUNT_TYPE_OAS"));
            int sel = ConsoleUtils.inputInt(getString("MESSAGE_SELECT_ACCOUNT_TYPE", 0, 2), 0, 2);
            if (accounts == null) {
                config.put("accounts", accounts = new JSONArray());
            }
            switch (sel) {
                case 0: {
                    JSONObject account = new JSONObject().put("playerName", ConsoleUtils.inputString(getString("ACCOUNT_TIP_LOGIN_OFFLINE_PLAYERNAME"))).put("selected", true).put("loginMethod", 0);
                    accounts.put(account);
                    Utils.saveConfig(config);
                    return account;
                }
                case 1: {
                    MicrosoftAccountLoginner.loginMicrosoftAccount(config, true);
                    while (true) {
                        if (MicrosoftAccountLoginner.status == 0) {
                            return null;
                        } else if (MicrosoftAccountLoginner.status == 1) {
                            return MicrosoftAccountLoginner.account;
                        }
                    }
                }
                case 2: {
                    return AccountOption.authlibInjectorLogin(ConsoleUtils.inputString(getString("ACCOUNT_TIP_LOGIN_OAS_ADDRESS")), true, config);
                }
                default:
                    return null;
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

    public static String addSlashIfMissingAtStart(String url) {
        if (!(url.charAt(0) == '/')) url = '/' + url;
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

    public static long getDefaultMemory() {
        return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize() / (4 * 1024 * 1024);
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
        if (Utils.isEmpty(host) || Utils.isEmpty(port)) return;
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
                if (isEmpty(path) && !isEmpty(library.optString("name"))) {

                    String name = library.optString("name");

                    SplitLibraryName nameSplit = splitLibraryName(name);
                    if (nameSplit != null) {

                        String fileName = nameSplit.getFileName();
                        path = getPathFromLibraryName(nameSplit) + "/" + fileName;
                    }
                }
                if (!isEmpty(path) || !isEmpty(url)) {
                    String url2 = null;
                    if (!isEmpty(url)) {
                        url2 = replaceUrl(url);//.replace("https://libraries.minecraft.net/", DownloadSource.getProvider().libraries()).replace("https://maven.minecraftforge.net/", DownloadSource.getProvider().forgeMaven());
                    }
                    return new Pair<>(url2, path);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            String name = library.optString("name");
            String url = library.optString("url", DownloadSource.getProvider().libraries());
            /*switch (url) {
                case "https://maven.fabricmc.net/":
                case "https://maven.fabricmc.net":
                    url = DownloadSource.getProvider().fabricMaven();
                    break;
                case "http://repo.maven.apache.org/maven2/":
                case "http://repo.maven.apache.org/maven2":
                    url = "https://repo.maven.apache.org/maven2/";
                    break;
                case "https://maven.minecraftforge.net/":
                case "https://maven.minecraftforge.net":
                case "https://files.minecraftforge.net/maven/":
                case "https://files.minecraftforge.net/maven":
                    if (!(DownloadSource.getProvider() instanceof DefaultApiProvider))
                        url = DownloadSource.getProvider().forgeMaven();
                    break;
            }*/
            url = replaceUrl(url);
            if (isEmpty(name))
                return null;
            SplitLibraryName nameSplit = splitLibraryName(name);
            if (nameSplit == null)
                return null;
            String fileName = nameSplit.getFileName();
            String path = getPathFromLibraryName(nameSplit) + "/" + fileName;
            return new Pair<>(addSlashIfMissing(url) + (name.startsWith("net.minecraftforge:forge:") ? path.substring(0, path.length() - 4) + "-universal.jar" : path), path);
        }
    }

    public static String replaceUrl(String url) {
        if (isEmpty(url)) return url;
        String a;
        if (url.contains(a = "https://libraries.minecraft.net/")) {
            url = url.replace(a, DownloadSource.getProvider().libraries());

        }
        if (url.contains(a = "https://maven.fabricmc.net/")) {
            url = url.replace(a, DownloadSource.getProvider().fabricMaven());

        }

        if (url.contains(a = "http://repo.maven.apache.org/maven2/")) {
            url = url.replace(a, "https://repo.maven.apache.org/maven2/");

        }

        if (url.contains(a = "https://maven.minecraftforge.net/")) {
            if (!(DownloadSource.getProvider() instanceof DefaultApiProvider)) {
                url = url.replace(a, DownloadSource.getProvider().forgeMaven());
            }

        }

        if (url.contains(a = "https://files.minecraftforge.net/maven/")) {
            if (!(DownloadSource.getProvider() instanceof DefaultApiProvider)) {
                url = url.replace(a, DownloadSource.getProvider().forgeMaven());
            }
        }
        if (url.contains(a = "http://repo.liteloader.com/")) {
            url = url.replace(a, "https://repo.liteloader.com/");
        }


        return url;
    }

    public static List<JSONObject> jsonArrayToJSONObjectList(JSONArray jsonArray) {
        return jsonArrayToJSONObjectList(jsonArray, null);
    }

    public static List<JSONObject> jsonArrayToJSONObjectList(JSONArray jsonArray, JSONObjectFilter filter) {
        List<JSONObject> list = new LinkedList<>();
        if (jsonArray == null || jsonArray.length() == 0) return list;
        for (Object object : jsonArray) {
            if (object instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) object;
                if (filter != null) {
                    if (filter.accept(jsonObject))
                        list.add(jsonObject);
                } else
                    list.add(jsonObject);
            }
        }
        return list;
    }


    public static String getTimezoneName() {
        return TimeZone.getDefault().getDisplayName();
    }

    public static GameVersion getVersionByJar(File jarFile) {
        try (JarFile jar = new JarFile(jarFile)) {
            ZipEntry versionEntry = jar.getEntry("version.json");
            if (versionEntry != null) {
                JSONObject jsonObject = parseJSONObject(Utils.inputStream2String(jar.getInputStream(versionEntry)));
                if (jsonObject != null) {
                    String name = jsonObject.optString("name");
                    String id = jsonObject.optString("id");
                    if (id.contains(" / ")) {
                        id = id.split(" / ")[0];
                    }
                    return new GameVersion(id, name);
                }
            }
            ZipEntry mainClass = jar.getEntry("net/minecraft/client/Minecraft.class");
            if (mainClass != null) {
                ConstantPool pool = org.jenkinsci.constant_pool_scanner.ConstantPoolScanner.parse(jar.getInputStream(mainClass), ConstantType.STRING);
                for (StringConstant stringConstant : pool.list(StringConstant.class)) {
                    String v = stringConstant.get();
                    String prefix = "Minecraft Minecraft ";
                    if (v.startsWith(prefix) && v.length() > prefix.length()) {
                        return new GameVersion(v.substring(prefix.length()), null);
                    }
                }
            }


            ZipEntry serverClass = jar.getEntry("net/minecraft/server/MinecraftServer.class");
            if (serverClass != null) {
                ConstantPool pool = org.jenkinsci.constant_pool_scanner.ConstantPoolScanner.parse(jar.getInputStream(serverClass), ConstantType.STRING);
                List<String> strings = new LinkedList<>();
                for (StringConstant stringConstant : pool.list(StringConstant.class)) {
                    String v = stringConstant.get();
                    strings.add(v);
                }
                int indexOf = -1;
                for (int i = 0; i < strings.size(); i++) {
                    if (strings.get(i).startsWith("Can't keep up!")) {
                        indexOf = i;
                        break;
                    }
                }
                if (indexOf >= 0) {
                    for (int i = indexOf - 1; i >= 0; --i) {
                        String s = strings.get(i);
                        if (s.matches(".*[0-9].*")) {

                            return new GameVersion(s, null);
                        }
                    }
                }
            }

        } catch (Exception ignore) {
        }
        return null;
    }

    @NotNull
    public static GameVersion getGameVersion(JSONObject json, File jar) {
        String v = json.optString("gameVersion");
        if (!isEmpty(v))
            return new GameVersion(v, null);

        GameVersion gameVersion = getVersionByJar(jar);
        if (gameVersion != null) {
            String id = gameVersion.id;
            String name = gameVersion.name;
            if (!isEmpty(id)) return new GameVersion(id, name);
        }
        return new GameVersion(null, null);
    }

    public static JSONArray mergeLibraries(List<JSONObject> source, List<JSONObject> target) {
        JSONArray jsonArray = new JSONArray();
        if ((source == null || source.size() == 0) && (target == null || target.size() == 0))
            return jsonArray;
        else if ((source != null && source.size() > 0) && (target == null || target.size() == 0)) {
            jsonArray.putAll(source);
            return jsonArray;
        } else if (source == null || source.size() == 0) {
            for (JSONObject j : target) {
                j.remove("clientreq");
                j.remove("serverreq");
            }
            jsonArray.putAll(target);
            return jsonArray;
        }
        jsonArray.putAll(source);
        for (JSONObject jsonObject : target) {
            String targetName = jsonObject.optString("name");
            int indexOf = -1;
            for (int j = 0; j < source.size(); j++) {
                JSONObject jsonObject1 = source.get(j);
                String sourceName = jsonObject1.optString("name");
                if (sourceName.equals(targetName)) {
                    indexOf = j;
                    break;
                } else {
                    String[] targetNameSplit = targetName.split(":");
                    String[] sourceNameSplit = sourceName.split(":");
                    if (targetNameSplit.length == sourceNameSplit.length && sourceNameSplit.length >= 3) {
                        if (Objects.equals(targetNameSplit[0], sourceNameSplit[0]) && Objects.equals(targetNameSplit[1], sourceNameSplit[1])) {
                            indexOf = j;
                            break;
                        }
                    }

                }
            }
            //if (withoutTargetServerreqAndClientreq) {
            jsonObject.remove("clientreq");
            jsonObject.remove("serverreq");
            //}
            if (indexOf < 0) {
                jsonArray.put(jsonObject);
            } else {
                jsonArray.put(indexOf, jsonObject);
            }
        }

        return jsonArray;
    }

    public static String getPathFromLibraryName(SplitLibraryName nameSplit) {
        return nameSplit.first.replace(".", "/") + "/" + nameSplit.second + "/" + nameSplit.version;
    }

    public static String getArchInt() {
        String value = System.getProperty("os.arch").trim().toLowerCase(Locale.ROOT);

        switch (value) {
            case "x8664":
            case "x86-64":
            case "x86_64":
            case "amd64":
            case "ia32e":
            case "em64t":
            case "x64":
            case "arm64":
            case "aarch64":
            case "mips64":
            case "mips64el":
            case "riscv":
            case "risc-v":
            case "ia64":
            case "ia64w":
            case "itanium64":
            case "sparcv9":
            case "sparc64":
            case "ppc64le":
            case "powerpc64le":
            case "loongarch64":
            case "s390x":
            case "ppc64":
            case "powerpc64":
                return "64";
            case "x8632":
            case "x86-32":
            case "x86_32":
            case "x86":
            case "i86pc":
            case "i386":
            case "i486":
            case "i586":
            case "i686":
            case "ia32":
            case "x32":
            case "arm":
            case "arm32":
            case "mips":
            case "mips32":
            case "mipsel":
            case "mips32el":
            case "ia64n":
            case "sparc":
            case "sparc32":
            case "ppc":
            case "ppc32":
            case "powerpc":
            case "powerpc32":
            case "s390":
            case "ppcle":
            case "ppc32le":
            case "powerpcle":
            case "powerpc32le":
            case "loongarch32":
                return "32";
            default:
                if (value.startsWith("armv7")) {
                    return "32";
                }
                if (value.startsWith("armv8") || value.startsWith("armv9")) {
                    return "64";
                }
                return "unknown";
        }
    }

    public static String getArchCheckedName() {
        String value = System.getProperty("os.arch").trim().toLowerCase(Locale.ROOT);

        switch (value) {
            case "x8664":
            case "x86-64":
            case "x86_64":
            case "amd64":
            case "ia32e":
            case "em64t":
            case "x64":
                return "x86_64";
            case "x8632":
            case "x86-32":
            case "x86_32":
            case "x86":
            case "i86pc":
            case "i386":
            case "i486":
            case "i586":
            case "i686":
            case "ia32":
            case "x32":
                return "x86";
            case "arm64":
            case "aarch64":
                return "arm64";
            case "arm":
            case "arm32":
                return "arm32";
            case "mips64":
                return "mips64";
            case "mips64el":
                return "mips64el";
            case "mips":
            case "mips32":
                return "mips";
            case "mipsel":
            case "mips32el":
                return "mipsel";
            case "riscv":
            case "risc-v":
                return "riscv";
            case "ia64":
            case "ia64w":
            case "itanium64":
                return "ia64";
            case "ia64n":
                return "ia32";
            case "sparcv9":
            case "sparc64":
                return "sparcv9";
            case "sparc":
            case "sparc32":
                return "sparc";
            case "ppc64":
            case "powerpc64":
                return "little".equals(System.getProperty("sun.cpu.endian")) ? "ppc64le" : "ppc64";
            case "ppc64le":
            case "powerpc64le":
                return "ppc64le";
            case "ppc":
            case "ppc32":
            case "powerpc":
            case "powerpc32":
                return "ppc";
            case "ppcle":
            case "ppc32le":
            case "powerpcle":
            case "powerpc32le":
                return "ppcle";
            case "s390":
                return "s390";
            case "s390x":
                return "s390x";
            case "loongarch32":
                return "loongarch32";
            case "loongarch64":
                return "loongarch64";
            default:
                if (value.startsWith("armv7")) {
                    return "arm32";
                }
                if (value.startsWith("armv8") || value.startsWith("armv9")) {
                    return "arm64";
                }
                return "unknown";
        }
    }

    public static SplitLibraryName splitLibraryName(String name) {
        return SplitLibraryName.valueOf(name);
    }

    public static String getExtension(String string) {
        if (isEmpty(string)) return null;
        int indexOf = string.lastIndexOf('.');
        if (indexOf < 0 || indexOf == string.length() - 1) return null;
        return string.substring(indexOf + 1);
    }

    public static void close(Closeable t) {
        try {
            t.close();
        } catch (IOException ignore) {
        }
    }

    public static boolean isValidAccount(JSONObject jsonObject1) {
        return jsonObject1 != null && !isEmpty(jsonObject1.optString("playerName")) && jsonObject1.has("loginMethod");
    }

    public static void downloadFileFailed(String url, File file, Exception e) {
        System.out.println(
                url.endsWith("/" + file.getName()) ?
                        getString("MESSAGE_FAILED_DOWNLOAD_FILE_WITH_REASON_WITH_URL", e, url, file.getParentFile().getAbsolutePath()) :
                        getString("MESSAGE_FAILED_DOWNLOAD_FILE_WITH_REASON_WITH_URL_WITH_NAME", e, url, file.getParentFile().getAbsolutePath(), file.getName()));
    }

    /**
     * x / x.x / x.x.x / x.x.x.x / x.x.x.x.x / x.x.x.x.x.x / ...
     *
     * @return 0 if un-comparable or they are the same, 1 if v1 > v2, -1 if v1 < v2
     **/
    public static int tryToCompareVersion(String v1, String v2) {
        if (isEmpty(v1) || isEmpty(v2)) return 0;
        if (v1.equals(v2)) return 0;
        try {
            String[] split1 = v1.split("\\.");
            String[] split2 = v2.split("\\.");
            /*if (split1.length == 0 || split2.length == 0) {
                return Integer.compare(Integer.parseInt(v1),Integer.parseInt(v2));
            }*/
            int s1l = split1.length;
            int[] s1 = new int[s1l];
            for (int i = 0; i < s1l; i++) {
                String split = split1[i];
                s1[i] = Integer.parseInt(split);
            }
            int s2l = split2.length;
            int[] s2 = new int[s2l];
            for (int i = 0; i < s2l; i++) {
                String split = split2[i];
                s2[i] = Integer.parseInt(split);
            }

            if (s1l > s2l) {
                int[] newS2 = new int[s1l];
                System.arraycopy(s2, 0, newS2, 0, s2l);
                s2 = newS2;
                s2l = s1l;
            } else if (s1l < s2l) {
                int[] newS1 = new int[s2l];
                System.arraycopy(s1, 0, newS1, 0, s1l);
                s1 = newS1;
                s1l = s2l;
            }

            for (int i = 0; i < s1l; i++) {
                int s11 = s1[i];
                int s22 = s2[i];
                if (s11 > s22) {
                    return 1;
                } else if (s11 < s22) {
                    return -1;
                }
            }
            return 0;

        } catch (Throwable ignore) {
            return 0;
        }
    }

    public static String getModuleVersion(JSONObject head, String mainClass, String libraryFirstAndSecond) {
        if (!isEmpty(mainClass) && !head.optString("mainClass").equals(mainClass)) return null;
        JSONArray libraries = head.optJSONArray("libraries");
        if (libraries == null || libraries.length() == 0) return null;
        for (Object o : libraries) {
            if (o instanceof JSONObject) {
                JSONObject library = (JSONObject) o;
                String name = library.optString("name");

                if (name.startsWith(libraryFirstAndSecond) && name.length() > libraryFirstAndSecond.length()) {
                    return name.substring(libraryFirstAndSecond.length());
                }
            }
        }
        return null;
    }

    public static String getModuleVersion(JSONObject head, String moduleName) {
        JSONObject module = head.optJSONObject(moduleName);
        if (module != null) {
            String version = module.optString("version");
            if (!isEmpty(version)) return version;
        }

        //兼容HMCL
        JSONArray patches = head.optJSONArray("patches");
        if (patches != null && patches.length() > 0) {
            for (Object o : patches) {
                if (o instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) o;
                    if (jsonObject.optString("id").equals(moduleName)) {
                        String version = jsonObject.optString("version");
                        if (!isEmpty(version)) return version;
                    }
                }
            }
        }
        return null;
    }

    public static String getFabricVersion(JSONObject head) {
        String first = getModuleVersion(head, "fabric");
        if (!isEmpty(first)) return first;
        return getModuleVersion(head, "net.fabricmc.loader.impl.launch.knot.KnotClient", "net.fabricmc:fabric-loader:");
    }

    public static String getLiteloaderVersion(JSONObject head) {
        String first = getModuleVersion(head, "liteloader");
        if (!isEmpty(first)) return first;
        return getModuleVersion(head, null, "com.mumfrey:liteloader:");
    }

    public static String getForgeVersion(JSONObject head) {
        String first = getModuleVersion(head, "forge");
        if (!isEmpty(first)) return first;
        String version = null;
        String second = getModuleVersion(head, null, "net.minecraftforge:forge:");
        if (isEmpty(second)) {
            second = getModuleVersion(head, null, "net.minecraftforge:fmlloader:");
        }
        if (!isEmpty(second)) {
            String[] split = second.split("-");
            if (split.length >= 2) {
                version = split[1];
            }
        }
        return version;
    }

    public static String getOptifineVersion(JSONObject head) {
        String first = getModuleVersion(head, "optifine");
        if (!isEmpty(first)) return first;
        String version = null;
        String origin = getModuleVersion(head, null, "optifine:OptiFine:");
        if (!isEmpty(origin)) {
            int indexOf = origin.indexOf('_');
            version = origin.substring(indexOf + 1);
        }
        return version;
    }

    public static String getQuiltVersion(JSONObject head) {
        String first = getModuleVersion(head, "quilt");
        if (!isEmpty(first)) return first;
        return getModuleVersion(head, "org.quiltmc.loader.impl.launch.knot.KnotClient", "org.quiltmc:quilt-loader:");
    }

    public static String[] xsplit(String s, String regex) {
        String[] ss = s.split(regex);
        if (ss[0].equals(s)) {
            return new String[0];
        }
        return ss;
    }
}
