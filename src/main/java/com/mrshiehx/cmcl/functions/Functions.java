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

public class Functions {
    public static Function get(String name) {
        switch (name) {
            case "install":
                return new InstallFunction();
            case "version":
                return new VersionFunction();
            case "account":
                return new AccountFunction();
            case "config":
                return new ConfigFunction();
            case "simplify":
                return new SimplifyCommandFunction();
            case "jvmArgs":
                return new JVMArgsFunction();
            case "gameArgs":
                return new GameArgsFunction();
            case "mod":
                return new ModFunction();
            case "modpack":
                return new ModpackFunction();
        }
        return null;
    }
}
