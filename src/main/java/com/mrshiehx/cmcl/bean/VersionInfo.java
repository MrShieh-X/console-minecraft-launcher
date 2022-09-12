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
 *
 */

package com.mrshiehx.cmcl.bean;

import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.bean.arguments.ValueArgument;
import com.mrshiehx.cmcl.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.*;

public class VersionInfo {
    public static final String SIGN_WORKING_DIRECTORY_IN_VERSION_DIR = "&*/\\$%";
    public static final VersionInfo EMPTY = new VersionInfo(null, null, 0, 0, 0, "", Collections.EMPTY_LIST, Collections.EMPTY_MAP, null, null, "");
    public final String workingDirectory;
    public final String javaPath;
    public final int maxMemory;
    public final int windowSizeWidth;
    public final int windowSizeHeight;
    public final String isFullscreen;//不用boolean是因为要表示一种“未设置”的状态
    @NotNull
    public final List<String> jvmArgs;
    @NotNull
    public final Map<String, String> gameArgs;
    public final String assetsDir;
    public final String resourcesDir;
    public final String exitWithMinecraft;//不用boolean是因为要表示一种“未设置”的状态

    public VersionInfo(String workingDirectory,
                       String javaPath,
                       int maxMemory,
                       int windowSizeWidth,
                       int windowSizeHeight,
                       String isFullscreen,
                       @NotNull List<String> jvmArgs,
                       @NotNull Map<String, String> gameArgs,
                       String assetsDir,
                       String resourcesDir,
                       String exitWithMinecraft) {
        this.workingDirectory = workingDirectory;
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
    }

    public static VersionInfo valueOf(JSONObject origin) {
        return new VersionInfo(origin.optString("gameDir"),
                origin.optString("javaPath"),
                origin.optInt("maxMemory"),
                origin.optInt("windowSizeWidth"),
                origin.optInt("windowSizeHeight"),
                origin.optString("isFullscreen"),
                Utils.parseJVMArgs(origin.optJSONArray("jvmArgs")),
                Utils.parseGameArgs(origin.optJSONObject("gameArgs")),
                origin.optString("assetsDir"),
                origin.optString("resourcesDir"),
                origin.optString("exitWithMinecraft"));
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
            Arguments arguments = new Arguments(gameArgsString, false, false);
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
                origin.optInt("maxMemory"),
                origin.optInt("width"),
                origin.optInt("height"),
                origin.optString("fullscreen"),
                Utils.parseJVMArgs(Utils.splitCommand(Utils.clearRedundantSpaces(origin.optString("javaArgs"))).toArray(new String[0])),
                gameArgs,
                null,
                null,
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
                !Utils.isEmpty(versionInfo.workingDirectory) ? versionInfo.workingDirectory : workingDirectory,
                !Utils.isEmpty(versionInfo.javaPath) ? versionInfo.javaPath : javaPath,
                versionInfo.maxMemory > 0 ? versionInfo.maxMemory : maxMemory,
                versionInfo.windowSizeWidth > 0 ? versionInfo.windowSizeWidth : windowSizeWidth,
                versionInfo.windowSizeHeight > 0 ? versionInfo.windowSizeHeight : windowSizeHeight,
                !Utils.isEmpty(versionInfo.isFullscreen) ? versionInfo.isFullscreen : isFullscreen,
                newJvmArgs,
                newGameArgs,
                !Utils.isEmpty(versionInfo.assetsDir) ? versionInfo.assetsDir : assetsDir,
                !Utils.isEmpty(versionInfo.resourcesDir) ? versionInfo.resourcesDir : resourcesDir,
                !Utils.isEmpty(versionInfo.exitWithMinecraft) ? versionInfo.exitWithMinecraft : exitWithMinecraft);
    }
}
