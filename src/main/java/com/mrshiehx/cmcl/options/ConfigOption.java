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
import com.mrshiehx.cmcl.bean.arguments.SingleArgument;
import com.mrshiehx.cmcl.bean.arguments.ValueArgument;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONObject;

import java.util.Map;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.*;

public class ConfigOption implements Option {
    @Override
    public void execute(Arguments arguments) {
        Argument subOption = arguments.opt(1);
        if (subOption == null) {
            System.out.println(getString("CONSOLE_GET_USAGE"));
            return;
        }
        String key = subOption.key;
        JSONObject jsonObject = Utils.getConfig();
        switch (key.toLowerCase()) {
            case "p":
                if (subOption instanceof SingleArgument) {
                    System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                } else if (subOption instanceof ValueArgument) {
                    ValueArgument valueArgument = (ValueArgument) subOption;
                    String value = valueArgument.value;
                    Object obj = jsonObject.opt(value);
                    if (obj != null) {
                        System.out.println(value +
                                '=' +
                                obj +
                                "(" +
                                obj.getClass().getSimpleName() +
                                ")");
                    } else {
                        System.out.println("null");
                    }
                }
                break;
            case "a":
                Map<String, Object> map = Utils.getConfig().toMap();
                if (map != null && map.size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    int size = map.entrySet().size();
                    int i = 0;
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        stringBuilder.append(entry.getKey())
                                .append('=')
                                .append(entry.getValue())
                                .append("(")
                                .append(entry.getValue().getClass().getSimpleName())
                                .append(")");
                        if (i + 1 < size) {
                            stringBuilder.append("\n");
                        }
                        i++;
                    }
                    System.out.println(stringBuilder);

                }
                break;
            case "c":
                javaPath = "";
                Utils.saveConfig(new JSONObject());
                break;
            case "r":
                if (!(subOption instanceof ValueArgument)) {
                    System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                    return;
                }

                String name2 = ((ValueArgument) subOption).value;
                jsonObject.remove(name2);
                if ("javaPath".equals(name2))
                    javaPath = "";
                Utils.saveConfig(jsonObject);
                break;
            case "s":
                Argument type = arguments.optArgument("t");
                Argument name = arguments.optArgument("n");
                Argument value = arguments.optArgument("v");
                if (type instanceof ValueArgument &&
                        name instanceof ValueArgument &&
                        value instanceof ValueArgument) {
                    ValueArgument typeA = (ValueArgument) type;
                    ValueArgument nameA = (ValueArgument) name;
                    ValueArgument valueA = (ValueArgument) value;
                    switch (typeA.value.toLowerCase()) {
                        case "s":
                            if ("javaPath".equals(nameA.value)) javaPath = valueA.value;
                            jsonObject.put(nameA.value, valueA.value);
                            Utils.saveConfig(jsonObject);
                            break;
                        case "b":
                            if ("javaPath".equals(nameA.value)) javaPath = "";
                            jsonObject.put(nameA.value, Boolean.parseBoolean(valueA.value));
                            Utils.saveConfig(jsonObject);
                            break;
                        case "i":
                            try {
                                jsonObject.put(nameA.value, Integer.parseInt(valueA.value));
                                if ("javaPath".equals(nameA.value)) javaPath = "";
                                Utils.saveConfig(jsonObject);
                            } catch (NumberFormatException e) {
                                System.out.println(String.format(getString("CONSOLE_UNSUPPORTED_VALUE"), valueA.value));
                            }
                            break;
                        case "f":
                            try {
                                jsonObject.put(nameA.value, Double.parseDouble(valueA.value));
                                if ("javaPath".equals(nameA.value)) javaPath = "";
                                Utils.saveConfig(jsonObject);
                            } catch (NumberFormatException e) {
                                System.out.println(String.format(getString("CONSOLE_UNSUPPORTED_VALUE"), valueA.value));
                            }
                            break;
                        default:
                            System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                            break;
                    }
                } else {
                    System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                }
                break;
            default:
                System.out.println(String.format(getString("CONSOLE_UNKNOWN_OPTION"), key));
                break;
        }
    }

    @Override
    public String getUsageName() {
        return "CONFIG";
    }
}
