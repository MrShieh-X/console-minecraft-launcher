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

package com.mrshiehx.cmcl.api.download;

public interface DownloadApiProvider {
    String versionManifest();//https://piston-meta.mojang.com/mc/game/version_manifest.json

    String authlibInjector();//https://authlib-injector.yushi.moe/artifact/latest.json

    String assets();//https://resources.download.minecraft.net/

    String versionClient();//https://launcher.mojang.com/

    String versionAssetsIndex();//https://piston-meta.mojang.com/

    String libraries();//https://libraries.minecraft.net/

    String versionJSON();//https://piston-meta.mojang.com/

    String fabricMeta();//https://meta.fabricmc.net/

    String fabricMaven();//https://maven.fabricmc.net/

    default String forge() {
        return "https://bmclapi2.bangbang93.com/forge/";
    }

    String forgeMaven();//https://files.minecraftforge.net/maven/ or https://maven.minecraftforge.net/

    default String thirdPartyForge() {
        return "https://bmclapi2.bangbang93.com/forge/download";
    }//https://bmclapi2.bangbang93.com/forge/download or https://download.mcbbs.net/forge/download

    String liteLoaderVersion();//http://dl.liteloader.com/versions/versions.json

    default String thirdPartyLiteLoaderDownload() {
        return "https://bmclapi2.bangbang93.com/liteloader/download";
    }//https://bmclapi2.bangbang93.com/liteloader/download or https://download.mcbbs.net/liteloader/download

    default String thirdPartyOptiFine() {
        return "https://bmclapi2.bangbang93.com/optifine/";
    }//https://bmclapi2.bangbang93.com/optifine/ or https://download.mcbbs.net/optifine/

    default String quiltMeta() {
        return "https://meta.quiltmc.org/";
    }

    default String quiltMaven() {
        return "https://maven.quiltmc.org/repository/release/";
    }
}
