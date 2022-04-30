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

import com.mrshiehx.cmcl.bean.AuthlibInformation;
import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.bean.arguments.ValueArgument;
import com.mrshiehx.cmcl.enums.GameCrashError;
import com.mrshiehx.cmcl.exceptions.EmptyNativesException;
import com.mrshiehx.cmcl.exceptions.LaunchException;
import com.mrshiehx.cmcl.exceptions.LibraryDefectException;
import com.mrshiehx.cmcl.exceptions.NotSelectedException;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.*;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;
import static com.mrshiehx.cmcl.modules.MinecraftLauncher.launchMinecraft;

public class StartOption implements Option {
    @Override
    public void execute(Arguments arguments) {
        Argument argument = arguments.optArgument(0);
        String version;
        JSONObject jsonObject = Utils.getConfig();
        if ((argument instanceof ValueArgument)) {
            ValueArgument valueArgument = (ValueArgument) argument;
            version = valueArgument.value;
        } else {
            version = jsonObject.optString("selectedVersion");
            if (isEmpty(version)) {
                System.out.println(getString("CONSOLE_NO_SELECTED_VERSION"));
                return;
            }
        }


        start(version, jsonObject);
    }

    public static void start(String version, JSONObject config) {
        if (!configFile.exists() || isEmpty(configContent) || isEmpty(javaPath) || !new File(javaPath).exists()) {
            System.out.println(getString("CONSOLE_INCORRECT_JAVA"));
        } else {
            File versionsFolder = new File(gameDir, "versions");
            File versionFolder = new File(versionsFolder, version);
            File versionJarFile = new File(versionFolder, version + ".jar");
            File versionJsonFile = new File(versionFolder, version + ".json");
            try {

                JSONObject account;
                try {
                    account = Utils.getSelectedAccount();
                } catch (NotSelectedException e) {
                    return;
                }

                String at = Utils.randomUUIDNoSymbol(), uu = Utils.getUUIDByName(account.optString("playerName", "XPlayer"));
                if (account.optInt("loginMethod") > 0) {
                    at = account.optString("accessToken", at);
                    uu = account.optString("uuid", uu);
                }

                runningMc = launchMinecraft(versionFolder,
                        versionJarFile,
                        versionJsonFile,
                        gameDir,
                        assetsDir,
                        respackDir,
                        account.optString("playerName", "XPlayer"),
                        config.optString("javaPath", Utils.getDefaultJavaPath()),
                        config.optLong("maxMemory", Utils.getDefaultMemory()),
                        128,
                        config.optInt("windowSizeWidth", 854),
                        config.optInt("windowSizeHeight", 480),
                        config.optBoolean("isFullscreen"),
                        at,
                        uu,
                        false,
                        !config.optBoolean("isFullscreen"),
                        account.optJSONObject("properties"),
                        Utils.parseJVMArgs(configContent.optJSONArray("jvmArgs")),
                        Utils.parseGameArgs(configContent.optJSONObject("gameArgs")),
                        StartOption.getAuthlibInformation(account, at, uu, true));

                final GameCrashError[] crashError = {null};
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BufferedReader dis = new BufferedReader(new InputStreamReader(runningMc.getInputStream()));
                        String line;
                        try {
                            while ((line = dis.readLine()) != null) {
                                System.out.println(line);
                                if (line.contains("cannot be cast to class java.net.URLClassLoader"))
                                    crashError[0] = GameCrashError.URLClassLoader;//旧版本Minecraft的Java版本过高问题，报Exception in thread "main" java.lang.ClassCastException: class jdk.internal.loader.ClassLoaders$AppClassLoader cannot be cast to class java.net.URLClassLoader，因为在Java9对相关代码进行了修改，所以要用Java8及更旧
                                else if (line.contains("Failed to load a library. Possible solutions:"))
                                    crashError[0] = GameCrashError.LWJGLFailedLoad;
                                else if (line.contains("java.lang.OutOfMemoryError:") || line.contains("Too small maximum heap"))
                                    crashError[0] = GameCrashError.MemoryTooSmall;

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {*/
                try {
                    runningMc.waitFor();
                    System.out.println(getString("MESSAGE_FINISHED_GAME"));
                    if (crashError[0] != null)
                        System.out.println(getString("MESSAGE_GAME_CRASH_CAUSE_TIPS", crashError[0].cause));
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                    /*}
                }).start();*/
            } catch (EmptyNativesException ex) {
                System.out.println(getString("EXCEPTION_NATIVE_LIBRARIES_NOT_FOUND"));
            } catch (LibraryDefectException ex) {
                VersionOption.executeNotFound(ex.list);
            } catch (LaunchException ex) {
                //ex.printStackTrace();
                System.out.println(getString("CONSOLE_FAILED_START") + ": " + ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println(getString("CONSOLE_FAILED_START") + ": " + ex);
            }
        }
    }

    @Override
    public String getUsageName() {
        return null;
    }

    public static AuthlibInformation getAuthlibInformation(JSONObject account, String token, String uuid, boolean allowOfflineSkin) {
        return AuthlibInformation.valuesOf(account, token, uuid, allowOfflineSkin);
    }
}
