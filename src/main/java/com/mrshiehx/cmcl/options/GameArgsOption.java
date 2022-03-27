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
import org.json.JSONObject;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;

public class GameArgsOption implements Option {
    @Override
    public void execute(Arguments arguments) {
        Argument subOption = arguments.opt(1);
        if (subOption == null) {
            System.out.println(getString("CONSOLE_GET_USAGE"));
            return;
        }
        String key = subOption.key;
        JSONObject jsonObject = Utils.getConfig();
        JSONObject gameArgs = jsonObject.optJSONObject("gameArgs");
        if (gameArgs == null) jsonObject.put("gameArgs", gameArgs = new JSONObject());
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
                System.out.println(gameArgs.toString(indentFactor));
                break;
            case "a": {
                Argument name = arguments.optArgument("n");
                if (!(name instanceof ValueArgument)) {
                    System.out.println(getString("CONSOLE_GET_USAGE"));
                    return;
                }
                String nameValue = ((ValueArgument) name).value;
                String value = "";

                Argument valueArg = arguments.optArgument("v");
                if ((valueArg instanceof ValueArgument)) {
                    value = ((ValueArgument) valueArg).value;
                }


                gameArgs.put(nameValue, value);
                Utils.saveConfig(jsonObject);
            }
            break;
            case "d":
                if (!(subOption instanceof ValueArgument)) {
                    System.out.println(getString("CONSOLE_GET_USAGE"));
                    return;
                }

                String value = ((ValueArgument) subOption).value;
                gameArgs.remove(value);
                Utils.saveConfig(jsonObject);
                break;
            default:
                Utils.printfln(getString("CONSOLE_UNKNOWN_OPTION"), key);
                break;
        }

    }

    @Override
    public String getUsageName() {
        return "GAME_ARGS";
    }
}
