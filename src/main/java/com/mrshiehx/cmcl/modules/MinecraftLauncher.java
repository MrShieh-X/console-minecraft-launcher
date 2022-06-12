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
package com.mrshiehx.cmcl.modules;

import com.mrshiehx.cmcl.ConsoleMinecraftLauncher;
import com.mrshiehx.cmcl.bean.*;
import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.bean.arguments.ValueArgument;
import com.mrshiehx.cmcl.exceptions.EmptyNativesException;
import com.mrshiehx.cmcl.exceptions.LaunchException;
import com.mrshiehx.cmcl.exceptions.LibraryDefectException;
import com.mrshiehx.cmcl.interfaces.Void;
import com.mrshiehx.cmcl.utils.OperatingSystem;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.authlib.AuthlibUtils;
import com.sun.management.OperatingSystemMXBean;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.regex.Pattern;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.*;
import static com.mrshiehx.cmcl.utils.Utils.splitCommand;
import static com.mrshiehx.cmcl.utils.Utils.clearRedundantSpaces;

public class MinecraftLauncher {
    /**
     * Get Minecraft Launch Command Arguments
     *
     * @param minecraftJarFile         jar file
     * @param minecraftVersionJsonFile json file
     * @param gameDir                  game directory
     * @param assetsDir                assets directory
     * @param resourcePacksDir         resource packs directory
     * @param playerName               player name
     * @param javaPath                 java path
     * @param maxMemory                max memory
     * @param miniMemory               mini memory
     * @param width                    window width
     * @param height                   window height
     * @param fullscreen               is window fullscreen
     * @param accessToken              access token of official account
     * @param uuid                     uuid of official account
     * @param isDemo                   is Minecraft demo
     * @param customScreenSize         does player custom the size of screen
     * @param startLaunch              after judgment, prepare to start, nullable
     * @param jvmArgs                  append jvm arguments (nullable)
     * @param gameArgs                 append game arguments (nullable)
     * @param authlibInformation       authlib-injector account (nullable)
     * @return Launch Command Arguments
     * @throws LaunchException        launch exception
     * @throws IOException            io or file exception
     * @throws JSONException          exception to parsing json
     * @throws LibraryDefectException exception to if some libraries are not found
     * @author MrShiehX
     * @update v1.3
     */
    public static List<String> getMinecraftLaunchCommandArguments(
            File versionDir,
            File minecraftJarFile,
            File minecraftVersionJsonFile,
            File gameDir,
            File assetsDir,
            File resourcePacksDir,
            String playerName,
            String javaPath,
            long maxMemory,
            int miniMemory,
            int width,
            int height,
            boolean fullscreen,
            String accessToken,
            String uuid,
            boolean isDemo,
            boolean customScreenSize,
            JSONObject properties,
            @Nullable Void startLaunch,
            @Nullable List<String> jvmArgs,
            @Nullable Map<String, String> gameArgs,
            @Nullable AuthlibInformation authlibInformation) throws
            LibraryDefectException,
            EmptyNativesException,
            LaunchException,
            IOException,
            JSONException {
        if (!new File(javaPath).exists()) {
            throw new LaunchException(getString("EXCEPTION_JAVA_NOT_FOUND"));
        }
        if (null == gameDir) {
            gameDir = new File(".minecraft");
        }
        if (null == assetsDir) {
            assetsDir = new File(gameDir, "assets");
        }
        if (null == resourcePacksDir) {
            resourcePacksDir = new File(gameDir, "resourcepacks");
        }

        if (!gameDir.exists()) {
            throw new LaunchException(getString("MESSAGE_NOT_FOUND_GAME_DIR"));
        }
        if (maxMemory == 0) {
            throw new LaunchException(getString("EXCEPTION_MAX_MEMORY_IS_ZERO"));
        }

        if (!assetsDir.exists()) {
            assetsDir.mkdirs();
        }

        if (!resourcePacksDir.exists()) {
            resourcePacksDir.mkdirs();
        }

        long physicalTotal = ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean())./*getTotalMemorySize*/getTotalPhysicalMemorySize() / 1048576;

        if (maxMemory > physicalTotal) {
            throw new LaunchException(getString("EXCEPTION_MAX_MEMORY_TOO_BIG"));
        }

        String contentOfJsonFile;
        if (!minecraftVersionJsonFile.exists()) {
            throw new LaunchException(getString("EXCEPTION_VERSION_JSON_NOT_FOUND"));
        } else {
            contentOfJsonFile = Utils.readFileContent(minecraftVersionJsonFile);
        }

        File authlibFile = null;
        if (authlibInformation != null) {
            if (authlibInformation.isEmpty()) authlibInformation = null;
        }

