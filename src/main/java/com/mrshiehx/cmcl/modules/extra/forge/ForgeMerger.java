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

package com.mrshiehx.cmcl.modules.extra.forge;

import com.mrshiehx.cmcl.ConsoleMinecraftLauncher;
import com.mrshiehx.cmcl.api.download.DownloadSource;
import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.bean.SplitLibraryName;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.modules.extra.ExtraMerger;
import com.mrshiehx.cmcl.modules.version.LibrariesDownloader;
import com.mrshiehx.cmcl.utils.ConsoleUtils;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.spi.FileSystemProvider;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.*;

public class ForgeMerger implements ExtraMerger {
    private static final String MODLOADER_NAME = "Forge";
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    private static boolean clChecked = false;
    private static ClassLoader parentClassLoader = null;

    /**
     * 将 Forge 的JSON合并到原版JSON
     *
     * @return key: 如果无法安装 Forge，是否继续安装；value:如果成功合并，则为需要安装的依赖库集合，否则为空
     **/
    public Pair<Boolean, List<JSONObject>> merge(String minecraftVersion, JSONObject headJSONObject, File minecraftJarFile, boolean askContinue, @Nullable String extraVersion) {
        Map<String, JSONObject> forges;
        try {
            forges = getForges(minecraftVersion);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Pair<>(askContinue && ConsoleUtils.yesOrNo(getString("INSTALL_MODLOADER_UNABLE_DO_YOU_WANT_TO_CONTINUE", MODLOADER_NAME)), null);
        }
        if (forges.size() == 0) {
            System.out.println(getString("INSTALL_MODLOADER_NO_INSTALLABLE_VERSION_2", MODLOADER_NAME));
            return new Pair<>(askContinue && ConsoleUtils.yesOrNo(getString("INSTALL_MODLOADER_UNABLE_DO_YOU_WANT_TO_CONTINUE", MODLOADER_NAME)), null);
        }

        JSONObject forge;
        if (isEmpty(extraVersion)) {

            List<Map.Entry<String, JSONObject>> list = new LinkedList<>(forges.entrySet());
            list.sort((o1, o2) -> {
                //2021-06-14T15:14:23.68Z
                try {
                    Date dt1 = TIME_FORMAT.parse(o1.getValue().optString("modified").substring(0, 19) + "+0000");
                    Date dt2 = TIME_FORMAT.parse(o2.getValue().optString("modified").substring(0, 19) + "+0000");
                    return Long.compare(dt2.getTime(), dt1.getTime());
                } catch (Exception ignore) {
                    //ignore.printStackTrace();
                }
                return 0;
            });


            System.out.print('[');
            for (int i = list.size() - 1; i >= 0; i--) {
                String versionName = list.get(i).getKey();
                if (versionName.contains(" ")) versionName = "\"" + versionName + "\"";
                System.out.print(versionName);//legal
                if (i > 0) {
                    System.out.print(", ");
                }
            }
            System.out.println(']');


            String forgeVersionInput = selectForgeVersion(getString("INSTALL_MODLOADER_SELECT", MODLOADER_NAME), forges);
            if (forgeVersionInput == null)
                return new Pair<>(false, null);

            forge = forges.get(forgeVersionInput);
            if (forge == null)
                return new Pair<>(false, null);
        } else {
            forge = forges.get(extraVersion);
            if (forge == null) {
                System.out.println(getString("INSTALL_MODLOADER_FAILED_NOT_FOUND_TARGET_VERSION", extraVersion).replace("${NAME}", "Forge"));
                return new Pair<>(askContinue && ConsoleUtils.yesOrNo(getString("INSTALL_MODLOADER_UNABLE_DO_YOU_WANT_TO_CONTINUE", MODLOADER_NAME)), null);
            }

        }


        /*JSONArray files = forge.optJSONArray("files");
        if (files == null || files.length() == 0) {
            System.out.println(getString("INSTALL_MODLOADER_NO_FILE", MODLOADER_NAME));
            return new Pair<>(askContinue && ConsoleUtils.yesOrNo(getString("INSTALL_MODLOADER_UNABLE_DO_YOU_WANT_TO_CONTINUE", MODLOADER_NAME)), null);
        }
        boolean has = false;
        for (Object o : files) {
            if (o instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) o;
                if (category.equals(jsonObject.optString("category")) && format.equals(jsonObject.optString("format"))) {
                    has = true;
                    break;
                }
            }
        }
        if (!has) {
            System.out.println(getString("INSTALL_MODLOADER_NO_FILE", MODLOADER_NAME));
            return new Pair<>(askContinue && ConsoleUtils.yesOrNo(getString("INSTALL_MODLOADER_UNABLE_DO_YOU_WANT_TO_CONTINUE", MODLOADER_NAME)), null);
        }*/
        try {
            return installInternal(forge, headJSONObject, minecraftVersion, minecraftJarFile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new Pair<>(askContinue && ConsoleUtils.yesOrNo(getString("INSTALL_MODLOADER_UNABLE_DO_YOU_WANT_TO_CONTINUE", MODLOADER_NAME)), null);
        }
    }

    public static Map<String, JSONObject> getForges(String minecraftVersion) throws Exception {
        JSONArray jsonArray;
        try {
            jsonArray = listForgeLoaderVersions(minecraftVersion);
        } catch (Exception e) {
            //e.printStackTrace();
            throw new Exception(getString("INSTALL_MODLOADER_FAILED_TO_GET_INSTALLABLE_VERSION", MODLOADER_NAME));
        }

        if (jsonArray.length() == 0) {
            throw new Exception(getString("INSTALL_MODLOADER_NO_INSTALLABLE_VERSION", MODLOADER_NAME));
        }

        String category = "installer";
        String format = "jar";
        Map<String, JSONObject> forges = new LinkedHashMap<>();
        for (Object object : jsonArray) {
            if (object instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) object;
                JSONArray files = jsonObject.optJSONArray("files");
                if (files != null && files.length() > 0) {
                    inside:
                    for (Object o : files) {
                        if (o instanceof JSONObject) {
                            JSONObject jsonObject2 = (JSONObject) o;
                            if (category.equals(jsonObject2.optString("category")) && format.equals(jsonObject2.optString("format"))) {
                                forges.put(jsonObject.optString("version"), jsonObject);
                                break inside;
                            }
                        }
                    }
                }
            }
        }
        return forges;
    }

