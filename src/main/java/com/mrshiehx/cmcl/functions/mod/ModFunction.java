/*
 * Console Minecraft Launcher
 * Copyright (C) 2021-2024  MrShiehX
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
import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.bean.arguments.ArgumentRequirement;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.functions.Function;
import com.mrshiehx.cmcl.modSources.Manager;
import com.mrshiehx.cmcl.modSources.curseforge.CurseForgeManager;
import com.mrshiehx.cmcl.modSources.curseforge.CurseForgeModManager;
import com.mrshiehx.cmcl.modSources.modrinth.ModrinthManager;
import com.mrshiehx.cmcl.modSources.modrinth.ModrinthModManager;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.console.InteractionUtils;
import com.mrshiehx.cmcl.utils.console.PercentageTextProgress;
import com.mrshiehx.cmcl.utils.internet.DownloadUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import static com.mrshiehx.cmcl.CMCL.getConfig;
import static com.mrshiehx.cmcl.CMCL.isEmpty;
import static com.mrshiehx.cmcl.utils.Utils.getString;

public class ModFunction implements Function {
    public final static Manager.DependencyInstaller MOD_CF_DEPENDENCY_INSTALLER = new Manager.DependencyInstaller() {
        @Override
        public void install(String mcVersion, String name, String id) {
            downloadMod(new CurseForgeModManager().getDownloadLink(id, name, mcVersion, null, false, MOD_CF_DEPENDENCY_INSTALLER));
        }
    };
    public final static Manager.DependencyInstaller MOD_MR_DEPENDENCY_INSTALLER = new Manager.DependencyInstaller() {
        @Override
        public void install(String mcVersion, String name, String id) {
            downloadMod(new ModrinthModManager().getDownloadLink(id, name, mcVersion, null, false, MOD_MR_DEPENDENCY_INSTALLER));
        }
    };

    @Override
    public void execute(Arguments arguments) {
        if (!Function.checkArgs(arguments, 2, 1,
                ArgumentRequirement.ofSingle("install"),
                ArgumentRequirement.ofSingle("info"),
                ArgumentRequirement.ofValue("source"),
                ArgumentRequirement.ofValue("n"),
                ArgumentRequirement.ofValue("name"),
                ArgumentRequirement.ofValue("id"),
                ArgumentRequirement.ofValue("limit"),
                ArgumentRequirement.ofValue("url"),
                ArgumentRequirement.ofValue("game-version"),
                ArgumentRequirement.ofValue("v"),
                ArgumentRequirement.ofValue("version"))) return;
        int count = 0;
        if (arguments.contains("install")) count++;
        if (arguments.contains("info")) count++;
        if (arguments.contains("url")) count++;

        if (count == 0) {
            System.out.println(getString("MOD_CONTAINS_NOTHING"));
            return;
        } else if (count > 1) {
            System.out.println(getString("MOD_CONTAINS_TWO_OR_MORE"));
            return;
        }


        int todo = -1;
        if (arguments.contains("install")) todo = 0;
        else if (arguments.contains("info")) todo = 1;
        else if (arguments.contains("url")) todo = 3;

        if (todo == 3) {
            downloadMod(arguments.opt("url"));
            return;
        }

        String sourceStr = arguments.opt("source");

        if (isEmpty(sourceStr)) {
            JSONObject config = getConfig();
            sourceStr = getModDownloadSource(config);
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
            CurseForgeManager cf = new CurseForgeModManager();
            JSONObject mod = CurseForgeSearcher.search(cf, modNameInput, modIdInput, limit);
            if (mod == null) return;
            String modName = mod.optString("name");
            if (todo == 0) {
                int modId = mod.optInt("id");
                String modDownloadLink = cf.getDownloadLink(String.valueOf(modId), modName, arguments.opt("game-version"), arguments.opt("v", arguments.opt("version")), false, MOD_CF_DEPENDENCY_INSTALLER);
                if (isEmpty(modDownloadLink)) return;
                downloadMod(modDownloadLink);
            } else if (todo == 1) {
                cf.printInformation(mod, modName);
            }
        } else {
            ModrinthManager mr = new ModrinthModManager();
            ModrinthSearcher.Result result = ModrinthSearcher.search(mr, modNameInput, modIdInput, limit);
            if (result == null) return;
            JSONObject mod = result.mod, modByID = result.modByID;
            String modName = result.modName, modID = result.modID;

            if (todo == 0) {
                String modDownloadLink = mr.getDownloadLink(modID, modName, arguments.opt("game-version"), arguments.opt("v", arguments.opt("version")), false, MOD_MR_DEPENDENCY_INSTALLER);
                if (isEmpty(modDownloadLink)) return;
                downloadMod(modDownloadLink);
            } else if (todo == 1) {
                mr.printInformation(mod, modByID, null, null);
            }
        }
    }

    public static File askStorage(File last, String $NAME) {
        //0覆盖，返回last
        //1其他路径，返回新路径
        //2取消下载，返回null

        System.out.println(getString("CF_STORAGE_FILE_EXISTS_OPERATIONS"));
        int sel = InteractionUtils.inputInt(getString("CF_STORAGE_FILE_EXISTS_SELECT_OPERATION", last.getAbsolutePath()), 0, 2);
        //if(sel==Integer.MAX_VALUE)return null;
        switch (sel) {
            case 0:
                return last;
            case 1:
                return askStorageForNewPath(last, $NAME);
            case 2:
            default:
                return null;
        }
    }

    private static File askStorageForNewPath(File last, String $NAME) {
        System.out.print(getString("CF_STORAGE_FILE_EXISTS").replace("${NAME}", $NAME));
        String path;
        try {
            path = new Scanner(System.in).nextLine();
        } catch (NoSuchElementException e) {
            return null;
        }
        if (isEmpty(path)) return askStorageForNewPath(last, $NAME);
        File file = new File(path, last.getName());
        if (!file.exists()) return file;
        return askStorage(file, $NAME);
    }

    @Override
    public String getUsageName() {
        return "mod";
    }

    public static void downloadMod(String modDownloadLink) {
        if (isEmpty(modDownloadLink))
            return;
        File mods = new File(CMCL.gameDir, "mods");
        mods.mkdirs();
        String fileName;
        try {
            fileName = URLDecoder.decode(modDownloadLink.substring(modDownloadLink.lastIndexOf('/') + 1), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        if (Utils.isEmpty(fileName)) fileName = System.currentTimeMillis() + ".jar";
        File modFile = new File(mods, fileName);
        if (modFile.exists()) {
            File file = askStorage(modFile, getString("CF_BESEARCHED_MOD_ALC"));
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

    public static String getModDownloadSource(JSONObject config) {
        String sourceStr = config.optString("modDownloadSource");
        if (!sourceStr.equalsIgnoreCase("curseforge") && !sourceStr.equalsIgnoreCase("modrinth") && !sourceStr.equalsIgnoreCase("cf") && !sourceStr.equalsIgnoreCase("mr")) {
            List<Pair<String, Integer>> sources = new ArrayList<>(2);
            sources.add(0, new Pair<>("CurseForge", 0));
            sources.add(1, new Pair<>("Modrinth", 1));
            for (Pair<String, Integer> pair : sources) {
                System.out.printf("[%d]%s\n", pair.getValue(), pair.getKey());
            }
            int defaultDownloadSource = 0;
            int value = 0;
            System.out.print(Utils.getString("CONSOLE_CHOOSE_DOWNLOAD_SOURCE_CF_OR_MR", defaultDownloadSource));
            try {
                value = Integer.parseInt(new Scanner(System.in).nextLine());
            } catch (NumberFormatException | NoSuchElementException ignore) {
            }
            String mds;
            if (value == 1) {
                mds = "modrinth";
            } else {
                mds = "curseforge";
            }
            config.put("modDownloadSource", mds);
            Utils.saveConfig(config);
        }
        return config.optString("modDownloadSource");
    }
}