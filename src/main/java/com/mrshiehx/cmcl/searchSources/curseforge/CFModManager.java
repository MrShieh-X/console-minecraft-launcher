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

package com.mrshiehx.cmcl.searchSources.curseforge;

import com.mrshiehx.cmcl.enums.CurseForgeSection;
import com.mrshiehx.cmcl.utils.Utils;

public class CFModManager extends CFManager {
    @Override
    protected CurseForgeSection getSection() {
        return CurseForgeSection.MOD;
    }

    @Override
    protected String getNameAllLowerCase() {
        return Utils.getString("CF_BESEARCHED_MOD_ALC");
    }

    @Override
    protected String getNameFirstUpperCase() {
        return Utils.getString("CF_BESEARCHED_MOD_FUC");
    }
}
