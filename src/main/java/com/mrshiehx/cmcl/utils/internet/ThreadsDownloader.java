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
 *
 */
package com.mrshiehx.cmcl.utils.internet;

import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.interfaces.Void;
import com.mrshiehx.cmcl.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.isEmpty;

/**
 * 多线程下载文件（无控制台百分比进度）
 *
 * @author MrShiehX
 **/
public class ThreadsDownloader {
    private final int totalThreadsCount;
    private int doneThreadsCount;

    private final int totalFilesCount;
    private int doneFilesCount;
    private final String preMessage;

    private boolean started;
    public final Map<Integer, List<Pair<String, File>>> maps;
    private final Void onDownloaded;
    private final boolean deleteTargetFileIfExist;

    public ThreadsDownloader(List<Pair<String, File>> files, boolean deleteTargetFileIfExist) {
        this(files, null, deleteTargetFileIfExist);
    }

    public ThreadsDownloader(List<Pair<String, File>> files, Void onDownloaded, boolean deleteTargetFileIfExist) {
        this(files, onDownloaded, Constants.DEFAULT_DOWNLOAD_THREAD_COUNT, deleteTargetFileIfExist);
    }

    public ThreadsDownloader(List<Pair<String, File>> files, Void onDownloaded, int totalThreadsCount, boolean deleteTargetFileIfExist) {
        this.totalFilesCount = files.size();
        this.onDownloaded = onDownloaded;
        this.deleteTargetFileIfExist = deleteTargetFileIfExist;
        if (totalFilesCount > 0) {
            this.totalThreadsCount = Math.min(totalFilesCount, totalThreadsCount);
            this.maps = new HashMap<>(this.totalThreadsCount);
            for (int i = 0; i < this.totalThreadsCount; i++) {
                maps.put(i, new LinkedList<>());
            }
            this.preMessage = "[%d/" + totalFilesCount + "]";
            if (totalFilesCount <= totalThreadsCount) {//注意：不是this.totalThreadsCount
                int i = 0;
                for (Pair<String, File> entry : files) {
                    getMap(i).add(entry);
                    i++;
                }
            } else {
                int quotient = totalFilesCount / this.totalThreadsCount;
                int remainder = totalFilesCount % this.totalThreadsCount;

                for (int i = 0; i < this.totalThreadsCount; i++) {
                    List<Pair<String, File>> map = getMap(i);
                    int start = quotient * i;
                    for (int j = start; j < start + quotient; j++) {
                        Pair<String, File> one = files.get(j);
                        map.add(one);
                    }
                }
                if (remainder > 0) {
                    int start = quotient * this.totalThreadsCount;
                    for (int i = 0; i < this.totalThreadsCount; i++) {
                        List<Pair<String, File>> map = getMap(i);
                        int j = start + i;
                        if (j < totalFilesCount) {
                            Pair<String, File> one = files.get(j);
                            map.add(one);
                        }
                    }
                }
            }
        } else {
            this.totalThreadsCount = 0;
            this.maps = Collections.emptyMap();
            this.preMessage = "[%d/0]";
        }
    }

    public void start() {
        if (totalFilesCount <= 0) {
            started = true;
            if (onDownloaded != null) {
                onDownloaded.execute();
            }
        }
        if (started) return;
        for (int i = 0; i < this.totalThreadsCount; i++) {
            int finalI = i;
            new Thread(() -> {
                List<Pair<String, File>> map = getMap(finalI);
                for (Pair<String, File> pair : map) {
                    String url = pair.getKey();
                    File file = pair.getValue();
                    if (isEmpty(url)) {
                        doneAddOneFile();
                        System.out.println(String.format(preMessage, doneFilesCount) + getString("EXCEPTION_NOT_FOUND_DOWNLOAD_LINK_WITH_FILENAME", file.getName()));
                        continue;
                    }
                    try {
                        DownloadUtils.multipleAttemptsDownload(url, file, deleteTargetFileIfExist);

                        doneAddOneFile();
                        //System.out.println(getString("MESSAGE_DOWNLOADING_FILE", url.substring(url.lastIndexOf('/') + 1)));
                        //不知为何多线程读取MAP会出现读取不了的问题（直接输出MESSAGE_DOWNLOADING_FILE（正在下载%s）），所以干脆直接输出文件名
                        System.out.println(String.format(preMessage, doneFilesCount) + url.substring(url.lastIndexOf('/') + 1));
                    } catch (IOException e) {
                        doneAddOneFile();
                        System.out.println(String.format(preMessage, doneFilesCount) + Utils.downloadFileFailedText(url, file, e));
                    }
                }
                //System.out.println(done+"/"+threadsCount);
                doneAddOneThread();
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
            if (doneThreadsCount >= this.totalThreadsCount) {
                if (onDownloaded != null) {
                    onDownloaded.execute();
                }
                break;
            }
        }
    }

    private List<Pair<String, File>> getMap(int index) {
        if (index >= totalThreadsCount) {
            throw new RuntimeException("unsupported number");
        }
        return maps.get(index);
    }

    private void doneAddOneThread() {
        synchronized (this) {
            doneThreadsCount++;
        }
    }

    private void doneAddOneFile() {
        synchronized (this) {
            doneFilesCount++;
        }
    }
}
