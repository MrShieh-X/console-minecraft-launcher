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

import com.mrshiehx.cmcl.bean.arguments.*;
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

        Argument second = arguments.optArgument(2);
        if (subOption instanceof TextArgument && second instanceof TextArgument) {
            TextArgument value = (TextArgument) second;
            jsonObject.put(key, value.key);
            if (isImmersiveMode) {
                if ("javaPath".equals(key)) javaPath = value.key;
                if ("language".equals(key)) setLanguage(value.key);

                if ("gameDir".equals(key)) setGameDirs();
                if ("assetsDir".equals(key)) setGameDirs();
                if ("resourcesDir".equals(key)) setGameDirs();

            }
            Utils.saveConfig(jsonObject);
            return;
        }

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
                                " (" +
                                Utils.getTypeText(obj.getClass().getSimpleName()) +
                                ")");
                    } else {
                        System.out.println("null");
                    }
                }
                break;
            case "o":
                int indentFactor = 2;
                if (subOption instanceof ValueArgument) {
                    ValueArgument valueA = ((ValueArgument) subOption);
                    try {
                        indentFactor = Integer.parseInt(valueA.value);
                    } catch (NumberFormatException e) {
                        System.out.println(getString("CONSOLE_UNSUPPORTED_VALUE", valueA.value));
                        return;
                    }
                }
                System.out.println(jsonObject.toString(indentFactor));
                break;

            case "a":
                Map<String, Object> map = jsonObject.toMap();
                if (map != null && map.size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    int size = map.entrySet().size();
                    int i = 0;
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        stringBuilder.append(entry.getKey())
                                .append('=')
                                .append(entry.getValue())
                                .append(" (")
                                .append(Utils.getTypeText(entry.getValue().getClass().getSimpleName()))
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
                Utils.saveConfig(new JSONObject());

                if (isImmersiveMode) {
                    javaPath = "";
                    setLanguage(null);
                    setGameDirs();
                }
                break;
            case "r":
                if (!(subOption instanceof ValueArgument)) {
                    System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                    return;
                }

                String name2 = ((ValueArgument) subOption).value;
                jsonObject.remove(name2);

                Utils.saveConfig(jsonObject);
                if (isImmersiveMode) {
                    if ("javaPath".equals(name2))
                        javaPath = "";
                    if ("language".equals(name2))
                        setLanguage(null);
                    if ("gameDir".equals(key)) setGameDirs();
                    if ("assetsDir".equals(key)) setGameDirs();
                    if ("resourcesDir".equals(key)) setGameDirs();
                }
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

                            jsonObject.put(nameA.value, valueA.value);
                            Utils.saveConfig(jsonObject);
                            if (isImmersiveMode) {
                                if ("javaPath".equals(nameA.value)) javaPath = valueA.value;
                                if ("language".equals(nameA.value)) setLanguage(valueA.value);
                                if ("gameDir".equals(key)) setGameDirs();
                                if ("assetsDir".equals(key)) setGameDirs();
                                if ("resourcesDir".equals(key)) setGameDirs();
                            }
                            break;
                        case "b":

                            jsonObject.put(nameA.value, Boolean.parseBoolean(valueA.value));
                            Utils.saveConfig(jsonObject);
                            if (isImmersiveMode) {
                                if ("javaPath".equals(nameA.value)) javaPath = "";
                                if ("language".equals(nameA.value)) setLanguage(null);
                                if ("gameDir".equals(key)) setGameDirs();
                                if ("assetsDir".equals(key)) setGameDirs();
                                if ("resourcesDir".equals(key)) setGameDirs();
                            }
                            break;
                        case "i":
                            try {
                                jsonObject.put(nameA.value, Integer.parseInt(valueA.value));

                                Utils.saveConfig(jsonObject);
                                if (isImmersiveMode) {
                                    if ("javaPath".equals(nameA.value)) javaPath = "";
                                    if ("language".equals(nameA.value)) setLanguage(null);
                                    if ("gameDir".equals(key)) setGameDirs();
                                    if ("assetsDir".equals(key)) setGameDirs();
                                    if ("resourcesDir".equals(key)) setGameDirs();
                                }
                            } catch (NumberFormatException e) {
                                System.out.println(getString("CONSOLE_UNSUPPORTED_VALUE", valueA.value));
                            }
                            break;
                        case "f":
                            try {
                                jsonObject.put(nameA.value, Double.parseDouble(valueA.value));

                                Utils.saveConfig(jsonObject);
                                if (isImmersiveMode) {
                                    if ("javaPath".equals(nameA.value)) javaPath = "";
                                    if ("language".equals(nameA.value)) setLanguage(null);
                                    if ("gameDir".equals(key)) setGameDirs();
                                    if ("assetsDir".equals(key)) setGameDirs();
                                    if ("resourcesDir".equals(key)) setGameDirs();
                                }

                            } catch (NumberFormatException e) {
                                System.out.println(getString("CONSOLE_UNSUPPORTED_VALUE", valueA.value));
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
                System.out.println(getString("CONSOLE_UNKNOWN_OPTION", key));
                break;
        }
    }

    @Override
    public String getUsageName() {
        return "CONFIG";
    }
}
