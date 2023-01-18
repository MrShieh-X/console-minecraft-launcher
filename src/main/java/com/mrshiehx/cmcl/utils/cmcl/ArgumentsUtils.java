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

package com.mrshiehx.cmcl.utils.cmcl;

import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ArgumentsUtils {
    public static List<String> parseJVMArgs(JSONArray jvmArgs) {
        List<Object> objs;
        List<String> result = new LinkedList<>();
        if (jvmArgs == null) return result;
        objs = jvmArgs.toList();

        for (Object obj : objs) {
            String v = Utils.valueOf(obj);
            if (!Utils.isEmpty(v)) {
                if (!v.contains("-Dminecraft.launcher.brand") && !v.contains("-Dminecraft.launcher.version") && !result.contains(v)) {
                    result.add(v);
                }
            }
        }
        return result;
    }

    public static List<String> parseJVMArgs(String[] jvmArgs) {
        List<String> result = new LinkedList<>();
        if (jvmArgs == null || jvmArgs.length == 0)
            return result;
        for (String v : jvmArgs) {
            if (!Utils.isEmpty(v)) {
                if (!v.contains("-Dminecraft.launcher.brand") && !v.contains("-Dminecraft.launcher.version") && !result.contains(v)) {
                    result.add(v);
                }
            }
        }
        return result;
    }

    public static Map<String, String> parseGameArgs(JSONObject gameArgs) {
        Map<String, Object> map;
        Map<String, String> result = new LinkedHashMap<>();
        if (gameArgs == null) {
            return result;
        } else {
            map = gameArgs.toMap();
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String va = Utils.valueOf(entry.getValue());
            String key = entry.getKey();
            if (!Utils.isEmpty(key) && !entry.getKey().equals("version") && !entry.getKey().equals("versionType") && !result.containsKey(key)) {
                result.put(key, va);
            }
        }
        return result;
    }
}
