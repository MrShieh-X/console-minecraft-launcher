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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FindOutRedundantStrings {
    public static void main2(String[] args) throws IOException {
        System.out.println("en:");
        main(LanguageEnum.ENGLISH.getTextMap());
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("can:");
        main(LanguageEnum.CANTONESE.getTextMap());
    }

    public static void main(Map<String, String> map) throws IOException {
        File dir = new File("build/classes/java/main");
        List<File> files = new LinkedList<>();
        traverseGetFiles(files, dir);
        List<String> contents = new LinkedList<>();
        for (File file : files) {
            if (!file.getName().equals("Cantonese.class") && !file.getName().equals("SimplifiedChinese.class") && !file.getName().equals("English.class")
                    && !file.getName().equals(FindOutRedundantStrings.class.getSimpleName() + ".class"))
                contents.add(readFileContent(file, true));
        }

        Set<String> finall = new HashSet<>();
        for (String name : map.keySet()) {
            boolean contains = false;
            for (String content : contents) {
                if (content.contains(name)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) finall.add(name);
        }
        for (String s : finall) {
            System.out.println(s);
        }
    }

    public static String readFileContent(File file, boolean switchLine) throws IOException {
        BufferedReader reader;
        StringBuilder sbf = new StringBuilder();
        reader = new BufferedReader(new FileReader(file));
        String tempStr;
        while ((tempStr = reader.readLine()) != null) {
            sbf.append(tempStr);
            if (switchLine)
                sbf.append('\n');
        }
        reader.close();
        return sbf.toString();
    }


    static void traverseGetFiles(List<File> list, File file) {
        if (file == null || !file.exists()) return;
        if (file.isFile()) {
            list.add(file);
        } else {
            File[] files = file.listFiles();
            if (files == null) return;
            for (File file1 : files) {
                if (file1.isFile()) {
                    if (!file1.getName().equals("strings.xml") && !file1.getName().endsWith(".png"))
                        list.add(file1);
                } else {
                    traverseGetFiles(list, file1);
                }
            }

        }

    }
}
