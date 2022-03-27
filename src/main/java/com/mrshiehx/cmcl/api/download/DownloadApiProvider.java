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
    String versionManifest();//https://launchermeta.mojang.com/mc/game/version_manifest.json

    String authlibInjector();//https://authlib-injector.yushi.moe/artifact/latest.json

    String assets();//https://resources.download.minecraft.net/

    String versionClient();//https://launcher.mojang.com/

    String versionAssetsIndex();//https://launchermeta.mojang.com/

    String libraries();//https://libraries.minecraft.net/

    String versionJSON();//https://launchermeta.mojang.com/


    String fabricMeta();//https://meta.fabricmc.net/

    String fabricMaven();//https://maven.fabricmc.net/

}
