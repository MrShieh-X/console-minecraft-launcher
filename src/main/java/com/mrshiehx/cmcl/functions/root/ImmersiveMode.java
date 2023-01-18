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
package com.mrshiehx.cmcl.functions.root;

import com.mrshiehx.cmcl.CMCL;
import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.exceptions.NotSelectedException;
import com.mrshiehx.cmcl.functions.Function;
import com.mrshiehx.cmcl.functions.Functions;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.cmcl.AccountUtils;
import com.mrshiehx.cmcl.utils.console.CommandUtils;
import org.json.JSONObject;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ImmersiveMode {
    public static void getIn() {
        CMCL.isImmersiveMode = true;

        while (true) {
            if (Constants.ECHO_OPEN_FOR_IMMERSIVE) {
                printPrompt();
            }
            Scanner scanner = new Scanner(System.in);
            String s;
            try {
                s = scanner.nextLine();
            } catch (NoSuchElementException e) {
                return;
            }
            if (!Utils.isEmpty(s)) {
                String command = format(s);
                switch (command) {
                    case "echo off":
                        Constants.ECHO_OPEN_FOR_IMMERSIVE = false;
                        break;
                    case "echo on":
                        Constants.ECHO_OPEN_FOR_IMMERSIVE = true;
                        break;
                    case "exit":
                        System.exit(0);
                        return;
                    default:
                        List<String> list = CommandUtils.splitCommand(command);
                        Arguments args = new Arguments(list.toArray(new String[0]), true);
                        if (args.getSize() == 0) continue;
                        Argument argument = args.optArgument(0);
                        if (argument == null) continue;
                        String key = argument.key;
                        if ("help".equals(key)) {
                            RootFunction.printHelp();
                            break;
                        }
                        Function function = Functions.MAP.get(key);
                        if (function == null) {
                            //交给RootOption
                            RootFunction.execute(args, true);
                            break;
                        } else {
                            Argument usage = args.optArgument(1);
                            if (usage != null && (usage.equals("help") || usage.equals("h"))) {
                                String name = function.getUsageName();
                                if (!Utils.isEmpty(name)) {
                                    System.out.println(CMCL.getHelpDocumentation(name));
                                } else {
                                    RootFunction.printHelp();
                                }

                            } else {
                                try {
                                    function.execute(args);
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                }
            }
        }
    }

    private static String format(String command) {
        return !Utils.isEmpty(command) ? CommandUtils.clearRedundantSpaces(command) : "";
    }

    private static void printPrompt() {
        //MrShiehX@1.18.2$
        JSONObject account = null;
        JSONObject config = Utils.getConfig();
        try {
            account = AccountUtils.getSelectedAccount(config, false);
        } catch (NotSelectedException ignore) {
        }


        String selectedVersion = config.optString("selectedVersion");
        StringBuilder stringBuilder = new StringBuilder();
        if (account != null && !Utils.isEmpty(account.optString("playerName"))) {
            stringBuilder.append(account.optString("playerName"));
            if (!Utils.isEmpty(selectedVersion)) {
                stringBuilder.append('@');
            }
        }
        if (!Utils.isEmpty(selectedVersion)) {
            stringBuilder.append(selectedVersion);
        }


        stringBuilder.append("$ ");
        System.out.print(stringBuilder);//legal
    }
}
