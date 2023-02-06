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

package com.mrshiehx.cmcl.utils.console;

import java.util.LinkedList;
import java.util.List;

public class CommandUtils {
    public static List<String> splitCommand(String src) {
        List<String> list = new LinkedList<>();
        boolean quoting = false;
        char[] chars = src.toCharArray();
        for (int i = 0; i < src.length(); i++) {
            char str = chars[i];
            boolean modifiedQuoting = false;
            if (str == '\"' && (i == 0 || chars[i - 1] != '\\')) {
                quoting = !quoting;
                modifiedQuoting = true;
            }
            if (!quoting) {
                if (!modifiedQuoting) {
                    if (str != ' ') {
                        if (i == 0) list.add(String.valueOf(str));
                        else list.set(list.size() - 1, list.get(list.size() - 1) + str);
                    } else {
                        list.add("");
                    }
                }
            } else {
                if (!modifiedQuoting) {
                    if (list.size() > 0) {
                        list.set(list.size() - 1, list.get(list.size() - 1) + str);
                    } else {
                        list.add("" + str);
                    }
                }
            }
        }
        if (list.size() == 0) {
            list.add(src);
        }
        return list;
    }

    public static String clearRedundantSpaces(String string) {
        char[] sourceChars = string.toCharArray();
        Object space = new Object();
        Object[] objects = new Object[string.length()];
        boolean quoting = false;
        for (int i = 0; i < sourceChars.length; i++) {
            char cha = sourceChars[i];
            if (cha == '\"' && (i == 0 || sourceChars[i - 1] != '\\')) {
                quoting = !quoting;
            }
            objects[i] = !quoting && cha == ' ' ? space : cha;
        }
        List<Character> list = new LinkedList<>();
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

            } else {
                list.add((Character) object);
            }
        }

        char[] chars = new char[list.size()];
        for (int i = 0; i < list.size(); i++) {
            chars[i] = list.get(i);
        }
        return quoting ? (new String(chars) + "\"").trim() : new String(chars).trim();//Judging whether it is still quoted, that is, judging whether the double quotes are complete
    }

    public static String argsToCommand(List<String> args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < args.size(); i++) {
            String str = args.get(i);
            if (str.contains(" ")) {
                str = "\"" + str + "\"";
                if (str.contains("\\")) {
                    str = str.replace("\\", "\\\\");
                }
            }
            stringBuilder.append(str);
            if (i + 1 != args.size()) {
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }

    public static String powershellString(String str) {
        return "'" + str.replace("'", "''") + "'";
    }
}
