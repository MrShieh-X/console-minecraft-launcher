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
import com.mrshiehx.cmcl.modules.modpack.ModrinthModpackInstaller;
import com.mrshiehx.cmcl.searchSources.curseforge.CFManager;
import com.mrshiehx.cmcl.searchSources.curseforge.CFModpackManager;
import com.mrshiehx.cmcl.enums.CurseForgeSection;
import com.mrshiehx.cmcl.modules.modpack.CurseForgeModpackInstaller;
import com.mrshiehx.cmcl.modules.modpack.MultiMCModpackInstaller;
import com.mrshiehx.cmcl.utils.*;
import com.mrshiehx.cmcl.utils.json.XJSONObject;
import org.json.JSONObject;

import java.io.File;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.*;

public class ModpackOption implements Option {
    @Override
    public void execute(Arguments arguments) {
        Argument subOption = arguments.opt(1);
        if (subOption == null) {
            System.out.println(getString("CONSOLE_GET_USAGE"));
            return;
        }
        String key = subOption.key;
        String lowerCase = key.toLowerCase();
        if (!"i".equals(lowerCase) && !"s".equals(lowerCase) && !"l".equals(lowerCase)) {
            System.out.println(getString("CONSOLE_UNKNOWN_OPTION", key));
            return;
        }

        JSONObject modpack;
        CFManager modManager = new CFModpackManager();
        if ((subOption instanceof ValueArgument) && ("i".equals(subOption.key) || "s".equals(subOption.key))) {

            ValueArgument valueArgument = (ValueArgument) subOption;

            String value = valueArgument.value;


            //mod=null;
            modpack = modManager.search(value);
            //mod = selectMod(value);
            if (modpack == null) return;

        } else if (arguments.optInt("c", Integer.MIN_VALUE) != Integer.MIN_VALUE) {
            int modpackId = arguments.optInt("c");
            try {
                modpack = modManager.getByID(String.valueOf(modpackId));
            } catch (CFManager.NotMinecraftAddon e) {
                System.out.println(getString("CF_GET_BY_ID_NOT_OF_MC", e.gameId).replace("${NAME}", getString("CF_BESEARCHED_MODPACK_ALC")));
                return;
            } catch (CFManager.IncorrectCategoryAddon e) {
                CurseForgeSection target = CurseForgeSection.valueOf(e.gameCategoryId);
                if (target == null) {
                    System.out.println(getString("CF_GET_BY_ID_INCORRECT_CATEGORY", e.gameCategoryId).replace("${NAME}", getString("CF_BESEARCHED_MODPACK_ALC")));
                } else {
                    System.out.println(getString("CF_GET_BY_ID_INCORRECT_CATEGORY_DETAIL").replace("${NAME}", getString("CF_BESEARCHED_MODPACK_ALC")).replace("${TARGET}", target.nameAllLowerCase));
                }
                return;
            } catch (Exception e) {
                System.out.println(getString("CF_GET_BY_ID_FAILED", e).replace("${NAME}", getString("CF_BESEARCHED_MODPACK_ALC")));
                return;
            }
        } else {
            Argument arg = arguments.optArgument("l");
            if (!(arg instanceof ValueArgument)) {
                System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                return;
            }
            File file = new File(((ValueArgument) arg).value);
            if (!file.exists() || file.isDirectory()) {
                System.out.println(getString("FILE_NOT_FOUND_OR_IS_A_DIRECTORY"));
                return;
            }
            String versionName = ConsoleUtils.inputStringInFilter(getString("MESSAGE_INPUT_VERSION_NAME"), getString("MESSAGE_INSTALL_INPUT_NAME_EXISTS"), string -> !isEmpty(string) && !Utils.versionExists(string));

            if (isEmpty(versionName)) return;

            int threadCount = arguments.optInt("t");
            File versionDir = new File(versionsDir, versionName);
            try (ZipFile zipFile = new ZipFile(file)) {
                installModpack(zipFile, file, versionDir, true, !arguments.contains("na"), !arguments.contains("nn"), !arguments.contains("nl"), threadCount > 0 ? threadCount : Constants.DEFAULT_DOWNLOAD_THREAD_COUNT);
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e));
                Utils.deleteDirectory(versionDir);
            }
            return;
        }
        switch (lowerCase) {
            case "i":

                /*System.out.println(getString("CF_SUPPORTED_GAME_VERSION",modName));
                for (int i = 0; i < mod.getJSONArray("gameVersionLatestFiles").length(); i++) {
                    System.out.println("(" + (i + 1) + ")" + mod.getJSONArray("gameVersionLatestFiles").getJSONObject(i).optString("gameVersion"));
                }*/

            {
                String modpackName = modpack.optString("name");
                int modId = modpack.optInt("id");


                String modpackDownloadLink = modManager.getDownloadLink(modId, modpackName);
                if (isEmpty(modpackDownloadLink)) return;
                downloadModpack(modpackDownloadLink, arguments);
            }
            break;

            case "s": {
                String modpackName = modpack.optString("name");
                modManager.printInformation(modpack, modpackName);
            }
            break;

            default:
                System.out.println(getString("CONSOLE_UNKNOWN_OPTION", key));
                break;
        }
    }

    private static int installModpack(ZipFile zipFile, File file, File versionDir, boolean keepFile, boolean installAssets, boolean installNatives, boolean installLibraries, int threadCount) {
        //MCBBS一定要放在 CurseForge 之前，否则 MCBBS 的会被误认为是 CurseForge
        /*try {
            return MCBBSModpackInstaller.tryToInstallMCBBSModpack(zipFile, file, versionDir, keepFile, installAssets, installNatives, installLibraries, threadCount);
        } catch (NotValidModPackFormat ignore) {
        }*/
        try {
            return CurseForgeModpackInstaller.tryToInstallCurseForgeModpack(zipFile, file, versionDir, keepFile, installAssets, installNatives, installLibraries, threadCount);
        } catch (NotValidModPackFormat ignore) {
        }
        try {
            return MultiMCModpackInstaller.tryToInstallMultiMCModpack(zipFile, file, versionDir, keepFile, installAssets, installNatives, installLibraries, threadCount);
        } catch (NotValidModPackFormat ignore) {
        }
        try {
            return ModrinthModpackInstaller.tryToInstallModrinthModpack(zipFile, file, versionDir, keepFile, installAssets, installNatives, installLibraries, threadCount);
        } catch (NotValidModPackFormat ignore) {
        }

        System.out.println(getString("MESSAGE_INSTALL_MODPACK_UNKNOWN_TYPE"));
        return -1;
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
        return "MODPACK";
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
            ZipEntry entry = zipFile.getEntry("manifest.json");
            //FileUtils.inputStream2File(zipFile.getInputStream(entry),new File(versionDir,"modpack.json"));
            JSONObject manifest = new XJSONObject(Utils.inputStream2String(zipFile.getInputStream(entry)));
            CurseForgeModpackInstaller.installCurseForgeModpack(manifest, zipFile, modpackFile, versionDir, arguments.contains("k"), !arguments.contains("na"), !arguments.contains("nn"), !arguments.contains("nl"), threadCount > 0 ? threadCount : Constants.DEFAULT_DOWNLOAD_THREAD_COUNT);
        } catch (Exception e) {
            System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e));
            Utils.deleteDirectory(versionDir);
        }
    }

    public static class NotValidModPackFormat extends Exception {
        public NotValidModPackFormat(String message) {
            super(message);
        }
    }
}