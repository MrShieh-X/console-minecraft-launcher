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

import com.mrshiehx.cmcl.bean.arguments.ArgumentRequirement;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.functions.Function;
import com.mrshiehx.cmcl.modSources.curseforge.CurseForgeManager;
import com.mrshiehx.cmcl.modSources.curseforge.CurseForgeModpackManager;
import com.mrshiehx.cmcl.modSources.modrinth.ModrinthManager;
import com.mrshiehx.cmcl.modSources.modrinth.ModrinthModpackManager;
import com.mrshiehx.cmcl.modules.modpack.CurseForgeModpackInstaller;
import com.mrshiehx.cmcl.modules.modpack.MCBBSModpackInstaller;
import com.mrshiehx.cmcl.modules.modpack.ModrinthModpackInstaller;
import com.mrshiehx.cmcl.modules.modpack.MultiMCModpackInstaller;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.cmcl.version.VersionUtils;
import com.mrshiehx.cmcl.utils.console.ConsoleUtils;
import com.mrshiehx.cmcl.utils.console.PercentageTextProgress;
import com.mrshiehx.cmcl.utils.internet.DownloadUtils;
import com.mrshiehx.cmcl.utils.json.XJSONObject;
import org.json.JSONObject;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.mrshiehx.cmcl.CMCL.*;

