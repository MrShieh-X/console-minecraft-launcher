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

import com.mrshiehx.cmcl.enums.ModrinthSection;
import com.mrshiehx.cmcl.modSources.modrinth.ModrinthManager;
import org.json.JSONObject;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.isEmpty;

public class ModrinthSearcher {
    public static Result search(ModrinthManager mr, String modNameInput, String modIdInput, int limit) {
        JSONObject mod, modByID;
        String modName, modID;
        if (!isEmpty(modNameInput)) {
            mod = mr.search(modNameInput, limit < 0 ? 50 : limit);
            if (mod == null)
                return null;
            modName = mod.optString("title");
            modID = mod.optString("project_id");

            try {
                modByID = mr.getByID(modID);
            } catch (Exception ignore) {
                modByID = null;
            }
        } else {
            try {
                modByID = mr.getByID(modIdInput);
                modName = modByID.optString("title");
                modID = modByID.optString("id");
                mod = null;
            } catch (ModrinthManager.IncorrectCategoryAddon e) {
                ModrinthSection target = e.section;
                System.out.println(getString("CF_GET_BY_ID_INCORRECT_CATEGORY_DETAIL").replace("${NAME}", mr.getSection().nameAllLowerCase).replace("${TARGET}", target != null ? target.nameAllLowerCase : "null"));
                return null;
            } catch (Exception e) {
                System.out.println(getString("CF_GET_BY_ID_FAILED", e).replace("${NAME}", mr.getSection().nameAllLowerCase));
                return null;
            }
        }
        return new Result(mod, modByID, modName, modID);
    }

    public static class Result {
        public final JSONObject mod, modByID;
        public final String modName, modID;

        public Result(JSONObject mod, JSONObject modByID, String modName, String modID) {
            this.mod = mod;
            this.modByID = modByID;
            this.modName = modName;
            this.modID = modID;
        }
    }
}
