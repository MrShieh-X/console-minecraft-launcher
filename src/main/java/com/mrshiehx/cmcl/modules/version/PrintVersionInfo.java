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
package com.mrshiehx.cmcl.modules.version;

import com.mrshiehx.cmcl.CMCL;
import com.mrshiehx.cmcl.bean.GameVersion;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.cmcl.version.VersionModuleUtils;
import com.mrshiehx.cmcl.utils.cmcl.version.VersionUtils;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.isEmpty;

public class PrintVersionInfo {
    public static void execute(File jsonFile, File jarFile, File versionDir, String versionName) {
        try {
            JSONObject head = new JSONObject(FileUtils.readFileContent(jsonFile));
            Map<String, String> information = new LinkedHashMap<>();


            GameVersion gameVersion = VersionUtils.getGameVersion(head, jarFile);
            if (!Utils.isEmpty(gameVersion.id) && Utils.isEmpty(gameVersion.name)) {
                information.put(getString("VERSION_INFORMATION_GAME_VERSION"), gameVersion.id);
            } else if (Utils.isEmpty(gameVersion.id) && !Utils.isEmpty(gameVersion.name)) {
                information.put(getString("VERSION_INFORMATION_GAME_VERSION"), gameVersion.name);
            } else if (!Utils.isEmpty(gameVersion.id) && !Utils.isEmpty(gameVersion.name)) {
                if (gameVersion.name.equals(gameVersion.id)) {
                    information.put(getString("VERSION_INFORMATION_GAME_VERSION"), gameVersion.name);
                } else {
                    information.put(getString("VERSION_INFORMATION_GAME_VERSION"), gameVersion.name + " (" + gameVersion.id + ")");
                }
            } else {
                information.put(getString("VERSION_INFORMATION_GAME_VERSION"), getString("VERSION_INFORMATION_GAME_VERSION_FAILED_GET"));
            }

            information.put(getString("VERSION_INFORMATION_VERSION_PATH"), versionDir.getAbsolutePath());


            String type = head.optString("type");
            if (!isEmpty(type)) {
                switch (type) {
                    case "release":
                        information.put(getString("VERSION_INFORMATION_VERSION_TYPE"), getString("VERSION_INFORMATION_VERSION_TYPE_RELEASE"));
                        break;
                    case "snapshot":
                        information.put(getString("VERSION_INFORMATION_VERSION_TYPE"), getString("VERSION_INFORMATION_VERSION_TYPE_SNAPSHOT"));
                        break;
                    case "old_beta":
                        information.put(getString("VERSION_INFORMATION_VERSION_TYPE"), getString("VERSION_INFORMATION_VERSION_TYPE_OLD_BETA"));
                        break;
                    case "old_alpha":
                        information.put(getString("VERSION_INFORMATION_VERSION_TYPE"), getString("VERSION_INFORMATION_VERSION_TYPE_OLD_ALPHA"));
                        break;
                }
            }


            String assets = head.optString("assets");
            if (!isEmpty(assets)) information.put(getString("VERSION_INFORMATION_ASSETS_VERSION"), assets);


            String releaseTime = head.optString("releaseTime");
            if (!isEmpty(releaseTime)) {
                DateFormat iso8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                Date parse = null;
                try {
                    parse = iso8601.parse(releaseTime);
                } catch (Exception e) {
                    try {
                        releaseTime = releaseTime.substring(0, 22) + releaseTime.substring(23);
                        parse = iso8601.parse(releaseTime);
                    } catch (Exception e1) {
                        information.put(getString("VERSION_INFORMATION_RELEASE_TIME"), getString("EXCEPTION_UNABLE_PARSE"));
                    }
                }
                if (parse != null) {
                    SimpleDateFormat format = new SimpleDateFormat(getString("TIME_FORMAT"), CMCL.getLocale());
                    information.put(getString("VERSION_INFORMATION_RELEASE_TIME"), format.format(parse) + " (" + TimeZone.getDefault().getDisplayName() + ")");

                }
            }

            JSONObject javaVersion = head.optJSONObject("javaVersion");
            if (javaVersion != null) {
                String component = javaVersion.optString("component");
                if (!isEmpty(component)) {
                    information.put(getString("VERSION_INFORMATION_JAVA_COMPONENT"), component);
                }
                String majorVersion = javaVersion.optString("majorVersion");
                if (!isEmpty(majorVersion)) {
                    information.put(getString("VERSION_INFORMATION_JAVA_VERSION"), majorVersion);
                }
            }
                    /*JSONObject fabric = head.optJSONObject("fabric");
                    if (fabric != null) {
                        String version = fabric.optString("version");
                        if (!isEmpty(version)) {
                            information.put(getString("VERSION_INFORMATION_FABRIC_VERSION"), version);
                        }
                    }*/


            String fabricVersion = VersionModuleUtils.getFabricVersion(head);
            if (!isEmpty(fabricVersion))
                information.put(getString("VERSION_INFORMATION_FABRIC_VERSION"), fabricVersion);

            String forgeVersion = VersionModuleUtils.getForgeVersion(head);
            if (!isEmpty(forgeVersion))
                information.put(getString("VERSION_INFORMATION_FORGE_VERSION"), forgeVersion);

            String liteloaderVersion = VersionModuleUtils.getLiteloaderVersion(head);
            if (!isEmpty(liteloaderVersion))
                information.put(getString("VERSION_INFORMATION_LITELOADER_VERSION"), liteloaderVersion);

            String optiFineVersion = VersionModuleUtils.getOptifineVersion(head);
            if (!isEmpty(optiFineVersion))
                information.put(getString("VERSION_INFORMATION_OPTIFINE_VERSION"), optiFineVersion);

            String quiltVersion = VersionModuleUtils.getQuiltVersion(head);
            if (!isEmpty(quiltVersion))
                information.put(getString("VERSION_INFORMATION_QUILT_VERSION"), quiltVersion);


            System.out.println(versionName + ":");//legal
            for (Map.Entry<String, String> entry : information.entrySet()) {
                System.out.print(entry.getKey());//legal
                System.out.println(entry.getValue());//legal
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(getString("UNABLE_GET_VERSION_INFORMATION"));
        }
    }
}
