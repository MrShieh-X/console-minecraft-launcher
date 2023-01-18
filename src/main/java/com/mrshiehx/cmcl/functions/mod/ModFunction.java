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

package com.mrshiehx.cmcl.functions.mod;

import com.mrshiehx.cmcl.CMCL;
import com.mrshiehx.cmcl.bean.arguments.ArgumentRequirement;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.functions.Function;
import com.mrshiehx.cmcl.modSources.curseforge.CurseForgeManager;
import com.mrshiehx.cmcl.modSources.curseforge.CurseForgeModManager;
import com.mrshiehx.cmcl.modSources.modrinth.ModrinthManager;
import com.mrshiehx.cmcl.modSources.modrinth.ModrinthModManager;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.console.PercentageTextProgress;
import com.mrshiehx.cmcl.utils.internet.DownloadUtils;
import org.json.JSONObject;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.isEmpty;

public class ModFunction implements Function {
    @Override
    public void execute(Arguments arguments) {
        if (!Function.checkArgs(arguments, 2, 1,
                ArgumentRequirement.ofSingle("install"),
                ArgumentRequirement.ofSingle("info"),
                ArgumentRequirement.ofValue("source"),
                ArgumentRequirement.ofValue("n"),
                ArgumentRequirement.ofValue("name"),
                ArgumentRequirement.ofValue("id"),
                ArgumentRequirement.ofValue("limit"))) return;
        if (arguments.contains("install") && arguments.contains("info")) {
            System.out.println(getString("MOD_CONTAINS_BOTH"));
            return;
        } else if (!arguments.contains("install") && !arguments.contains("info")) {
            System.out.println(getString("MOD_CONTAINS_BOTH_NOT"));
            return;
        }
        int todo = arguments.contains("install") ? 0 : 1;

        String sourceStr = arguments.opt("source");

        if (isEmpty(sourceStr)) {
            System.out.println(getString("MOD_NO_SOURCE"));
            return;
        }

        int source;
        switch (sourceStr.toLowerCase()) {
            case "cf":
            case "curseforge":
                source = 0;
                break;
            case "mr":
            case "modrinth":
                source = 1;
                break;
            default:
                System.out.println(Utils.getString("MOD_UNKNOWN_SOURCE", sourceStr));
                return;
        }

        String modNameInput = arguments.opt("n", arguments.opt("name"));
        String modIdInput = arguments.opt("id");

        if (!isEmpty(modNameInput) && !isEmpty(modIdInput)) {
            System.out.println(getString("MOD_CONTAINS_BOTH_NAME_AND_ID"));
            return;
        } else if (isEmpty(modNameInput) && isEmpty(modIdInput)) {
            System.out.println(getString("MOD_CONTAINS_BOTH_NOT_NAME_AND_ID"));
            return;
        }

        if (!isEmpty(modIdInput) && arguments.contains("limit")) {
            System.out.println(getString("MOD_ID_LIMIT_COEXIST"));
            return;
        }

        int limit = arguments.optInt("limit", 50);
        if (limit > 50 && source == 0) {
            System.out.println(getString("MOD_SEARCH_LIMIT_GREATER_THAN_FIFTY"));
            return;
        }


        if (source == 0) {
            CurseForgeManager cf = new CurseForgeModManager();
            JSONObject mod = CurseForgeSearcher.search(cf, modNameInput, modIdInput, limit);
            if (mod == null) return;
            String modName = mod.optString("name");
            if (todo == 0) {
                int modId = mod.optInt("id");
                String modDownloadLink = cf.getDownloadLink(String.valueOf(modId), modName, null);
                if (isEmpty(modDownloadLink)) return;
                downloadMod(modDownloadLink);
            } else {
                cf.printInformation(mod, modName);
            }
        } else {
            ModrinthManager mr = new ModrinthModManager();
            ModrinthSearcher.Result result = ModrinthSearcher.search(mr, modNameInput, modIdInput, limit);
            if (result == null) return;
            JSONObject mod = result.mod, modByID = result.modByID;
            String modName = result.modName, modID = result.modID;

            if (todo == 0) {
                String modDownloadLink = mr.getDownloadLink(modID, modName, null);
                if (isEmpty(modDownloadLink)) return;
                downloadMod(modDownloadLink);
            } else {
                mr.printInformation(mod, modByID, null, null);
            }
        }
    }


    private static File askStorage(File last) {
        System.out.print(getString("CF_STORAGE_FILE_EXISTS", last.getAbsolutePath()).replace("${NAME}", getString("CF_BESEARCHED_MOD_ALC")));
        String path;
        try {
            path = new Scanner(System.in).nextLine();
        } catch (NoSuchElementException e) {
            return null;
        }
        if (isEmpty(path)) return askStorage(last);
        File file = new File(path, last.getName());
        if (!file.exists()) return file;
        return askStorage(file);
    }

    @Override
    public String getUsageName() {
        return "mod";
    }


    public static void downloadMod(String modDownloadLink) {
        File mods = new File(CMCL.gameDir, "mods");
        mods.mkdirs();
        String fileName = modDownloadLink.substring(modDownloadLink.lastIndexOf('/') + 1);
        File modFile = new File(mods, fileName);
        if (modFile.exists()) {
            File file = askStorage(modFile);
            if (file != null) {
                modFile = file;
                mods = file.getParentFile();
            } else {
                return;
            }
        }
        try {
            System.out.print(getString("MESSAGE_DOWNLOADING_FILE_TO", fileName, mods.getAbsolutePath()));
            DownloadUtils.downloadFile(modDownloadLink, modFile, new PercentageTextProgress());
        } catch (Exception e) {
            if (Constants.isDebug()) e.printStackTrace();
            //Utils.downloadFileFailed(modDownloadLink, modFile, e);
            System.out.println(getString("MESSAGE_FAILED_DOWNLOAD_FILE_WITH_REASON", fileName, e));
        }
    }
}