    public static Pair<Boolean, List<JSONObject>> installInternal(JSONObject forge, JSONObject headJSONObject, String minecraftVersion, File minecraftJarFile) throws Exception {

        String category = "installer";
        String format = "jar";
        String forgeVersion = forge.optString("version");
        String branch = forge.optString("branch");
        String mcversion = forge.optString("mcversion");

        String s = minecraftVersion + "-" + forgeVersion
                + (!isEmpty(branch) ? "-" + branch : "");//1.18.2-40.0.9
        String fileName1 = "forge-" + s + "-" + category + "." + format;
        String fileName2 = "forge-" + s + "-" + minecraftVersion + "-" + category + "." + format;
        String first = DownloadSource.getProvider().forgeMaven() + "net/minecraftforge/forge/" + s + "/" + fileName1;
        String second = DownloadSource.getProvider().forgeMaven() + "net/minecraftforge/forge/" + s + "-" + minecraftVersion + "/" + fileName2;

        //https://bmclapi2.bangbang93.com/forge/download?mcversion=1.18.2&version=40.0.35&category=installer&format=jar
        //https://download.mcbbs.net/forge/download?mcversion=1.18.2&version=40.0.1&category=installer&format=jar


        File installer = new File("cmcl", "forge-" + s + ".jar");
        System.out.println(getString("INSTALL_MODLOADER_DOWNLOADING_FILE"));
        String finalDownload;
        try {
            ConsoleMinecraftLauncher.downloadFile(first, installer);
            finalDownload = first;
        } catch (Exception ignore) {
            try {
                ConsoleMinecraftLauncher.downloadFile(second, installer);
                finalDownload = second;
            } catch (Exception ignored) {

                StringBuilder stringBuilder = new StringBuilder();
                char start = '?';
                if (!isEmpty(mcversion)) {
                    stringBuilder.append(start).append("mcversion=").append(mcversion);
                    start = '&';
                }
                if (!isEmpty(forgeVersion)) {
                    stringBuilder.append(start).append("version=").append(forgeVersion);
                    start = '&';
                }
                if (!isEmpty(branch)) {
                    stringBuilder.append(start).append("branch=").append(branch);
                    start = '&';
                }
                stringBuilder.append(start).append("category=").append(category);
                start = '&';
                stringBuilder.append(start).append("format=").append(format);
                //start='&';

                String third = DownloadSource.getProvider().thirdPartyForge() + stringBuilder;

                try {
                    URL ConnectUrl = new URL(third);
                    HttpURLConnection connection = (HttpURLConnection) ConnectUrl.openConnection();
                    connection.setDoInput(true);
                    connection.setUseCaches(false);
                    connection.setDoOutput(true);
                    connection.setRequestMethod("GET");
                    FileUtils.bytes2File(installer, Utils.httpURLConnection2Bytes(connection), false);
                    finalDownload = third;
                } catch (Exception e) {
                    throw new Exception(getString("INSTALL_MODLOADER_FAILED_DOWNLOAD", MODLOADER_NAME) + ": " + e);
                }

            }
        }
        if (!installer.exists() || installer.length() == 0) {
            throw new Exception(getString("INSTALL_MODLOADER_FAILED_DOWNLOAD", MODLOADER_NAME));
        }
        JSONObject installProfile;
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(installer);
            installProfile = new JSONObject(Utils.inputStream2String(zipFile.getInputStream(zipFile.getEntry("install_profile.json"))));
        } catch (IOException e) {
            throw new Exception(getString("INSTALL_MODLOADER_FAILED_WITH_REASON", MODLOADER_NAME, getString("EXCEPTION_READ_FILE_WITH_PATH", installer.getAbsolutePath()) + ": " + e));
        } catch (JSONException e) {
            throw new Exception(getString("INSTALL_MODLOADER_FAILED_WITH_REASON", MODLOADER_NAME, getString("EXCEPTION_PARSE_FILE")));
        }

