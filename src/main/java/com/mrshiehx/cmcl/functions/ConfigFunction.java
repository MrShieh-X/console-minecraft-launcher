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
 */
package com.mrshiehx.cmcl.functions;

import com.mrshiehx.cmcl.CMCL;
import com.mrshiehx.cmcl.bean.arguments.*;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.console.PrintingUtils;
import org.json.JSONObject;

import java.util.Arrays;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.isImmersiveMode;

public class ConfigFunction implements Function {
    @Override
    public void execute(Arguments arguments) {
        if (!Function.checkArgs(arguments, 2, 1,
                ArgumentRequirement.ofSingle("a"),
                ArgumentRequirement.ofSingle("all"),
                ArgumentRequirement.ofSingle("getRaw"),
                ArgumentRequirement.ofSingle("c"),
                ArgumentRequirement.ofSingle("clear"),
                ArgumentRequirement.ofSingle("v"),
                ArgumentRequirement.ofSingle("view"),
                ArgumentRequirement.ofValue("getRaw"),
                ArgumentRequirement.ofValue("d"),
                ArgumentRequirement.ofValue("delete"))) return;
        JSONObject config = Utils.getConfig();
        Argument firstArg = arguments.optArgument(1);
        if (firstArg instanceof TextArgument) {
            String key = firstArg.key;
            Argument secondArg = arguments.optArgument(2);
            if (secondArg == null) {
                Object obj = config.opt(key);
                if (obj != null) {
                    System.out.println(key +
                            '=' +
                            obj +
                            " (" +
                            Utils.getTypeText(obj.getClass().getSimpleName()) +
                            ")");
                } else {
                    System.out.println("null");
                }
                return;
            }
            String value = secondArg.originArray[0];
            config.put(key, value);
            Utils.saveConfig(config);
            if (isImmersiveMode) {
                CMCL.initConfig();
            }
        } else if (firstArg instanceof SingleArgument) {
            String key = firstArg.key;
            switch (key) {
                case "a":
                case "all":
                    PrintingUtils.printTable(
                            new String[]{getString("TABLE_CONFIG_ALL_NAME"),
                                    getString("TABLE_CONFIG_ALL_TYPE"),
                                    getString("TABLE_CONFIG_ALL_VALUE")},
                            new int[]{25, 11, 30},
                            false,
                            config.toMap().entrySet().stream()
                                    .map(stringObjectEntry -> {
                                        String value;
                                        switch (stringObjectEntry.getKey()) {
                                            case "accounts":
                                                value = getString("TABLE_CONFIG_ALL_VIEW_SEPARATELY", stringObjectEntry.getKey());
                                                break;
                                            default:
                                                value = String.valueOf(stringObjectEntry.getValue());
                                                break;
                                        }
                                        return new String[]{
                                                stringObjectEntry.getKey(),
                                                Utils.getTypeText(stringObjectEntry.getValue().getClass().getSimpleName()),
                                                value};
                                    }).toArray(String[][]::new));
                    break;
                case "c":
                case "clear":
                    Utils.saveConfig(new JSONObject());
                    if (isImmersiveMode) {
                        CMCL.initConfig();
                    }
                    break;
                case "v":
                case "view":
                    PrintingUtils.printTable(
                            new String[]{getString("TABLE_SETTABLE_CONFIG_NAME"),
                                    getString("TABLE_SETTABLE_CONFIG_TYPE"),
                                    getString("TABLE_SETTABLE_CONFIG_MEANING")},
                            new int[]{25, 11, 30},
                            false,
                            Arrays.stream(
                                            getString("MESSAGE_CONFIGURATIONS_TABLE_CONTENT").split("\n"))
                                    .map(item -> item.split("\\|"))
                                    .toArray(String[][]::new)
                    );
                    System.out.println(getString("MESSAGE_CONFIGURATIONS_TIP"));
                    break;
                case "getRaw":
                    System.out.println(config.toString(2));
                    break;
                default:
                    System.out.println(getString("CONSOLE_UNKNOWN_COMMAND_OR_MEANING", firstArg.originString));
                    break;
            }
        } else if (firstArg instanceof ValueArgument) {
            String key = firstArg.key;
            Argument arg = arguments.optArgument(1);
            String value = ((ValueArgument) arg).value;
            switch (key) {
                case "getRaw":
                    int indentFactor;
                    try {
                        indentFactor = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        System.out.println(getString("CONSOLE_UNSUPPORTED_VALUE", value));
                        break;
                    }
                    System.out.println(config.toString(indentFactor));
                    break;
                case "d":
                case "delete":
                    config.remove(value);
                    Utils.saveConfig(config);
                    if (isImmersiveMode) {
                        CMCL.initConfig();
                    }
                    break;
                default:
                    System.out.println(getString("CONSOLE_UNKNOWN_COMMAND_OR_MEANING", firstArg.originString));
                    break;
            }
        }
    }

    @Override
    public String getUsageName() {
        return "config";
    }
}
