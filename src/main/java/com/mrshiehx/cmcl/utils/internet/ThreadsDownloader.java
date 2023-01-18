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
package com.mrshiehx.cmcl.utils.internet;

import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.interfaces.Void;
import com.mrshiehx.cmcl.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.isEmpty;

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
    private final boolean deleteIfExist;

    public ThreadsDownloader(List<Pair<String, File>> files, boolean deleteIfExist) {
        this(files, null, deleteIfExist);
    }

    public ThreadsDownloader(List<Pair<String, File>> files, Void onDownloaded, boolean deleteIfExist) {
        this(files, onDownloaded, Constants.DEFAULT_DOWNLOAD_THREAD_COUNT, deleteIfExist);
    }

    public ThreadsDownloader(List<Pair<String, File>> files, Void onDownloaded, int threadsCount, boolean deleteIfExist) {
        this.threadsCount = threadsCount;
        this.maps = new HashMap<>();
        for (int i = 0; i < threadsCount; i++) {
            maps.put(i, new LinkedList<>());
        }
        this.onDownloaded = onDownloaded;
        this.deleteIfExist = deleteIfExist;

        int size = files.size();
        if (size > 0) {
            if (size <= 10) {
                int i = 0;
                for (Pair<String, File> entry : files) {
                    getMap(i).add(entry);
                    i++;
                }
            } else {
                int quotient = size / threadsCount;
                int remainder = size % threadsCount;

                for (int i = 0; i < threadsCount; i++) {
                    List<Pair<String, File>> map = getMap(i);
                    int start = quotient * i;
                    for (int j = start; j < start + quotient; j++) {
                        Pair<String, File> one = files.get(j);
                        map.add(one);
                    }
                }
                if (remainder > 0) {
                    int start = quotient * threadsCount;
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
                    if (isEmpty(url)) {
                        System.out.println(getString("EXCEPTION_NOT_FOUND_DOWNLOAD_LINK_WITH_FILENAME", file.getName()));
                        continue;
                    }
                    //System.out.println(getString("MESSAGE_DOWNLOADING_FILE", url.substring(url.lastIndexOf('/') + 1)));
                    //不知为何多线程读取MAP会出现读取不了的问题（直接输出MESSAGE_DOWNLOADING_FILE），所以直接输出文件名
                    System.out.println(url.substring(url.lastIndexOf('/') + 1));
                    try {
                        DownloadUtils.multipleAttemptsDownload(url, file, deleteIfExist);
                    } catch (IOException e) {
                        Utils.downloadFileFailed(url, file, e);
                        //System.out.println(getString("MESSAGE_FAILED_DOWNLOAD_FILE_WITH_REASON", file.getName(),e));
                    }
                }
                //System.out.println(done+"/"+threadsCount);
                doneAddOne();
                /*if (done == threadsCount) {
                    if (onDownloaded != null) {
                        onDownloaded.execute();
                    }
                }*/
            }).start();
        }
        started = true;
        while (true) {
            //System.out.println(done+"/"+threadsCount);
            try {
                Thread.sleep(1);
            } catch (Exception ignore) {

            }
            if (done >= threadsCount) {
                if (onDownloaded != null) {
                    onDownloaded.execute();
                }
                break;
            }
        }
    }

    private List<Pair<String, File>> getMap(int count) {
        if (count >= threadsCount) {
            throw new RuntimeException("unsupported number");
        }
        return maps.get(count);
    }

    private void doneAddOne() {
        synchronized (this) {
            done++;
        }
    }
}
