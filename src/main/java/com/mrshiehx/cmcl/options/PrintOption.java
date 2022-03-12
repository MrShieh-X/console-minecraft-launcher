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
            JSONObject jsonObject = Utils.getConfig();
            File versionsFolder = new File(gameDir, "versions");
            File versionFolder = new File(versionsFolder, version);
            File versionJarFile = new File(versionFolder, version + ".jar");
            File versionJsonFile = new File(versionFolder, version + ".json");
            try {
                String at = "0", uu = null;
                if (jsonObject.optInt("loginMethod") > 0) {
                    at = jsonObject.optString("accessToken", "0");
                    uu = jsonObject.optString("uuid", null);
                }
                System.out.println(getString("CONSOLE_START_COMMAND"));
                System.out.println(getMinecraftLaunchCommand(versionJarFile,
                        versionJsonFile,
                        gameDir,
                        assetsDir,
                        respackDir,
                        jsonObject.optString("playerName", "XPlayer"),
                        jsonObject.optString("javaPath", Utils.getDefaultJavaPath()),
                        jsonObject.optInt("maxMemory", 1024),
                        128,
                        jsonObject.optInt("windowSizeWidth", 854),
                        jsonObject.optInt("windowSizeHeight", 480),
                        jsonObject.optBoolean("isFullscreen"),
                        at,
                        uu,
                        false,
                        !jsonObject.optBoolean("isFullscreen")));
            } catch (EmptyNativesException ex) {
                System.out.println(getString("EXCEPTION_NATIVE_LIBRARIES_NOT_FOUND"));
            } catch (LibraryDefectException ex) {
                VersionOption.executeNotFound(ex.list);
            } catch (LaunchException ex) {
                //ex.printStackTrace();
                System.out.println(getString("CONSOLE_FAILED_START") + ": " + ex.getMessage());
            } catch (Exception ex) {
                //ex.printStackTrace();
                System.out.println(getString("CONSOLE_FAILED_START") + ": " + ex);
            }
        }
    }

    @Override
    public String getUsageName() {
        return null;
    }
}
