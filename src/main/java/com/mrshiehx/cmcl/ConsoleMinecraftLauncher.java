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
package com.mrshiehx.cmcl;

import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.constants.Languages;
import com.mrshiehx.cmcl.options.HelpOption;
import com.mrshiehx.cmcl.options.Option;
import com.mrshiehx.cmcl.options.Options;
import com.mrshiehx.cmcl.options.StartOption;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.PercentageTextProgress;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ConsoleMinecraftLauncher {
    public static final String CLIENT_ID = Constants.CLIENT_ID;
    public static final String CMCL_COPYRIGHT = Constants.COPYRIGHT;
    public static final String CMCL_VERSION = Constants.CMCL_VERSION;
    public static final File configFile = Constants.configFile;

    public static File gameDir;
    public static File assetsDir;
    public static File respackDir;
    public static File versionsDir;
    public static File librariesDir;
    public static Process runningMc;

    public static JSONObject configContent;
    public static String javaPath = "";

    private static String language;

    static {
        initConfig();
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (runningMc != null && configContent.optBoolean("exitWithMinecraft")) {
                if (runningMc.isAlive()) {
                    runningMc.destroy();
                }
            }
        }));
        Arguments arguments = new Arguments(args);
        Argument arg = arguments.optArgument(0);
        if (arg != null) {
            console(arguments);
        } else {
            String version;
            JSONObject jsonObject = Utils.getConfig();
            version = jsonObject.optString("selectedVersion");
            if (isEmpty(version)) {
                System.out.println(getString("CONSOLE_NO_SELECTED_VERSION"));
                return;
            }
            StartOption.start(version, jsonObject);
        }
    }

    private static void console(Arguments args) {
        Argument argument = args.optArgument(0);
        if (argument != null) {
            String key = argument.key;
            Option option = Options.MAP.get(key.toLowerCase());
            if (option != null) {
                Argument usage = args.optArgument(1);
                if (usage != null && !(option instanceof HelpOption)) {
                    if (usage.equals("usage") || usage.equals("help") || usage.equals("u") || usage.equals("h")) {
                        String name = option.getUsageName();
                        if (!isEmpty(name)) {
                            System.out.println(getUsage(name));
                        } else {
                            System.out.println(getUsage("TITLE"));
                        }
                    } else {
                        option.execute(args);
                    }
                } else {
                    option.execute(args);
                }
            } else {
                System.out.println(getString("CONSOLE_UNKNOWN_OPTION", key));
            }
        }
    }

    public static String getUsage(String usageName) {
        if (isEmpty(usageName)) return null;
        String t = ("zh".equals(getLanguage()) ? Languages.zhUsage : Languages.enUsage).get(usageName);
        return isEmpty(t) ? "" : t;
    }

    public static JSONObject initConfig() {
        if (configFile.exists()) {
            try {
                configContent = new JSONObject(Utils.readFileContent(configFile));
            } catch (Exception e) {
                e.printStackTrace();
                configContent = new JSONObject();
            }
            javaPath = configContent.optString("javaPath", Utils.getDefaultJavaPath());
            gameDir = new File(!isEmpty(configContent.optString("gameDir")) ? configContent.optString("gameDir") : ".minecraft");
            assetsDir = !isEmpty(configContent.optString("assetsDir")) ? new File(configContent.optString("assetsDir")) : new File(gameDir, "assets");
            respackDir = !isEmpty(configContent.optString("resourcesDir")) ? new File(configContent.optString("resourcesDir")) : new File(gameDir, "resourcepacks");
        } else {
            initDefaultDirs();
            configContent = new JSONObject();
            configContent.put("language", Locale.getDefault().getLanguage());
            configContent.put("javaPath", javaPath = Utils.getDefaultJavaPath());
            configContent.put("maxMemory", Utils.getDefaultMemory());
            configContent.put("windowSizeWidth", 854);
            configContent.put("windowSizeHeight", 480);
            configContent.put("exitWithMinecraft", false);
            try {
                configFile.createNewFile();
                FileWriter writer = new FileWriter(configFile, false);
                writer.write(configContent.toString(com.mrshiehx.cmcl.constants.Constants.INDENT_FACTOR));
                writer.close();
            } catch (IOException E) {
                E.printStackTrace();
            }
        }
        versionsDir = new File(gameDir, "versions");
        librariesDir = new File(gameDir, "libraries");
        initProxy(configContent);
        return configContent;
    }

    private static void initProxy(JSONObject configContent) {
        String proxyHost = configContent.optString("proxyHost");
        if (isEmpty(proxyHost)) return;
        Utils.setProxy(proxyHost, configContent.optString("proxyPort"), configContent.optString("proxyUsername"), configContent.optString("proxyPassword"));
    }


    public static void initDefaultDirs() {
        gameDir = new File(".minecraft");
        assetsDir = new File(gameDir, "assets");
        respackDir = new File(gameDir, "resourcepacks");
    }


    public static int numberOfAStringStartInteger(String target) {
        int r = 0;
        char[] targetChars = target.toCharArray();
        for (int i = 0; i < target.length(); i++) {
            if (targetChars[i] == '0' || targetChars[i] == '1' || targetChars[i] == '2' || targetChars[i] == '3' || targetChars[i] == '4' || targetChars[i] == '5' || targetChars[i] == '6' || targetChars[i] == '7' || targetChars[i] == '8' || targetChars[i] == '9') {
                r++;
            } else {
                break;
            }
        }
        return r;
    }

    public static void downloadFile(String url, File to) throws IOException {
        /*Utils.createFile(to, true);
        BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(to);
        byte dataBuffer[] = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            fileOutputStream.write(dataBuffer, 0, bytesRead);
        }
        fileOutputStream.close();*/
        downloadFile(url, to, null);
    }

    public static void downloadFile(String urla, File to, @Nullable PercentageTextProgress progressBar) throws IOException {
        try {
            Utils.createFile(to, true);
            URL url = new URL(urla);
            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
            int completeFileSize = httpConnection.getContentLength();
            if (progressBar != null)
                progressBar.setMaximum(completeFileSize);

            BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
            FileOutputStream fos = new java.io.FileOutputStream(to);
            BufferedOutputStream bout = new BufferedOutputStream(
                    fos, 1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;
                if (progressBar != null)
                    progressBar.setValue((int) downloadedFileSize);
                bout.write(data, 0, x);
            }
            if (progressBar != null)
                progressBar.setValue(completeFileSize);
            bout.close();
            fos.close();
            in.close();
        } catch (IOException e) {
            if (progressBar != null && !progressBar.done) System.out.println();
            throw e;
        }
    }


    public static byte[] downloadBytes(String url) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte dataBuffer[] = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            byteArrayOutputStream.write(dataBuffer, 0, bytesRead);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] downloadBytes(String urla, @Nullable PercentageTextProgress progressBar) throws IOException {
        try {
            URL url = new URL(urla);
            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
            int completeFileSize = httpConnection.getContentLength();
            if (progressBar != null)
                progressBar.setMaximum(completeFileSize);

            BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;
                if (progressBar != null)
                    progressBar.setValue((int) downloadedFileSize);
                byteArrayOutputStream.write(data, 0, x);
            }
            if (progressBar != null)
                progressBar.setValue(completeFileSize);
            in.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            if (progressBar != null && !progressBar.done) System.out.println();
            throw e;
        }
    }


    public static boolean isEmpty(String s) {
        return null == s || s.length() == 0;
    }

    public static boolean isEmpty(JSONObject s) {
        return null == s || s.length() == 0;
    }


    public static void unZip(File srcFile, File to, @Nullable PercentageTextProgress progressBar) throws IOException {
        int BUFFER_SIZE = 2048;
        if (srcFile != null && srcFile.exists()) {
            ZipFile zipFile = new ZipFile(srcFile);

            int size = zipFile.size();
            if (progressBar != null)
                progressBar.setMaximum(size);
            Enumeration<?> entries = zipFile.entries();
            int progress = 0;
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();

                File targetFile = new File(to, entry.getName());
                if (entry.isDirectory()) {
                    targetFile.mkdirs();
                } else {
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }
                    targetFile.createNewFile();
                    InputStream is = zipFile.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[BUFFER_SIZE];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.close();
                    is.close();
                }
                progress++;
                if (progressBar != null)
                    progressBar.setValue(progress);
            }
            zipFile.close();
            if (progressBar != null)
                progressBar.setValue(size);
        }
    }


    public static String getString(String name, Object... objects) {
        return String.format(getString(name), objects);
    }

    public static String getString(String name) {
        if (getLanguage().equals("zh")) {
            String text = Languages.zh.get(name);
            if (!Utils.isEmpty(text)) return text;
        }
        String text = Languages.en.get(name);
        if (!Utils.isEmpty(text)) return text;
        else return name;
    }

    public static String getLanguage() {
        if (isEmpty(language)) {
            String lang = Utils.getConfig().optString("language");
            if (isEmpty(lang)) {
                configContent.put("language", language = Locale.getDefault().getLanguage());
                Utils.saveConfig(configContent);
            } else {
                language = lang;
            }
        }
        return language;
    }


    public static List<String> listVersions(File versionsDir) {
        List<String> versionsStrings = new ArrayList<>();
        if (versionsDir == null) return versionsStrings;
        File[] files = versionsDir.listFiles(pathname -> {
            if (!pathname.isDirectory()) return false;
            File[] files1 = pathname.listFiles();
            if (files1 == null || files1.length < 2) return false;
            return new File(pathname, pathname.getName() + ".json").exists() && new File(pathname, pathname.getName() + ".jar").exists();
        });
        if (files != null && files.length > 0) {
            for (File file : files) {
                versionsStrings.add(file.getName());
            }
        }
        return versionsStrings;
    }
}