        JSONObject version = null;
        List<JSONObject> librariesNeedToInstall;
        if (installProfile.has("spec")) {
            //new
            if (!installProfile.optString("minecraft").equals(minecraftVersion)) {
                throw new Exception(getString("INSTALL_MODLOADER_FAILED_MC_VERSION_MISMATCH", MODLOADER_NAME));
            }
            String json = installProfile.optString("json");

            FileSystem installerFileSystem = null;//不会NULL
            for (FileSystemProvider fileSystemProvider : FileSystemProvider.installedProviders()) {
                if (fileSystemProvider.getScheme().equalsIgnoreCase("jar")) {
                    try {
                        version = new JSONObject(new String(Files.readAllBytes((installerFileSystem = (fileSystemProvider.newFileSystem(installer.toPath(), new HashMap<>()))).getPath(json))));
                        break;
                    } catch (IOException e) {
                        throw new Exception(getString("INSTALL_MODLOADER_FAILED_WITH_REASON", MODLOADER_NAME, getString("EXCEPTION_READ_FILE_WITH_PATH", json) + ": " + e));

                    } catch (JSONException e) {
                        throw new Exception(getString("INSTALL_MODLOADER_FAILED_WITH_REASON", MODLOADER_NAME, getString("EXCEPTION_PARSE_FILE_WITH_PATH", json)));

                    }
                }
            }
            if (version == null) {
                throw new Exception(getString("INSTALL_MODLOADER_FAILED_WITH_REASON", MODLOADER_NAME, getString("EXCEPTION_READ_FILE_WITH_PATH", json)));

            }

            JSONArray installProfileLibraries = installProfile.optJSONArray("libraries");
            if (installProfileLibraries != null && installProfileLibraries.length() > 0) {
                for (Object object : installProfileLibraries) {
                    if (object instanceof JSONObject) {
                        JSONObject library = (JSONObject) object;
                        Pair<String, String> pair = Utils.getLibraryDownloadURLAndStoragePath(library);
                        if (pair == null) {
                            System.out.println(getString("MESSAGE_NOT_FOUND_LIBRARY_DOWNLOAD_URL", library.optString("name")));
                            continue;
                        }
                        String url = pair.getKey();
                        if (isEmpty(url)) {
                            String path = pair.getValue();

                            File file = new File(librariesDir, path);
                            if (file.length() <= 0) {
                                String path2 = "/maven" + Utils.addSlashIfMissingAtStart(path);
                                try {
                                    System.out.println(getString("MESSAGE_UNZIPPING_FILE", path2));
                                    Utils.createFile(file, true);
                                    FileUtils.bytes2File(file, Files.readAllBytes(installerFileSystem.getPath(path2)), false);
                                } catch (Exception e) {
                                    System.out.println(getString("MESSAGE_FAILED_TO_DECOMPRESS_FILE", path2, e));
                                }
                            }
                        } else {
                            LibrariesDownloader.downloadSingleLibrary(library);
                        }
                    }
                }
            }
            {
                String forgePath = installProfile.optString("path");
                if (!isEmpty(forgePath)) {
                    SplitLibraryName nameSplit = Utils.splitLibraryName(forgePath);
                    if (nameSplit != null) {

                        String fileName = nameSplit.getFileName();
                        String path = Utils.getPathFromLibraryName(nameSplit) + "/" + fileName;

                        File file = new File(librariesDir, path);
                        if (file.length() <= 0) {
                            String path2 = "/maven" + Utils.addSlashIfMissingAtStart(path);
                            try {
                                System.out.println(getString("MESSAGE_UNZIPPING_FILE", path2));
                                Utils.createFile(file, true);
                                FileUtils.bytes2File(file, Files.readAllBytes(installerFileSystem.getPath(path2)), false);
                            } catch (Exception e) {
                                System.out.println(getString("MESSAGE_FAILED_TO_DECOMPRESS_FILE", path2, e));
                            }
                        }

                    }


                }
            }


            Map<String, String> data = new HashMap<>();
            File temp = new File("cmcl", "forge" + System.currentTimeMillis());
            temp.mkdirs();
            JSONObject dataJSON = installProfile.optJSONObject("data");
            for (Map.Entry<String, Object> entry : dataJSON.toMap().entrySet()) {
                if ((entry.getValue() instanceof Map)) {
                    Object clientObject = ((Map<?, ?>) entry.getValue()).get("client");
                    if (!(clientObject instanceof String)) continue;
                    String client = (String) clientObject;
                    if (!Utils.isEmpty(client)) {
                        String key = entry.getKey();

                        if (client.charAt(0) == '[' && client.charAt(client.length() - 1) == ']') { //Artifact

                            String inside = client.substring(1, client.length() - 1);
                            SplitLibraryName nameSplit = SplitLibraryName.valueOf(inside);
                            if (nameSplit == null) continue;
                            File libraryFile = nameSplit.getPhysicalFile();
                            data.put(key, libraryFile.getAbsolutePath());

                        } else if (client.charAt(0) == '\'' && client.charAt(client.length() - 1) == '\'') { //Literal
                            String inside = client.substring(1, client.length() - 1);
                            data.put(key, inside);
                        } else {
                            File target = new File(temp, client);
                            try {
                                if (target.exists()) target.delete();
                                FileUtils.bytes2File(target, Files.readAllBytes(installerFileSystem.getPath(client)), false);
                            } catch (Exception e) {
                                System.out.println(getString("MESSAGE_FAILED_TO_DECOMPRESS_FILE", client, e));
                            }
                            data.put(key, target.getAbsolutePath());
                        }
                    }
                }
            }

            data.put("SIDE", "client");
            data.put("MINECRAFT_JAR", minecraftJarFile.getAbsolutePath());
            data.put("MINECRAFT_VERSION", installProfile.optString("minecraft"));
            data.put("ROOT", gameDir.getAbsolutePath());
            data.put("INSTALLER", installer.getAbsolutePath());
            data.put("LIBRARY_DIR", librariesDir.getAbsolutePath());

            JSONArray processorsJSON = installProfile.optJSONArray("processors");
            List<JSONObject> processors = Utils.jsonArrayToJSONObjectList(processorsJSON, jsonObject -> {
                JSONArray sides = jsonObject.optJSONArray("sides");
                boolean contains = false;
                if (sides != null && sides.length() > 0) {
                    for (Object side : sides) {
                        if ("client".equals(side)) {
                            contains = true;
                            break;
                        }
                    }
                } else {
                    contains = true;
                }
                return contains;
            });


            for (JSONObject processor : processors) {
                /*JSONObject outputsJSONObject=processor.optJSONObject("processor");
                Map<String,String>outputs=new HashMap<>();
                if(outputsJSONObject!=null){
                    for(Map.Entry<String,Object>output:outputsJSONObject.toMap().entrySet()){
                        if(!(output.getValue() instanceof String))continue;
                        String key=output.getKey();
                        String value=(String)output.getValue();
                        if (key.charAt(0) == '[' && key.endsWith("]")) {
                            SplitLibraryName splitLibraryName=SplitLibraryName.valueOf(key.substring(1, key.length() - 1));
                            key = splitLibraryName.getPhysicalFile().getAbsolutePath();
                        } else {
                            key = replaceTokens(data, key);
                        }

                        if (!Utils.isEmpty(value))
                            value = replaceTokens(data, value);

                        outputs.put(key, value);
                        ......
                    }
                }*/

                SplitLibraryName splitLibraryName = SplitLibraryName.valueOf(processor.optString("jar"));
                if (splitLibraryName == null) {
                    continue;
                }
                File processorJarPhysicalFile = splitLibraryName.getPhysicalFile();
                if (!processorJarPhysicalFile.exists() || !processorJarPhysicalFile.isFile()) {
                    continue;
                }

                try (JarFile jarFile = new JarFile(processorJarPhysicalFile)) {
                    String mainClass = jarFile.getManifest().getMainAttributes().getValue(Attributes.Name.MAIN_CLASS);
                    List<URL> classpath = new ArrayList<>();
                    classpath.add(processorJarPhysicalFile.toURI().toURL());
                    for (Object o : processor.optJSONArray("classpath")) {
                        if (o instanceof String) {
                            SplitLibraryName splitLibraryName1 = SplitLibraryName.valueOf((String) o);
                            if (splitLibraryName1 != null) {
                                classpath.add(splitLibraryName1.getPhysicalFile().toURI().toURL());
                            }
                        }
                    }

                    List<String> args = new ArrayList<>();
                    for (Object o : processor.optJSONArray("args")) {
                        if (o instanceof String) {
                            String arg = (String) o;
                            char start = arg.charAt(0);
                            char end = arg.charAt(arg.length() - 1);

                            if (start == '[' && end == ']') {
                                SplitLibraryName splitLibraryName1 = SplitLibraryName.valueOf(arg.substring(1, arg.length() - 1));
                                if (splitLibraryName1 == null) continue;
                                args.add(splitLibraryName1.getPhysicalFile().getAbsolutePath());
                            } else {
                                args.add(replaceTokens(data, arg));
                            }
                        }
                    }


                    ClassLoader cl = new URLClassLoader(classpath.toArray(new URL[0]), getParentClassloader());
                    // Set the thread context classloader to be our newly constructed one so that service loaders work
                    Thread currentThread = Thread.currentThread();
                    ClassLoader threadClassloader = currentThread.getContextClassLoader();
                    currentThread.setContextClassLoader(cl);
                    try {
                        Class<?> cls = Class.forName(mainClass, true, cl);
                        Method main = cls.getDeclaredMethod("main", String[].class);
                        main.invoke(null, (Object) args.toArray(new String[0]));
                    } catch (InvocationTargetException ite) {
                        Throwable e = ite.getCause();
                        e.printStackTrace();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    } finally {
                        // Set back to the previous classloader
                        currentThread.setContextClassLoader(threadClassloader);
                    }


                } catch (Exception e) {
                    System.out.println(getString("MESSAGE_INSTALL_FORGE_FAILED_EXECUTE_PROCESSOR", e));
                }
            }

            Utils.close(zipFile);
            installer.delete();
            Utils.deleteDirectory(temp);

        } else if (installProfile.optJSONObject("install") != null && installProfile.optJSONObject("versionInfo") != null) {
            ///old
            if (!installProfile.optJSONObject("install", new JSONObject()).optString("minecraft").equals(minecraftVersion)) {
                throw new Exception(getString("INSTALL_MODLOADER_FAILED_MC_VERSION_MISMATCH", MODLOADER_NAME));

            }
            version = installProfile.optJSONObject("versionInfo");
            JSONObject installJSONObject = installProfile.optJSONObject("install");
            String path = installJSONObject.optString("path");
            String filePath = installJSONObject.optString("filePath");

            SplitLibraryName nameSplit = Utils.splitLibraryName(path);
            File libraryFile = nameSplit.getPhysicalFile();

            try {
                FileUtils.inputStream2File(zipFile.getInputStream(zipFile.getEntry(filePath)), libraryFile);
                zipFile.close();
                installer.delete();
            } catch (IOException e) {
                throw new Exception(getString("INSTALL_MODLOADER_FAILED_WITH_REASON", MODLOADER_NAME, getString("MESSAGE_FAILED_TO_DECOMPRESS_FILE", filePath, e)));

            }


        } else {
            throw new Exception(getString("INSTALL_MODLOADER_FAILED_UNKNOWN_TYPE", MODLOADER_NAME));

        }


