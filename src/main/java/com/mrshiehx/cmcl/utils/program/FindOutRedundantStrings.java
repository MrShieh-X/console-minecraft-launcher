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

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FindOutRedundantStrings {
    public static void main2(String[] args) throws Exception {
        System.out.println("en:");
        main(Languages.en);
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("zh:");
        main(Languages.zh);


        //clear();
    }

    public static void main(Map<String, String> map) throws Exception {
        File dir = new File("src/main/java");
        List<File> files = new LinkedList<>();
        tj(files, dir);
        List<String> contents = new LinkedList<>();
        for (File file : files) {
            if (!file.getName().equals("Languages.java") && !file.getName().equals(FindOutRedundantStrings.class.getSimpleName() + ".java"))
                contents.add(readFileContent(file, false));
        }

        List<String> finall = new LinkedList<>();
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

    public static void modifyFile(File file, String content, boolean append) throws IOException {
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file, append);
        //BufferedWriter writer = new BufferedWriter(fileWriter);
        fileWriter.append(content);
        fileWriter.flush();
        fileWriter.close();
    }

    public static String readFileContent(File file, boolean huanhang) throws IOException {
        BufferedReader reader = null;
        StringBuilder sbf = new StringBuilder();
        reader = new BufferedReader(new FileReader(file));
        String tempStr;
        while ((tempStr = reader.readLine()) != null) {
            sbf.append(tempStr);
            if (huanhang) sbf.append('\n');
        }
        reader.close();
        return sbf.toString();
    }


    static void tj(List<File> list, File file) {
        if (file != null && file.exists()) {
            if (file.isFile()) {
                list.add(file);
            } else {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File file1 : files) {
                        if (file1.isFile()) {
                            if (!file1.getName().equals("strings.xml") && !file1.getName().endsWith(".png"))
                                list.add(file1);
                        } else {
                            tj(list, file1);
                        }
                    }
                }
            }
        }
    }
}
