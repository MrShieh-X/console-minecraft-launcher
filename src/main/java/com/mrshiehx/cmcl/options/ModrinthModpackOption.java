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
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.enums.ModrinthSection;
import com.mrshiehx.cmcl.modules.modpack.ModrinthModpackInstaller;
import com.mrshiehx.cmcl.searchSources.modrinth.ModrinthManager;
import com.mrshiehx.cmcl.searchSources.modrinth.ModrinthModpackManager;
import com.mrshiehx.cmcl.utils.ConsoleUtils;
import com.mrshiehx.cmcl.utils.PercentageTextProgress;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.json.XJSONObject;
import org.json.JSONObject;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.*;

public class ModrinthModpackOption implements Option {
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

        JSONObject modSearch, modByID;
        String modName, modID;
        ModrinthManager modManager = new ModrinthModpackManager();
        if ((subOption instanceof ValueArgument) && ("i".equals(subOption.key) || "s".equals(subOption.key))) {

            ValueArgument valueArgument = (ValueArgument) subOption;

            String value = valueArgument.value;


            modSearch = modManager.search(value, arguments.optInt("l", 50));
            if (modSearch == null) return;
            modName = modSearch.optString("title");
            modID = modSearch.optString("project_id");

            try {
                modByID = modManager.getByID(modID);
            } catch (Exception ignore) {
                modByID = null;
            }


        } else if (!isEmpty(arguments.opt("c"))) {
            try {
                modByID = modManager.getByID(arguments.opt("c"));
                modName = modByID.optString("title");
                modID = modByID.optString("id");
                modSearch = null;
            } catch (ModrinthManager.IncorrectCategoryAddon e) {
                ModrinthSection target = (e.section);
                System.out.println(getString("CF_GET_BY_ID_INCORRECT_CATEGORY_DETAIL").replace("${NAME}", getString("CF_BESEARCHED_MODPACK_ALC")).replace("${TARGET}", target != null ? target.nameAllLowerCase : "null"));
                return;
            } catch (Exception e) {
                System.out.println(getString("CF_GET_BY_ID_FAILED", e).replace("${NAME}", getString("CF_BESEARCHED_MODPACK_ALC")));
                return;
            }
        } else {
            System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
            return;
        }

        switch (lowerCase) {
            case "i":

                String modDownloadLink = modManager.getDownloadLink(modID, modName, null);
                if (isEmpty(modDownloadLink)) return;
                downloadModpack(modDownloadLink, arguments);
                break;

            case "s":
                modManager.printInformation(modSearch, modByID, null, null);
                break;

            default:
                System.out.println(getString("CONSOLE_UNKNOWN_OPTION", key));
                break;
        }
    }

    private static File askStorage(File last) {
        System.out.print(getString("CF_STORAGE_FILE_EXISTS", last.getAbsolutePath()).replace("${NAME}", getString("CF_BESEARCHED_MODPACK_ALC")));
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
        return "MODPACK2";
    }


    private static void downloadModpack(String modDownloadLink, Arguments arguments) {
        File mods = new File("cmcl", "modpacks");
        mods.mkdirs();
        String fileName = modDownloadLink.substring(modDownloadLink.lastIndexOf('/') + 1);
        File modpackFile = new File(mods, fileName);
        if (modpackFile.exists()) {
            File file = askStorage(modpackFile);
            if (file != null) {
                modpackFile = file;
                mods = file.getParentFile();
            } else {
                return;
            }
        }
        try {
            System.out.print(getString("MESSAGE_DOWNLOADING_FILE_TO", fileName, mods.getAbsolutePath()));
            ConsoleMinecraftLauncher.downloadFile(modDownloadLink, modpackFile, new PercentageTextProgress());
        } catch (Exception e) {
            System.out.println(getString("MESSAGE_FAILED_DOWNLOAD_FILE_WITH_REASON", fileName, e));
            return;
        }

        String versionName = ConsoleUtils.inputStringInFilter(getString("MESSAGE_INPUT_VERSION_NAME"), getString("MESSAGE_INSTALL_INPUT_NAME_EXISTS"), string -> !isEmpty(string) && !Utils.versionExists(string));


        if (isEmpty(versionName)) return;

        int threadCount = arguments.optInt("t");
        File versionDir = new File(versionsDir, versionName);

        try (ZipFile zipFile = new ZipFile(modpackFile)) {
            ZipEntry entry = zipFile.getEntry("modrinth.index.json");
            //FileUtils.inputStream2File(zipFile.getInputStream(entry),new File(versionDir,"modpack.json"));
            JSONObject manifest = new XJSONObject(Utils.inputStream2String(zipFile.getInputStream(entry)));
            ModrinthModpackInstaller.installModrinthModpack(manifest, zipFile, modpackFile, versionDir, arguments.contains("k"), !arguments.contains("na"), !arguments.contains("nn"), !arguments.contains("nl"), threadCount > 0 ? threadCount : Constants.DEFAULT_DOWNLOAD_THREAD_COUNT);
        } catch (Exception e) {
            System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e));
            Utils.deleteDirectory(versionDir);
        }
    }

}