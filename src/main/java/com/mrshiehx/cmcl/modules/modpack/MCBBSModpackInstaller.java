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

package com.mrshiehx.cmcl.modules.modpack;

import com.mrshiehx.cmcl.interfaces.Abandoned;
import com.mrshiehx.cmcl.options.ModpackOption;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.json.XJSONObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.isEmpty;

/**
 * Semi Finished Products 半成品
 **/
@Abandoned
public class MCBBSModpackInstaller {
    public static int tryToInstallMCBBSModpack(ZipFile zipFile, File file, File versionDir, boolean keepFile, boolean installAssets, boolean installNatives, boolean installLibraries, int threadCount) throws ModpackOption.NotValidModPackFormat {
        ZipEntry entry = zipFile.getEntry("mcbbs.packmeta");
        //FileUtils.inputStream2File(zipFile.getInputStream(entry),new File(versionDir,"modpack.json"));
        if (entry == null) {
            throw new ModpackOption.NotValidModPackFormat("not a MCBBS modpack");
        }

        JSONObject mcbbsPackmeta;
        try {
            InputStream i = zipFile.getInputStream(entry);
            mcbbsPackmeta = new XJSONObject(Utils.inputStream2String(i));
        } catch (Exception e) {
            /*System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e));
            Utils.deleteDirectory(versionDir);
            return -1;*/
            throw new ModpackOption.NotValidModPackFormat(e.getMessage());
        }

        List<JSONObject> addons = Utils.jsonArrayToJSONObjectList(mcbbsPackmeta.optJSONArray("addons"));

        String gameVersion = null;
        String forgeVersion = null;
        String liteloaderVersion = null;
        String fabricVersion = null;
        String optifineVersion = null;
        for (JSONObject addon : addons) {
            String id = addon.optString("id");
            String version = addon.optString("version");
            switch (id) {
                case "game":
                    gameVersion = version;
                    break;
                case "forge":
                    forgeVersion = version;
                    break;
                case "liteloader":
                    liteloaderVersion = version;
                    break;
                case "fabric":
                    fabricVersion = version;
                    break;
                case "optifine":
                    optifineVersion = version;
                    break;
            }
        }

        if (isEmpty(gameVersion)) {
            /*System.out.println(getString("MESSAGE_INSTALL_MODPACK_NOT_FOUND_GAME_VERSION"));
            return -1;*/
            throw new ModpackOption.NotValidModPackFormat(getString("MESSAGE_INSTALL_MODPACK_NOT_FOUND_GAME_VERSION"));
        }
        if (!isEmpty(forgeVersion) && !isEmpty(fabricVersion)) {
            System.out.println(getString("MESSAGE_INSTALL_MODPACK_COEXIST", "Forge", "Fabric"));
            return -1;
        }
        if (!isEmpty(liteloaderVersion) && !isEmpty(fabricVersion)) {
            System.out.println(getString("MESSAGE_INSTALL_MODPACK_COEXIST", "LiteLoader", "Fabric"));
            return -1;
        }
        if (!isEmpty(optifineVersion) && !isEmpty(fabricVersion)) {
            System.out.println(getString("MESSAGE_INSTALL_MODPACK_COEXIST", "OptiFine", "Fabric"));
            return -1;
        }

        zipFile.stream().forEach((Consumer<ZipEntry>) zipEntry -> {
            String overrides = "overrides";
            if (!zipEntry.getName().startsWith(overrides)) return;
            File to = new File(versionDir, zipEntry.getName().substring(9));
            if (zipEntry.isDirectory()) {
                to.mkdirs();
            } else {
                try {
                    FileUtils.inputStream2File(zipFile.getInputStream(zipEntry), to);
                } catch (IOException e) {
                    System.out.println(getString("MESSAGE_FAILED_TO_DECOMPRESS_FILE", zipEntry.getName(), e));
                }
            }
        });

        String fileApi = mcbbsPackmeta.optString("fileApi");
        if (!isEmpty(fileApi)) {

        }
        JSONArray libraries = mcbbsPackmeta.optJSONArray("libraries");/*code merge*/

        return 0;
    }

}
