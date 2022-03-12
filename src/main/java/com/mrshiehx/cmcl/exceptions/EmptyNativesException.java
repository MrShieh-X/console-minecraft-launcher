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

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;

public class EmptyNativesException extends LaunchException {
    public final JSONArray libraries;

    public EmptyNativesException(@Nullable JSONArray libraries) {
        super(getString("EXCEPTION_NATIVE_LIBRARIES_NOT_FOUND"));
        this.libraries = libraries;
    }
}
