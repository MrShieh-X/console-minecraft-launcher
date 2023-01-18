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

package com.mrshiehx.cmcl.utils.cmcl.version;

import com.mrshiehx.cmcl.api.download.DefaultApiProvider;
import com.mrshiehx.cmcl.api.download.DownloadSource;
import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.bean.SplitLibraryName;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.internet.NetworkUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class VersionLibraryUtils {
    public static SplitLibraryName splitLibraryName(String name) {
        return SplitLibraryName.valueOf(name);
    }

    /**
     * @return KEY is downloadURL and VALUE is storage path
     **/
    public static Pair<String, String> getLibraryDownloadURLAndStoragePath(JSONObject library) {
        JSONObject downloads = library.optJSONObject("downloads");
        if (downloads != null) {
            JSONObject artifactJo = downloads.optJSONObject("artifact");
            if (artifactJo != null) {
                String path = artifactJo.optString("path");
                String url = artifactJo.optString("url");
                if (Utils.isEmpty(path) && !Utils.isEmpty(library.optString("name"))) {

                    String name = library.optString("name");

                    SplitLibraryName nameSplit = splitLibraryName(name);
                    if (nameSplit != null) {

                        String fileName = nameSplit.getFileName();
                        path = Utils.getPathFromLibraryName(nameSplit) + "/" + fileName;
                    }
                }
                if (!Utils.isEmpty(path) || !Utils.isEmpty(url)) {
                    String url2 = null;
                    if (!Utils.isEmpty(url)) {
                        url2 = replaceUrl(url);//.replace("https://libraries.minecraft.net/", DownloadSource.getProvider().libraries()).replace("https://maven.minecraftforge.net/", DownloadSource.getProvider().forgeMaven());
                    }
                    return new Pair<>(url2, path);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            String name = library.optString("name");
            String url = library.optString("url", DownloadSource.getProvider().libraries());
            /*switch (url) {
                case "https://maven.fabricmc.net/":
                case "https://maven.fabricmc.net":
                    url = DownloadSource.getProvider().fabricMaven();
                    break;
                case "http://repo.maven.apache.org/maven2/":
                case "http://repo.maven.apache.org/maven2":
                    url = "https://repo.maven.apache.org/maven2/";
                    break;
                case "https://maven.minecraftforge.net/":
                case "https://maven.minecraftforge.net":
                case "https://files.minecraftforge.net/maven/":
                case "https://files.minecraftforge.net/maven":
                    if (!(DownloadSource.getProvider() instanceof DefaultApiProvider))
                        url = DownloadSource.getProvider().forgeMaven();
                    break;
            }*/
            url = replaceUrl(url);
            if (Utils.isEmpty(name))
                return null;
            SplitLibraryName nameSplit = splitLibraryName(name);
            if (nameSplit == null)
                return null;
            String fileName = nameSplit.getFileName();
            String path = Utils.getPathFromLibraryName(nameSplit) + "/" + fileName;
            return new Pair<>(NetworkUtils.addSlashIfMissing(url) + (name.startsWith("net.minecraftforge:forge:") ? path.substring(0, path.length() - 4) + "-universal.jar" : path), path);
        }
    }

    public static JSONArray mergeLibraries(List<JSONObject> source, List<JSONObject> target) {
        JSONArray jsonArray = new JSONArray();
        source = source != null ? source : Collections.emptyList();
        target = target != null ? target : Collections.emptyList();
        jsonArray.putAll(source);
        for (JSONObject jsonObject : target) {
            String targetName = jsonObject.optString("name");
            int indexOf = -1;
            for (int j = 0; j < source.size(); j++) {
                JSONObject jsonObject1 = source.get(j);
                String sourceName = jsonObject1.optString("name");
                if (sourceName.equals(targetName)) {
                    indexOf = j;
                    break;
                } else {
                    String[] targetNameSplit = targetName.split(":");
                    String[] sourceNameSplit = sourceName.split(":");
                    if (targetNameSplit.length == sourceNameSplit.length && sourceNameSplit.length >= 3) {
                        if (Objects.equals(targetNameSplit[0], sourceNameSplit[0]) && Objects.equals(targetNameSplit[1], sourceNameSplit[1])) {
                            indexOf = j;
                            break;
                        }
                    }

                }
            }
            //if (withoutTargetServerreqAndClientreq) {
            jsonObject.remove("clientreq");
            jsonObject.remove("serverreq");
            //}
            if (indexOf < 0) {
                jsonArray.put(jsonObject);
            } else {
                jsonArray.put(indexOf, jsonObject);
            }
        }

        return jsonArray;
    }

    public static String replaceUrl(String url) {
        if (Utils.isEmpty(url)) return url;
        String a;
        if (url.contains(a = "https://libraries.minecraft.net/")) {
            url = url.replace(a, DownloadSource.getProvider().libraries());

        }
        if (url.contains(a = "https://maven.fabricmc.net/")) {
            url = url.replace(a, DownloadSource.getProvider().fabricMaven());

        }

        if (url.contains(a = "http://repo.maven.apache.org/maven2/")) {
            url = url.replace(a, "https://repo.maven.apache.org/maven2/");

        }

        if (url.contains(a = "https://maven.minecraftforge.net/")) {
            if (!(DownloadSource.getProvider() instanceof DefaultApiProvider)) {
                url = url.replace(a, DownloadSource.getProvider().forgeMaven());
            }

        }

        if (url.contains(a = "https://files.minecraftforge.net/maven/")) {
            if (!(DownloadSource.getProvider() instanceof DefaultApiProvider)) {
                url = url.replace(a, DownloadSource.getProvider().forgeMaven());
            }
        }
        if (url.contains(a = "http://repo.liteloader.com/")) {
            url = url.replace(a, "https://repo.liteloader.com/");
        }


        return url;
    }
}
