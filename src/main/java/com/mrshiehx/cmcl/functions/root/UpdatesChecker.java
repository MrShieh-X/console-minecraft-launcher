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

package com.mrshiehx.cmcl.functions.root;

import com.mrshiehx.cmcl.CMCL;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.internet.DownloadUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

public class UpdatesChecker {
    public static void execute() {
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
        int latestVersionCode = newVersion.optInt("latestVersionCode");
        if (latestVersionCode > Constants.CMCL_VERSION_CODE) {
            String latestVersionName = newVersion.optString("latestVersionName");
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
            System.out.println(Utils.getString("MESSAGE_NEW_VERSION", latestVersionName, updateDate, urls.toString(), CMCL.getLanguage().locale == Locale.CHINA ? updateContentZh : updateContentEn));
        } else {
            System.out.println(Utils.getString("MESSAGE_CURRENT_IS_LATEST_VERSION"));
        }
    }
}