        if (authlibInformation != null) {
            try {
                authlibFile = AuthlibUtils.getAuthlibInjector();
            } catch (Exception e) {
                Utils.printflnErr(getString("UNAVAILABLE_AUTHLIB_ACCOUNT_REASON"), e);
                Utils.printflnErr(authlibInformation.forOfflineSkin ? getString("UNAVAILABLE_CUSTOM_SKIN") : getString("UNAVAILABLE_AUTHLIB_ACCOUNT"));

                authlibInformation = null;
            }
        }

        if (authlibInformation != null) {
            if (!Utils.isEmpty(authlibInformation.uuid)) uuid = authlibInformation.uuid;
            if (!Utils.isEmpty(authlibInformation.token)) accessToken = authlibInformation.token;
        }

        if (startLaunch != null) startLaunch.execute();

        JSONObject headJsonObject = new JSONObject(contentOfJsonFile);

        if (!isEmpty(headJsonObject.optString("inheritsFrom"))) {
            throw new LaunchException(getString("EXCEPTION_INCOMPLETE_VERSION"));
        } else {
            if (!minecraftJarFile.exists()) {
                throw new LaunchException(getString("EXCEPTION_VERSION_NOT_FOUND"));
            }
        }

        JSONObject javaVersionJO = headJsonObject.optJSONObject("javaVersion");
        int javaVersionInt = Utils.getJavaVersion(javaPath);
        if (javaVersionInt > -1 && javaVersionJO != null) {
            int majorVersion = javaVersionJO.optInt("majorVersion", -1);
            if (majorVersion != -1 && javaVersionInt < majorVersion) {
                throw new LaunchException(String.format(getString("EXCEPTION_JAVA_VERSION_TOO_LOW"), majorVersion, javaVersionInt));
            }
        }


        //File loggingFile = new File(minecraftVersionJsonFile.getParentFile(), "log4j2.xml");

        List<String> arguments = new LinkedList<>();
        List<String> minecraftArguments = new LinkedList<>();
        List<String> jvmArguments = new LinkedList<>();
        //String id = headJsonObject.optString("id", "1.0");


        JSONObject assetIndexObject = headJsonObject.optJSONObject("assetIndex");

        String assetsIndex = assetIndexObject.optString("id");

        //String jvmArgumentsBuilder=null;

            /*18wXXa;arguments
            16wXXa;minecraftArguments
            17w44a;arguments
            17w42a;minecraftArguments
            3D Shareware v1.34;arguments
            1.RV-Pre1; minecraftArguments
            1.13 arguments
            1.12 minecraftArguments
            */

        if (headJsonObject.optJSONObject("arguments") != null) {
            getGameArguments(headJsonObject, isDemo, customScreenSize, minecraftArguments);
            getJavaVirtualMachineArguments(headJsonObject, isDemo, customScreenSize, jvmArguments);
        }

        String args = headJsonObject.optString("minecraftArguments");
        if (!Utils.isEmpty(args)) {
            List<String> argus = splitCommand(clearRedundantSpaces(args));
            minecraftArguments.addAll(argus);
        }

        //v1.3 去除冗余
        Arguments a = Arguments.valueOf(minecraftArguments, false, false).removeDuplicate();
        List<Argument> arguments1 = new LinkedList<>(a.getArguments());

        arguments1.sort((o1, o2) -> {
            if ("tweakClass".equals(o1.key) && !"tweakClass".equals(o2.key)) {
                return -1;
            } else if (!"tweakClass".equals(o1.key) && "tweakClass".equals(o2.key)) {
                return 1;
            } else if (!"tweakClass".equals(o1.key) /*&& !"tweakClass".equals(o2.key)*/) {
                return 0;
            } else {
                if (o1 instanceof ValueArgument && o2 instanceof ValueArgument) {
                    String o1s = ((ValueArgument) o1).value;
                    String o2s = ((ValueArgument) o2).value;
                    String f1 = "net.minecraftforge.legacy._1_5_2.LibraryFixerTweaker";
                    String f2 = "cpw.mods.fml.common.launcher.FMLTweaker";
                    String f3 = "net.minecraftforge.fml.common.launcher.FMLTweaker";
                    if (o1s.equals(f1) || o1s.equals(f2) || o1s.equals(f3)) {
                        return -1;
                    } else if (o2s.equals(f1) || o2s.equals(f2) || o2s.equals(f3)) {
                        return 1;
                    } else return 0;
                } else {
                    return 0;
                }
            }
        });