public class ModpackFunction implements Function {
    @Override
    public void execute(Arguments arguments) {
        if (!Function.checkArgs(arguments, 2, 1,
                ArgumentRequirement.ofSingle("install"),
                ArgumentRequirement.ofSingle("info"),
                ArgumentRequirement.ofSingle("no-assets"),
                ArgumentRequirement.ofSingle("no-libraries"),
                ArgumentRequirement.ofSingle("no-natives"),
                ArgumentRequirement.ofSingle("k"),
                ArgumentRequirement.ofSingle("keep-file"),
                ArgumentRequirement.ofValue("file"),
                ArgumentRequirement.ofValue("storage"),
                ArgumentRequirement.ofValue("source"),
                ArgumentRequirement.ofValue("n"),
                ArgumentRequirement.ofValue("name"),
                ArgumentRequirement.ofValue("id"),
                ArgumentRequirement.ofValue("limit"),
                ArgumentRequirement.ofValue("t"),
                ArgumentRequirement.ofValue("thread"))) return;
        int count = 0;
        if (arguments.contains("install")) count++;
        if (arguments.contains("info")) count++;
        if (arguments.contains("file")) count++;

        if (count == 0) {
            System.out.println(getString("MODPACK_CONTAINS_NOTHING"));
            return;
        } else if (count > 1) {
            System.out.println(getString("MODPACK_CONTAINS_TWO_OR_MORE"));
            return;
        }

        int todo = -1;
        if (arguments.contains("install")) todo = 0;
        else if (arguments.contains("info")) todo = 1;
        else if (arguments.contains("file")) todo = 2;

        if (todo == 2) {
            File modpack = new File(arguments.opt("file"));
            if (!modpack.exists() || modpack.isDirectory()) {
                System.out.println(getString("FILE_NOT_FOUND_OR_IS_A_DIRECTORY"));
                return;
            }
            String versionStorageName = arguments.opt("storage");
            if (isEmpty(versionStorageName)) versionStorageName = ConsoleUtils.inputStringInFilter(
                    getString("MESSAGE_INPUT_VERSION_NAME"),
                    getString("MESSAGE_INSTALL_INPUT_NAME_EXISTS"),
                    string -> !isEmpty(string) && !VersionUtils.versionExists(string));
            if (isEmpty(versionStorageName)) return;
            if (VersionUtils.versionExists(versionStorageName)) {
                System.out.println(getString("MESSAGE_INSTALL_INPUT_NAME_EXISTS", versionStorageName));
                return;
            }
            int threadCount = arguments.optInt("t", arguments.optInt("thread"));
            File versionDir = new File(versionsDir, versionStorageName);
            try (ZipFile zipFile = new ZipFile(modpack)) {
                installModpack(zipFile, modpack, versionDir, true, !arguments.contains("no-assets"), !arguments.contains("no-natives"), !arguments.contains("no-libraries"), threadCount > 0 ? threadCount : Constants.DEFAULT_DOWNLOAD_THREAD_COUNT);
            } catch (Exception e) {
                System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e));
                FileUtils.deleteDirectory(versionDir);
            }
            return;
        }

        String sourceStr = arguments.opt("source");

        if (isEmpty(sourceStr)) {
            JSONObject config = getConfig();
            sourceStr = config.optString("modpackDownloadSource");
            if (!sourceStr.equalsIgnoreCase("curseforge") && !sourceStr.equalsIgnoreCase("modrinth")) {
                sourceStr = ConsoleUtils.inputStringInFilter(getString("CONSOLE_CHOOSE_DOWNLOAD_SOURCE_CF_OR_MR"), getString("CONSOLE_CHOOSE_DOWNLOAD_SOURCE_CF_OR_MR_UNKNOWN"), string -> "curseforge".equalsIgnoreCase(string) || "modrinth".equalsIgnoreCase(string));
                if (sourceStr != null) {
                    Utils.saveConfig(config.put("modpackDownloadSource", sourceStr));
                }
            }
        }

        int source;
        switch (Objects.requireNonNull(sourceStr).toLowerCase()) {
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
            CurseForgeManager cf = new CurseForgeModpackManager();
            JSONObject mod = CurseForgeSearcher.search(cf, modNameInput, modIdInput, limit);
            if (mod == null) return;
            String modpackName = mod.optString("name");
            if (todo == 0) {
                int modId = mod.optInt("id");
                String modpackDownloadLink = cf.getDownloadLink(String.valueOf(modId), modpackName, null);
                if (isEmpty(modpackDownloadLink)) return;
                String versionStorageName = arguments.opt("storage");
                if (isEmpty(versionStorageName)) versionStorageName = ConsoleUtils.inputStringInFilter(
                        getString("MESSAGE_INPUT_VERSION_NAME"),
                        getString("MESSAGE_INSTALL_INPUT_NAME_EXISTS"),
                        string -> !isEmpty(string) && !VersionUtils.versionExists(string));
                if (isEmpty(versionStorageName))
                    return;
                if (VersionUtils.versionExists(versionStorageName)) {
                    System.out.println(getString("MESSAGE_INSTALL_INPUT_NAME_EXISTS", versionStorageName));
                    return;
                }
                downloadModpack(versionStorageName, modpackDownloadLink, arguments, source);
            } else {
                cf.printInformation(mod, modpackName);
            }
        } else {
            ModrinthManager mr = new ModrinthModpackManager();
            ModrinthSearcher.Result result = ModrinthSearcher.search(mr, modNameInput, modIdInput, limit);
            if (result == null) return;
            JSONObject mod = result.mod, modByID = result.modByID;
            String modName = result.modName, modID = result.modID;

            if (todo == 0) {
                String modDownloadLink = mr.getDownloadLink(modID, modName, null);
                if (isEmpty(modDownloadLink)) return;
                String versionStorageName = arguments.opt("storage");
                if (isEmpty(versionStorageName)) versionStorageName = ConsoleUtils.inputStringInFilter(
                        getString("MESSAGE_INPUT_VERSION_NAME"),
                        getString("MESSAGE_INSTALL_INPUT_NAME_EXISTS"),
                        string -> !isEmpty(string) && !VersionUtils.versionExists(string));
                if (isEmpty(versionStorageName)) return;
                if (VersionUtils.versionExists(versionStorageName)) {
                    System.out.println(getString("MESSAGE_INSTALL_INPUT_NAME_EXISTS", versionStorageName));
                    return;
                }
                downloadModpack(versionStorageName, modDownloadLink, arguments, source);
            } else {
                mr.printInformation(mod, modByID, null, null);
            }
        }
    }

    private static int installModpack(ZipFile zipFile, File file, File versionDir, boolean keepFile, boolean installAssets, boolean installNatives, boolean installLibraries, int threadCount) {
        //MCBBS一定要放在 CurseForge 之前，否则 MCBBS 的会被误认为是 CurseForge
        try {
            return MCBBSModpackInstaller.tryToInstallMCBBSModpack(zipFile, file, versionDir, keepFile, installAssets, installNatives, installLibraries, threadCount);
        } catch (NotValidModPackFormat ignore) {
        }
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
        return "modpack";
    }


    private static void downloadModpack(String versionName, String modDownloadLink, Arguments arguments, int source) {
        File modpacks = new File(".cmcl", "modpacks");
        modpacks.mkdirs();
        String fileName = modDownloadLink.substring(modDownloadLink.lastIndexOf('/') + 1);
        File modpackFile = new File(modpacks, fileName);
        if (modpackFile.exists()) {
            File file = askStorage(modpackFile);
            if (file != null) {
                modpackFile = file;
                modpacks = file.getParentFile();
            } else {
                return;
            }
        }
        try {
            System.out.print(getString("MESSAGE_DOWNLOADING_FILE_TO", fileName, modpacks.getAbsolutePath()));
            DownloadUtils.downloadFile(modDownloadLink, modpackFile, new PercentageTextProgress());
        } catch (Exception e) {
            System.out.println(getString("MESSAGE_FAILED_DOWNLOAD_FILE_WITH_REASON", fileName, e));
            return;
        }

        int threadCount = arguments.optInt("t", arguments.optInt("thread"));
        File versionDir = new File(versionsDir, versionName);

        try (ZipFile zipFile = new ZipFile(modpackFile)) {
            ZipEntry entry = zipFile.getEntry(source == 0 ? "manifest.json" : "modrinth.index.json");
            //FileUtils.inputStream2File(zipFile.getInputStream(entry),new File(versionDir,"modpack.json"));
            JSONObject manifest = new XJSONObject(Utils.inputStream2String(zipFile.getInputStream(entry)));
            if (source == 0)
                CurseForgeModpackInstaller.installCurseForgeModpack(manifest, zipFile, modpackFile, versionDir, arguments.contains("k") || arguments.contains("keep-file"), !arguments.contains("no-assets"), !arguments.contains("no-natives"), !arguments.contains("no-libraries"), threadCount > 0 ? threadCount : Constants.DEFAULT_DOWNLOAD_THREAD_COUNT);
            else
                ModrinthModpackInstaller.installModrinthModpack(manifest, zipFile, modpackFile, versionDir, arguments.contains("k") || arguments.contains("keep-file"), !arguments.contains("no-assets"), !arguments.contains("no-natives"), !arguments.contains("no-libraries"), threadCount > 0 ? threadCount : Constants.DEFAULT_DOWNLOAD_THREAD_COUNT);
        } catch (Exception e) {
            System.out.println(getString("EXCEPTION_INSTALL_MODPACK", e));
            FileUtils.deleteDirectory(versionDir);
        }
    }

    public static class NotValidModPackFormat extends Exception {
        public NotValidModPackFormat(String message) {
            super(message);
        }
    }
}