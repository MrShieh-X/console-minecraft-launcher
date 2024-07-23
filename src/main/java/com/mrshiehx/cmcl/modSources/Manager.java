/*
 * Console Minecraft Launcher
 * Copyright (C) 2021-2024  MrShiehX
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
package com.mrshiehx.cmcl.modSources;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

public abstract class Manager<Section> {
    public abstract Section getSection();

    public abstract String getDownloadLink(String modId, String modName, @Nullable String mcversion, @Nullable String addonVersion, boolean isModpack, DependencyInstaller dependencyInstaller);

    protected abstract String getNameAllLowerCase();

    public abstract JSONObject search(String searchContent, int limit);

    public interface DependencyInstaller {
        void install(String mcVersion, String name, String id);
    }
}
