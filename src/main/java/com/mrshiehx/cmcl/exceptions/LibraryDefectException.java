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
 */
package com.mrshiehx.cmcl.exceptions;

import com.mrshiehx.cmcl.bean.Library;

import java.util.List;

public class LibraryDefectException extends LaunchException {
    public final List<Library> list;

    public LibraryDefectException(List<Library> list) {
        super(String.format(list.size() == 1 ? "the library file is not found: %s" : "library files below are not found:\n%s", toS(list)));
        this.list = list;
    }

    private static String toS(List<Library> list) {
        if (list.size() == 1) {
            return (list.get(0).libraryJSONObject.optString("name"));
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            Library library = list.get(i);
            stringBuilder.append("       ").append(library.libraryJSONObject.optString("name"));
            if (i + 1 != list.size()) {
                stringBuilder.append('\n');
            }
        }
        return stringBuilder.toString();
    }
}
