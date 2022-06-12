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

package com.mrshiehx.cmcl.modules.extra.liteloader;

import com.mrshiehx.cmcl.modules.extra.ExtraInstaller;
import com.mrshiehx.cmcl.modules.extra.ExtraMerger;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONObject;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;

public class LiteloaderInstaller extends ExtraInstaller {
    @Override
    protected String getExtraName() {
        return "LiteLoader";
    }

    @Override
    protected ExtraMerger getExtraMerger() {
        return new LiteloaderMerger();
    }

    @Override
    protected boolean checkInstalled(JSONObject gameJSON) {
        if (!Utils.isEmpty(Utils.getLiteloaderVersion(gameJSON))) {
            System.out.println(getString("INSTALL_MODLOADER_ALREADY_INSTALL", getExtraName()));
            return false;
        }

        if (!Utils.isEmpty(Utils.getFabricVersion(gameJSON))) {
            System.out.println(getString("INSTALL_MODLOADER_ALREADY_INSTALL_ANOTHER_ONE", getExtraName(), "Fabric"));
            return false;
        }
        return true;
    }
}
