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
 */

package com.mrshiehx.cmcl.modules.extra;

import com.mrshiehx.cmcl.bean.Pair;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.isEmpty;

public interface ExtraMerger {
    Pair<Boolean, List<JSONObject>> merge(String minecraftVersion, JSONObject headJSONObject, File jarFile, boolean askContinue, @Nullable String extraVersion);

    public static <V> String selectExtraVersion(String text, Map<String, V> extras, String defaultVersion, String extraName) {
        System.out.print(text);//legal
        Scanner scanner = new Scanner(System.in);
        try {
            String s = scanner.nextLine();
            if (!isEmpty(s)) {
                V versionObject = extras.get(s);
                if (versionObject != null) return s;
                return selectExtraVersion(getString("INSTALL_MODLOADER_SELECT_NOT_FOUND", s, extraName, defaultVersion), extras, defaultVersion, extraName);
            } else {
                return defaultVersion;
            }
        } catch (NoSuchElementException ignore) {
            return null;
        }
    }
}
