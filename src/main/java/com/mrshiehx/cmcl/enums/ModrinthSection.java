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

package com.mrshiehx.cmcl.enums;

import java.util.Objects;

import static com.mrshiehx.cmcl.CMCL.getString;

public enum ModrinthSection {
    MOD("mod", getString("CF_INFORMATION_MOD_NAME"), getString("CF_INFORMATION_MOD_ID"), getString("CF_BESEARCHED_MOD_ALC"), getString("CF_BESEARCHED_MOD_FUC")),
    MODPACK("modpack", getString("CF_INFORMATION_MODPACK_NAME"), getString("CF_INFORMATION_MODPACK_ID"), getString("CF_BESEARCHED_MODPACK_ALC"), getString("CF_BESEARCHED_MODPACK_FUC"));

    public final String projectType;
    public final String informationNameTip;
    public final String informationIdTip;
    public final String nameAllLowerCase;
    public final String nameFirstUpperCase;

    ModrinthSection(String projectType, String informationNameTip, String informationIdTip, String nameAllLowerCase, String nameFirstUpperCase) {
        this.projectType = projectType;
        this.informationNameTip = informationNameTip;
        this.informationIdTip = informationIdTip;
        this.nameAllLowerCase = nameAllLowerCase;
        this.nameFirstUpperCase = nameFirstUpperCase;
    }

    @Override
    public String toString() {
        return projectType;
    }

    public static ModrinthSection xvalueOf(String projectType) {
        for (ModrinthSection modrinthSection : values()) {
            if (Objects.equals(projectType, modrinthSection.projectType)) return modrinthSection;
        }
        return null;
    }
}
