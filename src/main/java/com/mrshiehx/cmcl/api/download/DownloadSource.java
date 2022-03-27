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

import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONObject;

public class DownloadSource {
    private static final DefaultApiProvider defaultApiProvider = new DefaultApiProvider();
    private static final BMCLApiProvider bmclApiProvider = new BMCLApiProvider();
    private static final MCBBSApiProvider mcbbsApiProvider = new MCBBSApiProvider();

    public static DownloadApiProvider getProvider() {
        return getProvider(Utils.getConfig());
    }

    public static DownloadApiProvider getProvider(JSONObject config) {
        int s = config.optInt("downloadSource");
        if (s == 1) {
            return bmclApiProvider;
        } else if (s == 2) {
            return mcbbsApiProvider;
        } else {
            return defaultApiProvider;
        }
    }
}