        minecraftArguments.clear();
        for (Argument argument : arguments1) {
            minecraftArguments.add("--" + argument.key);
            if (argument instanceof ValueArgument) {
                minecraftArguments.add(((ValueArgument) argument).value);
            }
        }

        boolean hasThree = false;
        int hasOFT = -1;
        boolean hasOFTorOFFT = false;
        int size = minecraftArguments.size();
        for (int i = 0; i < size; i++) {
            String argument = minecraftArguments.get(i);
            if (("-tweakClass".equals(argument) || "--tweakClass".equals(argument) || "/tweakClass".equals(argument)) && (i + 1 < size)) {
                String tweakClass = minecraftArguments.get(i + 1);

                boolean aa = "optifine.OptiFineTweaker".equals(tweakClass);
                if (aa) {
                    hasOFT = i + 1;
                }
                if ((aa || "OptiFineForgeTweaker.OptiFineTweaker".equals(tweakClass)) & !hasOFTorOFFT) {
                    hasOFTorOFFT = true;
                }
                if (("com.mumfrey.liteloader.launch.LiteLoaderTweaker".equals(tweakClass)
                        || "net.minecraftforge.fml.common.launcher.FMLTweaker".equals(tweakClass)
                        || "cpw.mods.fml.common.launcher.FMLTweaker".equals(tweakClass)
                        || "net.minecraftforge.legacy._1_5_2.LibraryFixerTweaker".equals(tweakClass))) {
                    hasThree = true;
                }

                if (hasOFT >= 0 && hasThree && hasOFTorOFFT) break;
            }
        }

        if (hasOFT >= 0 && hasThree) {
            minecraftArguments.set(hasOFT, "optifine.OptiFineForgeTweaker");
        }


        String mainClass = headJsonObject.optString("mainClass", "net.minecraft.client.main.Main");

        boolean var = hasThree && hasOFTorOFFT && ("net.minecraft.launchwrapper.Launch".equals(mainClass) || "cpw.mods.modlauncher.Launcher".equals(mainClass));

        JSONArray libraries = headJsonObject.optJSONArray("libraries");
        File librariesFile = new File(gameDir, "libraries");

        ThreeReturns<List<Library>, List<Library>, Boolean> pair = getLibraries(libraries, librariesFile, var);
        List<Library> librariesPaths = pair.first;
        List<Library> notFound = pair.second;


        if (notFound.size() > 0) {
            throw new LibraryDefectException(notFound);
        }


        File nativesFolder = Utils.getNativesDir(minecraftVersionJsonFile.getParentFile());
        StringBuilder librariesString = new StringBuilder();
        for (Library library : librariesPaths) {
            librariesString.append(library.localFile.getAbsolutePath()).append(File.pathSeparator);
        }
        librariesString.append(minecraftJarFile.getAbsolutePath());

        String assetsPath = assetsDir.getAbsolutePath();

        if (assetsIndex.equals("legacy")) {
            assetsPath = new File(assetsDir, "virtual/legacy").getAbsolutePath();
        }


