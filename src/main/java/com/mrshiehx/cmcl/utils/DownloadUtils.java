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
 *
 */

package com.mrshiehx.cmcl.utils;

import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.HttpURLConnection;
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
        try {
            Utils.createFile(to, true);
            URL url = new URL(urlString);
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
        } catch (IOException e) {
            if (progressBar != null && !progressBar.done) System.out.println();
            to.delete();
            throw e;
        }
    }


    public static byte[] downloadBytes(String url) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] dataBuffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            byteArrayOutputStream.write(dataBuffer, 0, bytesRead);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] downloadBytes(String urlString, @Nullable PercentageTextProgress progressBar) throws IOException {
        try {
            URL url = new URL(urlString);
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
        } catch (IOException e) {
            if (progressBar != null && !progressBar.done) System.out.println();
            throw e;
        }
    }

    public static void multipleAttemptsDownload(String url, File to) throws IOException {
        multipleAttemptsDownload(url, to, null, 5);
    }

    public static void multipleAttemptsDownload(String url, File to, int times) throws IOException {
        multipleAttemptsDownload(url, to, null, times);
    }

    public static void multipleAttemptsDownload(String urlString, File to, @Nullable PercentageTextProgress progressBar, int times) throws IOException {
        IOException finalThrowable = null;
        for (int i = 0; i < times; i++) {
            try {
                multipleAttemptsDownloadInternal(urlString, to, progressBar);
                return;
            } catch (IOException e) {
                finalThrowable = e;
                if (progressBar != null && progressBar.printed && !progressBar.done) {
                    progressBar.setValue(0);
                }
            }
        }
        if (finalThrowable != null) throw finalThrowable;
    }


    private static void multipleAttemptsDownloadInternal(String urlString, File to, @Nullable PercentageTextProgress progressBar) throws IOException {
        try {
            Utils.createFile(to, true);
            URL url = new URL(urlString);
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
        } catch (IOException e) {
            to.delete();
            throw e;
        }
    }

}
