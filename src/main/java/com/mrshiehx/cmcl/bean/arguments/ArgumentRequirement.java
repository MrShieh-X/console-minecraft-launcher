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

public class ArgumentRequirement {
    public final Class<? extends Argument> clazz;
    public final String key;

    private ArgumentRequirement(Class<? extends Argument> clazz, String key) {
        this.clazz = clazz;
        this.key = key;
    }

    public static ArgumentRequirement ofSingle(String key) {
        return new ArgumentRequirement(SingleArgument.class, key);
    }

    public static ArgumentRequirement ofValue(String key) {
        return new ArgumentRequirement(ValueArgument.class, key);
    }
}