        for (int i = 0; i < minecraftArguments.size(); i++) {
                /*boolean replace = true;
                String replaceTo = null;*/
            String source;
            String s = minecraftArguments.get(i);

            if (s.contains(source = "${main_class}")) {
                minecraftArguments.set(i, s = s.replace(source, headJsonObject.optString("mainClass", "net.minecraft.client.main.Main")));
            }
            if (s.contains(source = "${auth_player_name}")) {
                minecraftArguments.set(i, s = s.replace(source, playerName));
            }
            if (s.contains(source = "${version_name}")) {
                minecraftArguments.set(i, s = s.replace(source, /*minecraftJarFile.getParentFile().getName()*/headJsonObject.optString("id")));
            }
            String n = (authlibInformation != null && !authlibInformation.forOfflineSkin) ? String.format("CMCL %s(%s)", CMCL_VERSION, authlibInformation.serverName) : "CMCL " + CMCL_VERSION;
            if (s.contains(source = "${version_type}")) {
                minecraftArguments.set(i, s = s.replace(source, n));
            }
            if (s.contains(source = "${profile_name}")) {
                minecraftArguments.set(i, s = s.replace(source, n));
            }
            if (s.contains(source = "${auth_access_token}")) {
                minecraftArguments.set(i, s = s.replace(source, accessToken));
            }
            if (s.contains(source = "${auth_session}")) {
                minecraftArguments.set(i, s = s.replace(source, accessToken));
            }
            if (s.contains(source = "${game_directory}")) {
                minecraftArguments.set(i, s = s.replace(source, isModpack(versionDir, headJsonObject) ? versionDir.getAbsolutePath() : gameDir.getAbsolutePath()));
            }
            if (s.contains(source = "${assets_root}")) {
                minecraftArguments.set(i, s = s.replace(source, assetsDir.getAbsolutePath()));
            }
            if (s.contains(source = "${assets_index_name}")) {
                minecraftArguments.set(i, s = s.replace(source, assetsIndex));
            }
            if (s.contains(source = "${auth_uuid}")) {
                minecraftArguments.set(i, s = s.replace(source, isEmpty(uuid) ? Utils.getUUIDByName(playerName) : uuid));
            }
            if (s.contains(source = "${user_type}")) {
                minecraftArguments.set(i, s = s.replace(source, "mojang"));
            }
            if (s.contains(source = "${game_assets}")) {
                minecraftArguments.set(i, s = s.replace(source, assetsPath));
            }
            if (s.contains(source = "${user_properties}")) {
                minecraftArguments.set(i, s = s.replace(source, properties != null ? properties.toString() : "{}"));
            }
            if (s.contains(source = "${resolution_width}")) {
                minecraftArguments.set(i, s = s.replace(source, String.valueOf(width)));
            }
            if (s.contains(source = "${resolution_height}")) {
                minecraftArguments.set(i, s = s.replace(source, String.valueOf(height)));
            }
            if (s.contains(source = "${primary_jar}")) {
                minecraftArguments.set(i, s = s.replace(source, minecraftJarFile.getAbsolutePath()));
            }
            if (s.contains(source = "${classpath_separator}")) {
                minecraftArguments.set(i, s = s.replace(source, File.pathSeparator));
            }
            if (s.contains(source = "${primary_jar_name}")) {
                minecraftArguments.set(i, s = s.replace(source, minecraftJarFile.getName()));
            }
            if (s.contains(source = "${library_directory}")) {
                minecraftArguments.set(i, s = s.replace(source, librariesFile.getAbsolutePath()));
            }
            if (s.contains(source = "${libraries_directory}")) {
                minecraftArguments.set(i, s = s.replace(source, librariesFile.getAbsolutePath()));
            }
            if (s.contains(source = "${language}")) {
                minecraftArguments.set(i, s = s.replace(source, ConsoleMinecraftLauncher.getLocale().toString()));
            }
        }

        if (resourcePacksDir != null && resourcePacksDir.exists() && !resourcePacksDir.equals(new File(gameDir, "resourcepacks"))) {
            minecraftArguments.add("--resourcePackDir");
            minecraftArguments.add(resourcePacksDir.getAbsolutePath());
        }
        if (fullscreen) minecraftArguments.add("--fullscreen");


        File[] nativesFiles = nativesFolder.listFiles();
        if (pair.third && (!nativesFolder.exists() || nativesFiles == null || nativesFiles.length == 0)) {
            throw new EmptyNativesException(libraries);
        }

        //String javaArgument;
        if (jvmArguments.size() > 0) {
            jvmArguments.add(0, "-Xmn" + miniMemory + "m");
            jvmArguments.add(1, "-Xmx" + maxMemory + "m");
            jvmArguments.add(2, "-Dfile.encoding=UTF-8");

            for (int i = 0; i < jvmArguments.size(); i++) {
                /*boolean replace = true;
                String replaceTo = null;*/
                String source;
                String s = jvmArguments.get(i);

                /*else if (s.contains(source = "${xxxxxxxxxxxxxxxxxxxx}")) {
                    jvmArguments.set(i, s=s.replace(source, valueeeeeeeeee));
                }*/
                if (s.contains(source = "${natives_directory}")) {
                    jvmArguments.set(i, s = s.replace(source, nativesFolder.getAbsolutePath()));
                }
                if (s.contains(source = "${launcher_name}")) {
                    jvmArguments.set(i, s = s.replace(source, (authlibInformation != null && !authlibInformation.forOfflineSkin) ? String.format("CMCL(%s)", authlibInformation.serverName) : "CMCL"));
                }
                if (s.contains(source = "${launcher_version}")) {
                    jvmArguments.set(i, s = s.replace(source, CMCL_VERSION));
                }
                if (s.contains(source = "${classpath}")) {
                    jvmArguments.set(i, s = s.replace(source, librariesString.toString()));
                }
                if (s.contains(source = "${file_separator}")) {
                    jvmArguments.set(i, s = s.replace(source, OperatingSystem.FILE_SEPARATOR));
                }
                if (s.contains(source = "${classpath_separator}")) {
                    jvmArguments.set(i, s = s.replace(source, OperatingSystem.PATH_SEPARATOR));
                }
                if (s.contains(source = "${library_directory}")) {
                    jvmArguments.set(i, s = s.replace(source, librariesFile.getAbsolutePath()));
                }
                if (s.contains(source = "${libraries_directory}")) {
                    jvmArguments.set(i, s = s.replace(source, librariesFile.getAbsolutePath()));
                }
                if (s.contains(source = "${primary_jar_name}")) {
                    jvmArguments.set(i, s = s.replace(source, minecraftJarFile.getName()));
                }
                if (s.contains(source = "${version_name}")) {
                    jvmArguments.set(i, s = s.replace(source, headJsonObject.optString("id")));
                }
            }

        } else {
            jvmArguments.add("-Xmn" + miniMemory + "m");
            jvmArguments.add("-Xmx" + maxMemory + "m");
            jvmArguments.add("-Dfile.encoding=UTF-8");
            jvmArguments.add("-Djava.library.path=" + nativesFolder.getAbsolutePath());
            jvmArguments.add("-Dminecraft.launcher.brand=CMCL");
            jvmArguments.add("-Dminecraft.launcher.version=" + CMCL_VERSION);
            jvmArguments.add("-cp");
            jvmArguments.add(librariesString.toString());
        }

