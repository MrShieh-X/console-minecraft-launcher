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

package com.mrshiehx.cmcl.options;

import com.mrshiehx.cmcl.ConsoleMinecraftLauncher;
import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.bean.arguments.ValueArgument;
import com.mrshiehx.cmcl.enums.CurseForgeSection;
import com.mrshiehx.cmcl.searchSources.curseforge.CFManager;
import com.mrshiehx.cmcl.searchSources.curseforge.CFModManager;
import com.mrshiehx.cmcl.utils.PercentageTextProgress;
import org.json.JSONObject;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.isEmpty;

public class ModOption implements Option {
    @Override
    public void execute(Arguments arguments) {
        Argument subOption = arguments.opt(1);
        if (subOption == null) {
            System.out.println(getString("CONSOLE_GET_USAGE"));
            return;
        }
        String key = subOption.key;
        String lowerCase = key.toLowerCase();
        if (!"i".equals(lowerCase) && !"s".equals(lowerCase)) {
            System.out.println(getString("CONSOLE_UNKNOWN_OPTION", key));
            return;
        }

        JSONObject mod;
        CFManager modManager = new CFModManager();
        if ((subOption instanceof ValueArgument) && ("i".equals(subOption.key) || "s".equals(subOption.key))) {

            ValueArgument valueArgument = (ValueArgument) subOption;

            String value = valueArgument.value;


            //mod=null;
            mod = modManager.search(value);
            //mod = selectMod(value);
            if (mod == null) return;

        } else if (arguments.optInt("c", Integer.MIN_VALUE) != Integer.MIN_VALUE) {
            int modId = arguments.optInt("c");
            try {
                mod = modManager.getByID(String.valueOf(modId));
            } catch (CFManager.NotMinecraftAddon e) {
                System.out.println(getString("CF_GET_BY_ID_NOT_OF_MC", e.gameId).replace("${NAME}", getString("CF_BESEARCHED_MOD_ALC")));
                return;
            } catch (CFManager.IncorrectCategoryAddon e) {
                CurseForgeSection target = CurseForgeSection.valueOf(e.gameCategoryId);
                if (target == null) {
                    System.out.println(getString("CF_GET_BY_ID_INCORRECT_CATEGORY", e.gameCategoryId).replace("${NAME}", getString("CF_BESEARCHED_MOD_ALC")));
                } else {
                    System.out.println(getString("CF_GET_BY_ID_INCORRECT_CATEGORY_DETAIL").replace("${NAME}", getString("CF_BESEARCHED_MOD_ALC")).replace("${TARGET}", target.nameAllLowerCase));
                }
                return;
            } catch (Exception e) {
                System.out.println(getString("CF_GET_BY_ID_FAILED", e).replace("${NAME}", getString("CF_BESEARCHED_MOD_ALC")));
                return;
            }
        } else {
            System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
            return;
        }
        String modName = mod.optString("name");
        switch (lowerCase) {
            case "i":

                /*System.out.println(getString("CF_SUPPORTED_GAME_VERSION",modName));
                for (int i = 0; i < mod.getJSONArray("gameVersionLatestFiles").length(); i++) {
                    System.out.println("(" + (i + 1) + ")" + mod.getJSONArray("gameVersionLatestFiles").getJSONObject(i).optString("gameVersion"));
                }*/

                int modId = mod.optInt("id");


                String modDownloadLink = modManager.getDownloadLink(modId, modName);
                if (isEmpty(modDownloadLink)) return;
                downloadMod(modDownloadLink);

                break;

            case "s":
                modManager.printInformation(mod, modName);
                break;

            default:
                System.out.println(getString("CONSOLE_UNKNOWN_OPTION", key));
                break;
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
        return "MOD";
    }


    private static void downloadMod(String modDownloadLink) {
        File mods = new File(ConsoleMinecraftLauncher.gameDir, "mods");
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
            ConsoleMinecraftLauncher.downloadFile(modDownloadLink, modFile, new PercentageTextProgress());
        } catch (Exception e) {

            //Utils.downloadFileFailed(modDownloadLink, modFile, e);
            System.out.println(getString("MESSAGE_FAILED_DOWNLOAD_FILE_WITH_REASON", fileName, e));
        }
    }
}