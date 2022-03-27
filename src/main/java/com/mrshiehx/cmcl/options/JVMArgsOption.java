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
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;

public class JVMArgsOption implements Option {
    @Override
    public void execute(Arguments arguments) {
        Argument subOption = arguments.opt(1);
        if (subOption == null) {
            System.out.println(getString("CONSOLE_GET_USAGE"));
            return;
        }
        String key = subOption.key;
        JSONObject jsonObject = Utils.getConfig();
        JSONArray jvmArgs = jsonObject.optJSONArray("jvmArgs");
        if (jvmArgs == null) jsonObject.put("jvmArgs", jvmArgs = new JSONArray());
        switch (key.toLowerCase()) {
            case "p":
                int indentFactor;
                if (!(subOption instanceof ValueArgument)) {
                    indentFactor = 2;
                } else {
                    String value = ((ValueArgument) subOption).value;
                    try {
                        indentFactor = Utils.parse(value);
                    } catch (Exception e) {
                        return;
                    }

                }
                System.out.println(jvmArgs.toString(indentFactor));
                break;
            case "a": {
                if (!(subOption instanceof ValueArgument)) {
                    System.out.println(getString("CONSOLE_GET_USAGE"));
                    return;
                }
                String value = ((ValueArgument) subOption).value;
                jvmArgs.put(value);
                Utils.saveConfig(jsonObject);
            }
            break;
            case "d":
                if (!(subOption instanceof ValueArgument)) {
                    System.out.println(getString("CONSOLE_GET_USAGE"));
                    return;
                }

                String value = ((ValueArgument) subOption).value;
                try {
                    int order = Utils.parse(value);
                    jvmArgs.remove(order);
                    Utils.saveConfig(jsonObject);
                } catch (Exception e) {
                    return;
                }
                break;
            default:
                Utils.printfln(getString("CONSOLE_UNKNOWN_OPTION"), key);
                break;
        }

    }

    @Override
    public String getUsageName() {
        return "JVM_ARGS";
    }
}
