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
 */

package com.mrshiehx.cmcl.modules.extra.fabric;

import com.mrshiehx.cmcl.modules.extra.ExtraInstaller;
import com.mrshiehx.cmcl.modules.extra.ExtraMerger;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.cmcl.version.VersionModuleUtils;
import org.json.JSONObject;

import static com.mrshiehx.cmcl.CMCL.getString;

public class FabricInstaller extends ExtraInstaller {
    @Override
    protected String getExtraName() {
        return "Fabric";
    }

    @Override
    protected ExtraMerger getExtraMerger() {
        return new FabricMerger();
    }

    @Override
    protected boolean checkInstallable(JSONObject gameJSON) {
        if (!Utils.isEmpty(VersionModuleUtils.getFabricVersion(gameJSON))) {
            System.out.println(getString("INSTALL_MODLOADER_ALREADY_INSTALL", getExtraName()));
            return false;
        }

        if (!Utils.isEmpty(VersionModuleUtils.getForgeVersion(gameJSON))) {
            System.out.println(getString("INSTALL_MODLOADER_ALREADY_INSTALL_ANOTHER_ONE", getExtraName(), "Forge"));
            return false;
        }
        if (!Utils.isEmpty(VersionModuleUtils.getLiteloaderVersion(gameJSON))) {
            System.out.println(getString("INSTALL_MODLOADER_ALREADY_INSTALL_ANOTHER_ONE", getExtraName(), "LiteLoader"));
            return false;
        }
        if (!Utils.isEmpty(VersionModuleUtils.getOptifineVersion(gameJSON))) {
            System.out.println(getString("INSTALL_MODLOADER_ALREADY_INSTALL_ANOTHER_ONE", getExtraName(), "OptiFine"));
            return false;
        }
        if (!Utils.isEmpty(VersionModuleUtils.getQuiltVersion(gameJSON))) {
            System.out.println(getString("INSTALL_MODLOADER_ALREADY_INSTALL_ANOTHER_ONE", getExtraName(), "Quilt"));
            return false;
        }
        if (!Utils.isEmpty(VersionModuleUtils.getNeoForgeVersion(gameJSON))) {
            System.out.println(getString("INSTALL_MODLOADER_ALREADY_INSTALL_ANOTHER_ONE", getExtraName(), "NeoForge"));
            return false;
        }
        return true;
    }
}
