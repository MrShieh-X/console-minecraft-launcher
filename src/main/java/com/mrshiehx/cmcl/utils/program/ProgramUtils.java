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

package com.mrshiehx.cmcl.utils.program;

import com.mrshiehx.cmcl.constants.languages.LanguageEnum;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.mrshiehx.cmcl.utils.Utils.isEmpty;

public class ProgramUtils {
    public static void main2(String[] args) {
        System.out.println("in En not in Can: ");
        compareTwoLanguages(LanguageEnum.ENGLISH.getTextMap(), LanguageEnum.CANTONESE.getTextMap());
        System.out.println();
        System.out.println("in Can not in En: ");
        compareTwoLanguages(LanguageEnum.CANTONESE.getTextMap(), LanguageEnum.ENGLISH.getTextMap());
        System.out.println();
        System.out.println("in Can not in Zh: ");
        compareTwoLanguages(LanguageEnum.CANTONESE.getTextMap(), LanguageEnum.SIMPLIFIED_CHINESE.getTextMap());
        System.out.println();
        /*System.out.println("in Zh not in Can: ");
        compareTwoLanguages(Languages.getZh(),Languages.getCantonese());//永远为空
        System.out.println();*/
    }

    /**
     * 返回存在于x而不存在于y中的字符串名称
     **/
    public static void compareTwoLanguages(Map<String, String> x, Map<String, String> y) {
        x.entrySet().stream().filter(e -> isEmpty(y.get(e.getKey()))).forEach(System.out::println);
    }

    public static void printStringsThatChineseNotHave() {
        List<String> fin = new LinkedList<>();
        for (Map.Entry<String, String> e : LanguageEnum.ENGLISH.getTextMap().entrySet()) {
            String s = e.getKey();
            if (isEmpty(LanguageEnum.SIMPLIFIED_CHINESE.getTextMap().get(s))) {
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

    public static void compareLibrary(String xstr, String ystr) {
        List<String> x = Arrays.asList(xstr.split(";"));
        List<String> y = Arrays.asList(ystr.split(";"));

        LinkedList<String> xHaveYNo = new LinkedList<>();
        List<String> yHaveXNo = new LinkedList<>();

        for (String oldo : x) {
            if (!y.contains(oldo)) xHaveYNo.add(oldo);
        }
        for (String neee : y) {
            if (!x.contains(neee)) yHaveXNo.add(neee);
        }

        System.out.println("xHaveYNo:");
        for (String k : xHaveYNo) {
            System.out.println(k);
        }
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("yHaveXNo:");
        for (String k : yHaveXNo) {
            System.out.println(k);
        }
    }
}
