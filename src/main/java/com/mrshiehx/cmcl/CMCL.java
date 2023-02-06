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
package com.mrshiehx.cmcl;

import com.mrshiehx.cmcl.bean.RunningMinecraft;
import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.bean.arguments.SingleArgument;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.constants.languages.Languages;
import com.mrshiehx.cmcl.functions.Function;
import com.mrshiehx.cmcl.functions.Functions;
import com.mrshiehx.cmcl.functions.root.RootFunction;
import com.mrshiehx.cmcl.functions.root.VersionStarter;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.cmcl.AccountUtils;
import com.mrshiehx.cmcl.utils.internet.NetworkUtils;
import com.mrshiehx.cmcl.utils.system.JavaUtils;
import com.mrshiehx.cmcl.utils.system.OperatingSystem;
import com.mrshiehx.cmcl.utils.system.SystemUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class CMCL {
    public static File gameDir;
    public static File assetsDir;
    public static File resourcePacksDir;
    public static File versionsDir;
    public static File librariesDir;
    public static File launcherProfiles;
    public static RunningMinecraft runningMc;

    private static JSONObject configJSONObject;
    public static String javaPath;

    private static String language;
    private static Locale locale;

    public static boolean isImmersiveMode;

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (runningMc != null && runningMc.exitWithMinecraft) {
                if (runningMc.process.isAlive()) {
                    runningMc.process.destroy();
                }
            }
        }));
        //if(Constants.isDebug()) System.out.println(Arrays.toString(args));
        if (args.length == 0) {
            JSONObject jsonObject = Utils.getConfig();
            String version = jsonObject.optString("selectedVersion");
            if (!version.isEmpty()) {
                VersionStarter.start(version, jsonObject);
            } else {
                RootFunction.printHelp();
            }
        } else {
            String first = args[0];
            Function function = Functions.MAP.get(first);
            boolean isRoot;
            if (function != null) {
                isRoot = false;
            } else {
                isRoot = true;
                function = new RootFunction();
            }
            Arguments arguments = new Arguments(args, true);

            Argument second = arguments.optArgument(1);
            if (!isRoot) {
                if (second == null) {
                    System.out.println(getHelpDocumentation(function.getUsageName()));
                    return;
                }
                if ("h".equals(second.key) || "help".equals(second.key)) {
                    if (second instanceof SingleArgument) {
                        String name = function.getUsageName();
                        if (!isEmpty(name)) {
                            System.out.println(getHelpDocumentation(name));
                        } else {
                            RootFunction.printHelp();
                        }
                    } else {
                        System.out.println(getString("CONSOLE_HELP_WRONG_WRITE", second.originString));
                    }
                } else {
                    function.execute(arguments);
                }
            } else {
                function.execute(arguments);
            }
        }
    }

    public static String getHelpDocumentation(String name) {
        if (isEmpty(name)) return null;
        String lang;
        String t;
        if ("zh".equalsIgnoreCase(getLanguage())) {
            t = Languages.getZhHelp().get(name);
        } else if ("cantonese".equalsIgnoreCase(getLanguage())) {
            t = Languages.getCantoneseHelp().get(name);
        } else
            t = Languages.getEnHelp().get(name);
        return !isEmpty(t) ? t : "";
    }


    public static JSONObject initConfig() {
        File configFile = getConfigFile();
        if (configFile.exists()) {
            String EXCEPTION_READ_CONFIG_FILE = Optional.ofNullable(("zh".equals(Locale.getDefault().getLanguage()) ? Languages.getZh() : Languages.getEn()).get("EXCEPTION_READ_CONFIG_FILE")).orElse("Failed to read the configuration file, please make sure the configuration file (cmcl.json) is readable and the content is correct: %s");
            String configFileContent;
            try {
                configFileContent = FileUtils.readFileContent(configFile);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(String.format(EXCEPTION_READ_CONFIG_FILE, e));
                System.exit(1);
                return configJSONObject;
            }
            if (isEmpty(configFileContent)) {
                configJSONObject = new JSONObject();
            } else {
                try {
                    configJSONObject = new JSONObject(configFileContent);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(String.format(EXCEPTION_READ_CONFIG_FILE, e));
                    System.exit(1);
                    return configJSONObject;
                }
            }
            JSONArray accounts = configJSONObject.optJSONArray("accounts");
            if (accounts != null && accounts.length() > 0) {
                boolean alreadyHave = false;
                for (Object o : accounts) {
                    if (!(o instanceof JSONObject)) continue;
                    JSONObject account = (JSONObject) o;
                    if (!account.optBoolean("selected")) continue;
                    if (alreadyHave) {
                        account.put("selected", false);
                    } else {
                        alreadyHave = true;
                    }
                }
                for (int i = accounts.length() - 1; i >= 0; i--) {
                    if (!AccountUtils.isValidAccount(accounts.opt(i))) {
                        accounts.remove(i);
                    }
                }
                Utils.saveConfig(configJSONObject);
            }
            javaPath = configJSONObject.optString("javaPath", JavaUtils.getDefaultJavaPath());
            gameDir = new File(!isEmpty(configJSONObject.optString("gameDir")) ? configJSONObject.optString("gameDir") : ".minecraft");
            assetsDir = !isEmpty(configJSONObject.optString("assetsDir")) ? new File(configJSONObject.optString("assetsDir")) : new File(gameDir, "assets");
            resourcePacksDir = !isEmpty(configJSONObject.optString("resourcesDir")) ? new File(configJSONObject.optString("resourcesDir")) : new File(gameDir, "resourcepacks");
        } else {
            initDefaultDirs();
            configJSONObject = new JSONObject();
            configJSONObject.put("language", Locale.getDefault().getLanguage());
            configJSONObject.put("javaPath", javaPath = JavaUtils.getDefaultJavaPath());
            configJSONObject.put("maxMemory", SystemUtils.getDefaultMemory());
            configJSONObject.put("windowSizeWidth", 854);
            configJSONObject.put("windowSizeHeight", 480);
            try {
                FileUtils.createFile(configFile, false);
                FileWriter writer = new FileWriter(configFile, false);
                writer.write(configJSONObject.toString(com.mrshiehx.cmcl.constants.Constants.INDENT_FACTOR));
                writer.close();
            } catch (IOException e) {
                if (Constants.isDebug()) e.printStackTrace();
                e.printStackTrace();
            }
        }
        initChangelessDirs();
        initProxy(configJSONObject);
        return configJSONObject;
    }

    private static void initChangelessDirs() {
        versionsDir = new File(gameDir, "versions");
        librariesDir = new File(gameDir, "libraries");
        launcherProfiles = new File(gameDir, "launcher_profiles.json");
    }

    private static void initDefaultDirs() {
        gameDir = new File(".minecraft");
        assetsDir = new File(gameDir, "assets");
        resourcePacksDir = new File(gameDir, "resourcepacks");
    }

    private static void initProxy(JSONObject configContent) {
        String proxyHost = configContent.optString("proxyHost");
        String proxyPort = configContent.optString("proxyPort");
        if (isEmpty(proxyHost) || isEmpty(proxyPort)) return;
        NetworkUtils.setProxy(proxyHost, proxyPort, configContent.optString("proxyUsername"), configContent.optString("proxyPassword"));
    }

    public static boolean isEmpty(String s) {
        return null == s || s.length() == 0;
    }

    public static boolean isEmpty(JSONObject s) {
        return null == s || s.length() == 0;
    }

    public static String getString(String name, Object... objects) {
        return String.format(getString(name), objects);
    }

    public static String getString(String name) {
        //先检查粤语的，如果粤语没有的就读取中文的
        if ("cantonese".equalsIgnoreCase(getLanguage())) {
            String text = Languages.getCantonese().get(name);
            if (!Utils.isEmpty(text))
                return text;
        }
        if ("zh".equalsIgnoreCase(getLanguage()) || "cantonese".equalsIgnoreCase(getLanguage())) {
            String text = Languages.getZh().get(name);
            if (!Utils.isEmpty(text))
                return text;
        }
        String text = Languages.getEn().get(name);
        if (!Utils.isEmpty(text)) return text;
        else return name;
    }

    public static String getLanguage() {
        if (language == null) {
            String lang = Utils.getConfig().optString("language");
            if (isEmpty(lang)) {
                Utils.saveConfig(Utils.getConfig().put("language", language = Locale.getDefault().getLanguage()));
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
            if (files1 == null || files1.length < 1) return false;
            return new File(pathname, pathname.getName() + ".json").exists() /*&& new File(pathname, pathname.getName() + ".jar").exists()*/;
        });
        if (files != null) {
            for (File file : files) {
                versionsStrings.add(file.getName());
            }
        }

        return versionsStrings;
    }

    public static void createLauncherProfiles() {
        if (launcherProfiles.exists()) return;
        try {
            launcherProfiles.createNewFile();
            FileUtils.writeFile(launcherProfiles, "{\"selectedProfile\": \"(Default)\",\"profiles\": {\"(Default)\": {\"name\": \"(Default)\"}},\"clientToken\": \"88888888-8888-8888-8888-888888888888\"}", false);
        } catch (Exception ignore) {
        }
    }

    public static Locale getLocale() {
        if (locale == null) {
            String lang = getLanguage();
            if ("zh".equalsIgnoreCase(lang)) {
                locale = Locale.SIMPLIFIED_CHINESE;
            } else if ("cantonese".equalsIgnoreCase(lang)) {
                locale = Locale.CHINA;
            } else
                locale = Locale.ENGLISH;
        }
        return locale;
    }

    public static File getConfigFile() {
        if (OperatingSystem.CURRENT_OS == OperatingSystem.LINUX) {
            File inConfigDir = new File(System.getProperty("user.home"), ".config/cmcl/cmcl.json");
            if (inConfigDir.exists()) {
                return inConfigDir;
            }
            if (Constants.DEFAULT_CONFIG_FILE.exists()) {
                return Constants.DEFAULT_CONFIG_FILE;
            }
            return inConfigDir;
        }
        return Constants.DEFAULT_CONFIG_FILE;
    }

    public static void saveConfig(JSONObject jsonObject) {
        configJSONObject = jsonObject;
        File configFile = CMCL.getConfigFile();
        try {
            if (!configFile.exists())
                FileUtils.createFile(configFile, false);
            FileWriter writer = new FileWriter(configFile, false);
            writer.write(jsonObject.toString(com.mrshiehx.cmcl.constants.Constants.INDENT_FACTOR));
            writer.close();
        } catch (IOException e) {
            if (Constants.isDebug()) e.printStackTrace();
            System.out.println(getString("EXCEPTION_SAVE_CONFIG", e));
        }
    }

    public static JSONObject getConfig() {
        if (configJSONObject != null)
            return configJSONObject;
        return initConfig();
    }
}