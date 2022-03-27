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
import com.mrshiehx.cmcl.utils.ConsoleUtils;
import com.mrshiehx.cmcl.utils.PercentageTextProgress;
import com.mrshiehx.cmcl.utils.Utils;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.isEmpty;

public class ModOption implements Option {
    private static final String SEARCH_ADDONS = "https://addons-ecs.forgesvc.net/api/v2/addon/search?gameId=432";
    private static final String GET_ADDON_INFORMATION = "https://addons-ecs.forgesvc.net/api/v2/addon/";
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    @Override
    public void execute(Arguments arguments) {
        Argument subOption = arguments.opt(1);
        if (subOption == null) {
            System.out.println(getString("CONSOLE_GET_USAGE"));
            return;
        }
        String key = subOption.key;

        if (!(subOption instanceof ValueArgument)) {

            System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
            return;
        }
        ValueArgument valueArgument = (ValueArgument) subOption;

        String value = valueArgument.value;
        JSONObject mod = selectMod(value);
        if (mod == null) return;

        String modName = mod.optString("name");
        switch (key.toLowerCase()) {
            case "i":

                /*System.out.println(getString("MOD_SUPPORTED_GAME_VERSION",modName));
                for (int i = 0; i < mod.getJSONArray("gameVersionLatestFiles").length(); i++) {
                    System.out.println("(" + (i + 1) + ")" + mod.getJSONArray("gameVersionLatestFiles").getJSONObject(i).optString("gameVersion"));
                }*/

                int modId = mod.optInt("id");
                JSONArray modAllVersionsJsonArrayFather;

                try {
                    modAllVersionsJsonArrayFather = new JSONArray(Utils.get(GET_ADDON_INFORMATION + modId + "/files"));
                } catch (Exception e) {
                    System.out.println(getString("MOD_FAILED_TO_GET_ALL_FILES", e));
                    return;
                }

                //用来排序 https://addons-ecs.forgesvc.net/api/v2/addon/297344/files 的 Map，因为 Get 下来的 Json 是乱序的
                //String 是支持的 MC 版本，因为一个模组可以支持多个版本，一个文件一个 JsonObject ，所以采用 ArrayList<JSONObject> ，这是个一对多的 HashMap
                //一个 MC 版本对应多个 JsonObject
                Map<String, ArrayList<JSONObject>> modClassificationMap = new HashMap<>();
                //开始装载并排序
                for (int i = 0; i < modAllVersionsJsonArrayFather.length(); i++) {
                    JSONObject modVersion = modAllVersionsJsonArrayFather.optJSONObject(i);
                    if (modVersion == null) continue;
                    JSONArray gameVersion = modVersion.optJSONArray("gameVersion");
                    for (int j = 0; j < gameVersion.length(); j++) {
                        String ver = gameVersion.optString(j);
                        ArrayList<JSONObject> oneMcVerOfModSupport;
                        oneMcVerOfModSupport = modClassificationMap.containsKey(ver) ? modClassificationMap.get(ver) : new ArrayList<>();
                        //格式化时间 2019-07-22T01:56:42.27Z -> 2019-07-22T01:56:42
                        modVersion.put("fileDate", modVersion.optString("fileDate").substring(0, 19));
                        oneMcVerOfModSupport.add(modVersion);
                        modsTimeSort(oneMcVerOfModSupport);
                        modClassificationMap.put(ver, oneMcVerOfModSupport);
                    }
                }

                modClassificationMap.remove("Fabric");
                modClassificationMap.remove("Forge");
                modClassificationMap.remove("Rift");

                //Mod 支持的所有 MC 版本
                ArrayList<String> modSupportedMcVer = new ArrayList<>();

                //循环遍历 Map ，并将该 Mod 所有支持的 MC 版本提取出来并装载到 modSupportedMcVer 进行排序，因为这玩意儿在 Json 里面是乱序的
                //比如：1.15.2  1.18.1  1.16.5  1.16.4  1.12  1.14.4
                for (Map.Entry<String, ArrayList<JSONObject>> entry : modClassificationMap.entrySet()) {
                    //System.out.println(entry.getKey() + ":");
                    String version = entry.getKey().replace("-Snapshot", ".-1");
                    if (!modSupportedMcVer.contains(version)) {
                        modSupportedMcVer.add(version);
                    }
                    /*for (int i = 0; i < entry.getValue().size(); i++) {
                        System.out.println(entry.getValue().get(i).optString("fileName"));
                    }*/
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

                System.out.println(getString("MOD_SUPPORTED_GAME_VERSION", modName));

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
                    System.out.print(version);
                    if (containSpace) System.out.print("\"");
                    if (i + 1 != modSupportedMcVer.size()) {
                        System.out.print(", ");
                    }
                }

                System.out.print("]\n");

                String modSupportMinecraftVersion = ConsoleUtils.inputString(getString("MOD_INPUT_GAME_VERSION"), modSupportedMcVer);
                List<JSONObject> versions = modClassificationMap.get(modSupportMinecraftVersion);
                if (versions == null) return;
                if (versions.size() == 0) {
                    System.out.println(getString("MOD_NO_VERSION_FOR_GAME_VERSION"));
                    return;
                }
                AnsiConsole.systemInstall();
                for (int i = versions.size() - 1; i >= 0; i--) {
                    System.out.print(Ansi.ansi().fg(Ansi.Color.WHITE).a("[") + "" + Ansi.ansi().fg(Ansi.Color.CYAN).a(i + 1) + Ansi.ansi().fg(Ansi.Color.WHITE).a("]" + (versions.get(i).optString("fileName")) + "\n"));
                }
                AnsiConsole.systemUninstall();

                int modVersionOfSingleMcVersion = ConsoleUtils.inputInt(getString("MOD_INPUT_MOD_VERSION", 1, versions.size()), 1, versions.size()) - 1;

                if (modVersionOfSingleMcVersion == Integer.MAX_VALUE - 1)
                    return;
                //System.out.println(modClassificationMap.get(modSupportMinecraftVersion).get(modVersionOfSingleMcVersion).optString("downloadUrl"));

                String modDownloadLink = modClassificationMap.get(modSupportMinecraftVersion).get(modVersionOfSingleMcVersion).optString("downloadUrl");
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
                    System.out.println(getString("MESSAGE_FAILED_DOWNLOAD_FILE_WITH_REASON", fileName, e));
                }

                break;

            case "s":
                Map<String, String> information = new LinkedHashMap<>();
                if (!isEmpty(modName)) information.put(getString("MOD_INFORMATION_NAME"), modName);
                String id = mod.optString("id");
                if (!isEmpty(id)) information.put(getString("MOD_INFORMATION_ID"), id);
                String summary = mod.optString("summary");
                if (!isEmpty(summary)) information.put(getString("MOD_INFORMATION_SUMMARY"), summary);

                JSONArray authorsJSONArray = mod.optJSONArray("authors");
                if (authorsJSONArray != null && authorsJSONArray.length() > 0) {
                    StringBuilder author = new StringBuilder();
                    List<JSONObject> authors = Utils.jsonArrayToJSONObjectList(authorsJSONArray);
                    for (JSONObject jsonObject : authors) {
                        String name = jsonObject.optString("name");
                        if (!isEmpty(name)) {
                            author.append("\n      ").append(name).append('\n');
                            String url = jsonObject.optString("url");
                            if (!isEmpty(url)) {
                                author.append(getString("MOD_INFORMATION_AUTHOR_URL")).append(url);
                            }
                        }
                    }
                    information.put(getString("MOD_INFORMATION_AUTHORS"), author.toString());
                }
                JSONArray gameVersionLatestFiles = mod.optJSONArray("gameVersionLatestFiles");
                if (gameVersionLatestFiles != null && gameVersionLatestFiles.length() > 0) {
                    List<JSONObject> gameVersionLatestFilesList = Utils.jsonArrayToJSONObjectList(gameVersionLatestFiles);
                    if (gameVersionLatestFilesList.size() > 0) {
                        JSONObject first = gameVersionLatestFilesList.get(0);
                        String gameVersion = first.optString("gameVersion");
                        if (!isEmpty(gameVersion))
                            information.put(getString("MOD_INFORMATION_LATEST_GAME_VERSION"), gameVersion);
                    }
                }
                JSONArray modLoaders = mod.optJSONArray("modLoaders");
                if (modLoaders != null && modLoaders.length() > 0)
                    information.put(getString("MOD_INFORMATION_MOD_LOADERS"), Arrays.toString(((modLoaders.toList()).toArray())));

                String dateModified = mod.optString("dateModified");
                if (!Utils.isEmpty(dateModified) && dateModified.length() >= 19) {
                    String dateString;
                    try {
                        Date date = TIME_FORMAT.parse(dateModified.substring(0, 19) + "+0000");
                        SimpleDateFormat format = new SimpleDateFormat(getString("TIME_FORMAT"));
                        dateString = format.format(date) + " (" + Utils.getTimezoneName() + ")";
                    } catch (Exception e) {
                        dateString = getString("EXCEPTION_UNABLE_PARSE");
                    }
                    information.put(getString("MOD_INFORMATION_DATE_MODIFIED"), dateString);
                }

                String dateCreated = mod.optString("dateCreated");
                if (!Utils.isEmpty(dateCreated) && dateCreated.length() >= 19) {
                    String dateString;
                    try {
                        Date date = TIME_FORMAT.parse(dateCreated.substring(0, 19) + "+0000");
                        SimpleDateFormat format = new SimpleDateFormat(getString("TIME_FORMAT"));
                        dateString = format.format(date) + " (" + Utils.getTimezoneName() + ")";
                    } catch (Exception e) {
                        dateString = getString("EXCEPTION_UNABLE_PARSE");
                    }
                    information.put(getString("MOD_INFORMATION_DATE_CREATED"), dateString);
                }

                String dateReleased = mod.optString("dateReleased");
                if (!Utils.isEmpty(dateReleased) && dateReleased.length() >= 19) {
                    String dateString;
                    try {
                        Date date = TIME_FORMAT.parse(dateReleased.substring(0, 19) + "+0000");
                        SimpleDateFormat format = new SimpleDateFormat(getString("TIME_FORMAT"));
                        dateString = format.format(date) + " (" + Utils.getTimezoneName() + ")";
                    } catch (Exception e) {
                        dateString = getString("EXCEPTION_UNABLE_PARSE");
                    }
                    information.put(getString("MOD_INFORMATION_DATE_RELEASED"), dateString);
                }


                String issueTrackerUrl = mod.optString("issueTrackerUrl");
                if (!isEmpty(issueTrackerUrl))
                    information.put(getString("MOD_INFORMATION_ISSUE_TRACKER_URL"), issueTrackerUrl);
                String sourceUrl = mod.optString("sourceUrl");
                if (!isEmpty(sourceUrl)) information.put(getString("MOD_INFORMATION_SOURCE_URL"), sourceUrl);
                String websiteUrl = mod.optString("websiteUrl");
                if (!isEmpty(websiteUrl)) information.put(getString("MOD_INFORMATION_WEBSITE_URL"), websiteUrl);


                if (information.size() == 0) {
                    System.out.println(getString("MOD_INFORMATION_NOTHING"));
                } else {
                    System.out.println(modName + ":");
                    for (Map.Entry<String, String> entry : information.entrySet()) {
                        System.out.print(entry.getKey());
                        System.out.println(entry.getValue());
                    }
                }
                break;

            default:
                System.out.println(getString("CONSOLE_UNKNOWN_OPTION", key));
                break;
        }
    }

    private File askStorage(File last) {
        System.out.print(getString("MOD_STORAGE_FILE_EXISTS", last.getAbsolutePath()));
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

    private static String getAuthor(JSONArray authors) {
        if (authors == null || authors.length() == 0) return null;

        String first = null;
        int count = 0;
        for (int i = 0; i < authors.length(); i++) {
            JSONObject jsonObject = authors.optJSONObject(i);
            if (jsonObject != null) {
                String name = jsonObject.optString("name");
                if (!isEmpty(name)) {
                    if (i == 0) {
                        first = name;
                    } else {
                        count++;
                    }
                }
            }
        }
        if (first == null) return null;
        return count == 0 ? Ansi.ansi().fg(Ansi.Color.RED).a(first).toString() : (Ansi.ansi().fg(Ansi.Color.RED).a(first) + " " + Ansi.ansi().fg(Ansi.Color.WHITE).a(getString("MOD_AUTHOR_MORE", count)));
    }

    @Override
    public String getUsageName() {
        return "MOD";
    }

    //按时间排序每个 JsonObject
    private static void modsTimeSort(List<JSONObject> list) {
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

    private static JSONObject selectMod(String search) {
        JSONArray searchResult;
        try {
            searchResult = new JSONArray(Utils.get(SEARCH_ADDONS + "&searchFilter=" + URLEncoder.encode(search, StandardCharsets.UTF_8.name())));
        } catch (Exception e) {
            System.out.println(getString("MOD_FAILED_SEARCH", e));
            return null;
        }

        List<JSONObject> list = Utils.jsonArrayToJSONObjectList(searchResult);

        if (list.size() == 0) {
            System.out.println(getString("MOD_SEARCH_NOTHING"));
            return null;
        }


        for (int i = list.size() - 1; i >= 0; i--) {
            try {
                AnsiConsole.systemInstall();
                JSONObject result = list.get(i);
                if (result == null) continue;
                String author = getAuthor(result.optJSONArray("authors"));
                if (!isEmpty(author)) {
                    author = Ansi.ansi().fg(Ansi.Color.WHITE).a("/ ") + "" + author + " ";
                } else {
                    author = "";
                }

                String gameVersionAndFileName = null;
                JSONArray gameVersionLatestFiles = result.optJSONArray("gameVersionLatestFiles");
                if (gameVersionLatestFiles != null && gameVersionLatestFiles.length() > 0) {
                    JSONObject first = gameVersionLatestFiles.optJSONObject(0);
                    if (first != null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        String gameVersion = first.optString("gameVersion");
                        String projectFileName = first.optString("projectFileName");
                        boolean notEmptyBoth = !isEmpty(gameVersion) && !isEmpty(projectFileName);
                        if (!isEmpty(gameVersion) || !isEmpty(projectFileName)) {
                            stringBuilder.append("(");
                        }
                        if (!isEmpty(gameVersion)) {
                            stringBuilder.append(gameVersion);
                        }
                        if (notEmptyBoth) {
                            stringBuilder.append(", ");
                        }
                        if (!isEmpty(projectFileName)) {
                            stringBuilder.append(projectFileName);
                        }

                        if (!isEmpty(gameVersion) || !isEmpty(projectFileName)) {
                            stringBuilder.append(")");
                        }


                        gameVersionAndFileName = stringBuilder.toString();
                    }
                }


                System.out.println(
                        Ansi.ansi().fg(Ansi.Color.CYAN).a(i + 1) + " " +
                                Ansi.ansi().fg(Ansi.Color.GREEN).a(
                                        result
                                                .optString("name")) +
                                Ansi.ansi().fg(Ansi.Color.WHITE).a(" ") + author +
                                (!Utils.isEmpty(gameVersionAndFileName) ? Ansi.ansi().fg(Ansi.Color.WHITE).a(gameVersionAndFileName) : "")
                );


                AnsiConsole.systemUninstall();

                String summary = result.optString("summary");
                if (!Utils.isEmpty(summary)) System.out.println("    " + summary);
            } catch (Exception e) {
                System.out.println(getString("MOD_FAILED_TO_SHOW_SOMEONE", i + 1, e));
            }
        }

        int number = ConsoleUtils.inputInt(Utils.getString("MOD_SELECT_TARGET", 1, list.size()), 1, list.size());
        if (number != Integer.MAX_VALUE) {
            return list.get(number - 1);
        }
        return null;
    }
}