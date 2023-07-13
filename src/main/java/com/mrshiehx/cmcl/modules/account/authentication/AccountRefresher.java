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

package com.mrshiehx.cmcl.modules.account.authentication;

import com.mrshiehx.cmcl.exceptions.ExceptionWithDescription;
import com.mrshiehx.cmcl.modules.account.authentication.microsoft.MicrosoftAuthentication;
import com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.authlib.AuthlibInjectorAuthentication;
import com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.nide8auth.Nide8AuthAuthentication;
import org.json.JSONArray;
import org.json.JSONObject;

public class AccountRefresher {
    /**
     * Refresh account
     *
     * @return whether the account has been modified
     **/
    public static boolean execute(JSONObject selectedAccount, JSONArray accounts) throws ExceptionWithDescription {
        int loginMethod = selectedAccount.optInt("loginMethod");
        if (loginMethod == 1) {
            return AuthlibInjectorAuthentication.refresh(selectedAccount, accounts);
        } else if (loginMethod == 2) {
            return MicrosoftAuthentication.refresh(selectedAccount, accounts);
        } else if (loginMethod == 3) {
            return Nide8AuthAuthentication.refresh(selectedAccount, accounts);
        } else return false;
    }
}
