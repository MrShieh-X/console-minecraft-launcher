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

import com.mrshiehx.cmcl.CMCL;
import com.mrshiehx.cmcl.api.download.DownloadSource;
import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.bean.SplitLibraryName;
import com.mrshiehx.cmcl.utils.internet.DownloadUtils;
import org.json.JSONObject;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Utils {
    public static String getString(String name) {
        return CMCL.getString(name);
    }

    public static String getString(String name, Object... objects) {
        return CMCL.getString(name, objects);
    }

    public static <K, V> Map<K, V> mapOf(Iterable<Pair<K, V>> pairs) {
        Map<K, V> map = new LinkedHashMap<>();
        for (Pair<K, V> pair : pairs)
            map.put(pair.getKey(), pair.getValue());
        return map;
    }

    public static boolean isEmpty(CharSequence c) {
        return c == null || c.length() == 0;
    }

    public static String valueOf(Object value) {
        return value == null ? "" : value.toString();
    }

    public static void saveConfig(JSONObject config) {
        CMCL.saveConfig(config);
    }

    public static JSONObject getConfig() {
        return CMCL.getConfig();
    }

    public static <X> List<X> removeDuplicate(List<X> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = list.size() - 1; j > i; j--) {
                if (Objects.equals(list.get(j), list.get(i))) {
                    list.remove(j);
                }
            }
        }
        return list;
    }


    public static void printfln(String str, Object... object) {
        System.out.printf(str, object);//legal
        System.out.println();
    }

    public static void printflnErr(String str, Object... object) {
        System.out.printf(str, object);//legal
        System.out.println();
    }


    public static String inputStream2String(InputStream stream) throws IOException {
        try (InputStream is = stream) {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            while (true) {
                int len = is.read(buf);
                if (len == -1)
                    break;
                result.write(buf, 0, len);
            }
            return result.toString(UTF_8.name());
        }
    }

    public static String inputStream2String(InputStream stream, int length) throws IOException {
        try (InputStream is = stream) {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buf = new byte[length];

            int len = is.read(buf, 0, length);
            result.write(buf, 0, len);
            return result.toString(UTF_8.name());
        }
    }

    public static File downloadVersionsFile() throws IOException {
        File cmcl = CMCL.getCMCLWorkingDirectory();
        cmcl.mkdirs();
        File versionsFile = new File(cmcl, "versions.json");
        FileUtils.createFile(versionsFile, true);
        DownloadUtils.downloadFile(DownloadSource.getProvider().versionManifest(), versionsFile);
        return versionsFile;
    }

    public static String getTypeText(String simpleName) {
        if ("String".equals(simpleName)) {
            return getString("DATATYPE_STRING");
        } else if ("Boolean".equals(simpleName)) {
            return getString("DATATYPE_BOOLEAN");
        } else if ("Integer".equals(simpleName)) {
            return getString("DATATYPE_INTEGER");
        } else if ("Double".equals(simpleName) || "Float".equals(simpleName) || "BigDecimal".equals(simpleName)) {
            return getString("DATATYPE_FRACTION");
        } else if ("ArrayList".equals(simpleName)) {
            return getString("DATATYPE_JSON_ARRAY");
        } else if ("HashMap".equals(simpleName)) {
            return getString("DATATYPE_JSON_OBJECT");
        } else return simpleName;
    }

    public static int parseWithPrompting(String value) throws Exception {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.out.println(getString("CONSOLE_UNSUPPORTED_VALUE", value));
            throw new Exception();
        }
    }


    public static List<String> addDoubleQuotationMark(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).contains(" ")) {
                list.set(i, "\"" + list.get(i) + "\"");
            }
        }
        return list;
    }

    public static String randomUUIDNoSymbol() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String getPathFromLibraryName(SplitLibraryName nameSplit) {
        return nameSplit.first.replace(".", "/") + "/" + nameSplit.second + "/" + nameSplit.version;
    }

    public static String getExtension(String string) {
        if (isEmpty(string)) return null;
        int indexOf = string.lastIndexOf('.');
        if (indexOf < 0 || indexOf == string.length() - 1) return null;
        return string.substring(indexOf + 1);
    }

    public static void close(Closeable t) {
        try {
            t.close();
        } catch (IOException ignore) {
        }
    }


    public static String downloadFileFailedText(String url, File file, Exception e) {
        return url.endsWith("/" + file.getName()) ?
                getString("MESSAGE_FAILED_DOWNLOAD_FILE_WITH_REASON_WITH_URL", e, url, file.getParentFile().getAbsolutePath()) :
                getString("MESSAGE_FAILED_DOWNLOAD_FILE_WITH_REASON_WITH_URL_WITH_NAME", e, url, file.getParentFile().getAbsolutePath(), file.getName());
    }

    public static void downloadFileFailed(String url, File file, Exception e) {
        System.out.println(downloadFileFailedText(url, file, e));
    }

    public static String[] xsplit(String s, String regex) {
        String[] ss = s.split(regex);
        if (ss[0].equals(s)) {
            return new String[0];
        }
        return ss;
    }

    public static <T> Stream<T> iteratorToStream(Iterator<T> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false);
    }

    public static String getExecutableFilePath() {
        try {
            return URLDecoder.decode(Utils.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
