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

package com.mrshiehx.cmcl.modSources.modrinth;

import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.enums.ModrinthSection;
import com.mrshiehx.cmcl.modSources.Manager;
import com.mrshiehx.cmcl.modSources.curseforge.CurseForgeManager;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.cmcl.version.VersionUtils;
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
import java.util.stream.Collectors;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.isEmpty;

public abstract class ModrinthManager extends Manager<ModrinthSection> {

    public abstract ModrinthSection getSection();

    private static final String ROOT = "https://api.modrinth.com/v2/";
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    @Override
    public JSONObject search(String searchContent, int limit) {
        JSONArray searchResult;
        try {
            JSONObject jsonObject = new JSONObject(NetworkUtils.get(ROOT + "search?query=" + URLEncoder.encode(searchContent, StandardCharsets.UTF_8.name()) + "&limit=" + (limit < 0 ? 50 : limit) + "&facets=" + URLEncoder.encode("[[\"project_type:" + getSection().projectType + "\"]]", StandardCharsets.UTF_8.name())));
            searchResult = jsonObject.optJSONArray("hits");
        } catch (Exception e) {
            System.out.println(getString("MESSAGE_FAILED_SEARCH", e));
            return null;
        }

        List<JSONObject> list = JSONUtils.jsonArrayToJSONObjectList(searchResult);

        if (list.size() == 0) {
            System.out.println(getString("NO_SEARCH_RESULTS"));
            return null;
        }

        for (int i = list.size() - 1; i >= 0; i--) {
            try {
                JSONObject result = list.get(i);
                if (result == null) continue;
                String gameVersion = null;
                JSONArray versions = result.optJSONArray("versions");
                if (versions != null && versions.length() > 0) {

                    List<String> list2 = new LinkedList<>();
                    for (Object o : versions.toList()) {
                        if (o instanceof String) {
                            list2.add((String) o);
                        }
                    }
                    list2.sort((o1, o2) -> -VersionUtils.tryToCompareVersion(o1, o2));

                    if (list2.size() > 0) {
                        gameVersion = list2.get(0);
                    }
                }


                CurseForgeManager.printOne(i + 1, result.optString("title"), result.optString("description"), Ansi.ansi().fg(Ansi.Color.RED).a(result.optString("author")).toString(), gameVersion, null);
            } catch (Exception e) {
                System.out.println(getString("CF_FAILED_TO_SHOW_SOMEONE", i + 1, e).replace("${NAME}", getSection().nameAllLowerCase));
            }
        }

        int number = ConsoleUtils.inputInt(Utils.getString("CF_SELECT_TARGET", 1, list.size()).replace("${NAME}", getSection().nameAllLowerCase), 1, list.size());
        if (number != Integer.MAX_VALUE) {
            return list.get(number - 1);
        }
        return null;
    }

