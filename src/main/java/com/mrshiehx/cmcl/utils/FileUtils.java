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
 */
package com.mrshiehx.cmcl.utils;

import com.mrshiehx.cmcl.interfaces.filters.StringFilter;
import com.mrshiehx.cmcl.utils.console.PercentageTextProgress;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtils {
    public static void hexWrite(String bytes, File file) throws IOException {
        hexWrite(hexString2Bytes(bytes), file);
    }

    public static void hexWrite(byte[] bytes, File file) throws IOException {
        createFile(file);
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(bytes);
        fop.flush();
        fop.close();
    }

    public static byte[] hexString2Bytes(String hex) {
        if (Utils.isEmpty(hex)) {
            return null;
        } else {
            int length = hex.length();
            if (length % 2 != 0) return null;
            byte[] bytes = new byte[length / 2];
            int j = 0;
            for (int i = 0; i < length; i += 2) {
                bytes[j++] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
            }
            return bytes;
        }
    }

    public static String bytesToString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte aByte : bytes) {
            builder.append(String.format("%02X", aByte));
        }
        return builder.toString().toUpperCase();
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static void bytes2File(File file, byte[] bytes, boolean append) throws IOException {
        createFile(file, false);

        FileOutputStream fileOutputStream = new FileOutputStream(file, append);
        fileOutputStream.write(bytes, 0, bytes.length);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public static byte[] toByteArray(File input) throws IOException {
        return toByteArray(new FileInputStream(input));
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    public static void inputStream2File(InputStream ins, File file) throws IOException {
        createFile(file, true);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        BufferedInputStream bis = new BufferedInputStream(ins);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        while ((bytesRead = bis.read(buffer, 0, 8192)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
        bos.close();
        bis.close();
    }

    public static byte[] inputStream2ByteArray(InputStream resourceAsStream) throws IOException {
        if (resourceAsStream != null) {
        /*byte[] bytes = new byte[0];
        bytes = new byte[resourceAsStream.available()];
        resourceAsStream.read(bytes);
        return new String(bytes);*/
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = resourceAsStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
            return output.toByteArray();
        } else return null;
    }

    public static void deleteDirectory(File directory) {
        if (directory != null) {
            if (directory.exists()) {
                if (directory.isDirectory()) {
                    File[] files = directory.listFiles();
                    if (files != null) {
                        if (files.length != 0) {
                            for (File file : files) {
                                if (file.isFile()) {
                                    file.delete();
                                } else {
                                    deleteDirectory(file);
                                }
                            }
                        }
                    }
                }
                directory.delete();

            }
        }
    }

    public static void copyDirectory(File from, String toWillNewDirNameIsAtFromName, String afterThatName) throws IOException {
        if (from != null && !Utils.isEmpty(toWillNewDirNameIsAtFromName) && from.exists()) {
            if (from.isFile()) {
                copyFile(from, new File(toWillNewDirNameIsAtFromName, afterThatName));
                return;
            }
            File toWillNewDirNameIsAtFrom = new File(toWillNewDirNameIsAtFromName);
            File to = new File(toWillNewDirNameIsAtFrom, afterThatName);
            if (!to.exists()) to.mkdirs();
            for (File file : from.listFiles()) {
                if (file.isFile()) {
                    copyFile(file, new File(to, file.getName()));
                } else {
                    copyDirectory(file, to.getAbsolutePath(), file.getName());
                }
            }
        }
    }

    public static File createFile(File file) throws IOException {
        return createFile(file, true);
    }

    public static File createFile(File file, boolean delete) throws IOException {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        if (delete && file.exists()) file.delete();
        if (!file.exists()) file.createNewFile();
        return file;
    }

    public static void copyFile(File source, File to)
            throws IOException {
        if (null == source) return;
        if (source.isDirectory()) copyDirectory(source, to.getParent(), to.getName());
        createFile(to, true);
        InputStream input = new FileInputStream(source);
        OutputStream output = new FileOutputStream(to);
        byte[] buf = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buf)) != -1) {
            output.write(buf, 0, bytesRead);
        }
        input.close();
        output.close();
    }

    public static String readFileContent(File file) throws IOException {
        BufferedReader reader;
        StringBuilder sbf = new StringBuilder();
        FileReader fr = new FileReader(file);
        reader = new BufferedReader(fr);
        String tempStr;
        while ((tempStr = reader.readLine()) != null) {
            sbf.append(tempStr).append('\n');
        }
        fr.close();
        reader.close();
        return sbf.length() == 0 ? "" : sbf.substring(0, sbf.length() - 1);
    }

    public static byte[] getBytes(File file) throws IOException {
        return toByteArray(file);
    }

    public static void writeFile(File file, String content, boolean append) throws IOException {
        createFile(file, false);
        FileWriter writer = new FileWriter(file, append);
        writer.write(content);
        writer.close();
    }


    public static void unZip(File zipFileSource, File to, @Nullable PercentageTextProgress progressBar, StringFilter filenameFilter) throws IOException {
        int BUFFER_SIZE = 2048;
        if (zipFileSource != null && zipFileSource.exists()) {
            ZipFile zipFile = new ZipFile(zipFileSource);

            int size = zipFile.size();
            if (progressBar != null)
                progressBar.setMaximum(size);
            Enumeration<?> entries = zipFile.entries();
            int progress = 0;
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (filenameFilter != null) {
                    if (!filenameFilter.accept(entry.getName())) {
                        continue;
                    }
                }

                File targetFile = new File(to, entry.getName());
                if (entry.isDirectory()) {
                    targetFile.mkdirs();
                } else {
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }
                    if (targetFile.exists()) targetFile.delete();
                    targetFile.createNewFile();
                    InputStream is = zipFile.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[BUFFER_SIZE];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.close();
                    is.close();
                }
                progress++;
                if (progressBar != null)
                    progressBar.setValue(progress);
            }
            zipFile.close();
            if (progressBar != null)
                progressBar.setValue(size);
        }
    }

}
