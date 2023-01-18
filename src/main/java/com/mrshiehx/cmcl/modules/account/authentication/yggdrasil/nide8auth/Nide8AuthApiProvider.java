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
package com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.nide8auth;

import com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.YggdrasilAuthenticationApiProvider;

public class Nide8AuthApiProvider implements YggdrasilAuthenticationApiProvider {
    private final String baseUrl;

    public Nide8AuthApiProvider(String serverId) {
        this.baseUrl = "https://auth.mc-user.com:233/" + serverId + "/";
    }

    @Override
    public String getAuthenticationURL() {
        return baseUrl + "authserver/authenticate";
    }

    @Override
    public String getRefreshmentURL() {
        return baseUrl + "authserver/refresh";
    }

    @Override
    public String getValidationURL() {
        return baseUrl + "authserver/validate";
    }

    @Override
    public String getSkinUploadURL(String uuid) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getProfilePropertiesURL(String uuid) {
        return baseUrl + "sessionserver/session/minecraft/profile/" + uuid.replace("-", "");
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
