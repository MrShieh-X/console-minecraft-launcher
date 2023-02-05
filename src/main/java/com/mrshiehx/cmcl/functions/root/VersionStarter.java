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
package com.mrshiehx.cmcl.functions.root;

import com.mrshiehx.cmcl.CMCL;
import com.mrshiehx.cmcl.bean.RunningMinecraft;
import com.mrshiehx.cmcl.bean.VersionConfig;
import com.mrshiehx.cmcl.enums.GameCrashError;
import com.mrshiehx.cmcl.exceptions.*;
import com.mrshiehx.cmcl.functions.AccountFunction;
import com.mrshiehx.cmcl.functions.VersionFunction;
import com.mrshiehx.cmcl.modules.MinecraftLauncher;
import com.mrshiehx.cmcl.modules.account.authentication.AccountRefresher;
import com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.authlib.AuthlibInjectorInformation;
import com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.nide8auth.Nide8AuthInformation;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.cmcl.AccountUtils;
import com.mrshiehx.cmcl.utils.cmcl.ArgumentsUtils;
import com.mrshiehx.cmcl.utils.cmcl.version.VersionUtils;
import com.mrshiehx.cmcl.utils.console.ConsoleUtils;
import com.mrshiehx.cmcl.utils.json.JSONUtils;
import com.mrshiehx.cmcl.utils.system.SystemUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import static com.mrshiehx.cmcl.CMCL.*;
import static com.mrshiehx.cmcl.modules.MinecraftLauncher.launchMinecraft;

public class VersionStarter {
    public static VersionConfig getVersionInfo(File versionFolder) {
        File versionInfoFile = new File(versionFolder, "cmclversion.json");
        File versionInfoFileHMCL = new File(versionFolder, "hmclversion.cfg");
        JSONObject cmclJO = null, hmclJO = null;
        if (versionInfoFile.exists()) {
            try {
                cmclJO = JSONUtils.parseJSONObject(FileUtils.readFileContent(versionInfoFile));
            } catch (Throwable ignored) {
            }
        }
        if (versionInfoFileHMCL.exists()) {
            try {
                hmclJO = JSONUtils.parseJSONObject(FileUtils.readFileContent(versionInfoFileHMCL));
            } catch (Throwable ignored) {
            }
        }

        VersionConfig cmcl = null, hmcl = null;
        if (cmclJO != null) {
            cmcl = VersionConfig.valueOf(cmclJO);
        }
        if (hmclJO != null) {
            hmcl = VersionConfig.valueOfHMCL(hmclJO);
        }

        VersionConfig Final = VersionConfig.EMPTY;
        if (cmcl != null && hmcl != null) {
            Final = hmcl.merge(cmcl);
        } else if (cmcl != null) {
            Final = cmcl;
        } else if (hmcl != null) {
            Final = hmcl;
        }
        return Final;
    }

    public static void execute(String version) {
        JSONObject config = Utils.getConfig();

        if (isEmpty(version)) {
            version = config.optString("selectedVersion");
            if (isEmpty(version)) {
                System.out.println(getString("CONSOLE_NO_SELECTED_VERSION"));
                return;
            }
        }
        if (!config.has("exitWithMinecraft")) {
            config.put("exitWithMinecraft", ConsoleUtils.yesOrNo(getString("CONSOLE_ASK_EXIT_WITH_MC")));
        }
        if (!config.has("printStartupInfo")) {
            config.put("printStartupInfo", ConsoleUtils.yesOrNo(getString("CONSOLE_ASK_PRINT_STARTUP_INFO")));
        }
        if (!config.has("checkAccountBeforeStart")) {
            config.put("checkAccountBeforeStart", ConsoleUtils.yesOrNo(getString("CONSOLE_ASK_CHECK_ACCOUNT")));
        }
        Utils.saveConfig(config);

        start(version, config);
    }

