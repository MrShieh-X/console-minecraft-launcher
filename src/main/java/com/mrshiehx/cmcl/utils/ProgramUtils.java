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

package com.mrshiehx.cmcl.utils;

import com.mrshiehx.cmcl.constants.Languages;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.mrshiehx.cmcl.utils.Utils.isEmpty;

public class ProgramUtils {
    public static void printStringsThatEnglishNotHave() {
        List<String> fin = new LinkedList<>();

        for (Map.Entry<String, String> e : Languages.zh.entrySet()) {
            String s = e.getKey();
            if (isEmpty(Languages.en.get(s))) {
                fin.add(s);
            }
        }

        for (String s : fin) {
            System.out.println(s);
        }
    }

    public static void printMap(Map<?, ?> map) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        printStringsThatEnglishNotHave();
    }
}
