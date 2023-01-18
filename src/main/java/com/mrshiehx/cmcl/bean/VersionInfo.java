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

package com.mrshiehx.cmcl.bean;

import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.bean.arguments.ValueArgument;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.cmcl.ArgumentsUtils;
import com.mrshiehx.cmcl.utils.console.CommandUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.*;

public class VersionInfo {
    public static final String SIGN_WORKING_DIRECTORY_IN_VERSION_DIR = "&*/  \n\t\r\t\f\b\b\n \\$%";
    public static final VersionInfo EMPTY = new VersionInfo(null, null, null, null, null, "", Collections.EMPTY_LIST, Collections.EMPTY_MAP, null, null, "", "", "");
    public final String gameDir;
    public final String javaPath;
    public final String maxMemory;//同下
    public final String windowSizeWidth;//同下
    public final String windowSizeHeight;//同下
    public final String isFullscreen;//不用boolean是因为要表示一种“未设置”的状态
    @NotNull
    public final List<String> jvmArgs;
    @NotNull
    public final Map<String, String> gameArgs;
    public final String assetsDir;
    public final String resourcesDir;
    public final String exitWithMinecraft;//不用boolean是因为要表示一种“未设置”的状态
    public final String printStartupInfo;//不用boolean是因为要表示一种“未设置”的状态
    public final String checkAccountBeforeStart;//不用boolean是因为要表示一种“未设置”的状态

    public VersionInfo(String gameDir,
                       String javaPath,
                       String maxMemory,
                       String windowSizeWidth,
                       String windowSizeHeight,
                       String isFullscreen,
                       @NotNull List<String> jvmArgs,
                       @NotNull Map<String, String> gameArgs,
                       String assetsDir,
                       String resourcesDir,
                       String exitWithMinecraft,
                       String printStartupInfo,
                       String checkAccountBeforeStart) {
        this.gameDir = gameDir;
        this.javaPath = javaPath;
        this.maxMemory = maxMemory;
        this.windowSizeWidth = windowSizeWidth;
        this.windowSizeHeight = windowSizeHeight;
        this.isFullscreen = isFullscreen;
        this.jvmArgs = jvmArgs;
        this.gameArgs = gameArgs;
        this.assetsDir = assetsDir;
        this.resourcesDir = resourcesDir;
        this.exitWithMinecraft = exitWithMinecraft;
        this.printStartupInfo = printStartupInfo;
        this.checkAccountBeforeStart = checkAccountBeforeStart;
    }

    public static VersionInfo valueOf(JSONObject origin) {
        return new VersionInfo(origin.optString("gameDir"),
                origin.optString("javaPath"),
                origin.optString("maxMemory"),
                origin.optString("windowSizeWidth"),
                origin.optString("windowSizeHeight"),
                origin.optString("isFullscreen"),
                ArgumentsUtils.parseJVMArgs(origin.optJSONArray("jvmArgs")),
                ArgumentsUtils.parseGameArgs(origin.optJSONObject("gameArgs")),
                origin.optString("assetsDir"),
                origin.optString("resourcesDir"),
                origin.optString("exitWithMinecraft"),
                origin.optString("printStartupInfo"),
                origin.optString("checkAccountBeforeStart"));
    }

    public static VersionInfo valueOfHMCL(JSONObject origin) {
        //workingDirectory
        String workingDirectory = null;
        {
            int gameDirType = origin.optInt("gameDirType");
            if (gameDirType == 1) {
                workingDirectory = SIGN_WORKING_DIRECTORY_IN_VERSION_DIR;
            } else if (gameDirType == 2) {
                workingDirectory = origin.optString("gameDir");
            }
        }
        Map<String, String> gameArgs = new LinkedHashMap<>();
        String gameArgsString = origin.optString("minecraftArgs");
        if (!Utils.isEmpty(gameArgsString)) {
            Arguments arguments = new Arguments(gameArgsString, false);
            for (Argument argument : arguments.getArguments()) {
                String key = argument.key;
                if (Objects.equals(key, "version") || Objects.equals(key, "versionType")) continue;
                if (argument instanceof ValueArgument) {
                    gameArgs.put(key, ((ValueArgument) argument).value);
                } else {
                    gameArgs.put(key, null);
                }
            }
        }
        return new VersionInfo(workingDirectory,
                origin.optString("defaultJavaPath"),
                origin.optString("maxMemory"),
                origin.optString("width"),
                origin.optString("height"),
                origin.optString("fullscreen"),
                ArgumentsUtils.parseJVMArgs(CommandUtils.splitCommand(CommandUtils.clearRedundantSpaces(origin.optString("javaArgs"))).toArray(new String[0])),
                gameArgs,
                null,
                null,
                "",
                "",
                "");
    }

    public VersionInfo merge(VersionInfo versionInfo) {
        Map<String, String> newGameArgs = new LinkedHashMap<>();
        newGameArgs.putAll(versionInfo.gameArgs);
        newGameArgs.putAll(gameArgs);
        List<String> newJvmArgs = new LinkedList<>();
        newJvmArgs.addAll(versionInfo.jvmArgs);
        newJvmArgs.addAll(jvmArgs);
        return new VersionInfo(
                !Utils.isEmpty(versionInfo.gameDir) ? versionInfo.gameDir : gameDir,
                !Utils.isEmpty(versionInfo.javaPath) ? versionInfo.javaPath : javaPath,
                !Utils.isEmpty(versionInfo.maxMemory) ? versionInfo.maxMemory : maxMemory,
                !Utils.isEmpty(versionInfo.windowSizeWidth) ? versionInfo.windowSizeWidth : windowSizeWidth,
                !Utils.isEmpty(versionInfo.windowSizeHeight) ? versionInfo.windowSizeHeight : windowSizeHeight,
                !Utils.isEmpty(versionInfo.isFullscreen) ? versionInfo.isFullscreen : isFullscreen,
                newJvmArgs,
                newGameArgs,
                !Utils.isEmpty(versionInfo.assetsDir) ? versionInfo.assetsDir : assetsDir,
                !Utils.isEmpty(versionInfo.resourcesDir) ? versionInfo.resourcesDir : resourcesDir,
                !Utils.isEmpty(versionInfo.exitWithMinecraft) ? versionInfo.exitWithMinecraft : exitWithMinecraft,
                !Utils.isEmpty(versionInfo.printStartupInfo) ? versionInfo.printStartupInfo : printStartupInfo,
                !Utils.isEmpty(versionInfo.checkAccountBeforeStart) ? versionInfo.checkAccountBeforeStart : checkAccountBeforeStart);
    }
}
