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

import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.console.PercentageTextProgress;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadUtils {
    public static void downloadFile(String url, File to) throws IOException {
        /*Utils.createFile(to, true);
        BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(to);
        byte dataBuffer[] = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            fileOutputStream.write(dataBuffer, 0, bytesRead);
        }
        fileOutputStream.close();*/
        downloadFile(url, to, null);
    }

    public static void downloadFile(String urlString, File to, @Nullable PercentageTextProgress progressBar) throws IOException {
        downloadFile(urlString, to, progressBar, true);
    }

    public static void downloadFile(String urlString, File to, @Nullable PercentageTextProgress progressBar, boolean deleteIfExist) throws IOException {
        FileUtils.createFile(to, deleteIfExist || to.length() == 0);
        try {
            URL url = new URL(NetworkUtils.encodeURL(urlString));
            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
            int completeFileSize = httpConnection.getContentLength();
            if (progressBar != null)
                progressBar.setMaximum(completeFileSize);
            httpConnection.setConnectTimeout(5000);
            httpConnection.setReadTimeout(5000);

            BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
            FileOutputStream fos = new java.io.FileOutputStream(to);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;
                if (progressBar != null)
                    progressBar.setValue((int) downloadedFileSize);
                bout.write(data, 0, x);
            }
            if (progressBar != null)
                progressBar.setValue(completeFileSize);
            bout.close();
            fos.close();
            in.close();
        } catch (MalformedURLException e) {
            throw e;
        } catch (IOException e) {
            if (progressBar != null && !progressBar.done)
                System.out.println();
            if (Utils.getConfig().optBoolean("proxyEnabled"))
                System.err.println(Utils.getString("EXCEPTION_NETWORK_WRONG_PLEASE_CHECK_PROXY"));
            to.delete();//万一没开始下载就失败了，如果deleteIfExist（是否原文件存在就删除）为true，那才删，否则没叫删除的，没开始下载就失败了，就不动原文件
            throw e;
        }
    }


    public static byte[] downloadBytes(String url) throws IOException {
        try {
            BufferedInputStream in = new BufferedInputStream(new URL(NetworkUtils.encodeURL(url)).openStream());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                byteArrayOutputStream.write(dataBuffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        } catch (MalformedURLException e) {
            throw e;
        } catch (IOException e) {
            if (Utils.getConfig().optBoolean("proxyEnabled"))
                System.err.println(Utils.getString("EXCEPTION_NETWORK_WRONG_PLEASE_CHECK_PROXY"));
            throw e;
        }
    }

    public static byte[] downloadBytes(String urlString, @Nullable PercentageTextProgress progressBar) throws IOException {
        try {
            URL url = new URL(NetworkUtils.encodeURL(urlString));
            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
            int completeFileSize = httpConnection.getContentLength();
            if (progressBar != null)
                progressBar.setMaximum(completeFileSize);

            BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;
                if (progressBar != null)
                    progressBar.setValue((int) downloadedFileSize);
                byteArrayOutputStream.write(data, 0, x);
            }
            if (progressBar != null)
                progressBar.setValue(completeFileSize);
            in.close();
            return byteArrayOutputStream.toByteArray();
        } catch (MalformedURLException e) {
            throw e;
        } catch (IOException e) {
            if (progressBar != null && !progressBar.done) System.out.println();
            if (Utils.getConfig().optBoolean("proxyEnabled"))
                System.err.println(Utils.getString("EXCEPTION_NETWORK_WRONG_PLEASE_CHECK_PROXY"));
            throw e;
        }
    }

    public static void multipleAttemptsDownload(String url, File to, boolean deleteIfExist) throws IOException {
        multipleAttemptsDownload(url, to, null, 5, deleteIfExist);
    }

    public static void multipleAttemptsDownload(String url, File to, int times, boolean deleteIfExist) throws IOException {
        multipleAttemptsDownload(url, to, null, times, deleteIfExist);
    }

    public static void multipleAttemptsDownload(String urlString, File to, @Nullable PercentageTextProgress progressBar, int times, boolean deleteIfExist) throws IOException {
        IOException finalThrowable = null;
        FileUtils.createFile(to, deleteIfExist || to.length() == 0);
        for (int i = 0; i < times; i++) {
            try {
                multipleAttemptsDownloadInternal(urlString, to, progressBar, deleteIfExist);
                return;
            } catch (MalformedURLException e) {
                if (progressBar != null && progressBar.printed && !progressBar.done) {
                    progressBar.setValue(0);
                }
                throw e;
            } catch (IOException e) {
                finalThrowable = e;
                if (progressBar != null && progressBar.printed && !progressBar.done) {
                    progressBar.setValue(0);
                }
            }
        }
        if (finalThrowable != null) {
            if (Utils.getConfig().optBoolean("proxyEnabled"))
                System.err.println(Utils.getString("EXCEPTION_NETWORK_WRONG_PLEASE_CHECK_PROXY"));
            throw finalThrowable;
        }
    }


    private static void multipleAttemptsDownloadInternal(String urlString, File to, @Nullable PercentageTextProgress progressBar, boolean deleteIfExist) throws MalformedURLException, IOException {
        try {
            URL url = new URL(NetworkUtils.encodeURL(urlString));
            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
            int completeFileSize = httpConnection.getContentLength();
            if (progressBar != null)
                progressBar.setMaximum(completeFileSize);
            httpConnection.setConnectTimeout(5000);
            httpConnection.setReadTimeout(5000);

            BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
            FileOutputStream fos = new java.io.FileOutputStream(to);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;
                if (progressBar != null)
                    progressBar.setValue((int) downloadedFileSize);
                bout.write(data, 0, x);
            }
            if (progressBar != null)
                progressBar.setValue(completeFileSize);
            bout.close();
            fos.close();
            in.close();
        } catch (MalformedURLException e) {
            throw e;
        } catch (IOException e) {
            if (deleteIfExist) to.delete();//万一没开始下载就失败了，如果deleteIfExist（是否原文件存在就删除）为true，那才删，否则没叫删除的，没开始下载就失败了，就不动原文件
            throw e;
        }
    }
}