    public void printInformation(@Nullable JSONObject bySearch, JSONObject byID, @Nullable String modName, @Nullable String modId) {
        Map<String, String> information = new LinkedHashMap<>();
        //System.out.println(bySearch);


        String id = modId;
        String description = null;
        String author = null;
        String latestGameVersion = null;
        String dateModified = null;
        String dateCreated = null;
        int downloads = -1;
        Collection<String> categories1 = new HashSet<>();
        List<Pair<String, String>> donations = new LinkedList<>();

        if (bySearch != null) {
            id = bySearch.optString("project_id", modId);
            description = bySearch.optString("description");
            author = bySearch.optString("author");
            latestGameVersion = bySearch.optString("latest_version");
            dateModified = bySearch.optString("date_modified");
            dateCreated = bySearch.optString("date_created");
            String a = bySearch.optString("title");
            if (!isEmpty(a))
                modName = a;

            downloads = bySearch.optInt("downloads", -1);
            JSONArray categories = bySearch.optJSONArray("categories");
            if (categories != null && categories.length() > 0) {
                for (Object o : categories) {
                    if (o instanceof String) {
                        categories1.add((String) o);
                    }
                }
            }

        }

        if (byID != null) {
            if (isEmpty(description))
                description = byID.optString("description");
            if (isEmpty(dateModified))
                dateModified = byID.optString("updated");
            if (isEmpty(dateCreated))
                dateCreated = byID.optString("published");
            if (isEmpty(id))
                id = byID.optString("id");
            if (downloads < 0)
                downloads = byID.optInt("downloads", -1);
            if (isEmpty(modName))
                modName = byID.optString("title");

            String a = byID.optString("title");
            if (!isEmpty(a))
                modName = a;

            JSONArray categories = byID.optJSONArray("categories");
            if (categories != null && categories.length() > 0) {
                for (Object o : categories) {
                    if (o instanceof String) {
                        categories1.add((String) o);
                    }
                }
            }
            JSONArray donation_urls = byID.optJSONArray("donation_urls");

            if (donation_urls != null && donation_urls.length() > 0) {
                for (Object o : donation_urls) {
                    if (o instanceof JSONObject) {
                        String platform = ((JSONObject) o).optString("platform");
                        String url = ((JSONObject) o).optString("url");
                        if (!isEmpty(platform) && !isEmpty(url)) {
                            donations.add(new Pair<>(platform, url));
                        }
                    }
                }
            }
        }

        if (!isEmpty(modName))
            information.put(getSection().informationNameTip, modName);
        if (!isEmpty(id))
            information.put(getSection().informationIdTip, id);
        if (!isEmpty(description))
            information.put(getString("CF_INFORMATION_SUMMARY"), description);
        if (!isEmpty(author))
            information.put(getString("CF_INFORMATION_AUTHOR"), author);

        if (!isEmpty(latestGameVersion))
            information.put(getString("CF_INFORMATION_LATEST_GAME_VERSION"), latestGameVersion);

        if (categories1.size() > 0) {
            information.put(getString("CF_INFORMATION_CATEGORIES"), Arrays.toString(categories1.toArray()));
        }


        if (!Utils.isEmpty(dateModified) && dateModified.length() >= 19) {
            String dateString = CurseForgeManager.parseDate(dateModified);
            information.put(getString("CF_INFORMATION_DATE_MODIFIED"), dateString);
        }

        if (!Utils.isEmpty(dateCreated) && dateCreated.length() >= 19) {
            String dateString = CurseForgeManager.parseDate(dateCreated);
            information.put(getString("CF_INFORMATION_DATE_CREATED"), dateString);
        }

        if (downloads >= 0)
            information.put(getString("CF_INFORMATION_DOWNLOADS"), String.valueOf(downloads));


        if (donations.size() > 0) {
            StringBuilder donation = new StringBuilder();
            for (Pair<String, String> pair : donations) {
                String name = pair.getKey();
                donation.append('\n').append("      ").append(name).append('\n');
                String url = pair.getValue();
                donation.append(getString("CF_INFORMATION_DONATION_URL")).append(url);
            }
            information.put(getString("CF_INFORMATION_DONATION"), donation.toString());
        }


        if (byID != null) {
            String issueTrackerUrl = byID.optString("issues_url");
            if (!isEmpty(issueTrackerUrl))
                information.put(getString("CF_INFORMATION_ISSUE_TRACKER_URL"), issueTrackerUrl);

            String sourceUrl = byID.optString("source_url");
            if (!isEmpty(sourceUrl))
                information.put(getString("CF_INFORMATION_SOURCE_URL"), sourceUrl);

            String bodyUrl = byID.optString("body_url");
            if (!isEmpty(bodyUrl))
                information.put(getString("CF_INFORMATION_WEBSITE_URL"), bodyUrl);

            String wikiUrl = byID.optString("wiki_url");
            if (!isEmpty(wikiUrl))
                information.put(getString("CF_INFORMATION_WIKI_URL"), wikiUrl);

            String discordUrl = byID.optString("discord_url");
            if (!isEmpty(discordUrl))
                information.put(getString("CF_INFORMATION_DISCORD_URL"), discordUrl);
        }

        if (information.size() == 0) {
            System.out.println(getString("CF_INFORMATION_NOTHING", getSection().nameAllLowerCase));
        } else {
            System.out.println(modName + ":");//legal
            for (Map.Entry<String, String> entry : information.entrySet()) {
                System.out.print(entry.getKey());//legal
                System.out.println(entry.getValue());//legal
            }
        }
    }


