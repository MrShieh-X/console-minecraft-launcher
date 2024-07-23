/*
 * Console Minecraft Launcher
 * Copyright (C) 2021-2024  MrShiehX
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

import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

public class VersionModuleUtils {
    public static String getModuleVersion(JSONObject head, String mainClass, String libraryFirstAndSecond) {
        if (!Utils.isEmpty(mainClass) && !head.optString("mainClass").equals(mainClass)) return null;
        JSONArray libraries = head.optJSONArray("libraries");
        if (libraries == null || libraries.length() == 0) return null;
        for (Object o : libraries) {
            if (o instanceof JSONObject) {
                JSONObject library = (JSONObject) o;
                String name = library.optString("name");

                if (name.startsWith(libraryFirstAndSecond) && name.length() > libraryFirstAndSecond.length()) {
                    return name.substring(libraryFirstAndSecond.length());
                }
            }
        }
        return null;
    }

    public static String getModuleVersion(JSONObject head, String moduleName) {
        JSONObject module = head.optJSONObject(moduleName);
        if (module != null) {
            String version = module.optString("version");
            if (!Utils.isEmpty(version)) return version;
        }

        //兼容HMCL
        JSONArray patches = head.optJSONArray("patches");
        if (patches != null && patches.length() > 0) {
            for (Object o : patches) {
                if (o instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) o;
                    if (jsonObject.optString("id").equals(moduleName)) {
                        String version = jsonObject.optString("version");
                        if (!Utils.isEmpty(version)) return version;
                    }
                }
            }
        }
        return null;
    }

    public static String getFabricVersion(JSONObject head) {
        String first = getModuleVersion(head, "fabric");
        if (!Utils.isEmpty(first)) return first;
        return getModuleVersion(head, "net.fabricmc.loader.impl.launch.knot.KnotClient", "net.fabricmc:fabric-loader:");
    }

    public static String getLiteloaderVersion(JSONObject head) {
        String first = getModuleVersion(head, "liteloader");
        if (!Utils.isEmpty(first)) return first;
        return getModuleVersion(head, null, "com.mumfrey:liteloader:");
    }

    public static String getForgeVersion(JSONObject head) {
        String first = getModuleVersion(head, "forge");
        if (!Utils.isEmpty(first)) return first;
        JSONObject arguments = head.optJSONObject("arguments");
        if (arguments != null) {
            JSONArray game = arguments.optJSONArray("game");
            if (game != null) {
                int indexOf = game.toList().indexOf("--fml.forgeVersion");
                if (indexOf >= 0 && indexOf + 1 < game.length())
                    return game.optString(indexOf + 1);
            }
        }
        String version = null;
        String second = getModuleVersion(head, null, "net.minecraftforge:forge:");
        if (Utils.isEmpty(second)) {
            second = getModuleVersion(head, null, "net.minecraftforge:fmlloader:");
        }
        if (!Utils.isEmpty(second)) {
            String[] split = second.split("-");
            if (split.length >= 2) {
                version = split[1];
            }
        }
        return version;
    }

    public static String getOptifineVersion(JSONObject head) {
        String first = getModuleVersion(head, "optifine");
        if (!Utils.isEmpty(first)) return first;
        String version = null;
        String origin = getModuleVersion(head, null, "optifine:OptiFine:");
        if (!Utils.isEmpty(origin)) {
            int indexOf = origin.indexOf('_');
            version = origin.substring(indexOf + 1);
        }
        return version;
    }

    public static String getQuiltVersion(JSONObject head) {
        String first = getModuleVersion(head, "quilt");
        if (!Utils.isEmpty(first)) return first;
        return getModuleVersion(head, "org.quiltmc.loader.impl.launch.knot.KnotClient", "org.quiltmc:quilt-loader:");
    }

    public static String getNeoForgeVersion(JSONObject head) {
        String first = getModuleVersion(head, "neoforge");
        if (!Utils.isEmpty(first)) return first;
        JSONObject arguments = head.optJSONObject("arguments");
        if (arguments != null) {
            JSONArray game = arguments.optJSONArray("game");
            if (game != null) {
                int indexOf = game.toList().indexOf("--fml.neoForgeVersion");
                if (indexOf >= 0 && indexOf + 1 < game.length())
                    return game.optString(indexOf + 1);
            }
        }
        return null;
    }
}
