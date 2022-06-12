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

package com.mrshiehx.cmcl.modules.extra.fabric;

import com.mrshiehx.cmcl.api.download.DownloadSource;
import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.modules.extra.ExtraMerger;
import com.mrshiehx.cmcl.utils.ConsoleUtils;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.*;

/**
 * Fabric 与原版的合并器
 **/
public class FabricMerger implements ExtraMerger {
    private static final String MODLOADER_NAME = "Fabric";

    /**
     * 将 Fabric 的JSON合并到原版JSON
     *
     * @return key: 如果无法安装Fabric，是否继续安装 value:如果成功合并，则为需要安装的依赖库集合，否则为空
     **/
    public Pair<Boolean, List<JSONObject>> merge(String minecraftVersion, JSONObject headJSONObject, File jarFile, boolean askContinue) {
        JSONArray jsonArray;
        try {
            jsonArray = listFabricLoaderVersions(minecraftVersion);
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(getString("INSTALL_MODLOADER_FAILED_TO_GET_INSTALLABLE_VERSION", MODLOADER_NAME));
            return new Pair<>(askContinue && ConsoleUtils.yesOrNo(getString("INSTALL_MODLOADER_UNABLE_DO_YOU_WANT_TO_CONTINUE", MODLOADER_NAME)), null);
        }


        if (jsonArray.length() == 0) {
            System.out.println(getString("INSTALL_MODLOADER_NO_INSTALLABLE_VERSION", MODLOADER_NAME));
            return new Pair<>(askContinue && ConsoleUtils.yesOrNo(getString("INSTALL_MODLOADER_UNABLE_DO_YOU_WANT_TO_CONTINUE", MODLOADER_NAME)), null);
        }
        Map<String, JSONObject> fabrics = new LinkedHashMap<>();
        for (Object object : jsonArray) {
            if (object instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) object;
                fabrics.put(jsonObject.optJSONObject("loader", new JSONObject()).optString("version"), jsonObject);
            }
        }
        System.out.print('[');

        List<String> fabricVersions = new ArrayList<>(fabrics.keySet());
        for (int i = fabricVersions.size() - 1; i >= 0; i--) {
            String versionName = fabricVersions.get(i);
            if (versionName.contains(" ")) versionName = "\"" + versionName + "\"";
            System.out.print(versionName);//legal
            if (i > 0) {
                System.out.print(", ");
            }
        }
        System.out.println(']');

        String fabricVersion = selectFabricVersion(getString("INSTALL_MODLOADER_SELECT", MODLOADER_NAME), fabrics);
        if (fabricVersion == null)
            return new Pair<>(false, null);


