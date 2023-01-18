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
package com.mrshiehx.cmcl.functions;

import com.mrshiehx.cmcl.functions.mod.ModFunction;
import com.mrshiehx.cmcl.functions.mod.ModpackFunction;

import java.util.HashMap;
import java.util.Map;

public class Functions {
    public static final Map<String, Function> MAP = new HashMap<>();

    static {
        MAP.put("config", new ConfigFunction());
        MAP.put("account", new AccountFunction());
        MAP.put("version", new VersionFunction());
        MAP.put("jvmArgs", new JVMArgsFunction());
        MAP.put("gameArgs", new GameArgsFunction());
        MAP.put("install", new InstallFunction());
        MAP.put("mod", new ModFunction());
        MAP.put("modpack", new ModpackFunction());
    }
}
