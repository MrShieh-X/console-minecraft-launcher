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

package com.mrshiehx.cmcl.modules.fabric;

import com.mrshiehx.cmcl.api.download.DownloadSource;
import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.utils.ConsoleUtils;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.*;

/**
 * Fabric 与原版的合并器
 **/
public class FabricMerger {
    /**
     * 将 Fabric 的JSON合并到原版JSON
     *
     * @return key: 如果无法安装Fabric，是否继续安装 value:如果成功合并，则为需要安装的依赖库集合，否则为空
     **/
    public static Pair<Boolean, List<JSONObject>> merge(String minecraftVersion, JSONObject headJSONObject, boolean askContinue) {
        JSONArray jsonArray;
        try {
            jsonArray = listFabricLoaderVersions(minecraftVersion);
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println(getString("INSTALL_FABRIC_FAILED_TO_GET_INSTALLABLE_VERSION"));
            return new Pair<>(askContinue && ConsoleUtils.yesOrNo(getString("INSTALL_FABRIC_UNABLE_DO_YOU_WANT_TO_CONTINUE")), null);
        }

        if (jsonArray.length() == 0) {
            System.out.println(getString("INSTALL_FABRIC_NO_INSTALLABLE_VERSION"));
            return new Pair<>(askContinue && ConsoleUtils.yesOrNo(getString("INSTALL_FABRIC_UNABLE_DO_YOU_WANT_TO_CONTINUE")), null);
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

        String fabricVersion = selectFabricVersion(getString("INSTALL_FABRIC_SELECT"), fabrics);
        if (fabricVersion == null)
            return new Pair<>(false, null);
        String jsonUrl = DownloadSource.getProvider().fabricMeta() + String.format("v2/versions/loader/%s/%s/profile/json", minecraftVersion, fabricVersion);
        String targetJSONString;
        try {
            targetJSONString = Utils.get(jsonUrl);
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println(getString("INSTALL_FABRIC_FAILED_TO_GET_TARGET_JSON"));
            return new Pair<>(askContinue && ConsoleUtils.yesOrNo(getString("INSTALL_FABRIC_UNABLE_DO_YOU_WANT_TO_CONTINUE")), null);
        }
        JSONObject fabricJSON = Utils.parseJSONObject(targetJSONString);
        if (fabricJSON == null) {
            System.out.println(getString("INSTALL_FABRIC_FAILED_TO_PARSE_TARGET_JSON"));
            return new Pair<>(askContinue && ConsoleUtils.yesOrNo(getString("INSTALL_FABRIC_UNABLE_DO_YOU_WANT_TO_CONTINUE")), null);
        }


        if ("not found".equals(fabricJSON.optString("message"))) {
            System.out.println(getString("INSTALL_FABRIC_RESPONSE_NOT_FOUND"));
            return new Pair<>(askContinue && ConsoleUtils.yesOrNo(getString("INSTALL_FABRIC_UNABLE_DO_YOU_WANT_TO_CONTINUE")), null);
        }

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

            }
        }
        return new Pair<>(true, list);
    }

    private static String selectFabricVersion(String text, Map<String, JSONObject> fabrics) {
        System.out.print(text);//legal
        Scanner scanner = new Scanner(System.in);
        try {
            String s = scanner.nextLine();
            if (!isEmpty(s)) {
                JSONObject jsonObject = fabrics.get(s);
                if (jsonObject != null) return s;
                return selectFabricVersion(String.format(getString("INSTALL_FABRIC_SELECT_NOT_FOUND"), s), fabrics);
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
