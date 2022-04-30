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
package com.mrshiehx.cmcl.options;

import java.util.HashMap;
import java.util.Map;

public class Options {
    public static final Map<String, Option> MAP = new HashMap<>();

    static {
        MAP.put("usage", new HelpOption());
        MAP.put("help", new HelpOption());
        MAP.put("u", new HelpOption());
        MAP.put("h", new HelpOption());

        MAP.put("a", new AboutOption());
        MAP.put("l", new ListOption());
        MAP.put("p", new PrintOption());
        MAP.put("b", new StartOption());
        MAP.put("s", new SelectOption());
        MAP.put("i", new ImmersiveModeOption());

        MAP.put("select", new SelectOption());
        MAP.put("start", new StartOption());
        MAP.put("print", new PrintOption());
        MAP.put("about", new AboutOption());
        MAP.put("ls", new ListOption());
        MAP.put("list", new ListOption());

        MAP.put("config", new ConfigOption());
        MAP.put("account", new AccountOption());
        MAP.put("version", new VersionOption());
        MAP.put("install", new InstallOption());
        MAP.put("jvmArgs", new JVMArgsOption());
        MAP.put("gameArgs", new GameArgsOption());
        MAP.put("mod", new ModOption());
        MAP.put("modpack", new ModpackOption());
        MAP.put("immersive", new ImmersiveModeOption());


        Map<String, Option> ne = new HashMap<>(MAP);
        MAP.clear();
        for (Map.Entry<String, Option> entry : ne.entrySet()) {
            MAP.put(entry.getKey().toLowerCase(), entry.getValue());
        }
    }
}
