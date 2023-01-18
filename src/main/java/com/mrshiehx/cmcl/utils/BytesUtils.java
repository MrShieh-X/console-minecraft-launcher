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

package com.mrshiehx.cmcl.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class BytesUtils {
    public static String bytesToString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        if (bytes != null && bytes.length > 0) {
            for (byte aByte : bytes) {
                builder.append(String.format("%02X", aByte));
            }
        }
        return builder.toString().toUpperCase();
    }

    public static String getBytesHashSHA256String(byte[] bytes) throws NoSuchAlgorithmException {
        return bytesToString(getBytesHashSHA256(bytes)).toLowerCase();
    }

    public static byte[] getBytesHashSHA256(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(bytes, 0, bytes.length);
        return digest.digest();
    }
}
