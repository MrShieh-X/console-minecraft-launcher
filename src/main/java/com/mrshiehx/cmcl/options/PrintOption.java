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
package com.mrshiehx.cmcl.options;

import com.mrshiehx.cmcl.ConsoleMinecraftLauncher;
import com.mrshiehx.cmcl.bean.VersionInfo;
import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.bean.arguments.ValueArgument;
import com.mrshiehx.cmcl.exceptions.EmptyNativesException;
import com.mrshiehx.cmcl.exceptions.InvalidJavaException;
import com.mrshiehx.cmcl.exceptions.LaunchException;
import com.mrshiehx.cmcl.exceptions.LibraryDefectException;
import com.mrshiehx.cmcl.modules.MinecraftLauncher;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.*;
import static com.mrshiehx.cmcl.modules.MinecraftLauncher.getMinecraftLaunchCommand;

public class PrintOption implements Option {
    @Override
    public void execute(Arguments arguments) {
        Argument argument = arguments.optArgument(0);
        if (!(argument instanceof ValueArgument)) {
            System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
            return;
        }
        ValueArgument valueArgument = (ValueArgument) argument;
        String version = valueArgument.value;

        JSONObject config = Utils.getConfig();
        /*String javaPath = config.optString("javaPath", Utils.getDefaultJavaPath());

        if (isEmpty(javaPath) || !new File(javaPath).exists()) {
            System.out.println(getString("CONSOLE_INCORRECT_JAVA"));
        } else {*/
        File versionsFolder = new File(gameDir, "versions");
        File versionFolder = new File(versionsFolder, version);
        File versionJarFile = new File(versionFolder, version + ".jar");
        File versionJsonFile = new File(versionFolder, version + ".json");
        String command;
        try {
            JSONObject account = Utils.getSelectedAccountIfNotLoginNow(config);

            if (account == null) return;

            if (!Utils.isEmpty(account.optString("offlineSkin")) || !Utils.isEmpty(account.optString("providedSkin"))) {
                System.out.println(getString("PRINT_COMMAND_NOT_SUPPORT_OFFLINE_CUSTOM_SKIN"));
            }

            String at = Utils.randomUUIDNoSymbol(), uu = Utils.getUUIDByName(account.optString("playerName", "XPlayer"));
            if (account.optInt("loginMethod") > 0) {
                at = account.optString("accessToken", at);
                uu = account.optString("uuid", uu);
            }


            VersionInfo versionInfo = StartOption.getVersionInfo(versionFolder);
            File workingDirectory = !Utils.isEmpty(versionInfo.workingDirectory) ?
                    ((Objects.equals(versionInfo.workingDirectory, VersionInfo.SIGN_WORKING_DIRECTORY_IN_VERSION_DIR))
                            ? versionFolder
                            : (MinecraftLauncher.isModpack(versionFolder) ? versionFolder : new File(versionInfo.workingDirectory)))
                    : (MinecraftLauncher.isModpack(versionFolder) ? versionFolder : gameDir);


            String javaPath;
            int maxMemory;
            int windowSizeWidth;
            int windowSizeHeight;

            String viJavaPath = versionInfo.javaPath;
            String viMaxMemory = versionInfo.maxMemory;
            String viWindowSizeWidth = versionInfo.windowSizeWidth;
            String viWindowSizeHeight = versionInfo.windowSizeHeight;
            if (isEmpty(viJavaPath)) javaPath = ConsoleMinecraftLauncher.javaPath;
            else {
                try {
                    javaPath = MinecraftLauncher.getRealJavaPath(viJavaPath);
                } catch (InvalidJavaException e) {
                    javaPath = ConsoleMinecraftLauncher.javaPath;
                    System.out.println(getString("WARNING_VCFG_JAVA_INCORRECT"));
                }
            }
            if (isEmpty(viMaxMemory)) maxMemory = config.optInt("maxMemory", (int) Utils.getDefaultMemory());
            else {
                try {
                    maxMemory = Integer.parseInt(viMaxMemory);
                    if (maxMemory <= 0) {
                        maxMemory = config.optInt("maxMemory", (int) Utils.getDefaultMemory());
                        System.out.println(getString("WARNING_VCFG_MAX_MEMORY_INCORRECT"));
                    }
                } catch (NumberFormatException n) {
                    maxMemory = config.optInt("maxMemory", (int) Utils.getDefaultMemory());
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


            boolean isFullscreen = !Utils.isEmpty(versionInfo.isFullscreen) ? Boolean.parseBoolean(versionInfo.isFullscreen) : config.optBoolean("isFullscreen");
            List<String> jvmArgs = Utils.parseJVMArgs(config.optJSONArray("jvmArgs"));
            jvmArgs.addAll(versionInfo.jvmArgs);
            Map<String, String> gameArgs = Utils.parseGameArgs(config.optJSONObject("gameArgs"));
            gameArgs.putAll(versionInfo.gameArgs);
            File assetsDir = !Utils.isEmpty(versionInfo.assetsDir) ? new File(versionInfo.assetsDir) : ConsoleMinecraftLauncher.assetsDir;
            File resourcesDir = !Utils.isEmpty(versionInfo.resourcesDir) ? new File(versionInfo.resourcesDir) : respackDir;
            //boolean exitWithMinecraft = !Utils.isEmpty(versionInfo.exitWithMinecraft) ? Boolean.parseBoolean(versionInfo.exitWithMinecraft) : config.optBoolean("exitWithMinecraft");
            if (isEmpty(javaPath) || !new File(javaPath).exists()) {
                System.out.println(getString("CONSOLE_INCORRECT_JAVA"));
                return;
            }
            //ConsoleMinecraftLauncher.exitWithMinecraft = exitWithMinecraft;

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
                    at,
                    uu,
                    false,
                    !isFullscreen,
                    account.optJSONObject("properties"),
                    jvmArgs,
                    gameArgs,
                    StartOption.getAuthlibInformation(account, at, uu, false));
        } catch (EmptyNativesException ex) {
            System.out.println(getString("EXCEPTION_NATIVE_LIBRARIES_NOT_FOUND"));
            return;
        } catch (LibraryDefectException ex) {
            VersionOption.executeNotFound(ex.list);
            return;
        } catch (LaunchException ex) {
            //ex.printStackTrace();
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
        System.out.println("cd " + (Utils.isWindows() ? "/D " : "") + (path.contains(" ") ? ("\"" + path + "\"") : path));
        System.out.println(command);//legal
        System.out.println("===================================================================================================================");

        //}
    }

    @Override
    public String getUsageName() {
        return null;
    }
}
