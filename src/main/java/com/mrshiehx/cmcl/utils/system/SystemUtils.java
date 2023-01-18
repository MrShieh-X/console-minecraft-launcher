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

import com.sun.management.OperatingSystemMXBean;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.URI;
import java.util.Locale;

public class SystemUtils {
    private static final String[] linuxBrowsers = {
            "xdg-open",
            "google-chrome",
            "firefox",
            "microsoft-edge",
            "opera",
            "konqueror",
            "mozilla"
    };

    public static void openLink(String link) {
        if (link == null)
            return;

        if (java.awt.Desktop.isDesktopSupported()) {
            new Thread(() -> {
                if (OperatingSystem.CURRENT_OS == OperatingSystem.LINUX) {
                    for (String browser : linuxBrowsers) {
                        try (final InputStream is = Runtime.getRuntime().exec(new String[]{"which", browser}).getInputStream()) {
                            if (is.read() != -1) {
                                Runtime.getRuntime().exec(new String[]{browser, link});
                                return;
                            }
                        } catch (Throwable ignored) {
                        }
                        //Logging.LOG.log(Level.WARNING, "No known browser found");
                    }
                }
                try {
                    java.awt.Desktop.getDesktop().browse(new URI(link));
                } catch (Throwable e) {
                    if (OperatingSystem.CURRENT_OS == OperatingSystem.OSX)
                        try {
                            Runtime.getRuntime().exec(new String[]{"/usr/bin/open", link});
                        } catch (IOException e2) {
                            //Logging.LOG.log(Level.WARNING, "Unable to open link: " + link, e2);
                        }
                    //Logging.LOG.log(Level.WARNING, "Failed to open link: " + link, e);
                }
            }).start();

        }
    }

    public static boolean isWindows() {
        /*return File.separator.equals("\\")
                || File.separatorChar == '\\'
                ||*//*AccessController.doPrivileged(OSInfo.getOSTypeAction()) == OSInfo.OSType.WINDOWS*//*System.getProperty("os.name").toLowerCase().contains("windows");*/
        return OperatingSystem.CURRENT_OS == OperatingSystem.WINDOWS;
    }

    public static long getDefaultMemory() {
        return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize() / (4 * 1024 * 1024);
    }

    public static String getArchInt() {
        String value = System.getProperty("os.arch").trim().toLowerCase(Locale.ROOT);

        switch (value) {
            case "x8664":
            case "x86-64":
            case "x86_64":
            case "amd64":
            case "ia32e":
            case "em64t":
            case "x64":
            case "arm64":
            case "aarch64":
            case "mips64":
            case "mips64el":
            case "riscv":
            case "risc-v":
            case "ia64":
            case "ia64w":
            case "itanium64":
            case "sparcv9":
            case "sparc64":
            case "ppc64le":
            case "powerpc64le":
            case "loongarch64":
            case "s390x":
            case "ppc64":
            case "powerpc64":
                return "64";
            case "x8632":
            case "x86-32":
            case "x86_32":
            case "x86":
            case "i86pc":
            case "i386":
            case "i486":
            case "i586":
            case "i686":
            case "ia32":
            case "x32":
            case "arm":
            case "arm32":
            case "mips":
            case "mips32":
            case "mipsel":
            case "mips32el":
            case "ia64n":
            case "sparc":
            case "sparc32":
            case "ppc":
            case "ppc32":
            case "powerpc":
            case "powerpc32":
            case "s390":
            case "ppcle":
            case "ppc32le":
            case "powerpcle":
            case "powerpc32le":
            case "loongarch32":
                return "32";
            default:
                if (value.startsWith("armv7")) {
                    return "32";
                }
                if (value.startsWith("armv8") || value.startsWith("armv9")) {
                    return "64";
                }
                return "unknown";
        }
    }

    public static String getArchCheckedName() {
        String value = System.getProperty("os.arch").trim().toLowerCase(Locale.ROOT);

        switch (value) {
            case "x8664":
            case "x86-64":
            case "x86_64":
            case "amd64":
            case "ia32e":
            case "em64t":
            case "x64":
                return "x86_64";
            case "x8632":
            case "x86-32":
            case "x86_32":
            case "x86":
            case "i86pc":
            case "i386":
            case "i486":
            case "i586":
            case "i686":
            case "ia32":
            case "x32":
                return "x86";
            case "arm64":
            case "aarch64":
                return "arm64";
            case "arm":
            case "arm32":
                return "arm32";
            case "mips64":
                return "mips64";
            case "mips64el":
                return "mips64el";
            case "mips":
            case "mips32":
                return "mips";
            case "mipsel":
            case "mips32el":
                return "mipsel";
            case "riscv":
            case "risc-v":
                return "riscv";
            case "ia64":
            case "ia64w":
            case "itanium64":
                return "ia64";
            case "ia64n":
                return "ia32";
            case "sparcv9":
            case "sparc64":
                return "sparcv9";
            case "sparc":
            case "sparc32":
                return "sparc";
            case "ppc64":
            case "powerpc64":
                return "little".equals(System.getProperty("sun.cpu.endian")) ? "ppc64le" : "ppc64";
            case "ppc64le":
            case "powerpc64le":
                return "ppc64le";
            case "ppc":
            case "ppc32":
            case "powerpc":
            case "powerpc32":
                return "ppc";
            case "ppcle":
            case "ppc32le":
            case "powerpcle":
            case "powerpc32le":
                return "ppcle";
            case "s390":
                return "s390";
            case "s390x":
                return "s390x";
            case "loongarch32":
                return "loongarch32";
            case "loongarch64":
                return "loongarch64";
            default:
                if (value.startsWith("armv7")) {
                    return "arm32";
                }
                if (value.startsWith("armv8") || value.startsWith("armv9")) {
                    return "arm64";
                }
                return "unknown";
        }
    }
}
