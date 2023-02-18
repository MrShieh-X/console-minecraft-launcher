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
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public enum LanguageEnum {
    SIMPLIFIED_CHINESE(Collections.singleton("zh"), new SimplifiedChinese(), Locale.SIMPLIFIED_CHINESE),
    ENGLISH(Collections.singleton("en"), new English(), Locale.ENGLISH),
    CANTONESE(Collections.singleton("cantonese"), new Cantonese(), Locale.CHINA);

    public final Set<String> codes;
    public final Locale locale;
    public final Language language;


    private Map<String, String> textMap;
    private Map<String, String> helpMap;

    LanguageEnum(Set<String> code, Language language, Locale locale) {
        this.codes = code;
        this.language = language;
        this.locale = locale;
    }

    public Map<String, String> getTextMap() {
        if (textMap == null) {
            textMap = language.getTextMap();
        }
        return textMap;
    }

    public Map<String, String> getHelpMap() {
        if (helpMap == null) {
            helpMap = language.getHelpMap();
        }
        return helpMap;
    }

    public static LanguageEnum overriddenValueOf(String code) {
        for (LanguageEnum languageEnum : values()) {
            if (languageEnum.codes.contains(code))
                return languageEnum;
        }
        return ENGLISH;
    }
}
