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

package com.mrshiehx.cmcl.utils.system;

import com.mrshiehx.cmcl.utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaUtils {
    public static String getDefaultJavaPath() {
        String s = System.getProperty("java.home");
        return Utils.isEmpty(s) ? "" : new File(s, SystemUtils.isWindows() ? "bin\\java.exe" : "bin/java").getAbsolutePath();
    }


    public static String getOriginalJavaVersion(String javaFile) {
        try {
            String version = null;
            Process process = new ProcessBuilder(javaFile, "-XshowSettings:properties", "-version").start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream(), OperatingSystem.NATIVE_CHARSET))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    Matcher m = Pattern.compile("java\\.version = (?<version>.*)").matcher(line);
                    if (m.find()) {
                        version = m.group("version");
                        break;
                    }
                }
            }

            if (version == null) {
                process = new ProcessBuilder(javaFile, "-version").start();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream(), OperatingSystem.NATIVE_CHARSET))) {
                    for (String line; (line = reader.readLine()) != null; ) {
                        Matcher m = Pattern.compile("version \"(?<version>(.*?))\"").matcher(line);
                        if (m.find()) {
                            version = m.group("version");
                            break;
                        }
                    }
                }
            }
            return version;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getJavaVersion(String javaFile) {
        try {
            String version = getOriginalJavaVersion(javaFile);
            if (version != null) {
                if (version.startsWith("1.")) {
                    version = version.substring(2, 3);
                } else {
                    int dot = version.indexOf(".");
                    if (dot != -1) {
                        version = version.substring(0, dot);
                    }
                }
                return Integer.parseInt(version);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getJavaVersion() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if (dot != -1) {
                version = version.substring(0, dot);
            }
        }
        return Integer.parseInt(version);
    }
}