        JSONArray forgeLibraries = version.optJSONArray("libraries");
        librariesNeedToInstall = new LinkedList<>();
        if (forgeLibraries != null) {
            for (Object o : forgeLibraries) {
                if (o instanceof JSONObject) {
                    JSONObject j = (JSONObject) o;
                    String name = j.optString("name");
                    if (!name.startsWith("net.minecraftforge:forge:")) {
                        SplitLibraryName nameSplit2 = Utils.splitLibraryName(name);
                        if (nameSplit2 != null) {
                            File file = nameSplit2.getPhysicalFile();
                            if (!file.exists() && file.length() == 0) librariesNeedToInstall.add(j);
                        }
                    }
                }
            }
            JSONArray mcLibraries = headJSONObject.optJSONArray("libraries");
            headJSONObject.put("libraries", Utils.mergeLibraries(Utils.jsonArrayToJSONObjectList(mcLibraries), Utils.jsonArrayToJSONObjectList(forgeLibraries)));
        }


        String mainClass = version.optString("mainClass");
        if (!Utils.isEmpty(mainClass)) headJSONObject.put("mainClass", mainClass);

        JSONObject forgeInHead = new JSONObject();
        forgeInHead.put("version", forgeVersion);
        forgeInHead.put("jarUrl", finalDownload);
        headJSONObject.put("forge", forgeInHead);