        int jvmArgusIndex = 3;
        if (authlibInformation != null) {
            jvmArguments.add(jvmArgusIndex++, "-Dauthlibinjector.side=client");
            jvmArguments.add(jvmArgusIndex++, "-Dauthlibinjector.noShowServerName");
            jvmArguments.add(jvmArgusIndex++, "-javaagent:" + authlibFile.getAbsolutePath() + "=" + authlibInformation.serverAddress);
            if (!Utils.isEmpty(authlibInformation.metadataEncoded))
                jvmArguments.add(jvmArgusIndex++, "-Dauthlibinjector.yggdrasil.prefetched=" + authlibInformation.metadataEncoded);
        }


        JSONObject config = Utils.getConfig();
        String proxyHost = config.optString("proxyHost");
        String proxyPort = config.optString("proxyPort");
        if (!Utils.isEmpty(proxyHost)
                && !Utils.isEmpty(proxyPort)
                && config.optString("proxyUsername").isEmpty()
                && config.optString("proxyPassword").isEmpty()) {
            jvmArguments.add(jvmArgusIndex++, "-Dhttp.proxyHost=" + proxyHost);
            jvmArguments.add(jvmArgusIndex++, "-Dhttp.proxyPort=" + proxyPort);
            jvmArguments.add(jvmArgusIndex++, "-Dhttps.proxyHost=" + proxyHost);
            jvmArguments.add(jvmArgusIndex++, "-Dhttps.proxyPort=" + proxyPort);
        }


        /*JSONObject logging = headJsonObject.optJSONObject("logging");
        if (logging != null) {
            JSONObject client = logging.optJSONObject("client");
            if (client != null) {
                String argument = client.optString("argument");
                if (!loggingFile.exists() || loggingFile.length() == 0) {
                    JSONObject file = client.optJSONObject("file");
                    if (file != null) {
                        String url = file.optString("url");
                        if (!Utils.isEmpty(url)) {
                            try {
                                System.out.print(String.format(getString("MESSAGE_DOWNLOADING_FILE"), loggingFile.getName()));
                                ConsoleMinecraftLauncher.downloadFile(url,loggingFile,new XProgressBar());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (loggingFile.exists() && loggingFile.length() > 0) {
                    jvmArguments.add(jvmArgusIndex++, argument.replace("${path}", loggingFile.getAbsolutePath()));
                }
            }
        }*/

        /*自定义JVM参数*/
        List<String> addableJvmArgs = new LinkedList<>();

        if (jvmArgs != null && jvmArgs.size() > 0) {
            for (String arg : jvmArgs) {
                int indexOf = arg.indexOf('=');
                String ab = null;
                if (indexOf >= 0) {
                    ab = arg.substring(0, indexOf) + "=";
                }

                boolean contains = false;
                int replace = -1;
                if (!isEmpty(ab)) {
                    for (int i = 0; i < jvmArguments.size(); i++) {
                        String rarg = jvmArguments.get(i);
                        if (rarg.startsWith(ab)) {
                            contains = true;
                            if (indexOf + 1 < arg.length()) {
                                replace = i;
                            }
                            break;
                        }
                    }
                } else {
                    for (String rarg : jvmArguments) {
                        if (rarg.equals(arg)) {
                            contains = true;
                            break;
                        }
                    }
                }
                if (!contains) {
                    addableJvmArgs.add(arg);
                } else {
                    if (replace >= 0) {
                        jvmArguments.set(replace, arg);
                    }
                }
            }
        }


