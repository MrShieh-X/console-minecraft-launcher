/*
 * Console Minecraft Launcher
 * Copyright (C) 2021-2024  MrShiehX
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionConfig {
    public static final VersionConfig EMPTY = new VersionConfig(null, null, null, null, null, "", Collections.EMPTY_LIST, Collections.EMPTY_MAP, null, null, "", "", "", "", null, null, null, null);
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
    public final String isolate;//同上
    public final String qpLogFile;
    public final String qpSaveName;
    public final String qpServerAddress;
    public final String qpRealmsID;

    public VersionConfig(String gameDir,
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
                         String checkAccountBeforeStart,
                         String isolate,
                         String qpLogFile,
                         String qpSaveName,
                         String qpServerAddress,
                         String qpRealmsID) {
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
        this.isolate = isolate;
        this.qpLogFile = qpLogFile;
        this.qpSaveName = qpSaveName;
        this.qpServerAddress = qpServerAddress;
        this.qpRealmsID = qpRealmsID;
    }

    public static VersionConfig valueOf(JSONObject origin) {
        return new VersionConfig(origin.optString("gameDir"),
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
                origin.optString("checkAccountBeforeStart"),
                origin.optString("isolate"),
                origin.optString("qpLogFile"),
                origin.optString("qpSaveName"),
                origin.optString("qpServerAddress"),
                origin.optString("qpRealmsID"));
    }

    public static VersionConfig valueOfPCL2(String setupIni) {
        if (Utils.isEmpty(setupIni)) return EMPTY;
        String[] lines = setupIni.split("\n");
        Map<String, String> gameArgs = new LinkedHashMap<>();
        List<String> jvmArgs = Collections.emptyList();
        String javaPath = null;
        String isolate = "";
        String qpServerAddress = null;
        for (String line : lines) {
            int indexOf = line.indexOf(":");
            if (indexOf <= 0) continue;//小于0的情况：若不存在:则返回-1
            String name = line.substring(0, indexOf);
            String value;
            Matcher matcher = Pattern.compile(name + ":\\s*(?<value>[\\s\\S]*)").matcher(line);
            if (matcher.find()) {
                value = matcher.group("value");
            } else value = "";

            if (!Utils.isEmpty(value)) {
                switch (name) {
                    case "VersionAdvanceGame":
                        Arguments arguments = new Arguments(value, false);
                        for (Argument argument : arguments.getArguments()) {
                            String key = argument.key;
                            if (Objects.equals(key, "version") || Objects.equals(key, "versionType")) continue;
                            if (argument instanceof ValueArgument) {
                                gameArgs.put(key, ((ValueArgument) argument).value);
                            } else {
                                gameArgs.put(key, null);
                            }
                        }
                        break;
                    case "VersionArgumentJavaSelect":
                        try {
                            javaPath = new JSONObject(value).optString("Path");
                        } catch (JSONException ignored) {
                        }
                        break;
                    case "VersionAdvanceJvm":
                        jvmArgs = ArgumentsUtils.parseJVMArgs(CommandUtils.splitCommand(CommandUtils.clearRedundantSpaces(value)).toArray(new String[0]));
                        break;
                    case "VersionArgumentIndie":
                        if ("1".equals(value)) {
                            isolate = "true";
                        } else if ("2".equals(value)) {
                            isolate = "false";
                        } else {
                            isolate = "";
                        }
                        break;
                    case "VersionServerEnter":
                        qpServerAddress = value;
                        break;
                }
            }
        }
        return new VersionConfig(null, javaPath, "", "", "", "", jvmArgs, gameArgs, null, null, "", "", "", isolate, null, null, qpServerAddress, null);
    }

    public static VersionConfig valueOfHMCL(JSONObject origin) {
        //workingDirectory
        String isolate = "false";
        String workingDirectory = null;
        {
            int gameDirType = origin.optInt("gameDirType");
            if (gameDirType == 1) {
                isolate = "true";
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
        return new VersionConfig(workingDirectory,
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
                "",
                isolate,
                null,
                null,
                null,/*I don't know*/
                null);
    }

    /**
     * 合并本对象与 <code>versionConfig</code> 并返回新的版本配置对象。
     * <p>若 <code>versionConfig</code> 某项为空才会把本对象的对应项作为新版本配置对象的对应项。
     **/
    public VersionConfig mergeTo(VersionConfig versionConfig) {
        Map<String, String> newGameArgs = new LinkedHashMap<>();
        newGameArgs.putAll(versionConfig.gameArgs);
        newGameArgs.putAll(gameArgs);
        List<String> newJvmArgs = new LinkedList<>();
        newJvmArgs.addAll(versionConfig.jvmArgs);
        newJvmArgs.addAll(jvmArgs);
        return new VersionConfig(
                !Utils.isEmpty(versionConfig.gameDir) ? versionConfig.gameDir : gameDir,
                !Utils.isEmpty(versionConfig.javaPath) ? versionConfig.javaPath : javaPath,
                !Utils.isEmpty(versionConfig.maxMemory) ? versionConfig.maxMemory : maxMemory,
                !Utils.isEmpty(versionConfig.windowSizeWidth) ? versionConfig.windowSizeWidth : windowSizeWidth,
                !Utils.isEmpty(versionConfig.windowSizeHeight) ? versionConfig.windowSizeHeight : windowSizeHeight,
                !Utils.isEmpty(versionConfig.isFullscreen) ? versionConfig.isFullscreen : isFullscreen,
                newJvmArgs,
                newGameArgs,
                !Utils.isEmpty(versionConfig.assetsDir) ? versionConfig.assetsDir : assetsDir,
                !Utils.isEmpty(versionConfig.resourcesDir) ? versionConfig.resourcesDir : resourcesDir,
                !Utils.isEmpty(versionConfig.exitWithMinecraft) ? versionConfig.exitWithMinecraft : exitWithMinecraft,
                !Utils.isEmpty(versionConfig.printStartupInfo) ? versionConfig.printStartupInfo : printStartupInfo,
                !Utils.isEmpty(versionConfig.checkAccountBeforeStart) ? versionConfig.checkAccountBeforeStart : checkAccountBeforeStart,
                !Utils.isEmpty(versionConfig.isolate) ? versionConfig.isolate : isolate,
                !Utils.isEmpty(versionConfig.qpLogFile) ? versionConfig.qpLogFile : qpLogFile,
                !Utils.isEmpty(versionConfig.qpSaveName) ? versionConfig.qpSaveName : qpSaveName,
                !Utils.isEmpty(versionConfig.qpServerAddress) ? versionConfig.qpServerAddress : qpServerAddress,
                !Utils.isEmpty(versionConfig.qpRealmsID) ? versionConfig.qpRealmsID : qpRealmsID);
    }
}
