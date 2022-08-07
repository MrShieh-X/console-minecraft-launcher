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
 *
 */

package com.mrshiehx.cmcl.options;

import com.mrshiehx.cmcl.ConsoleMinecraftLauncher;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.utils.DownloadUtils;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

public class CheckForUpdatesOption implements Option {
    @Override
    public void execute(Arguments arguments) {
        JSONObject newVersion = null;
        for (String url : Constants.CHECK_FOR_UPDATES_INFORMATION_URLS) {
            try {
                newVersion = new JSONObject(new String(DownloadUtils.downloadBytes(url)));
                break;
            } catch (Exception ignored) {
            }
        }
        if (newVersion == null) {
            System.out.println(Utils.getString("MESSAGE_FAILED_TO_CHECK_FOR_UPDATES"));
            return;
        }
        String latestVersionName = newVersion.optString("latestVersionName");
        if (!Objects.equals(latestVersionName, Constants.CMCL_VERSION)) {
            String updateDate = newVersion.optString("updateDate");
            String updateContentEn = newVersion.optString("updateContentEn");
            String updateContentZh = newVersion.optString("updateContentZh");
            StringBuilder urls = new StringBuilder();
            JSONArray latestVersionDownloadUrls = newVersion.optJSONArray("latestVersionDownloadUrls");
            for (Object o : latestVersionDownloadUrls) {
                if (o instanceof String) {
                    urls.append("  ").append((String) o).append('\n');
                }
            }
            System.out.println(Utils.getString("MESSAGE_NEW_VERSION", latestVersionName, updateDate, urls.toString(), ConsoleMinecraftLauncher.getLanguage().equalsIgnoreCase("zh") ? updateContentZh : updateContentEn));
        } else {
            System.out.println(Utils.getString("MESSAGE_CURRENT_IS_LATEST_VERSION"));
        }
    }

    @Override
    public String getUsageName() {
        return null;
    }
}
