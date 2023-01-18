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
package com.mrshiehx.cmcl.bean.arguments;

import java.util.Objects;

public abstract class Argument {
    public final String originString;
    public final String[] originArray;//按照引号外空格划分，所以每一项内都允许有空格；不可为空，至少有一项
    public final String key;

    protected Argument(String originString, String[] originArray, String key) {
        this.originString = originString;
        this.originArray = originArray;
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Argument argument = (Argument) o;
        return Objects.equals(key, argument.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    public boolean equals(String key) {
        return key.equals(this.key);
    }

}
