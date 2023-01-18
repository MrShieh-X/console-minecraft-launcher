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

package com.mrshiehx.cmcl.modSources.curseforge;

import com.mrshiehx.cmcl.CMCL;
import com.mrshiehx.cmcl.enums.CurseForgeSection;
import com.mrshiehx.cmcl.modSources.Manager;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.console.ConsoleUtils;
import com.mrshiehx.cmcl.utils.internet.NetworkUtils;
import com.mrshiehx.cmcl.utils.json.JSONUtils;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.isEmpty;

public abstract class CurseForgeManager extends Manager<CurseForgeSection> {
    private static final String SEARCH_ADDONS = "https://api.curseforge.com/v1/mods/search?gameId=432";
    private static final String GET_ADDON_INFORMATION = "https://api.curseforge.com/v1/mods/";
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    public abstract CurseForgeSection getSection();

    protected abstract String getNameAllLowerCase();

    protected abstract String getNameFirstUpperCase();

    public String getDownloadLink(String modId, String modName, @Nullable String mcversion) {
        JSONArray modAllVersionsJsonArrayFather;
        try {
            JSONObject jsonObject = new JSONObject(NetworkUtils.curseForgeGet(GET_ADDON_INFORMATION + modId + "/files?pageSize=10000"));
            modAllVersionsJsonArrayFather = jsonObject.optJSONArray("data");
        } catch (Exception e) {
            System.out.println(getString("MOD_FAILED_TO_GET_ALL_FILES", e).replace("${NAME}", getNameAllLowerCase()));
            return null;
        }

        //用来排序 https://addons-ecs.forgesvc.net/api/v2/addon/297344/files 的 Map，因为 Get 下来的 Json 是乱序的
        //String 是支持的 MC 版本，因为一个模组可以支持多个版本，一个文件一个 JsonObject ，所以采用 ArrayList<JSONObject> ，这是个一对多的 HashMap
        //一个 MC 版本对应多个 JsonObject
        Map<String, ArrayList<JSONObject>> modClassificationMap = new HashMap<>();
        //开始装载并排序
        for (int i = 0; i < modAllVersionsJsonArrayFather.length(); i++) {
            JSONObject modVersion = modAllVersionsJsonArrayFather.optJSONObject(i);
            if (modVersion == null) continue;
            JSONArray gameVersion = modVersion.optJSONArray("gameVersions");
            for (int j = 0; j < gameVersion.length(); j++) {
                String ver = gameVersion.optString(j);
                ArrayList<JSONObject> oneMcVerOfModSupport;
                oneMcVerOfModSupport = modClassificationMap.containsKey(ver) ? modClassificationMap.get(ver) : new ArrayList<>();
                //格式化时间 2019-07-22T01:56:42.27Z -> 2019-07-22T01:56:42
                modVersion.put("fileDate", modVersion.optString("fileDate").substring(0, 19));
                oneMcVerOfModSupport.add(modVersion);
                addonFilesTimeSort(oneMcVerOfModSupport);
                modClassificationMap.put(ver, oneMcVerOfModSupport);
            }
        }

        modClassificationMap.remove("Fabric");
        modClassificationMap.remove("Forge");
        modClassificationMap.remove("Rift");


        String modSupportMinecraftVersion;

        if (!isEmpty(mcversion) && modClassificationMap.get(mcversion) != null) {
            modSupportMinecraftVersion = mcversion;
        } else {
            //Mod 支持的所有 MC 版本
            ArrayList<String> modSupportedMcVer = new ArrayList<>();

            //循环遍历 Map ，并将该 Mod 所有支持的 MC 版本提取出来并装载到 modSupportedMcVer 进行排序，因为这玩意儿在 Json 里面是乱序的
            //比如：1.15.2  1.18.1  1.16.5  1.16.4  1.12  1.14.4
            for (Map.Entry<String, ArrayList<JSONObject>> entry : modClassificationMap.entrySet()) {
                String version = entry.getKey().replace("-Snapshot", ".-1");
                if (!modSupportedMcVer.contains(version)) {
                    modSupportedMcVer.add(version);
                }
            }

            //排序算法
            modSupportedMcVer.sort((o1, o2) -> {
                String[] o1s = o1.split("\\.");
                String[] o2s = o2.split("\\.");

                if (o1s.length == 0 || o2s.length == 0) return 0;

                int[] o1i = new int[o1s.length];
                int[] o2i = new int[o2s.length];
                try {
                    for (int i = 0; i < o1s.length; i++) {
                        String o1String = o1s[i];
                        o1i[i] = Integer.parseInt(o1String);
                    }
                    for (int i = 0; i < o2s.length; i++) {
                        String o2String = o2s[i];
                        o2i[i] = Integer.parseInt(o2String);
                    }
                } catch (Exception e) {
                    return 0;
                }

                if (o1i[1] > o2i[1]) {
                    return -1;
                } else if (o1i[1] < o2i[1]) {
                    return 1;
                } else {
                    if (o1i.length >= 3 && o2i.length >= 3) {
                        return Integer.compare(o2i[2], o1i[2]);
                    } else if (o1i.length >= 3) {
                        return Integer.compare(0, o1i[2]);
                    } else if (o2i.length >= 3) {
                        return Integer.compare(o2i[2], 0);
                    } else {
                        return 0;
                    }
                }
            });

            System.out.println(getString("CF_SUPPORTED_GAME_VERSION", modName));

            for (int i = 0; i < modSupportedMcVer.size(); i++) {
                String version = modSupportedMcVer.get(i);
                if (version.endsWith(".-1")) {
                    modSupportedMcVer.set(i, version.replace(".-1", "-Snapshot"));
                }
            }

            System.out.print('[');
            for (int i = 0; i < modSupportedMcVer.size(); i++) {
                String version = modSupportedMcVer.get(i);
                boolean containSpace = version.contains(" ");
                if (containSpace) System.out.print("\"");
                System.out.print(version);//legal
                if (containSpace) System.out.print("\"");
                if (i + 1 != modSupportedMcVer.size()) {
                    System.out.print(", ");
                }
            }
            System.out.print("]\n");
            modSupportMinecraftVersion = ConsoleUtils.inputStringInFilter(getString("CF_INPUT_GAME_VERSION"), getString("CONSOLE_INPUT_STRING_NOT_FOUND"), modSupportedMcVer::contains);
        }

        List<JSONObject> versions = modClassificationMap.get(modSupportMinecraftVersion);
        if (versions == null)
            return null;
        if (versions.size() == 0) {
            System.out.println(getString("CF_NO_VERSION_FOR_GAME_VERSION", getNameAllLowerCase()));
            return null;
        }
        AnsiConsole.systemInstall();
        for (int i = versions.size() - 1; i >= 0; i--) {
            System.out.print(Ansi.ansi().fg(Ansi.Color.WHITE).a("[") + "" + Ansi.ansi().fg(Ansi.Color.CYAN).a(i + 1) + Ansi.ansi().fg(Ansi.Color.WHITE).a("]" + (versions.get(i).optString("fileName")) + "\n"));
        }
        AnsiConsole.systemUninstall();

        int modVersionOfSingleMcVersion = ConsoleUtils.inputInt(getString("CF_INPUT_VERSION", 1, versions.size()).replace("${NAME}", getNameAllLowerCase()), 1, versions.size(), true, -1);

        if (modVersionOfSingleMcVersion == Integer.MAX_VALUE || modVersionOfSingleMcVersion == -1)
            return null;

        JSONObject targetFile = versions.get(modVersionOfSingleMcVersion - 1);

        JSONArray jsonArray = targetFile.optJSONArray("dependencies");
        if (jsonArray != null && jsonArray.length() > 0) {
            Map<Integer, String> list = new HashMap<>();
            for (Object object : jsonArray) {
                if (object instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) object;
                    if (jsonObject.optInt("relationType") == 3 && jsonObject.has("modId")) {
                        int addonId = jsonObject.optInt("modId");
                        String name = null;
                        try {
                            JSONObject head = new JSONObject(NetworkUtils.curseForgeGet(GET_ADDON_INFORMATION + addonId)).optJSONObject("data");
                            name = head.optString("name");
                        } catch (Exception ignore) {
                        }
                        list.put(addonId, name);
                    }

                }
            }
            if (list.size() > 0) {
                System.out.println();
                System.out.println(getString("CF_DEPENDENCIES_TIP").replace("${NAME}", getNameAllLowerCase()));
                int i = 0;
                for (Map.Entry<Integer, String> entry : list.entrySet()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    int id = entry.getKey();
                    String name = entry.getValue();
                    stringBuilder.append(getString("CF_DEPENDENCY_INFORMATION_ID", id)).append('\n');
                    if (!isEmpty(name)) {
                        stringBuilder.append(getString("CF_DEPENDENCY_INFORMATION_NAME", name));
                    }
                    if (i + 1 < list.size()) {
                        stringBuilder.append('\n');
                    }
                    System.out.println(stringBuilder);//legal
                    i++;
                }
                System.out.println();
            }
        }
        return targetFile.optString("downloadUrl");
    }

    @Override
    public JSONObject search(String searchContent, int limit) {
        JSONArray searchResult;
        try {
            JSONObject jsonObject = new JSONObject(NetworkUtils.curseForgeGet(SEARCH_ADDONS + "&pageSize=" + limit + "&classId=" + getSection().sectionId + "&searchFilter=" + URLEncoder.encode(searchContent, StandardCharsets.UTF_8.name())));
            searchResult = jsonObject.optJSONArray("data");
        } catch (Exception e) {
            System.out.println(getString("MESSAGE_FAILED_SEARCH", e));
            return null;
        }

        List<JSONObject> list = JSONUtils.jsonArrayToJSONObjectList(searchResult);

        if (list.size() == 0) {
            System.out.println(getString("NO_SEARCH_RESULTS"));
            return null;
        }

        Collections.reverse(list);


        for (int i = list.size() - 1; i >= 0; i--) {
            try {
                String gameVersion = null;
                String projectFileName = null;
                JSONObject result = list.get(i);
                if (result == null) continue;
                JSONArray gameVersionLatestFiles = result.optJSONArray("latestFilesIndexes");
                if (gameVersionLatestFiles != null && gameVersionLatestFiles.length() > 0) {
                    JSONObject first = gameVersionLatestFiles.optJSONObject(0);
                    if (first != null) {
                        gameVersion = first.optString("gameVersion");
                        projectFileName = first.optString("filename");
                    }
                }


                printOne(i + 1, result.optString("name"), result.optString("summary"), getAuthor(result.optJSONArray("authors")), gameVersion, projectFileName);
            } catch (Exception e) {
                System.out.println(getString("CF_FAILED_TO_SHOW_SOMEONE", i + 1, e).replace("${NAME}", getNameAllLowerCase()));
            }
        }

        int number = ConsoleUtils.inputInt(Utils.getString("CF_SELECT_TARGET", 1, list.size()).replace("${NAME}", getNameAllLowerCase()), 1, list.size());
        if (number != Integer.MAX_VALUE) {
            return list.get(number - 1);
        }
        return null;
    }


    public static void printOne(int order, String name, String summary, String author, String latestGameVersion, String latestFileName) {
        AnsiConsole.systemInstall();
        if (isEmpty(name)) return;
        if (!isEmpty(author)) {
            author = Ansi.ansi().fg(Ansi.Color.WHITE).a("/ ") + "" + author + " ";
        } else {
            author = "";
        }

        String gameVersionAndFileName;

        StringBuilder stringBuilder = new StringBuilder();
        boolean notEmptyBoth = !isEmpty(latestGameVersion) && !isEmpty(latestFileName);
        if (!isEmpty(latestGameVersion) || !isEmpty(latestFileName)) {
            stringBuilder.append("(");
        }
        if (!isEmpty(latestGameVersion)) {
            stringBuilder.append(latestGameVersion);
        }
        if (notEmptyBoth) {
            stringBuilder.append(", ");
        }
        if (!isEmpty(latestFileName)) {
            stringBuilder.append(latestFileName);
        }

        if (!isEmpty(latestGameVersion) || !isEmpty(latestFileName)) {
            stringBuilder.append(")");
        }


        gameVersionAndFileName = stringBuilder.toString();


        System.out.println(
                Ansi.ansi().fg(Ansi.Color.CYAN).a(order) + " " +
                        Ansi.ansi().fg(Ansi.Color.GREEN).a(
                                name) +
                        Ansi.ansi().fg(Ansi.Color.WHITE).a(" ") + author +
                        (!Utils.isEmpty(gameVersionAndFileName) ? Ansi.ansi().fg(Ansi.Color.WHITE).a(gameVersionAndFileName) : "")
        );


        AnsiConsole.systemUninstall();

        if (!Utils.isEmpty(summary)) System.out.println("    " + summary);
    }


    private static String getAuthor(JSONArray authors) {
        if (authors == null || authors.length() == 0) return null;

        String first = null;
        int count = 0;
        for (int i = 0; i < authors.length(); i++) {
            JSONObject jsonObject = authors.optJSONObject(i);
            if (jsonObject == null) continue;
            String name = jsonObject.optString("name");
            if (isEmpty(name)) continue;
            if (i == 0) {
                first = name;
            } else {
                count++;
            }
        }
        if (first == null) return null;
        return count == 0 ? Ansi.ansi().fg(Ansi.Color.RED).a(first).toString() : (Ansi.ansi().fg(Ansi.Color.RED).a(first) + " " + Ansi.ansi().fg(Ansi.Color.WHITE).a(getString("CF_AUTHOR_MORE", count)));
    }

    public JSONObject getByID(String modId) throws NotMinecraftAddon, IncorrectCategoryAddon, IOException {
        JSONObject mod = new JSONObject(NetworkUtils.curseForgeGet(GET_ADDON_INFORMATION + modId)).optJSONObject("data");
        int gameId = mod.optInt("gameId");
        if (gameId != 432) {
            throw new NotMinecraftAddon(gameId);
        }
        /*JSONObject categorySection = mod.optJSONObject("categorySection");
        if (categorySection == null) {
            throw new IncorrectCategoryAddon(-1);
        }*/

        int a = mod.optInt("classId");
        if (a != getSection().sectionId)
            throw new IncorrectCategoryAddon(a);

        return mod;
    }


    //按时间排序每个 JsonObject
    private static void addonFilesTimeSort(List<JSONObject> list) {
        list.sort((o1, o2) -> {
            //2021-06-14T15:14:23.68Z
            try {
                Date dt1 = TIME_FORMAT.parse(o1.optString("fileDate"));
                Date dt2 = TIME_FORMAT.parse(o2.optString("fileDate"));
                return Long.compare(dt2.getTime(), dt1.getTime());
            } catch (Exception ignore) {
            }
            return 0;
        });
    }

    public void printInformation(JSONObject mod, String modName) {
        Map<String, String> information = new LinkedHashMap<>();
        //System.out.println(mod);
        if (!isEmpty(modName))
            information.put(getSection().informationNameTip, modName);
        String id = mod.optString("id");
        if (!isEmpty(id))
            information.put(getSection().informationIdTip, id);
        String summary = mod.optString("summary");
        if (!isEmpty(summary))
            information.put(getString("CF_INFORMATION_SUMMARY"), summary);

        JSONArray authorsJSONArray = mod.optJSONArray("authors");
        if (authorsJSONArray != null && authorsJSONArray.length() > 0) {
            StringBuilder author = new StringBuilder();
            List<JSONObject> authors = JSONUtils.jsonArrayToJSONObjectList(authorsJSONArray);
            for (JSONObject jsonObject : authors) {
                String name = jsonObject.optString("name");
                if (!isEmpty(name)) {
                    author.append("\n      ").append(name).append('\n');
                    String url = jsonObject.optString("url");
                    if (!isEmpty(url)) {
                        author.append(getString("CF_INFORMATION_AUTHOR_URL")).append(url);
                    }
                }
            }
            information.put(getString("CF_INFORMATION_AUTHORS"), author.toString());
        }
        JSONArray gameVersionLatestFiles = mod.optJSONArray("latestFilesIndexes");
        if (gameVersionLatestFiles != null && gameVersionLatestFiles.length() > 0) {
            List<JSONObject> gameVersionLatestFilesList = JSONUtils.jsonArrayToJSONObjectList(gameVersionLatestFiles);
            if (gameVersionLatestFilesList.size() > 0) {
                JSONObject first = gameVersionLatestFilesList.get(0);
                String gameVersion = first.optString("gameVersion");
                if (!isEmpty(gameVersion))
                    information.put(getString("CF_INFORMATION_LATEST_GAME_VERSION"), gameVersion);
            }
        }

        String dateModified = mod.optString("dateModified");
        if (!Utils.isEmpty(dateModified) && dateModified.length() >= 19) {
            String dateString = parseDate(dateModified);
            information.put(getString("CF_INFORMATION_DATE_MODIFIED"), dateString);
        }

        String dateCreated = mod.optString("dateCreated");
        if (!Utils.isEmpty(dateCreated) && dateCreated.length() >= 19) {
            String dateString = parseDate(dateCreated);
            information.put(getString("CF_INFORMATION_DATE_CREATED"), dateString);
        }

        String dateReleased = mod.optString("dateReleased");
        if (!Utils.isEmpty(dateReleased) && dateReleased.length() >= 19) {
            String dateString = parseDate(dateReleased);
            information.put(getString("CF_INFORMATION_DATE_RELEASED"), dateString);
        }


        JSONObject links = mod.optJSONObject("links");
        if (links != null) {
            String issueTrackerUrl = links.optString("issuesUrl");
            if (!isEmpty(issueTrackerUrl))
                information.put(getString("CF_INFORMATION_ISSUE_TRACKER_URL"), issueTrackerUrl);

            String sourceUrl = links.optString("sourceUrl");
            if (!isEmpty(sourceUrl))
                information.put(getString("CF_INFORMATION_SOURCE_URL"), sourceUrl);

            String websiteUrl = links.optString("websiteUrl");
            if (!isEmpty(websiteUrl))
                information.put(getString("CF_INFORMATION_WEBSITE_URL"), websiteUrl);

            String wikiUrl = links.optString("wikiUrl");
            if (!isEmpty(wikiUrl))
                information.put(getString("CF_INFORMATION_WIKI_URL"), wikiUrl);
        }

        if (information.size() == 0) {
            System.out.println(getString("CF_INFORMATION_NOTHING", getNameAllLowerCase()));
        } else {
            System.out.println(modName + ":");//legal
            for (Map.Entry<String, String> entry : information.entrySet()) {
                System.out.print(entry.getKey());//legal
                System.out.println(entry.getValue());//legal
            }
        }
    }

    public static String parseDate(String sourceDate) {
        String dateString;
        try {
            Date date = TIME_FORMAT.parse(sourceDate.substring(0, 19) + "+0000");
            SimpleDateFormat format = new SimpleDateFormat(getString("TIME_FORMAT"), CMCL.getLocale());
            dateString = format.format(date) + " (" + TimeZone.getDefault().getDisplayName() + ")";
        } catch (Exception e) {
            dateString = getString("EXCEPTION_UNABLE_PARSE");
        }
        return dateString;
    }


    public static class NotMinecraftAddon extends Exception {
        public int gameId;

        public NotMinecraftAddon(int gameId) {
            super();
            this.gameId = gameId;
        }

        public NotMinecraftAddon(String message) {
            super(message);
        }

        public NotMinecraftAddon(String message, Throwable cause) {
            super(message, cause);
        }

        public NotMinecraftAddon(Throwable cause) {
            super(cause);
        }
    }

    public static class IncorrectCategoryAddon extends Exception {
        public int gameCategoryId;

        public IncorrectCategoryAddon(int gameCategoryId) {
            super();
            this.gameCategoryId = gameCategoryId;
        }

        public IncorrectCategoryAddon(String message) {
            super(message);
        }

        public IncorrectCategoryAddon(String message, Throwable cause) {
            super(message, cause);
        }

        public IncorrectCategoryAddon(Throwable cause) {
            super(cause);
        }
    }
}
