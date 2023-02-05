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
import com.mrshiehx.cmcl.bean.VersionConfig;
import com.mrshiehx.cmcl.exceptions.EmptyNativesException;
import com.mrshiehx.cmcl.exceptions.InvalidJavaException;
import com.mrshiehx.cmcl.exceptions.LaunchException;
import com.mrshiehx.cmcl.exceptions.LibraryDefectException;
import com.mrshiehx.cmcl.functions.VersionFunction;
import com.mrshiehx.cmcl.modules.MinecraftLauncher;
import com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.authlib.AuthlibInjectorInformation;
import com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.nide8auth.Nide8AuthInformation;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.cmcl.AccountUtils;
import com.mrshiehx.cmcl.utils.cmcl.ArgumentsUtils;
import com.mrshiehx.cmcl.utils.console.ConsoleUtils;
import com.mrshiehx.cmcl.utils.system.SystemUtils;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

import static com.mrshiehx.cmcl.CMCL.*;
import static com.mrshiehx.cmcl.modules.MinecraftLauncher.getMinecraftLaunchCommand;

public class CommandPrinter {
    public static void execute(String version) {
        JSONObject config = Utils.getConfig();

        if (isEmpty(version)) {
            version = config.optString("selectedVersion");
            if (isEmpty(version)) {
                System.out.println(getString("MESSAGE_TO_SELECT_VERSION"));
                return;
            }
        }
        if (!config.has("checkAccountBeforeStart")) {
            config.put("checkAccountBeforeStart", ConsoleUtils.yesOrNo(getString("CONSOLE_ASK_CHECK_ACCOUNT")));
        }
        Utils.saveConfig(config);
        File versionsFolder = new File(gameDir, "versions");
        File versionFolder = new File(versionsFolder, version);
        File versionJarFile = new File(versionFolder, version + ".jar");
        File versionJsonFile = new File(versionFolder, version + ".json");
        String command;
        try {
            JSONObject account = AccountUtils.getSelectedAccountIfNotLoginNow(config);

            if (account == null) return;

            if (!Utils.isEmpty(account.optString("offlineSkin")) || !Utils.isEmpty(account.optString("providedSkin"))) {
                System.out.println(getString("PRINT_COMMAND_NOT_SUPPORT_OFFLINE_CUSTOM_SKIN"));
            }

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


            boolean checkAccountBeforeStart = !Utils.isEmpty(versionConfig.checkAccountBeforeStart) ? Boolean.parseBoolean(versionConfig.checkAccountBeforeStart) : config.optBoolean("checkAccountBeforeStart");
            boolean isFullscreen = !Utils.isEmpty(versionConfig.isFullscreen) ? Boolean.parseBoolean(versionConfig.isFullscreen) : config.optBoolean("isFullscreen");
            List<String> jvmArgs = ArgumentsUtils.parseJVMArgs(config.optJSONArray("jvmArgs"));
            jvmArgs.addAll(versionConfig.jvmArgs);
            Map<String, String> gameArgs = ArgumentsUtils.parseGameArgs(config.optJSONObject("gameArgs"));
            gameArgs.putAll(versionConfig.gameArgs);
            File assetsDir = !Utils.isEmpty(versionConfig.assetsDir) ? new File(versionConfig.assetsDir) : CMCL.assetsDir;
            File resourcesDir = !Utils.isEmpty(versionConfig.resourcesDir) ? new File(versionConfig.resourcesDir) : resourcePacksDir;
            if (isEmpty(javaPath) || !new File(javaPath).exists()) {
                System.out.println(getString("CONSOLE_INCORRECT_JAVA"));
                return;
            }

            if (checkAccountBeforeStart) {
                if (!VersionStarter.checkAccount(account, config)) return;
            }

            command = getMinecraftLaunchCommand(versionJarFile,
                    versionJsonFile,
                    workingDirectory,
                    assetsDir,
                    resourcesDir,
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
                    AuthlibInjectorInformation.valuesOf(account, accessToken, uuid, false),
                    account.optInt("loginMethod") == 3 ? Nide8AuthInformation.valueOf(account) : null);
        } catch (EmptyNativesException ex) {
            System.out.println(getString("EXCEPTION_NATIVE_LIBRARIES_NOT_FOUND"));
            return;
        } catch (LibraryDefectException ex) {
            VersionFunction.executeNotFound(ex.list);
            return;
        } catch (LaunchException ex) {
            System.out.println(getString("CONSOLE_FAILED_START") + ": " + ex.getMessage());
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(getString("CONSOLE_FAILED_START") + ": " + ex);
            return;
        }
        String path = versionFolder.getAbsolutePath();
        System.out.println(getString("CONSOLE_START_COMMAND"));
        System.out.println("===================================================================================================================");
        System.out.println("cd " + (SystemUtils.isWindows() ? "/D " : "") + (path.contains(" ") ? ("\"" + path + "\"") : path));
        System.out.println(command);//legal
        System.out.println("===================================================================================================================");

        //}
    }
}
