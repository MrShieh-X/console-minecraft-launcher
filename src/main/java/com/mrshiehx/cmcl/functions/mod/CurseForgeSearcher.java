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
 *
 */
package com.mrshiehx.cmcl.functions.mod;

import com.mrshiehx.cmcl.enums.CurseForgeSection;
import com.mrshiehx.cmcl.modSources.curseforge.CurseForgeManager;
import org.json.JSONObject;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.isEmpty;

public class CurseForgeSearcher {
    public static JSONObject search(CurseForgeManager cf, String modNameInput, String modIdInput, int limit) {
        JSONObject mod;
        if (!isEmpty(modNameInput)) {
            mod = cf.search(modNameInput, limit < 0 ? 50 : limit);
        } else {
            String alc = cf.getSection().nameAllLowerCase;
            try {
                mod = cf.getByID(modIdInput);
            } catch (CurseForgeManager.NotMinecraftAddon e) {
                System.out.println(getString("CF_GET_BY_ID_NOT_OF_MC", e.gameId).replace("${NAME}", alc));
                return null;
            } catch (CurseForgeManager.IncorrectCategoryAddon e) {
                CurseForgeSection target = CurseForgeSection.valueOf(e.gameCategoryId);
                if (target == null) {
                    System.out.println(getString("CF_GET_BY_ID_INCORRECT_CATEGORY", e.gameCategoryId).replace("${NAME}", alc));
                } else {
                    System.out.println(getString("CF_GET_BY_ID_INCORRECT_CATEGORY_DETAIL").replace("${NAME}", alc).replace("${TARGET}", target.nameAllLowerCase));
                }
                return null;
            } catch (Exception e) {
                System.out.println(getString("CF_GET_BY_ID_FAILED", e).replace("${NAME}", alc));
                return null;
            }
        }
        return mod;
    }
}
