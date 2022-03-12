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
package com.mrshiehx.cmcl.constants;

import java.io.File;

public class Constants {
    public static final File configFile = new File("cmcl.json");
    public static final String CMCL_VERSION = "1.0";
    public static final String CLIENT_ID = "288ec5dd-6736-4d4b-9b96-30e083a8cad2";
    public static final String COPYRIGHT = "Copyright (C) 2021-2022  MrShiehX";
    public static final int INDENT_FACTOR = 2;//JsonObject转String的间隔
}