        try {
            return installInternal(minecraftVersion, fabricVersion, headJSONObject);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Pair<>(askContinue && ConsoleUtils.yesOrNo(getString("INSTALL_MODLOADER_UNABLE_DO_YOU_WANT_TO_CONTINUE", MODLOADER_NAME)), null);
        }
    }

    public static Pair<Boolean, List<JSONObject>> installInternal(String minecraftVersion, String fabricVersion, JSONObject headJSONObject) throws Exception {
        String jsonUrl = DownloadSource.getProvider().fabricMeta() + String.format("v2/versions/loader/%s/%s", minecraftVersion, fabricVersion);
        String targetJSONString;
        try {
            targetJSONString = Utils.get(jsonUrl);
        } catch (IOException e) {
            //e.printStackTrace();
            throw new Exception(getString("INSTALL_MODLOADER_FAILED_TO_GET_TARGET_JSON", MODLOADER_NAME));
        }
        JSONObject fabricJSONOrigin = Utils.parseJSONObject(targetJSONString);
        if (fabricJSONOrigin == null) {
            throw new Exception(getString("INSTALL_MODLOADER_FAILED_TO_PARSE_TARGET_JSON", MODLOADER_NAME));
        }

        JSONObject fabricJSON = new JSONObject();

        JSONObject loader = fabricJSONOrigin.optJSONObject("loader");
        JSONObject intermediary = fabricJSONOrigin.optJSONObject("intermediary");
        JSONObject launcherMeta = fabricJSONOrigin.optJSONObject("launcherMeta");
        if (launcherMeta != null) {
            Object mainClassObject = launcherMeta.opt("mainClass");
            if (mainClassObject instanceof String) {
                fabricJSON.put("mainClass", mainClassObject);
            } else if (mainClassObject instanceof JSONObject) {
                fabricJSON.put("mainClass", ((JSONObject) mainClassObject).optString("client"));
            }
            JSONObject launchwrapper = launcherMeta.optJSONObject("launchwrapper");
            if (launchwrapper != null) {
                JSONObject tweakers = launchwrapper.optJSONObject("tweakers");
                if (tweakers != null) {
                    JSONArray client = tweakers.optJSONArray("client");
                    if (client != null) {
                        for (Object o : client) {
                            if (o instanceof String) {
                                fabricJSON.put("arguments", new JSONObject().put("game", new JSONArray().put("--tweakClass").put(o)));
                                break;
                            }
                        }
                    }
                }
            }
            JSONObject libraries = launcherMeta.optJSONObject("libraries");
            if (libraries != null) {
                JSONArray common = libraries.optJSONArray("common");
                JSONArray server = libraries.optJSONArray("server");
                fabricJSON.put("libraries", common.putAll(server));
            }


        }

        JSONArray libraries = fabricJSON.optJSONArray("libraries");
        if (libraries == null) fabricJSON.put("libraries", libraries = new JSONArray());
        if (intermediary != null) {
            String maven = intermediary.optString("maven");
            if (!isEmpty(maven)) {
                libraries.put(new JSONObject().put("name", maven).put("url", "https://maven.fabricmc.net/"));
            }
        }
        if (loader != null) {
            String maven = loader.optString("maven");
            if (!isEmpty(maven)) {
                libraries.put(new JSONObject().put("name", maven).put("url", "https://maven.fabricmc.net/"));
            }
        }


        return new Pair<>(true, realMerge(headJSONObject, fabricJSON, fabricVersion, jsonUrl));
    }

    private static List<JSONObject> realMerge(JSONObject headJSONObject, JSONObject fabricJSON, String fabricVersion, String jsonUrl) {
        String mainClass = fabricJSON.optString("mainClass");
        if (!Utils.isEmpty(mainClass)) headJSONObject.put("mainClass", mainClass);

        JSONObject fabric = new JSONObject();
        fabric.put("version", fabricVersion);
        fabric.put("jsonUrl", jsonUrl);
        headJSONObject.put("fabric", fabric);


        JSONArray fabricLibraries = fabricJSON.optJSONArray("libraries");
        List<JSONObject> list = new LinkedList<>();
        if (fabricLibraries != null) {
            for (Object o : fabricLibraries) {
                if (o instanceof JSONObject) {
                    list.add((JSONObject) o);
                }
            }
            headJSONObject.optJSONArray("libraries").putAll(fabricLibraries);
        }

        JSONObject arguments = fabricJSON.optJSONObject("arguments");
        if (arguments != null) {
            JSONObject argumentsMC = headJSONObject.optJSONObject("arguments");
            if (argumentsMC != null) {
                JSONArray game = arguments.optJSONArray("game");
                if (game != null && game.length() > 0) {
                    argumentsMC.optJSONArray("game").putAll(game);
                }
                JSONArray jvm = arguments.optJSONArray("jvm");
                if (jvm != null && jvm.length() > 0) {
                    argumentsMC.optJSONArray("jvm").putAll(jvm);
                }
            } else {
                headJSONObject.put("arguments", arguments);
            }
        }
        return list;
    }

    private static String selectFabricVersion(String text, Map<String, JSONObject> fabrics) {
        System.out.print(text);//legal
        Scanner scanner = new Scanner(System.in);
        try {
            String s = scanner.nextLine();
            if (!isEmpty(s)) {
                JSONObject jsonObject = fabrics.get(s);
                if (jsonObject != null) return s;
                return selectFabricVersion(getString("INSTALL_MODLOADER_SELECT_NOT_FOUND", s, MODLOADER_NAME), fabrics);
            } else {
                return selectFabricVersion(text, fabrics);
            }
        } catch (NoSuchElementException ignore) {
            return null;
        }
    }

    //列出该 Minecraft 版本支持的所有 Fabric 版本
    private static JSONArray listFabricLoaderVersions(String minecraftVersion) throws Exception {
        return new JSONArray(Utils.get(DownloadSource.getProvider().fabricMeta() + "v2/versions/loader/" + minecraftVersion));
    }
}
