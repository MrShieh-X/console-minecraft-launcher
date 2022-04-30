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

public class MCBBSApiProvider implements DownloadApiProvider {
    @Override
    public String versionManifest() {
        return "https://download.mcbbs.net/mc/game/version_manifest.json";
    }

    @Override
    public String versionClient() {
        return "https://download.mcbbs.net/";
    }

    @Override
    public String versionAssetsIndex() {
        return "https://download.mcbbs.net/";
    }

    @Override
    public String assets() {
        return "https://download.mcbbs.net/assets/";
    }

    @Override
    public String libraries() {
        return "https://download.mcbbs.net/maven/";
    }

    @Override
    public String versionJSON() {
        return "https://download.mcbbs.net/";
    }

    @Override
    public String authlibInjector() {
        return "https://download.mcbbs.net/mirrors/authlib-injector/artifact/latest.json";
    }

    @Override
    public String fabricMeta() {
        return "https://download.mcbbs.net/fabric-meta/";
    }

    @Override
    public String fabricMaven() {
        return "https://download.mcbbs.net/maven/";
    }

    @Override
    public String forge() {
        return "https://download.mcbbs.net/forge/";
    }

    @Override
    public String forgeMaven() {
        return "https://download.mcbbs.net/maven/";
    }

    @Override
    public String thirdPartyForge() {
        return "https://download.mcbbs.net/forge/download";
    }
}
