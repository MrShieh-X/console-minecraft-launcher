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

package com.mrshiehx.cmcl.utils.json;

import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JSONUtils {
    /**
     * 解析 JSONObject
     *
     * @return 如果jsonObjectString是一个JSONObject，就返回被解析的JSONObject，否则返回null
     **/
    public static JSONObject parseJSONObject(String jsonObjectString) {
        if (Utils.isEmpty(jsonObjectString)) return null;
        try {
            return new JSONObject(jsonObjectString);
        } catch (Throwable ignored) {
        }
        return null;
    }

    /**
     * 解析 JSONArray
     *
     * @return 如果jsonObjectString是一个JSONObject，就返回被解析的JSONObject，否则返回null
     **/
    public static JSONArray parseJSONArray(String jsonArrayString) {
        if (Utils.isEmpty(jsonArrayString)) return null;
        try {
            return new JSONArray(jsonArrayString);
        } catch (Throwable ignored) {
        }
        return null;
    }

    /**
     * 对返回值操作将不会影响到原 JSONArray
     **/
    public static List<JSONObject> jsonArrayToJSONObjectList(JSONArray jsonArray) {
        return jsonArrayToJSONObjectList(jsonArray, null);
    }

    /**
     * 对返回值操作将不会影响到原 JSONArray
     **/
    public static List<JSONObject> jsonArrayToJSONObjectList(JSONArray jsonArray, Predicate<JSONObject> filter) {
        if (jsonArray == null || jsonArray.length() == 0)
            return new LinkedList<>();
        Stream<JSONObject> stream = Utils.iteratorToStream(jsonArray.iterator()).filter(x -> x instanceof JSONObject).map(x -> (JSONObject) x);
        if (filter != null) {
            stream = stream.filter(filter);
        }
        return stream.collect(Collectors.toList());
    }
}
