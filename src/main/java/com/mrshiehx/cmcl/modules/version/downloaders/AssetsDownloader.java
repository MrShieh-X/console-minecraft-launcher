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
package com.mrshiehx.cmcl.modules.version.downloaders;

import com.mrshiehx.cmcl.api.download.DownloadSource;
import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.exceptions.DescriptionException;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.internet.DownloadUtils;
import com.mrshiehx.cmcl.utils.internet.ThreadsDownloader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.mrshiehx.cmcl.CMCL.*;

public class AssetsDownloader {
    public static void start(JSONObject headVersionFile, int threadCount, com.mrshiehx.cmcl.interfaces.Void onDownloaded) throws DescriptionException {
        String assetsDirPath = Utils.getConfig().optString("assetsPath");
        File assetsDir = !Utils.isEmpty(assetsDirPath) ? new File(assetsDirPath) : new File(gameDir, "assets");
        File indexesDir = new File(assetsDir, "indexes");
        File objectsDir = new File(assetsDir, "objects");
        assetsDir.mkdirs();
        indexesDir.mkdirs();
        objectsDir.mkdirs();
        String assetsIndex = headVersionFile.optString("assets");
        if (isEmpty(assetsIndex)) {
            throw new com.mrshiehx.cmcl.exceptions.DescriptionException(getString("MESSAGE_INSTALL_DOWNLOAD_ASSETS_NO_INDEX"));
        }
        File assetsIndexFile = new File(indexesDir, assetsIndex + ".json");
        JSONObject assetIndexObject = headVersionFile.optJSONObject("assetIndex");
        String assetIndexUrl = assetIndexObject != null ? assetIndexObject.optString("url").replace("https://launchermeta.mojang.com/", DownloadSource.getProvider().versionAssetsIndex()).replace("https://piston-meta.mojang.com/", DownloadSource.getProvider().versionAssetsIndex()) : null;

        if (isEmpty(assetIndexUrl)) {
            throw new com.mrshiehx.cmcl.exceptions.DescriptionException(getString("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_ASSETS", getString("MESSAGE_EXCEPTION_DETAIL_NOT_FOUND_URL")));
        }
        try {
            DownloadUtils.downloadFile(assetIndexUrl, assetsIndexFile);
            JSONObject assetsJo = new JSONObject(FileUtils.readFileContent(assetsIndexFile));
            JSONObject objectsJo = assetsJo.optJSONObject("objects");

            Map<String, Object> map = objectsJo.toMap();
            List<String> nameList = new ArrayList<>(map.keySet());
            JSONArray names = new JSONArray(nameList);
            JSONArray objectsJa = objectsJo.toJSONArray(names);
            List<Pair<String, File>> list = new LinkedList<>();
            for (int i = 0; i < objectsJa.length(); i++) {
                JSONObject object = objectsJa.optJSONObject(i);
                if (object == null) {
                    continue;
                }
                String hash = object.optString("hash");
                try {
                    if (isEmpty(hash)) continue;
                    File file;
                    if (!assetsIndex.equals("legacy")) {
                        File dir = new File(objectsDir, hash.substring(0, 2));
                        dir.mkdirs();
                        file = new File(dir, hash);
                    } else {
                        file = new File(assetsDir, "virtual/legacy/" + nameList.get(i));
                        file.getParentFile().mkdirs();
                    }


                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    if (file.length() == 0)
                        list.add(new Pair<>(DownloadSource.getProvider().assets() + hash.substring(0, 2) + "/" + hash, file));

                } catch (Exception e1) {
                    throw new com.mrshiehx.cmcl.exceptions.DescriptionException(getString("MESSAGE_FAILED_DOWNLOAD_FILE", hash));
                }
            }
            if (Constants.isDebug()) System.out.println("threadCount: " + threadCount);
            ThreadsDownloader threadsDownloader = new ThreadsDownloader(list, onDownloaded, threadCount > 0 ? threadCount : Constants.DEFAULT_DOWNLOAD_THREAD_COUNT, false);
            threadsDownloader.start();
        } catch (Exception e1) {
            throw new com.mrshiehx.cmcl.exceptions.DescriptionException(getString("MESSAGE_INSTALL_FAILED_TO_DOWNLOAD_ASSETS", e1));
        }
    }
}
