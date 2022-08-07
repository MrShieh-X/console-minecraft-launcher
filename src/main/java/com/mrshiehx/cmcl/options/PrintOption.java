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

import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.bean.arguments.ValueArgument;
import com.mrshiehx.cmcl.exceptions.EmptyNativesException;
import com.mrshiehx.cmcl.exceptions.LaunchException;
import com.mrshiehx.cmcl.exceptions.LibraryDefectException;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONObject;

import java.io.File;

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


        if (!configFile.exists() || isEmpty(configContent) || isEmpty(javaPath) || !new File(javaPath).exists()) {
            System.out.println(getString("CONSOLE_INCORRECT_JAVA"));
        } else {
            JSONObject config = Utils.getConfig();
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

                command = getMinecraftLaunchCommand(versionFolder, versionJarFile,
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
                        StartOption.getAuthlibInformation(account, at, uu, false),
                        StartOption.getVersionInfo(versionFolder));
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

        }
    }

    @Override
    public String getUsageName() {
        return null;
    }
}