        String minecraftArguments = version.optString("minecraftArguments");
        JSONObject arguments = version.optJSONObject("arguments");
        if (!isEmpty(minecraftArguments)) {
            String hmca = headJSONObject.optString("minecraftArguments");
            if (hmca.isEmpty())
                headJSONObject.put("minecraftArguments", minecraftArguments);
            else {
                Arguments arguments1 = new Arguments(hmca, false, false);
                Arguments arguments2 = new Arguments(minecraftArguments, false, false);
                arguments1.merge(arguments2);
                headJSONObject.put("minecraftArguments", minecraftArguments = arguments1.toString("--"));
            }
        }

        if (arguments != null) {
            JSONObject argumentsMC = headJSONObject.optJSONObject("arguments");
            if (argumentsMC != null) {
                JSONArray gameMC = argumentsMC.optJSONArray("game");
                JSONArray jvmMC = argumentsMC.optJSONArray("jvm");
                JSONArray game = arguments.optJSONArray("game");
                if (game != null && game.length() > 0) {
                    if (gameMC == null) argumentsMC.put("game", gameMC = new JSONArray());
                    gameMC.putAll(game);
                }
                JSONArray jvm = arguments.optJSONArray("jvm");
                if (jvm != null && jvm.length() > 0) {
                    if (jvmMC == null) argumentsMC.put("jvm", jvmMC = new JSONArray());
                    jvmMC.putAll(jvm);
                }
            } else {
                headJSONObject.put("arguments", arguments);
            }
        }