    public JSONObject getByID(String id) throws IncorrectCategoryAddon, IOException {
        JSONObject mod = new JSONObject(NetworkUtils.get(ROOT + "project/" + id));

        /*JSONObject categorySection = mod.optJSONObject("categorySection");
        if (categorySection == null) {
            throw new IncorrectCategoryAddon(-1);
        }*/

        ModrinthSection a = ModrinthSection.xvalueOf(mod.optString("project_type"));
        if (a != getSection())
            throw new IncorrectCategoryAddon(a);

        return mod;
    }

    public String getDownloadLink(String modID, String modName, @Nullable String mcversion, @Nullable String addonVersion, Manager.DependencyInstaller dependencyInstaller) {
        JSONArray modAllVersionsJsonArrayFather;
        try {
            modAllVersionsJsonArrayFather = new JSONArray(NetworkUtils.get(ROOT + "project/" + modID + "/version"));
        } catch (Exception e) {
            System.out.println(getString("MOD_FAILED_TO_GET_ALL_FILES", e).replace("${NAME}", getNameAllLowerCase()));
            return null;
        }


        Map<String, ArrayList<Pair<JSONObject, JSONObject>>> modClassificationMap = new HashMap<>();
        //开始装载并排序
        for (int i = 0; i < modAllVersionsJsonArrayFather.length(); i++) {
            JSONObject modVersion = modAllVersionsJsonArrayFather.optJSONObject(i);
            if (modVersion == null) continue;
            JSONArray gameVersion = modVersion.optJSONArray("game_versions");
            for (int j = 0; j < gameVersion.length(); j++) {
                String ver = gameVersion.optString(j);
                ArrayList<Pair<JSONObject, JSONObject>> oneMcVerOfModSupport;
                oneMcVerOfModSupport = modClassificationMap.containsKey(ver) ? modClassificationMap.get(ver) : new ArrayList<>();
                //格式化时间 2019-07-22T01:56:42.27Z -> 2019-07-22T01:56:42
                modVersion.put("date_published", modVersion.optString("date_published").substring(0, 19));

                JSONArray files = modVersion.optJSONArray("files");
                if (files != null) {
                    for (Object object : files) {
                        if (object instanceof JSONObject) {
                            if (files.length() == 1) {
                                oneMcVerOfModSupport.add(new Pair<>((JSONObject) object, modVersion));
                            } else {
                                String url = ((JSONObject) object).optString("url");
                                String filename = ((JSONObject) object).optString("filename");
                                if (!isEmpty(url) &&
                                        !isEmpty(filename) &&
                                        //!filename.endsWith("-dev.jar")&&
                                        !filename.endsWith("-sources-dev.jar") &&
                                        !filename.endsWith("-sources.jar")) {
                                    oneMcVerOfModSupport.add(new Pair<>((JSONObject) object, modVersion));
                                }
                            }
                        }
                    }
                }

                addonFilesTimeSort(oneMcVerOfModSupport);
                modClassificationMap.put(ver, oneMcVerOfModSupport);
            }
        }


        String modSupportMinecraftVersion;


        if (!isEmpty(mcversion) && modClassificationMap.get(mcversion) != null) {
            modSupportMinecraftVersion = mcversion;
        } else {
            //Mod 支持的所有 MC 版本
            ArrayList<String> modSupportedMcVer = new ArrayList<>(modClassificationMap.keySet());

            //排序算法
            modSupportedMcVer.sort(VersionUtils.VERSION_COMPARATOR);
            Collections.reverse(modSupportedMcVer);

            if (isEmpty(modName)) {
                try {
                    modName = getByID(modID).optString("title");
                } catch (Exception ignored) {
                }
            }
            System.out.println(getString("CF_SUPPORTED_GAME_VERSION", modName));

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

        List<Pair<JSONObject, JSONObject>> versions = modClassificationMap.get(modSupportMinecraftVersion);
        if (versions == null)
            return null;
        if (versions.size() == 0) {
            System.out.println(getString("CF_NO_VERSION_FOR_GAME_VERSION", getNameAllLowerCase()));
            return null;
        }
        /*System.out.print('[');
        for(JSONObject j:versions){
            System.out.println(j);
            System.out.print(',');
        }
        System.out.print("\b]");*/
        Pair<JSONObject, JSONObject> targetFile = null;
        if (!isEmpty(addonVersion)) {
            List<Pair<JSONObject, JSONObject>> matches = versions.stream().filter(pair -> {
                String version_number = pair.getValue().optString("version_number");
                return version_number.contains(addonVersion);
            }).collect(Collectors.toList());
            if (matches.size() == 1) {
                targetFile = matches.get(0);
            }
        }

        if (targetFile == null) {
            AnsiConsole.systemInstall();
            for (int i = versions.size() - 1; i >= 0; i--) {
                System.out.print(Ansi.ansi().fg(Ansi.Color.WHITE).a("[") + "" + Ansi.ansi().fg(Ansi.Color.CYAN).a(i + 1) + Ansi.ansi().fg(Ansi.Color.WHITE).a("]" + (versions.get(i).getKey().optString("filename")) + "\n"));
            }
            AnsiConsole.systemUninstall();


            int modVersionOfSingleMcVersion = ConsoleUtils.inputInt(getString("CF_INPUT_VERSION", 1, versions.size()).replace("${NAME}", getNameAllLowerCase()), 1, versions.size(), true, -1);

            if (modVersionOfSingleMcVersion == Integer.MAX_VALUE || modVersionOfSingleMcVersion == -1)
                return null;

            targetFile = versions.get(modVersionOfSingleMcVersion - 1);
        }

        //System.out.println(targetFile.getValue());
        JSONArray jsonArray = targetFile.getValue().optJSONArray("dependencies");
        if (jsonArray != null && jsonArray.length() > 0) {
            Map<String, String> list = new HashMap<>();
            for (Object object : jsonArray) {
                if (object instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) object;
                    String dmodid = jsonObject.optString("project_id");
                    String name = null;
                    try {
                        JSONObject head = getByID(dmodid);
                        name = head.optString("title");
                    } catch (Exception ignore) {
                    }
                    if (!isEmpty(dmodid))
                        list.put(dmodid, name);
                }
            }
            if (list.size() > 0) {
                System.out.println();
                System.out.println(getString("CF_DEPENDENCIES_TIP").replace("${NAME}", getNameAllLowerCase()));
                int i = 0;
                for (Map.Entry<String, String> entry : list.entrySet()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String id = entry.getKey();
                    String name = entry.getValue();
                    stringBuilder.append(getString("CF_DEPENDENCY_INFORMATION_ID_STRING", id)).append('\n');
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
                for (Map.Entry<String, String> entry : list.entrySet()) {
                    dependencyInstaller.install(modSupportMinecraftVersion, entry.getValue(), entry.getKey());
                }
            }
        }
        return targetFile.getKey().optString("url");
    }

    @Override
    protected String getNameAllLowerCase() {
        return getSection().nameAllLowerCase;
    }

    //按时间排序每个 JsonObject
    private static void addonFilesTimeSort(ArrayList<Pair<JSONObject, JSONObject>> list) {
        list.sort((o1, o2) -> {
            //2021-06-14T15:14:23.68Z
            try {
                Date dt1 = TIME_FORMAT.parse(o1.getKey().optString("date_published"));
                Date dt2 = TIME_FORMAT.parse(o2.getKey().optString("date_published"));
                return Long.compare(dt2.getTime(), dt1.getTime());
            } catch (Exception ignore) {
            }
            return 0;
        });
    }

    public static class IncorrectCategoryAddon extends Exception {
        public ModrinthSection section;

        public IncorrectCategoryAddon(ModrinthSection section) {
            super();
            this.section = section;
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
