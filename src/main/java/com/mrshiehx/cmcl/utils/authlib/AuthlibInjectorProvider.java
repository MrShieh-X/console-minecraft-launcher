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
package com.mrshiehx.cmcl.utils.authlib;

import com.mrshiehx.cmcl.utils.Utils;

public class AuthlibInjectorProvider {
    private final String main;

    public AuthlibInjectorProvider(String url) {
        url = AuthlibUtils.addHttpsIfMissing(url);
        if (!url.endsWith("/"))
            url += "/";
        this.main = url;
    }

    public String getAuthenticationURL() {
        return Utils.addSlashIfMissing(main) + "authserver/authenticate";
    }

    public String getRefreshmentURL() {
        return Utils.addSlashIfMissing(main) + "authserver/refresh";
    }

    public String getValidationURL() {
        return Utils.addSlashIfMissing(main) + "authserver/validate";
    }

    public String getInvalidationURL() {
        return Utils.addSlashIfMissing(main) + "authserver/invalidate";
    }

    public String getSkinUploadURL(String uuid) throws UnsupportedOperationException {
        return Utils.addSlashIfMissing(main) + "api/user/profile/" + uuid/*.toString().replace("-", "")*/ + "/skin";
    }

    public String getProfilePropertiesURL(String uuid) {
        return Utils.addSlashIfMissing(main) + "sessionserver/session/minecraft/profile/" + uuid./*toString().*/replace("-", "");
    }

    public String toString() {
        return main;
    }
}
