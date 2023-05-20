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

package com.mrshiehx.cmcl.api.download;

import com.mrshiehx.cmcl.CMCL;
import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONObject;

import java.util.*;

public class DownloadSource {
    private static final DefaultApiProvider defaultApiProvider = new DefaultApiProvider();
    private static final BMCLApiProvider bmclApiProvider = new BMCLApiProvider();
    private static final MCBBSApiProvider mcbbsApiProvider = new MCBBSApiProvider();

    public static DownloadApiProvider getProvider() {
        return getProvider(Utils.getConfig());
    }

    public static DownloadApiProvider getProvider(JSONObject config) {
        if (!config.has("downloadSource")) {
            List<Pair<String, Integer>> sources = new ArrayList<>(3);
            sources.add(0, new Pair<>(Utils.getString("DOWNLOAD_SOURCE_OFFICIAL"), 0));
            sources.add(1, new Pair<>(Utils.getString("DOWNLOAD_SOURCE_BMCLAPI"), 1));
            sources.add(2, new Pair<>(Utils.getString("DOWNLOAD_SOURCE_MCBBS"), 2));
            for (Pair<String, Integer> pair : sources) {
                System.out.printf("[%d]%s\n", pair.getValue(), pair.getKey());
            }
            int defaultDownloadSource = CMCL.getLanguage().locale == Locale.CHINA ? 2 : 0;
            int value = defaultDownloadSource;
            System.out.print(Utils.getString("MESSAGE_SELECT_DOWNLOAD_SOURCE", defaultDownloadSource));
            try {
                value = Integer.parseInt(new Scanner(System.in).nextLine());
            } catch (NumberFormatException | NoSuchElementException ignore) {
            }
            config.put("downloadSource", value);
            Utils.saveConfig(config);
        }
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
