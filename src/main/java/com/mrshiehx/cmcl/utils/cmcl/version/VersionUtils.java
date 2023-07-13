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

package com.mrshiehx.cmcl.utils.cmcl.version;

import com.mrshiehx.cmcl.CMCL;
import com.mrshiehx.cmcl.bean.GameVersion;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.json.JSONUtils;
import com.mrshiehx.cmcl.utils.system.OperatingSystem;
import com.mrshiehx.cmcl.utils.system.SystemUtils;
import org.jenkinsci.constant_pool_scanner.ConstantPool;
import org.jenkinsci.constant_pool_scanner.ConstantType;
import org.jenkinsci.constant_pool_scanner.StringConstant;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class VersionUtils {
    public static final Comparator<String> VERSION_COMPARATOR = (o1, o2) -> {
        o1 = o1.replace("3D-Shareware-v1.34", "3D-Shareware-v1-34")
                .replace("3D Shareware v1.34", "3D-Shareware-v1-34")
                .replace(" Pre-Release ", "-pre");
        o2 = o2.replace("3D-Shareware-v1.34", "3D-Shareware-v1-34")
                .replace("3D Shareware v1.34", "3D-Shareware-v1-34")
                .replace(" Pre-Release ", "-pre");

        String[] o1s = Utils.xsplit(o1, "\\.");
        String[] o2s = Utils.xsplit(o2, "\\.");

        int ONE = -1;
        int NONE = 1;
        if (o1s.length == 0 || o2s.length == 0) {
            if (o1s.length == 0 && o2s.length == 0) {
                if ("20w14infinite".equals(o1)) {
                    o1 = "20w13c";
                }
                if ("20w14infinite".equals(o2)) {
                    o2 = "20w13c";
                }
                if ("22w13oneblockatatime".equals(o1)) {
                    o1 = "22w13b";
                }
                if ("22w13oneblockatatime".equals(o2)) {
                    o2 = "22w13b";
                }
                if ("3D-Shareware-v1-34".equals(o1)) {
                    o1 = "19w13c";
                }
                if ("3D-Shareware-v1-34".equals(o2)) {
                    o2 = "19w13c";
                }
                if ("1.RV-Pre1".equals(o1)) {
                    o1 = "16w13a";
                }
                if ("1.RV-Pre1".equals(o2)) {
                    o2 = "16w13a";
                }
                if ("23w13a_or_b".equals(o1)) {
                    o1 = "23w13b";
                }
                if ("23w13a_or_b".equals(o2)) {
                    o2 = "23w13b";
                }
                int[] o1i = new int[]{Integer.parseInt(o1.substring(0, 2)), Integer.parseInt(o1.substring(3, 5)), (int) o1.charAt(5)};
                int[] o2i = new int[]{Integer.parseInt(o2.substring(0, 2)), Integer.parseInt(o2.substring(3, 5)), (int) o2.charAt(5)};


                for (int i = 0; i < 3; i++) {
                    int o1ii = o1i[i];
                    int o2ii = o2i[i];
                    if (o1ii > o2ii) {
                        return NONE;
                    } else if (o1ii < o2ii) {
                        return ONE;
                    }
                }
                return 0;
            } else if (o1s.length == 0) {
                return ONE;
            } else {
                return NONE;
            }
        }

        int bigger = Math.max(o1s.length, o2s.length);

        int[] o1i = new int[bigger + 2];
        int[] o2i = new int[bigger + 2];

        for (int i = 0; i < bigger; i++) {
            if (i < o1s.length) {
                String o1String = o1s[i];
                if (o1String.contains("-pre")) {
                    o1i[i + 2] = Integer.parseInt(o1String.substring(0, o1String.indexOf("-pre")));
                    o1i[1] = Integer.parseInt(o1String.substring(o1String.indexOf("-pre") + 4));
                } else if (o1String.contains("-rc")) {
                    o1i[i + 2] = Integer.parseInt(o1String.substring(0, o1String.indexOf("-rc")));
                    o1i[0] = Integer.parseInt(o1String.substring(o1String.indexOf("-rc") + 3));
                } else {
                    o1i[i + 2] = Integer.parseInt(o1String);
                    o1i[0] = -1;
                    o1i[1] = -1;
                }
            }
        }
        for (int i = 0; i < bigger; i++) {
            if (i < o2s.length) {
                String o2String = o2s[i];
                if (o2String.contains("-pre")) {
                    o2i[i + 2] = Integer.parseInt(o2String.substring(0, o2String.indexOf("-pre")));
                    o2i[1] = Integer.parseInt(o2String.substring(o2String.indexOf("-pre") + 4));
                } else if (o2String.contains("-rc")) {
                    o2i[i + 2] = Integer.parseInt(o2String.substring(0, o2String.indexOf("-rc")));
                    o2i[0] = Integer.parseInt(o2String.substring(o2String.indexOf("-rc") + 3));
                } else {
                    o2i[i + 2] = Integer.parseInt(o2String);
                    o2i[0] = -1;
                    o2i[1] = -1;
                }
            }
        }


        for (int i = 2; i < bigger + 2; i++) {
            int o1ii = o1i[i];
            int o2ii = o2i[i];
            if (o1ii > o2ii) {
                return NONE;
            } else if (o1ii < o2ii) {
                return ONE;
            } else {
                if (i + 1 == bigger + 2) {
                    for (int j = 0; j < 2; j++) {
                        int o1rp = o1i[j];
                        int o2rp = o2i[j];
                        if (o1rp > o2rp) {
                            return NONE;
                        } else if (o1rp < o2rp) {
                            return ONE;
                        }
                    }
                }
            }
        }

        return 0;
    };

    public static String getNativeLibraryName(String path) {
        if (Utils.isEmpty(path)) return "";
        String splitter = File.separator;
        if (!path.contains(splitter) && !path.contains("\\") && !path.contains("/")) return path;
        path = path.replace(File.separatorChar, '/');
        splitter = "/";
        String[] strings = path.split(splitter);
        if (strings.length < 3) return path;

        return strings[strings.length - 3];
    }

    public static String getNativesDirName() {
        return "natives-" + OperatingSystem.CURRENT_OS.getCheckedName() + "-" + SystemUtils.getArchCheckedName();
    }

    public static boolean versionExists(String name) {
        return new File(CMCL.versionsDir, name + "/" + name + ".json").exists()
                /*&& new File(ConsoleMinecraftLauncher.versionsDir, name + "/" + name + ".jar").exists()*/;
    }

    public static File getNativesDir(File versionFile) {
        String defa = getNativesDirName();
        File defaul = new File(versionFile, defa);
        if (defaul.exists()) {
            File[] files = defaul.listFiles(File::isFile);
            if (files != null && files.length > 0) return defaul;
        }
        File[] files = versionFile.listFiles(pathname -> pathname.isDirectory() && pathname.getName().startsWith(defa));
        if (files != null && files.length > 0) {
            for (File file : files) {
                File[] files2 = file.listFiles(File::isFile);
                if (files2 != null && files2.length > 0) return file;
            }
        }
        return defaul;
    }

    public static GameVersion getVersionByJar(File jarFile) {
        try (JarFile jar = new JarFile(jarFile)) {
            ZipEntry versionEntry = jar.getEntry("version.json");
            if (versionEntry != null) {
                JSONObject jsonObject = JSONUtils.parseJSONObject(Utils.inputStream2String(jar.getInputStream(versionEntry)));
                if (jsonObject != null) {
                    String name = jsonObject.optString("name");
                    String id = jsonObject.optString("id");
                    if (id.contains(" / ")) {
                        id = id.split(" / ")[0];
                    }
                    return new GameVersion(id, name);
                }
            }
            ZipEntry mainClass = jar.getEntry("net/minecraft/client/Minecraft.class");
            if (mainClass != null) {
                ConstantPool pool = org.jenkinsci.constant_pool_scanner.ConstantPoolScanner.parse(jar.getInputStream(mainClass), ConstantType.STRING);
                for (StringConstant stringConstant : pool.list(StringConstant.class)) {
                    String v = stringConstant.get();
                    String prefix = "Minecraft Minecraft ";
                    if (v.startsWith(prefix) && v.length() > prefix.length()) {
                        return new GameVersion(v.substring(prefix.length()), null);
                    }
                }
            }


            ZipEntry serverClass = jar.getEntry("net/minecraft/server/MinecraftServer.class");
            if (serverClass != null) {
                ConstantPool pool = org.jenkinsci.constant_pool_scanner.ConstantPoolScanner.parse(jar.getInputStream(serverClass), ConstantType.STRING);
                List<String> strings = new LinkedList<>();
                for (StringConstant stringConstant : pool.list(StringConstant.class)) {
                    String v = stringConstant.get();
                    strings.add(v);
                }
                int indexOf = -1;
                for (int i = 0; i < strings.size(); i++) {
                    if (strings.get(i).startsWith("Can't keep up!")) {
                        indexOf = i;
                        break;
                    }
                }
                if (indexOf >= 0) {
                    for (int i = indexOf - 1; i >= 0; --i) {
                        String s = strings.get(i);
                        if (s.matches(".*[0-9].*")) {

                            return new GameVersion(s, null);
                        }
                    }
                }
            }

        } catch (Exception ignore) {
        }
        return null;
    }

    @NotNull
    public static GameVersion getGameVersion(JSONObject json, File jar) {
        String v = json.optString("gameVersion");
        if (!Utils.isEmpty(v))
            return new GameVersion(v, null);

        GameVersion gameVersion = getVersionByJar(jar);
        if (gameVersion != null) {
            String id = gameVersion.id;
            String name = gameVersion.name;
            if (!Utils.isEmpty(id)) return new GameVersion(id, name);
        }
        return new GameVersion(null, null);
    }

    /**
     * x / x.x / x.x.x / x.x.x.x / x.x.x.x.x / x.x.x.x.x.x / ...
     *
     * @return 0 if un-comparable or they are the same, 1 if v1 > v2, -1 if v1 < v2
     **/
    public static int tryToCompareVersion(String v1, String v2) {
        if (Utils.isEmpty(v1) || Utils.isEmpty(v2)) return 0;
        if (v1.equals(v2)) return 0;
        try {
            String[] split1 = v1.split("\\.");
            String[] split2 = v2.split("\\.");
            /*if (split1.length == 0 || split2.length == 0) {
                return Integer.compare(Integer.parseInt(v1),Integer.parseInt(v2));
            }*/
            int s1l = split1.length;
            int[] s1 = new int[s1l];
            for (int i = 0; i < s1l; i++) {
                String split = split1[i];
                s1[i] = Integer.parseInt(split);
            }
            int s2l = split2.length;
            int[] s2 = new int[s2l];
            for (int i = 0; i < s2l; i++) {
                String split = split2[i];
                s2[i] = Integer.parseInt(split);
            }

            if (s1l > s2l) {
                int[] newS2 = new int[s1l];
                System.arraycopy(s2, 0, newS2, 0, s2l);
                s2 = newS2;
                s2l = s1l;
            } else if (s1l < s2l) {
                int[] newS1 = new int[s2l];
                System.arraycopy(s1, 0, newS1, 0, s1l);
                s1 = newS1;
                s1l = s2l;
            }

            for (int i = 0; i < s1l; i++) {
                int s11 = s1[i];
                int s22 = s2[i];
                if (s11 > s22) {
                    return 1;
                } else if (s11 < s22) {
                    return -1;
                }
            }
            return 0;

        } catch (Throwable ignore) {
            return 0;
        }
    }
}
