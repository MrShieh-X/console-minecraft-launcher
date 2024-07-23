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
 */
package com.mrshiehx.cmcl.functions;

import com.mrshiehx.cmcl.bean.arguments.*;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.cmcl.version.VersionUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.versionsDir;

public class GameArgsFunction implements Function {
    @Override
    public void execute(Arguments arguments) {
        //因为参数内容可能会被误判，所以不检测
        /*if(!Function.checkArgs(arguments,2,1,
                ArgumentRequirement.ofSingle("p"),
                ArgumentRequirement.ofSingle("print"),
                ArgumentRequirement.ofValue("p"),
                ArgumentRequirement.ofValue("print"),
                ArgumentRequirement.ofValue("a"),
                ArgumentRequirement.ofValue("add"),
                ArgumentRequirement.ofValue("d"),
                ArgumentRequirement.ofValue("delete"),
                ArgumentRequirement.ofValue("v"),
                ArgumentRequirement.ofValue("version")))return;*/
        String version = arguments.opt("v", arguments.opt("version", null));
        JSONObject versionConfigJO = null;
        File versionConfigFile = null;
        if (!Utils.isEmpty(version)) {
            if (!VersionUtils.versionExists(version)) {
                System.out.println(getString("EXCEPTION_VERSION_NOT_FOUND", version));
                return;
            }
            versionConfigFile = new File(versionsDir, version + "/cmclversion.json");
            if (!versionConfigFile.exists()) {
                try {
                    FileUtils.createFile(versionConfigFile, false);
                    versionConfigJO = new JSONObject();
                } catch (IOException e) {
                    System.out.println(getString("EXCEPTION_CREATE_FILE_WITH_PATH", e));
                    return;
                }
            } else {
                try {
                    versionConfigJO = new JSONObject(FileUtils.readFileContent(versionConfigFile));
                } catch (Exception e) {
                    versionConfigJO = new JSONObject();
                }
            }
        }

        JSONObject jsonObject = versionConfigJO != null ? versionConfigJO : Utils.getConfig();
        JSONObject gameArgs = jsonObject.optJSONObject("gameArgs");
        if (gameArgs == null) jsonObject.put("gameArgs", gameArgs = new JSONObject());

        Argument firstArg = arguments.optArgument(1);
        if (firstArg instanceof SingleArgument) {
            String firstKey = firstArg.key;
            if (firstKey.equals("p") || firstKey.equals("print")) {
                System.out.println(gameArgs.toString(2));
            } else {
                System.out.println(getString("CONSOLE_UNKNOWN_COMMAND_OR_MEANING", firstArg.originString));
                return;
            }
        } else if (firstArg instanceof ValueArgument) {
            String firstKey = firstArg.key;
            String value = ((ValueArgument) firstArg).value;
            switch (firstKey) {
                case "p":
                case "print":
                    int indentFactor;
                    try {
                        indentFactor = Utils.parseWithPrompting(value);
                    } catch (Exception e) {
                        return;
                    }
                    System.out.println(gameArgs.toString(indentFactor));
                    break;
                case "a":
                case "add":
                    String valueValue = "";
                    Argument valueArg = arguments.optArgument(2);
                    if (valueArg instanceof TextArgument) {
                        valueValue = valueArg.originString;
                    }
                    gameArgs.put(value, valueValue);

                    if (versionConfigFile != null) {
                        try {
                            FileUtils.writeFile(versionConfigFile, jsonObject.toString(), false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Utils.saveConfig(jsonObject);
                    }
                    break;
                case "d":
                case "delete":
                    gameArgs.remove(value);
                    if (versionConfigFile != null) {
                        try {
                            FileUtils.writeFile(versionConfigFile, jsonObject.toString(), false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Utils.saveConfig(jsonObject);
                    }
                    break;
                default:
                    System.out.println(getString("CONSOLE_UNKNOWN_COMMAND_OR_MEANING", firstArg.originString));
                    break;
            }

        } else {
            System.out.println(getString("CONSOLE_UNKNOWN_COMMAND_OR_MEANING", firstArg.originString));
        }
    }

    @Override
    public String getUsageName() {
        return "gameArgs";
    }
}
