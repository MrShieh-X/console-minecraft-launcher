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
package com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.authlib;

import com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.YggdrasilAuthenticationApiProvider;
import com.mrshiehx.cmcl.utils.internet.NetworkUtils;

public class AuthlibInjectorApiProvider implements YggdrasilAuthenticationApiProvider {
    private final String main;

    public AuthlibInjectorApiProvider(String url) {
        url = NetworkUtils.addHttpsIfMissing(url);
        this.main = NetworkUtils.addSlashIfMissing(url);
    }

    @Override
    public String getAuthenticationURL() {
        return main + "authserver/authenticate";
    }

    @Override
    public String getRefreshmentURL() {
        return main + "authserver/refresh";
    }

    @Override
    public String getValidationURL() {
        return main + "authserver/validate";
    }

    @Override
    public String getSkinUploadURL(String uuid) throws UnsupportedOperationException {
        return main + "api/user/profile/" + uuid.replace("-", "") + "/skin";
    }

    @Override
    public String getProfilePropertiesURL(String uuid) {
        return main + "sessionserver/session/minecraft/profile/" + uuid.replace("-", "");
    }

    public String toString() {
        return main;
    }
}
