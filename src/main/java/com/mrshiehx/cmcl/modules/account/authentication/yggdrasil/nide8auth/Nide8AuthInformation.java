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
package com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.nide8auth;

import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONObject;

public class Nide8AuthInformation {
    public final String serverId;
    public final String serverName;

    public Nide8AuthInformation(String serverId, String serverName) {
        this.serverId = serverId;
        this.serverName = serverName;
    }

    public static Nide8AuthInformation valueOf(JSONObject account) {
        if (account.optInt("loginMethod") == 3 && !Utils.isEmpty(account.optString("serverId"))) {
            return new Nide8AuthInformation(account.optString("serverId"), account.optString("serverName"));
        }
        return null;
    }
}