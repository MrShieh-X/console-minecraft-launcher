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

import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.bean.arguments.SingleArgument;
import com.mrshiehx.cmcl.bean.arguments.ValueArgument;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.cmcl.version.VersionUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.versionsDir;

public class JVMArgsFunction implements Function {
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
        JSONObject versionConfigJSONObject = null;
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
                    versionConfigJSONObject = new JSONObject();
                } catch (IOException e) {
                    System.out.println(getString("EXCEPTION_CREATE_FILE_WITH_PATH", e));
                    return;
                }
            } else {
                try {
                    versionConfigJSONObject = new JSONObject(FileUtils.readFileContent(versionConfigFile));
                } catch (Exception e) {
                    versionConfigJSONObject = new JSONObject();
                }
            }
        }

        JSONObject jsonObject = versionConfigJSONObject != null ? versionConfigJSONObject : Utils.getConfig();
        JSONArray jvmArgs = jsonObject.optJSONArray("jvmArgs");
        if (jvmArgs == null) jsonObject.put("jvmArgs", jvmArgs = new JSONArray());

        Argument firstArg = arguments.optArgument(1);
        if (firstArg instanceof SingleArgument) {
            String firstKey = firstArg.key;
            if (firstKey.equals("p") || firstKey.equals("print")) {
                System.out.println(jvmArgs.toString(2));
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
                    System.out.println(jvmArgs.toString(indentFactor));
                    break;
                case "a":
                case "add":
                    jvmArgs.put(value);
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
                    try {
                        int order = Utils.parseWithPrompting(value);
                        jvmArgs.remove(order);
                        if (versionConfigFile != null) {
                            try {
                                FileUtils.writeFile(versionConfigFile, jsonObject.toString(), false);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Utils.saveConfig(jsonObject);
                        }
                    } catch (Exception e) {
                        return;
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
        return "jvmArgs";
    }
}