        /*自定义游戏参数*/
        List<String> addableGameArgs = new LinkedList<>();
        if (gameArgs != null && gameArgs.size() > 0) {
            for (Map.Entry<String, String> entry : gameArgs.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                boolean contains = false;
                if (isEmpty(value)) {
                    for (String arg : minecraftArguments) {
                        if (arg.equals("--" + key) || arg.equals("-" + key)) {
                            contains = true;
                            break;
                        }
                    }
                    if (!contains) {
                        addableGameArgs.add("--" + key);
                    }
                } else {
                    for (int i = 0; i < minecraftArguments.size(); i++) {
                        String arg = minecraftArguments.get(i);
                        if (arg.equals("--" + key) || arg.equals("-" + key)) {
                            contains = true;
                            if (i + 1 >= minecraftArguments.size()) {
                                minecraftArguments.add(value);
                            } else {
                                String next = minecraftArguments.get(i + 1);
                                if (!next.startsWith("--") && !next.startsWith("-")) {
                                    minecraftArguments.set(i + 1, value);
                                } else {
                                    minecraftArguments.add(i + 1, value);
                                }
                            }
                            break;
                        }
                    }
                    if (!contains) {
                        addableGameArgs.add("--" + key);
                        addableGameArgs.add(value);
                    }
                }
            }
        }


        arguments.add(javaPath);
        arguments.addAll(addableJvmArgs);
        arguments.addAll(jvmArguments);
        arguments.add(mainClass);
        arguments.addAll(minecraftArguments);
        arguments.addAll(addableGameArgs);

