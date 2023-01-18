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
package com.mrshiehx.cmcl.constants.languages;

import com.mrshiehx.cmcl.constants.languages.chinese.Cantonese;
import com.mrshiehx.cmcl.constants.languages.chinese.SimplifiedChinese;

import java.util.Collections;
import java.util.Map;

public class Languages {
    private static Map<String, String> zhUnmodifiable;
    private static Map<String, String> enUnmodifiable;
    private static Map<String, String> cantoneseUnmodifiable;
    private static Map<String, String> zhHelpUnmodifiable;
    private static Map<String, String> enHelpUnmodifiable;
    private static Map<String, String> cantoneseHelpUnmodifiable;

    public static Map<String, String> getZh() {
        if (zhUnmodifiable == null) {
            zhUnmodifiable = Collections.unmodifiableMap(new SimplifiedChinese().getTextMap());
        }
        return zhUnmodifiable;
    }

    public static Map<String, String> getEn() {
        if (enUnmodifiable == null) {
            enUnmodifiable = Collections.unmodifiableMap(new English().getTextMap());
        }
        return enUnmodifiable;
    }

    public static Map<String, String> getCantonese() {
        if (cantoneseUnmodifiable == null) {
            cantoneseUnmodifiable = Collections.unmodifiableMap(new Cantonese().getTextMap());
        }
        return cantoneseUnmodifiable;
    }

    public static Map<String, String> getEnHelp() {
        if (enHelpUnmodifiable == null) {
            enHelpUnmodifiable = Collections.unmodifiableMap(new English().getHelpMap());
        }
        return enHelpUnmodifiable;
    }

    public static Map<String, String> getZhHelp() {
        if (zhHelpUnmodifiable == null) {
            zhHelpUnmodifiable = Collections.unmodifiableMap(new SimplifiedChinese().getHelpMap());
        }
        return zhHelpUnmodifiable;
    }

    public static Map<String, String> getCantoneseHelp() {
        if (cantoneseHelpUnmodifiable == null) {
            cantoneseHelpUnmodifiable = Collections.unmodifiableMap(new SimplifiedChinese().getHelpMap());
        }
        return cantoneseHelpUnmodifiable;
    }
}
