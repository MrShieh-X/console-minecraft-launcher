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
 *
 */

package com.mrshiehx.cmcl.utils.program;

import com.mrshiehx.cmcl.constants.Languages;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.mrshiehx.cmcl.utils.Utils.isEmpty;

public class ProgramUtils {
    public static void main2(String[] args) {
        printStringsThatChineseNotHave();
        System.out.println();
        printStringsThatEnglishNotHave();
    }

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

    public static void printStringsThatChineseNotHave() {
        List<String> fin = new LinkedList<>();

        for (Map.Entry<String, String> e : Languages.en.entrySet()) {
            String s = e.getKey();
            if (isEmpty(Languages.zh.get(s))) {
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

    public static void compareLibrary() {
        String cmcl = "";
        String hmcl = "";

        List<String> old = Arrays.asList(cmcl.split(";"));
        List<String> nee = Arrays.asList(hmcl.split(";"));

        LinkedList<String> oldHaveNeeNo = new LinkedList<>();
        List<String> neeHaveOldNo = new LinkedList<>();

        for (String oldo : old) {
            if (!nee.contains(oldo)) oldHaveNeeNo.add(oldo);
        }
        for (String neee : nee) {
            if (!old.contains(neee)) neeHaveOldNo.add(neee);
        }

        System.out.println("oldHaveNeeNo:");
        for (String k : oldHaveNeeNo) {
            System.out.println(k);
        }
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("neeHaveOldNo:");
        for (String k : neeHaveOldNo) {
            System.out.println(k);
        }
    }
}
