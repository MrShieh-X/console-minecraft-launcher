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
package com.mrshiehx.cmcl.utils;

import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.interfaces.Void;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.downloadFile;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;

/**
 * 多线程下载文件（无控制台百分比进度）
 *
 * @author MrShiehX
 **/
public class ThreadsDownloader {
    private final int threadsCount;
    private boolean started;
    public final Map<Integer, List<Pair<String, File>>> maps;
    private final Void onDownloaded;
    private int done;

    public ThreadsDownloader(List<Pair<String, File>> files) {
        this(files, null);
    }

    public ThreadsDownloader(List<Pair<String, File>> files, Void onDownloaded) {
        this(files, onDownloaded, 10);
    }

    public ThreadsDownloader(List<Pair<String, File>> files, Void onDownloaded, int threadsCount) {
        this.threadsCount = threadsCount;
        this.maps = new HashMap<>();
        for (int i = 0; i < threadsCount; i++) {
            maps.put(i, new LinkedList<>());
        }
        this.onDownloaded = onDownloaded;

        int size = files.size();
        if (size > 0) {
            if (size <= 10) {
                int i = 0;
                for (Pair<String, File> entry : files) {
                    getMap(i).add(entry);
                    i++;
                }
            } else {
                int shang = size / threadsCount;
                int yu = size % threadsCount;

                for (int i = 0; i < threadsCount; i++) {
                    List<Pair<String, File>> map = getMap(i);
                    int start = shang * i;
                    for (int j = start; j < start + shang; j++) {
                        Pair<String, File> one = files.get(j);
                        map.add(one);
                    }
                }
                if (yu > 0) {
                    int start = shang * threadsCount;
                    for (int i = 0; i < threadsCount; i++) {
                        List<Pair<String, File>> map = getMap(i);


                        int j = start + i;
                        if (j < size) {
                            Pair<String, File> one = files.get(j);
                            map.add(one);
                        }
                    }
                }
            }
        }
    }

    public void start() {
        if (started) return;
        for (int i = 0; i < threadsCount; i++) {
            int finalI = i;
            new Thread(() -> {
                List<Pair<String, File>> map = getMap(finalI);
                for (Pair<String, File> pair : map) {
                    String url = pair.getKey();
                    File file = pair.getValue();
                    System.out.println(getString("MESSAGE_DOWNLOADING_FILE", url.substring(url.lastIndexOf('/') + 1)));
                    try {
                        downloadFile(url, file);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println(getString("MESSAGE_FAILED_DOWNLOAD_FILE", file.getName()));
                    }
                }
                done++;
                if (done == threadsCount) {
                    if (onDownloaded != null) {
                        onDownloaded.execute();
                    }
                }
            }).start();
        }
        started = true;
    }

    private List<Pair<String, File>> getMap(int count) {
        if (count >= threadsCount) {
            throw new RuntimeException("unsupported number");
        }
        return maps.get(count);
    }
}
