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

import com.mrshiehx.cmcl.ConsoleMinecraftLauncher;
import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.exceptions.NotSelectedException;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getUsage;

public class ImmersiveModeOption implements Option {
    @Override
    public void execute(Arguments argumentsOrigin) {
        ConsoleMinecraftLauncher.isImmersiveMode = true;

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
                if (command.equals("echo off")) {
                    Constants.ECHO_OPEN_FOR_IMMERSIVE = false;
                } else if (command.equals("echo on")) {
                    Constants.ECHO_OPEN_FOR_IMMERSIVE = true;
                } else {
                    List<String> list = Utils.splitCommand(command);
                    Arguments args = new Arguments(list.toArray(new String[0]), true);
                    if (args.size < 1) continue;
                    Argument argument = args.optArgument(0);
                    if (argument == null) continue;
                    String key = argument.key;
                    Option option = Options.MAP.get(key.toLowerCase());
                    if (option == null) {
                        System.out.println(getString("CONSOLE_IMMERSIVE_UNKNOWN", key));
                        continue;
                    }
                    Argument usage = args.optArgument(1);
                    if (usage != null && !(option instanceof HelpOption)) {
                        if (usage.equals("usage") || usage.equals("help") || usage.equals("u") || usage.equals("h")) {
                            String name = option.getUsageName();
                            if (!Utils.isEmpty(name)) {
                                System.out.println(getUsage(name));
                            } else {
                                System.out.println(getUsage("TITLE"));
                            }
                        } else {
                            try {
                                option.execute(args);
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (!(option instanceof ImmersiveModeOption)) {
                            try {
                                option.execute(args);
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }


            }
        }
    }


    private String format(String command) {
        return !Utils.isEmpty(command) ? clearRedundantSpaces(command.trim()) : "";
    }

    @Override
    public String getUsageName() {
        return null;
    }

    private String clearRedundantSpaces(String string) {
        char[] sourceChars = string.toCharArray();
        Object space = new Object();
        Object[] objects = new Object[string.length()];
        boolean yinyong = false;
        for (int i = 0; i < sourceChars.length; i++) {
            char cha = sourceChars[i];
            if (cha == '\"') {
                yinyong = !yinyong;
            }
            objects[i] = !yinyong && cha == ' ' ? space : cha;
        }
        List<Character> list = new ArrayList<>();
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            if (object == space) {
                list.add(' ');
                for (int j = i; j < objects.length; j++) {
                    if (objects[j] != space) {
                        i = j - 1;
                        break;
                    }
                }

            } else if (object instanceof Character) {
                list.add((Character) object);
            }
        }

        char[] chars = new char[list.size()];
        for (int i = 0; i < list.size(); i++) {
            chars[i] = list.get(i);
        }
        return new String(chars);
    }

    private static void printPrompt() {
        //MrShiehX@1.18.2$
        JSONObject account = null;
        try {
            account = Utils.getSelectedAccount(false);
        } catch (NotSelectedException ignore) {
        }


        JSONObject config = Utils.getConfig();
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
        System.out.print(stringBuilder);
    }
}