        return arguments;
    }


    /**
     * Get Minecraft Launch Command, see {@link com.mrshiehx.cmcl.modules.MinecraftLauncher#getMinecraftLaunchCommandArguments}
     *
     * @author MrShiehX
     */
    public static String getMinecraftLaunchCommand(
            File versionDir,
            File minecraftJarFile,
            File minecraftVersionJsonFile,
            File gameDir,
            File assetsDir,
            File resourcePacksDir,
            String playerName,
            String javaPath,
            long maxMemory,
            int miniMemory,
            int width,
            int height,
            boolean fullscreen,
            String accessToken,
            String uuid,
            boolean isDemo,
            boolean customScreenSize,
            JSONObject properties,
            @Nullable List<String> jvmArgs,
            @Nullable Map<String, String> gameArgs,
            @Nullable AuthlibInformation authlibInformation) throws
            LibraryDefectException,
            EmptyNativesException,
            LaunchException,
            IOException,
            JSONException {
        List<String> args = getMinecraftLaunchCommandArguments(versionDir, minecraftJarFile,
                minecraftVersionJsonFile,
                gameDir,
                assetsDir,
                resourcePacksDir,
                playerName,
                javaPath,
                maxMemory,
                miniMemory,
                width,
                height,
                fullscreen,
                accessToken,
                uuid,
                isDemo,
                customScreenSize,
                properties,
                null,
                jvmArgs,
                gameArgs,
                authlibInformation);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < args.size(); i++) {
            String str = args.get(i);


            if (str.contains(" ")) {
                str = "\"" + str + "\"";
                if (str.contains("\\")) {
                    str = str.replace("\\", "\\\\");
                }
            }
            stringBuilder.append(str);
            if (i + 1 != args.size()) {
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 检测是否符合条件（jvm参数、游戏参数natives文件和依赖库文件）
     *
     * @param rules      规则
     * @param isDemo     游戏参数时用
     * @param customSize 游戏参数时用
     **/
    public static boolean isMeetConditions(JSONArray rules, boolean isDemo, boolean customSize) {
        if (rules == null || rules.length() <= 0) return false;

        for (int i = 0; i < rules.length(); i++) {
            JSONObject first = rules.optJSONObject(i);
            if (first != null) {
                if ((first.has("os") || first.has("features"))) {
                    JSONObject os = first.optJSONObject("os");
                    JSONObject features = first.optJSONObject("features");
                    if (os != null) {
                        String action = first.optString("action");

                        String name = os.optString("name");
                        String version = os.optString("version");
                        String arch = os.optString("arch");


                        boolean hasNoConditionOfRules = false;
                        for (int j = 0; j < rules.length(); j++) {
                            if (j != i) {
                                JSONObject second = rules.optJSONObject(j);
                                if (second != null) {

                                    if (!second.has("name") && !second.has("version") && !second.has("arch")) {
                                        if ("allow".equals(second.optString("action"))) {
                                            hasNoConditionOfRules = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (isEmpty(name) && isEmpty(version) && isEmpty(arch)) {
                            if ("disallow".equals(action)) {
                                return false;
                            } else {
                                if (!hasNoConditionOfRules) {
                                    return false;
                                }
                            }
                        }
                        boolean base = true;
                        boolean namae = false;
                        if (!isEmpty(name)) {
                            base = (namae = name.equals(OperatingSystem.CURRENT_OS.getCheckedName()));
                        }
                        if (base/*如果osname匹配了才能判断version是否匹配*/ && !isEmpty(version)) {
                            String sversion = /*OperatingSystem.SYSTEM_VERSION*/System.getProperty("os.version");
                            if ("^10\\.".equals(version)) {
                                base = base && "10.0".equals(sversion);
                            } else {
                                base = base && Pattern.compile(version).matcher(sversion).matches();
                            }
                        }
                        if (!isEmpty(arch)) {
                            if (!isEmpty(name)) {
                                if (namae) {
                                    base = base && arch.equals(System.getProperty("os.arch"));
                                }
                            } else {
                                base = base && arch.equals(System.getProperty("os.arch"));
                            }
                        }
                        if (base) {
                            if ("disallow".equals(action)) {
                                return false;
                            }
                        } else {
                            if ("allow".equals(action)) {
                                if (!hasNoConditionOfRules) {
                                    return false;
                                }
                            }

                        }
                    }
                    if (features != null) {
                        boolean allow2 = first.optString("action").equals("allow");
                        if (features.has("is_demo_user")) {
                            boolean is_demo_user = features.optBoolean("is_demo_user");
                            if (!(allow2 && isDemo == is_demo_user)) return false;
                        } else if (features.has("has_custom_resolution")) {
                            boolean has_custom_resolution = features.optBoolean("has_custom_resolution");
                            if (!(allow2 && has_custom_resolution == customSize)) {
                                return false;
                            }
                        } else if (!allow2) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }


    /**
     * Launch Minecraft, see {@link com.mrshiehx.cmcl.modules.MinecraftLauncher#getMinecraftLaunchCommandArguments}
     *
     * @author MrShiehX
     */
    public static Process launchMinecraft(
            File versionDir,
            File minecraftJarFile,
            File minecraftVersionJsonFile,
            File gameDir,
            File assetsDir,
            File resourcePacksDir,
            String playerName,
            String javaPath,
            long maxMemory,
            int miniMemory,
            int width,
            int height,
            boolean fullscreen,
            String accessToken,
            String uuid,
            boolean isDemo,
            boolean customScreenSize,
            JSONObject properties,
            @Nullable List<String> jvmArgs,
            @Nullable Map<String, String> gameArgs,
            @Nullable AuthlibInformation authlibInformation) throws
            LibraryDefectException,
            EmptyNativesException,
            LaunchException,
            IOException,
            JSONException {
        List<String> args = getMinecraftLaunchCommandArguments(versionDir, minecraftJarFile,
                minecraftVersionJsonFile,
                gameDir,
                assetsDir,
                resourcePacksDir,
                playerName,
                javaPath,
                maxMemory,
                miniMemory,
                width,
                height,
                fullscreen,
                accessToken,
                uuid,
                isDemo,
                customScreenSize,
                properties,
                () -> System.out.println(getString("MESSAGE_STARTING_GAME")),
                jvmArgs,
                gameArgs,
                authlibInformation);

        ProcessBuilder processBuilder = new ProcessBuilder(args);
        processBuilder.directory(versionDir);
        processBuilder.redirectErrorStream(true);
        return processBuilder.start();
    }

    private static void getJavaVirtualMachineArguments(JSONObject headJsonObject, boolean isDemo, boolean customScreenSize, List<String> args) {
        getArguments(headJsonObject, "jvm", isDemo, customScreenSize, args);
    }

    private static void getGameArguments(JSONObject headJsonObject, boolean isDemo, boolean customScreenSize, List<String> args) {
        getArguments(headJsonObject, "game", isDemo, customScreenSize, args);
    }

    private static void getArguments(JSONObject headJsonObject, String name, boolean isDemo, boolean customScreenSize, List<String> args) {
        //StringBuilder arguments = new StringBuilder();
        JSONObject argumentsArray = headJsonObject.optJSONObject("arguments");
        JSONArray array = argumentsArray.optJSONArray(name);
        if (array == null) return;
        for (int i = 0; i < array.length(); i++) {
            Object obj = array.opt(i);
            if (obj instanceof String) {
                String a = (String) obj;
                args.add(a);
            } else if (obj instanceof JSONObject) {
                JSONObject jsonObject = array.optJSONObject(i);
                if (jsonObject != null && jsonObject.has("value")) {
                    Object value = jsonObject.opt("value");
                    if (value != null) {
                        boolean meetConditions = true;
                        JSONArray rules = jsonObject.optJSONArray("rules");
                        if (rules != null) {
                            meetConditions = isMeetConditions(rules, isDemo, customScreenSize);
                        }
                        if (meetConditions) {
                            if (value instanceof JSONArray) {
                                JSONArray value2 = (JSONArray) value;
                                for (int k = 0; k < value2.length(); k++) {
                                    if (value2.opt(k) instanceof String) {
                                        args.add(Utils.valueOf(value2.opt(k)));
                                    }
                                }
                            } else {
                                args.add(Utils.valueOf(value));
                            }
                        }
                    }
                }
            }
        }
        //return arguments.substring(0, arguments.length()-1);
    }

    public static ThreeReturns<List<Library>, List<Library>, Boolean> getLibraries(JSONArray libraries, File librariesDirectory) {
        return getLibraries(libraries, librariesDirectory, false);
    }

    /**
     * 获得依赖库
     *
     * @param libraries          依赖库JSONArray
     * @param librariesDirectory 依赖库目录
     * @return 1st 为存在的依赖库集合，2nd 为不存在的依赖库集合, 3rd 为是否拥有natives
     **/
    public static ThreeReturns<List<Library>, List<Library>, Boolean> getLibraries(JSONArray libraries, File librariesDirectory, boolean replaceOptiFineToOptiFineInstaller) {
        Map<String, Library> librariesPaths = new HashMap<>();
        Map<String, Library> notFound = new HashMap<>();
        boolean needNatives = false;
        //List<String> names = new ArrayList<>();
        for (int i = 0; i < libraries.length(); i++) {
            JSONObject library = libraries.optJSONObject(i);
            boolean meet = true;
            JSONArray rules = library.optJSONArray("rules");
            if (rules != null) {
                meet = isMeetConditions(rules, false, false);
            }
            if (meet) {
                JSONObject downloads = library.optJSONObject("downloads");
                if (downloads != null) {
                    if (downloads.optJSONObject("classifiers") != null) {
                        needNatives = true;
                        if (downloads.optJSONObject("artifact") == null) {
                            continue;
                        }
                    }
                }


                String name = library.optString("name");
                SplitLibraryName nameSplit = Utils.splitLibraryName(name);
                if (nameSplit == null) continue;


                if (replaceOptiFineToOptiFineInstaller
                        && "optifine".equals(nameSplit.first)
                        && "OptiFine".equals(nameSplit.second)) {
                    nameSplit = new SplitLibraryName(nameSplit.first, nameSplit.second, nameSplit.version, "installer", nameSplit.extension);
                }


                String key = nameSplit.first + ":" + nameSplit.second + (!isEmpty(nameSplit.classifier) ? (":" + nameSplit.classifier) : "");
                Library exist = librariesPaths.get(key);

                File libraryFile = nameSplit.getPhysicalFile();
                Library lb = new Library(library, libraryFile);

                if (exist == null) {

                    if (libraryFile.exists() && libraryFile.length() > 0) {
                        librariesPaths.put(key, lb);
                    } else {
                        notFound.put(key, lb);
                        /*if (!notFound.containsKey(key) *//*&& !library.optString("name").isEmpty()*//**//*&&((library.has("downloads") && library.optJSONObject("downloads").has("artifact"))||library.has("url"))*//*) {
                            notFound.put(key, lb);
                        }*/
                    }
                } else {
                    String existName = exist.libraryJSONObject.optString("name");
                    SplitLibraryName existNameSplit = Utils.splitLibraryName(existName);
                    if (existNameSplit == null) continue;
                    if (Objects.equals(existNameSplit.first, nameSplit.first) && Objects.equals(existNameSplit.second, nameSplit.second)) {
                        int compare = Utils.tryToCompareVersion(existNameSplit.version, nameSplit.version);
                        if (compare == -1 || (compare == 0 && lb.libraryJSONObject.length() > exist.libraryJSONObject.length())) {
                            if (libraryFile.exists() && libraryFile.length() > 0) {
                                librariesPaths.put(key, lb);
                                notFound.remove(key);
                            } else {
                                notFound.put(key, lb);
                                librariesPaths.remove(key);
                            }

                        }
                    }
                }


            }
        }
        return new ThreeReturns<>(new LinkedList<>(librariesPaths.values()), new LinkedList<>((notFound.values())), needNatives);
    }

    public static boolean isModpack(File versionDir, JSONObject versionJSON) {
        if (new File(versionDir, "modpack.cfg").exists()) return true;//兼容 HMCL
        return new File(versionDir, "modpack.json").exists();
    }
}