        return new Pair<>(true, librariesNeedToInstall);
    }


    private static String selectForgeVersion(String text, Map<String, JSONObject> forges) {
        System.out.print(text);//legal
        Scanner scanner = new Scanner(System.in);
        try {
            String s = scanner.nextLine();
            if (!isEmpty(s)) {
                JSONObject jsonObject = forges.get(s);
                if (jsonObject != null) return s;
                return selectForgeVersion(getString("INSTALL_MODLOADER_SELECT_NOT_FOUND", s, MODLOADER_NAME), forges);
            } else {
                return selectForgeVersion(text, forges);
            }
        } catch (NoSuchElementException ignore) {
            return null;
        }
    }

    private static JSONArray listForgeLoaderVersions(String minecraftVersion) throws Exception {
        return new JSONArray(Utils.get(DownloadSource.getProvider().forge() + "minecraft/" + minecraftVersion));
    }


    /**
     * @from ForgeInstaller
     **/
    public static String replaceTokens(Map<String, String> tokens, String value) {
        StringBuilder buf = new StringBuilder();

        for (int x = 0; x < value.length(); x++) {
            char c = value.charAt(x);
            if (c == '\\') {
                if (x == value.length() - 1)
                    throw new IllegalArgumentException("Illegal pattern (Bad escape): " + value);
                buf.append(value.charAt(++x));
            } else if (c == '{' || c == '\'') {
                StringBuilder key = new StringBuilder();
                for (int y = x + 1; y <= value.length(); y++) {
                    if (y == value.length())
                        throw new IllegalArgumentException("Illegal pattern (Unclosed " + c + "): " + value);
                    char d = value.charAt(y);
                    if (d == '\\') {
                        if (y == value.length() - 1)
                            throw new IllegalArgumentException("Illegal pattern (Bad escape): " + value);
                        key.append(value.charAt(++y));
                    } else if (c == '{' && d == '}') {
                        x = y;
                        break;
                    } else if (c == '\'' && d == '\'') {
                        x = y;
                        break;
                    } else
                        key.append(d);
                }
                if (c == '\'')
                    buf.append(key);
                else {
                    if (!tokens.containsKey(key.toString()))
                        throw new IllegalArgumentException("Illegal pattern: " + value + " Missing Key: " + key);
                    buf.append(tokens.get(key.toString()));
                }
            } else {
                buf.append(c);
            }
        }

        return buf.toString();
    }

    /**
     * @from ForgeInstaller
     **/
    private static synchronized ClassLoader getParentClassloader() { //Reflectively try and get the platform classloader, done this way to prevent hard dep on J9.
        if (!clChecked) {
            clChecked = true;
            if (!System.getProperty("java.version").startsWith("1.")) { //in 9+ the changed from 1.8 to just 9. So this essentially detects if we're <9
                try {
                    Method getPlatform = ClassLoader.class.getDeclaredMethod("getPlatformClassLoader");
                    parentClassLoader = (ClassLoader) getPlatform.invoke(null);
                } catch (Exception ignore) {

                }
            }
        }
        return parentClassLoader;
    }
}
