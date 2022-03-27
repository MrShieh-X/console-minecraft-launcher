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

import java.io.*;

public class FileUtils {
    public static void hexWrite(String bytes, File file) throws IOException {
        hexWrite(hexString2Bytes(bytes), file);
    }

    public static void hexWrite(byte[] bytes, File file) throws IOException {
        Utils.createFile(file);
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
        Utils.createFile(file, false);

        FileOutputStream fileOutputStream = new FileOutputStream(file, append);
        fileOutputStream.write(bytes, 0, bytes.length);
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    public static byte[] inputStream2ByteArray(InputStream input) throws IOException {
        return Utils.inputStream2ByteArray(input);
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
}