    public static void start(String version, JSONObject config) {
        /*String javaPath = config.optString("javaPath", Utils.getDefaultJavaPath());
        if (isEmpty(javaPath) || !new File(javaPath).exists()) {
            System.out.println(getString("CONSOLE_INCORRECT_JAVA"));
        } else {*/
        File versionsFolder = new File(gameDir, "versions");
        File versionFolder = new File(versionsFolder, version);
        File versionJarFile = new File(versionFolder, version + ".jar");
        File versionJsonFile = new File(versionFolder, version + ".json");
        try {

            JSONObject account = AccountUtils.getSelectedAccountIfNotLoginNow(config);

            if (account == null) return;

            String accessToken = Utils.randomUUIDNoSymbol(), uuid = AccountUtils.getUUIDByName(account.optString("playerName", "XPlayer"));
            if (account.optInt("loginMethod") > 0) {
                accessToken = account.optString("accessToken", accessToken);
                uuid = account.optString("uuid", uuid);
            }

            VersionConfig versionConfig = VersionStarter.getVersionInfo(versionFolder);
            File workingDirectory = Boolean.parseBoolean(versionConfig.isolate) ? versionFolder :
                    (!Utils.isEmpty(versionConfig.gameDir)
                            ? new File(versionConfig.gameDir)
                            : (MinecraftLauncher.isModpack(versionFolder) ? versionFolder : gameDir));

            String javaPath;
            int maxMemory;
            int windowSizeWidth;
            int windowSizeHeight;

            String viJavaPath = versionConfig.javaPath;
            String viMaxMemory = versionConfig.maxMemory;
            String viWindowSizeWidth = versionConfig.windowSizeWidth;
            String viWindowSizeHeight = versionConfig.windowSizeHeight;
            if (isEmpty(viJavaPath)) javaPath = CMCL.javaPath;
            else {
                try {
                    javaPath = MinecraftLauncher.getRealJavaPath(viJavaPath);
                } catch (InvalidJavaException e) {
                    javaPath = CMCL.javaPath;
                    System.out.println(getString("WARNING_VCFG_JAVA_INCORRECT"));
                }
            }
            if (isEmpty(viMaxMemory)) maxMemory = config.optInt("maxMemory", (int) SystemUtils.getDefaultMemory());
            else {
                try {
                    maxMemory = Integer.parseInt(viMaxMemory);
                    if (maxMemory <= 0) {
                        maxMemory = config.optInt("maxMemory", (int) SystemUtils.getDefaultMemory());
                        System.out.println(getString("WARNING_VCFG_MAX_MEMORY_INCORRECT"));
                    }
                } catch (NumberFormatException n) {
                    maxMemory = config.optInt("maxMemory", (int) SystemUtils.getDefaultMemory());
                }
            }

            if (isEmpty(viWindowSizeWidth)) windowSizeWidth = config.optInt("windowSizeWidth", 854);
            else {
                try {
                    windowSizeWidth = Integer.parseInt(viWindowSizeWidth);
                    if (windowSizeWidth <= 0) {
                        windowSizeWidth = config.optInt("windowSizeWidth", 854);
                        System.out.println(getString("WARNING_VCFG_WINDOW_SIZE_WIDTH_INCORRECT"));
                    }
                } catch (NumberFormatException n) {
                    windowSizeWidth = config.optInt("windowSizeWidth", 854);
                }
            }
            if (isEmpty(viWindowSizeHeight)) windowSizeHeight = config.optInt("windowSizeHeight", 480);
            else {
                try {
                    windowSizeHeight = Integer.parseInt(viWindowSizeHeight);
                    if (windowSizeHeight <= 0) {
                        windowSizeHeight = config.optInt("windowSizeHeight", 480);
                        System.out.println(getString("WARNING_VCFG_WINDOW_SIZE_HEIGHT_INCORRECT"));
                    }
                } catch (NumberFormatException n) {
                    windowSizeHeight = config.optInt("windowSizeHeight", 480);
                }
            }


            boolean isFullscreen = !Utils.isEmpty(versionConfig.isFullscreen) ? Boolean.parseBoolean(versionConfig.isFullscreen) : config.optBoolean("isFullscreen");
            List<String> jvmArgs = versionConfig.jvmArgs.size() > 0 ? versionConfig.jvmArgs : ArgumentsUtils.parseJVMArgs(config.optJSONArray("jvmArgs"));
            Map<String, String> gameArgs = versionConfig.gameArgs.size() > 0 ? versionConfig.gameArgs : ArgumentsUtils.parseGameArgs(config.optJSONObject("gameArgs"));
            File assetsDir = !Utils.isEmpty(versionConfig.assetsDir) ? new File(versionConfig.assetsDir) : CMCL.assetsDir;
            File resourcePackDir = !Utils.isEmpty(versionConfig.resourcesDir) ? new File(versionConfig.resourcesDir) : resourcePacksDir;
            boolean exitWithMinecraft = !Utils.isEmpty(versionConfig.exitWithMinecraft) ? Boolean.parseBoolean(versionConfig.exitWithMinecraft) : config.optBoolean("exitWithMinecraft");
            boolean printStartupInfo = !Utils.isEmpty(versionConfig.printStartupInfo) ? Boolean.parseBoolean(versionConfig.printStartupInfo) : config.optBoolean("printStartupInfo");
            boolean checkAccountBeforeStart = !Utils.isEmpty(versionConfig.checkAccountBeforeStart) ? Boolean.parseBoolean(versionConfig.checkAccountBeforeStart) : config.optBoolean("checkAccountBeforeStart");


            if (isEmpty(javaPath) || !new File(javaPath).exists()) {
                System.out.println(getString("CONSOLE_INCORRECT_JAVA"));
                return;
            }

            if (printStartupInfo) {
                StringBuilder sb = new StringBuilder();
                sb.append(getString("MESSAGE_STARTUP_INFO_MAIN")
                        .replace("${VERSION_NAME}", version)
                        .replace("${REAL_VERSION_NAME}", getVersion(versionJsonFile, versionJarFile))
                        .replace("${PLAYER_NAME}", account.optString("playerName", "XPlayer"))
                        .replace("${ACCOUNT_TYPE}", AccountFunction.getAccountType(account))
                        .replace("${JAVA_PATH}", javaPath)
                        .replace("${EXIT_WITH_MC}", String.valueOf(exitWithMinecraft))
                        .replace("${FULLSCREEN}", String.valueOf(isFullscreen))
                        .replace("${MAX_MEMORY}", maxMemory + "MB")
                        .replace("${WIDTH}", String.valueOf(windowSizeWidth))
                        .replace("${HEIGHT}", String.valueOf(windowSizeHeight))
                        .replace("${CHECK_ACCOUNT_BEFORE_START}", String.valueOf(checkAccountBeforeStart))
                        .replace("${GAME_DIR}", workingDirectory.getAbsolutePath())).append('\n');
                if (!assetsDir.equals(new File(workingDirectory, "assets"))) {
                    sb.append(getString("MESSAGE_STARTUP_INFO_ASSETS_DIR").replace("${ASSETS_DIR}", assetsDir.getAbsolutePath())).append('\n');
                }
                if (!resourcePackDir.equals(new File(workingDirectory, "resourcepacks"))) {
                    sb.append(getString("MESSAGE_STARTUP_INFO_RESOURCE_PACKS_DIR").replace("${RESOURCE_PACKS_DIR}", resourcePackDir.getAbsolutePath())).append('\n');
                }
                if (jvmArgs.size() > 0 || gameArgs.size() > 0) {
                    sb.append(getString("MESSAGE_STARTUP_INFO_ARGS")
                            .replace("${JVM_ARGS}", new JSONArray(jvmArgs).toString(2))
                            .replace("${GAME_ARGS}", new JSONObject(gameArgs).toString(2)));
                }
                System.out.println(sb);
            }

            if (checkAccountBeforeStart) {
                if (!checkAccount(account, config)) return;
            }

            runningMc = new RunningMinecraft(
                    launchMinecraft(versionFolder,
                            versionJarFile,
                            versionJsonFile,
                            workingDirectory,
                            assetsDir,
                            resourcePackDir,
                            account.optString("playerName", "XPlayer"),
                            javaPath,
                            maxMemory,
                            128,
                            windowSizeWidth,
                            windowSizeHeight,
                            isFullscreen,
                            accessToken,
                            uuid,
                            false,
                            !isFullscreen,
                            account.optJSONObject("properties"),
                            jvmArgs,
                            gameArgs,
                            AuthlibInjectorInformation.valuesOf(account, accessToken, uuid, true),
                            account.optInt("loginMethod") == 3 ? Nide8AuthInformation.valueOf(account) : null),
                    exitWithMinecraft);

            final GameCrashError[] crashError = {null};
            new Thread(() -> {
                BufferedReader dis = new BufferedReader(new InputStreamReader(runningMc.process.getInputStream()));
                String line;
                try {
                    while ((line = dis.readLine()) != null) {
                        System.out.println(line);//legal
                        if (line.contains("cannot be cast to class java.net.URLClassLoader"))
                            crashError[0] = GameCrashError.URLClassLoader;//旧版本Minecraft的Java版本过高问题，报Exception in thread "main" java.lang.ClassCastException: class jdk.internal.loader.ClassLoaders$AppClassLoader cannot be cast to class java.net.URLClassLoader，因为在Java9对相关代码进行了修改，所以要用Java8及更旧
                        else if (line.contains("Failed to load a library. Possible solutions:"))
                            crashError[0] = GameCrashError.LWJGLFailedLoad;
                        else if (line.contains("java.lang.OutOfMemoryError:") || line.contains("Too small maximum heap"))
                            crashError[0] = GameCrashError.MemoryTooSmall;
                        else if (line.contains("Unrecognized option: "))
                            crashError[0] = GameCrashError.JvmUnrecognizedOption;

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {*/
            try {
                runningMc.process.waitFor();
                System.out.println(getString("MESSAGE_FINISHED_GAME"));
                if (crashError[0] != null)
                    System.out.println(getString("MESSAGE_GAME_CRASH_CAUSE_TIPS", crashError[0].cause));
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        } catch (EmptyNativesException ex) {
            System.out.println(getString("EXCEPTION_NATIVE_LIBRARIES_NOT_FOUND"));
        } catch (LibraryDefectException ex) {
            VersionFunction.executeNotFound(ex.list);
        } catch (LaunchException ex) {
            System.out.println(getString("CONSOLE_FAILED_START") + ": " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(getString("CONSOLE_FAILED_START") + ": " + ex);
        }
    }

    public static boolean checkAccount(JSONObject account, JSONObject config) {
        try {
            if (AccountRefresher.execute(account, config.optJSONArray("accounts"))) {
                Utils.saveConfig(config);
            }
            return true;
        } catch (DescriptionException e) {
            e.print();
            System.out.println(getString("MESSAGE_TELL_USER_CHECK_ACCOUNT_CAN_BE_OFF"));
            return false;
        }
    }

    private static String getVersion(File versionJsonFile, File versionJarFile) {
        try {
            return Utils.valueOf(VersionUtils.getGameVersion(new JSONObject(FileUtils.readFileContent(versionJsonFile)), versionJarFile).id);
        } catch (Exception e) {
            return "null";
        }
    }
}
