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

package com.mrshiehx.cmcl.api.download;

public class DefaultApiProvider implements DownloadApiProvider {
    @Override
    public String versionManifest() {
        return "https://piston-meta.mojang.com/mc/game/version_manifest.json";
    }

    @Override
    public String versionClient() {
        return "https://launcher.mojang.com/";
    }

    @Override
    public String versionAssetsIndex() {
        return "https://piston-meta.mojang.com/";
    }

    @Override
    public String assets() {
        return "https://resources.download.minecraft.net/";
    }

    @Override
    public String libraries() {
        return "https://libraries.minecraft.net/";
    }

    @Override
    public String versionJSON() {
        return "https://piston-meta.mojang.com/";
    }

    @Override
    public String authlibInjectorFile() {
        return "https://authlib-injector.yushi.moe/artifact/latest.json";
    }

    @Override
    public String fabricMeta() {
        return "https://meta.fabricmc.net/";
    }

    @Override
    public String fabricMaven() {
        return "https://maven.fabricmc.net/";
    }

    @Override
    public String forgeMaven() {
        return "https://files.minecraftforge.net/maven/";
    }

    @Override
    public String liteLoaderVersion() {
        return "http://dl.liteloader.com/versions/versions.json";
    }
}
