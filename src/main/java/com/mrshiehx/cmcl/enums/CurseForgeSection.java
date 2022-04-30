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

package com.mrshiehx.cmcl.enums;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;

public enum CurseForgeSection {
    MOD(6, getString("CF_INFORMATION_MOD_NAME"), getString("CF_INFORMATION_MOD_ID")),
    MODPACK(4471, getString("CF_INFORMATION_MODPACK_NAME"), getString("CF_INFORMATION_MODPACK_ID"));

    public final int sectionId;
    public final String informationNameTip;
    public final String informationIdTip;

    CurseForgeSection(int sectionId, String informationNameTip, String informationIdTip) {
        this.sectionId = sectionId;
        this.informationNameTip = informationNameTip;
        this.informationIdTip = informationIdTip;
    }

    @Override
    public String toString() {
        return String.valueOf(sectionId);
    }
}
