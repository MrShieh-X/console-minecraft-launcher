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
package com.mrshiehx.cmcl.options;

import com.mrshiehx.cmcl.ConsoleMinecraftLauncher;
import com.mrshiehx.cmcl.bean.GameVersion;
import com.mrshiehx.cmcl.bean.Library;
import com.mrshiehx.cmcl.bean.ThreeReturns;
import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.bean.arguments.ValueArgument;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.modules.MinecraftLauncher;
import com.mrshiehx.cmcl.modules.extra.fabric.FabricInstaller;
import com.mrshiehx.cmcl.modules.extra.forge.ForgeInstaller;
import com.mrshiehx.cmcl.modules.extra.liteloader.LiteloaderInstaller;
import com.mrshiehx.cmcl.modules.extra.optifine.OptiFineInstaller;
import com.mrshiehx.cmcl.modules.extra.quilt.QuiltInstaller;
import com.mrshiehx.cmcl.modules.version.LibrariesDownloader;
import com.mrshiehx.cmcl.modules.version.NativesDownloader;
import com.mrshiehx.cmcl.modules.version.VersionInstaller;
import com.mrshiehx.cmcl.utils.ConsoleUtils;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.*;

public class VersionOption implements Option {
    @Override
    public void execute(Arguments arguments) {
        Argument subOption = arguments.opt(1);
        if (subOption == null) {
            System.out.println(getString("CONSOLE_GET_USAGE"));
            return;
        }

        if (!(subOption instanceof ValueArgument)) {
            System.out.println(getString("CONSOLE_GET_USAGE"));
            return;

        }

        String value = ((ValueArgument) subOption).value;


        String key = subOption.key;
        //JSONObject jsonObject = Utils.getConfig();
        File dir = new File(versionsDir, value);
        File jsonFile = new File(dir, value + ".json");
        switch (key.toLowerCase()) {
            case "d":
                Utils.deleteDirectory(dir);
                break;
            case "i":
                if (!jsonFile.exists()) {
                    System.out.println(getString("EXCEPTION_VERSION_NOT_FOUND"));
                    return;
                }
                try {
                    JSONObject head = new JSONObject(Utils.readFileContent(jsonFile));
                    Map<String, String> information = new LinkedHashMap<>();


                    GameVersion gameVersion = Utils.getGameVersion(head, new File(dir, value + ".jar"));
                    if (!Utils.isEmpty(gameVersion.id) && Utils.isEmpty(gameVersion.name)) {
                        information.put(getString("VERSION_INFORMATION_GAME_VERSION"), gameVersion.id);
                    } else if (Utils.isEmpty(gameVersion.id) && !Utils.isEmpty(gameVersion.name)) {
                        information.put(getString("VERSION_INFORMATION_GAME_VERSION"), gameVersion.name);
                    } else if (!Utils.isEmpty(gameVersion.id) && !Utils.isEmpty(gameVersion.name)) {
                        if (gameVersion.name.equals(gameVersion.id)) {
                            information.put(getString("VERSION_INFORMATION_GAME_VERSION"), gameVersion.name);
                        } else {
                            information.put(getString("VERSION_INFORMATION_GAME_VERSION"), gameVersion.name + " (" + gameVersion.id + ")");
                        }
                    } else {
                        information.put(getString("VERSION_INFORMATION_GAME_VERSION"), getString("VERSION_INFORMATION_GAME_VERSION_FAILED_GET"));
                    }

                    information.put(getString("VERSION_INFORMATION_VERSION_PATH"), dir.getAbsolutePath());


                    String type = head.optString("type");
                    if (!isEmpty(type)) {
                        switch (type) {
                            case "release":
                                information.put(getString("VERSION_INFORMATION_VERSION_TYPE"), getString("VERSION_INFORMATION_VERSION_TYPE_RELEASE"));
                                break;
                            case "snapshot":
                                information.put(getString("VERSION_INFORMATION_VERSION_TYPE"), getString("VERSION_INFORMATION_VERSION_TYPE_SNAPSHOT"));
                                break;
                            case "old_beta":
                                information.put(getString("VERSION_INFORMATION_VERSION_TYPE"), getString("VERSION_INFORMATION_VERSION_TYPE_OLD_BETA"));
                                break;
                            case "old_alpha":
                                information.put(getString("VERSION_INFORMATION_VERSION_TYPE"), getString("VERSION_INFORMATION_VERSION_TYPE_OLD_ALPHA"));
                                break;
                        }
                    }


                    String assets = head.optString("assets");
                    if (!isEmpty(assets)) information.put(getString("VERSION_INFORMATION_ASSETS_VERSION"), assets);


                    String releaseTime = head.optString("releaseTime");
                    if (!isEmpty(releaseTime)) {
                        DateFormat iso8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                        Date parse = null;
                        try {
                            parse = iso8601.parse(releaseTime);
                        } catch (Exception e) {
                            try {
                                releaseTime = releaseTime.substring(0, 22) + releaseTime.substring(23);
                                parse = iso8601.parse(releaseTime);
                            } catch (Exception e1) {
                                information.put(getString("VERSION_INFORMATION_RELEASE_TIME"), getString("EXCEPTION_UNABLE_PARSE"));
                            }
                        }
                        if (parse != null) {
                            SimpleDateFormat format = new SimpleDateFormat(getString("TIME_FORMAT"), ConsoleMinecraftLauncher.getLocale());
                            information.put(getString("VERSION_INFORMATION_RELEASE_TIME"), format.format(parse) + " (" + Utils.getTimezoneName() + ")");

                        }
                    }

                    JSONObject javaVersion = head.optJSONObject("javaVersion");
                    if (javaVersion != null) {
                        String component = javaVersion.optString("component");
                        if (!isEmpty(component)) {
                            information.put(getString("VERSION_INFORMATION_JAVA_COMPONENT"), component);
                        }
                        String majorVersion = javaVersion.optString("majorVersion");
                        if (!isEmpty(majorVersion)) {
                            information.put(getString("VERSION_INFORMATION_JAVA_VERSION"), majorVersion);
                        }
                    }
                    /*JSONObject fabric = head.optJSONObject("fabric");
                    if (fabric != null) {
                        String version = fabric.optString("version");
                        if (!isEmpty(version)) {
                            information.put(getString("VERSION_INFORMATION_FABRIC_VERSION"), version);
                        }
                    }*/


                    String fabricVersion = Utils.getFabricVersion(head);
                    if (!isEmpty(fabricVersion))
                        information.put(getString("VERSION_INFORMATION_FABRIC_VERSION"), fabricVersion);

                    String forgeVersion = Utils.getForgeVersion(head);
                    if (!isEmpty(forgeVersion))
                        information.put(getString("VERSION_INFORMATION_FORGE_VERSION"), forgeVersion);

                    String liteloaderVersion = Utils.getLiteloaderVersion(head);
                    if (!isEmpty(liteloaderVersion))
                        information.put(getString("VERSION_INFORMATION_LITELOADER_VERSION"), liteloaderVersion);

                    String optiFineVersion = Utils.getOptifineVersion(head);
                    if (!isEmpty(optiFineVersion))
                        information.put(getString("VERSION_INFORMATION_OPTIFINE_VERSION"), optiFineVersion);

                    String quiltVersion = Utils.getQuiltVersion(head);
                    if (!isEmpty(quiltVersion))
                        information.put(getString("VERSION_INFORMATION_QUILT_VERSION"), quiltVersion);


                    System.out.println(value + ":");//legal
                    for (Map.Entry<String, String> entry : information.entrySet()) {
                        System.out.print(entry.getKey());//legal
                        System.out.println(entry.getValue());//legal
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(getString("UNABLE_GET_VERSION_INFORMATION"));
                }
                break;
            case "r":
                Argument argument = arguments.optArgument("t");
                if (argument instanceof ValueArgument) {
                    if (!Utils.versionExists(value)) {
                        System.out.println(getString("EXCEPTION_VERSION_NOT_FOUND"));
                        return;
                    }
                    String to = ((ValueArgument) argument).value;
                    if (Utils.versionExists(to)) {
                        System.out.println(getString("MESSAGE_INSTALL_INPUT_NAME_EXISTS", to));
                        return;
                    }
                    try {
                        JSONObject head = new com.mrshiehx.cmcl.utils.json.XJSONObject(Utils.readFileContent(jsonFile));
                        head.put("id", to);
                        Utils.writeFile(jsonFile, head.toString(2), false);
                    } catch (Exception e) {
                        System.out.println(getString("MESSAGE_FAILED_RENAME_VERSION", e));
                        return;
                    }
                    File newFile = new File(versionsDir, to);
                    File file2 = new File(newFile, value + ".jar");
                    File file3 = new File(newFile, value + ".json");
                    dir.renameTo(newFile);
                    file2.renameTo(new File(newFile, to + ".jar"));
                    file3.renameTo(new File(newFile, to + ".json"));

                } else {
                    System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                }
                break;
            case "n":
                try {
                    if (!jsonFile.exists()) {
                        System.out.println(getString("EXCEPTION_VERSION_NOT_FOUND"));
                        return;
                    }
                    JSONArray libraries = new JSONObject(Utils.readFileContent(jsonFile)).optJSONArray("libraries");

                    System.out.println(getString("MESSAGE_INSTALL_DOWNLOADING_LIBRARIES"));
                    NativesDownloader.download(dir, Utils.jsonArrayToJSONObjectList(libraries));
                    System.out.println(getString("MESSAGE_REDOWNLOADED_NATIVES"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "l":
                try {
                    if (!jsonFile.exists()) {
                        System.out.println(getString("EXCEPTION_VERSION_NOT_FOUND"));
                        return;
                    }
                    JSONArray libraries = new JSONObject(Utils.readFileContent(jsonFile)).optJSONArray("libraries");
                    File librariesFile = new File(gameDir, "libraries");

                    ThreeReturns<List<Library>, List<Library>, Boolean> pair = MinecraftLauncher.getLibraries(libraries, librariesFile);
                    List<Library> notFound = pair.second;
                    if (notFound.size() == 0) {
                        System.out.println(getString("CONSOLE_EMPTY_LIST"));
                        return;
                    }
                    executeNotFound(notFound);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "f":
                if (!jsonFile.exists()) {
                    System.out.println(getString("EXCEPTION_VERSION_NOT_FOUND"));
                    return;
                }
                new FabricInstaller().install(jsonFile, new File(dir, value + ".jar"), arguments.opt("v"));

                break;
            case "o":
                if (!jsonFile.exists()) {
                    System.out.println(getString("EXCEPTION_VERSION_NOT_FOUND"));
                    return;
                }
                new ForgeInstaller().install(jsonFile, new File(dir, value + ".jar"), arguments.opt("v"));

                break;
            case "e":
                if (!jsonFile.exists()) {
                    System.out.println(getString("EXCEPTION_VERSION_NOT_FOUND"));
                    return;
                }
                new LiteloaderInstaller().install(jsonFile, new File(dir, value + ".jar"), arguments.opt("v"));

                break;
            case "p":
                if (!jsonFile.exists()) {
                    System.out.println(getString("EXCEPTION_VERSION_NOT_FOUND"));
                    return;
                }
                new OptiFineInstaller().install(jsonFile, new File(dir, value + ".jar"), arguments.opt("v"));

                break;
            case "q":
                if (!jsonFile.exists()) {
                    System.out.println(getString("EXCEPTION_VERSION_NOT_FOUND"));
                    return;
                }
                new QuiltInstaller().install(jsonFile, new File(dir, value + ".jar"), arguments.opt("v"));

                break;
            case "b":
                if (!jsonFile.exists()) {
                    System.out.println(getString("EXCEPTION_VERSION_NOT_FOUND"));
                    return;
                }
                JSONObject headJSONObjectIC;
                String headJSONObjectICContent;
                try {
                    headJSONObjectICContent = Utils.readFileContent(jsonFile);
                    headJSONObjectIC = new JSONObject(headJSONObjectICContent);
                } catch (IOException e) {
                    System.out.println(getString("EXCEPTION_READ_FILE_WITH_PATH", jsonFile.getAbsolutePath()));
                    return;
                }
                String inheritsFrom = headJSONObjectIC.optString("inheritsFrom");
                if (isEmpty(inheritsFrom)) {
                    System.out.println(getString("MESSAGE_COMPLETE_VERSION_IS_COMPLETE"));
                    return;
                }
                byte[] jarFileContent = null;

                File jarFile = new File(dir, value + ".jar");
                if (jarFile.exists()) {
                    try {
                        jarFileContent = Utils.getBytes(jarFile);
                    } catch (Exception ignore) {
                    }
                }
                try {
                    VersionInstaller.start(
                            inheritsFrom,
                            value,
                            new JSONObject(Utils.readFileContent(Utils.downloadVersionsFile())).optJSONArray("versions"),
                            true,
                            true,
                            true,
                            null,
                            Constants.DEFAULT_DOWNLOAD_THREAD_COUNT,
                            null,
                            null,
                            null,
                            null,
                            null,
                            () -> System.out.println(getString("MESSAGE_COMPLETED_VERSION")),
                            headJSONObject -> {
                                String mainClass = headJSONObjectIC.optString("mainClass");
                                if (!isEmpty(mainClass)) {
                                    headJSONObject.put("mainClass", mainClass);
                                }


                                String minecraftArguments = headJSONObjectIC.optString("minecraftArguments");
                                JSONObject arguments3 = headJSONObjectIC.optJSONObject("arguments");
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

                                if (arguments3 != null) {
                                    JSONObject argumentsMC = headJSONObject.optJSONObject("arguments");
                                    if (argumentsMC != null) {
                                        JSONArray gameMC = argumentsMC.optJSONArray("game");
                                        JSONArray jvmMC = argumentsMC.optJSONArray("jvm");
                                        JSONArray game = arguments3.optJSONArray("game");
                                        if (game != null && game.length() > 0) {
                                            if (gameMC == null) argumentsMC.put("game", gameMC = new JSONArray());
                                            gameMC.putAll(game);
                                        }
                                        JSONArray jvm = arguments3.optJSONArray("jvm");
                                        if (jvm != null && jvm.length() > 0) {
                                            if (jvmMC == null) argumentsMC.put("jvm", jvmMC = new JSONArray());
                                            jvmMC.putAll(jvm);
                                        }
                                    } else {
                                        headJSONObject.put("arguments", arguments3);
                                    }
                                }


                                JSONArray libraries = headJSONObject.optJSONArray("libraries");
                                if (libraries == null) headJSONObject.put("libraries", libraries = new JSONArray());
                                libraries.putAll(headJSONObjectIC.optJSONArray("libraries"));

                                //fabric-loader-0.14.7-1.18.2
                                //1.12.2-LiteLoader1.12.2
                                //1.18.2-forge-40.1.51
                                //quilt-loader-0.17.1-beta.2-1.19 or quilt-loader-0.17.0-1.19
                                /*String[] split = headJSONObjectIC.optString("id").split("-");
                                if (split.length == 4 && "fabric".equals(split[0]) && "loader".equals(split[1])) {
                                    headJSONObject.put("fabric", new JSONObject().put("version", split[2]));
                                } if ((split.length == 4||split.length == 5) && "quilt".equals(split[0]) && "loader".equals(split[1])) {
                                    headJSONObject.put("quilt", new JSONObject().put("version", split.length==5?(split[2]+"-"+split[3]):split[2]));
                                } else if (split.length == 3 && "forge".equals(split[1])) {
                                    headJSONObject.put("forge", new JSONObject().put("version", split[2]));
                                } else if (split.length == 2 && split[1].startsWith("LiteLoader")) {
                                    headJSONObject.put("liteloader", new JSONObject().put("version", split[1].substring(10)));
                                }*/
                                String fabricVersion = Utils.getFabricVersion(headJSONObject);
                                if (!isEmpty(fabricVersion)) {
                                    headJSONObject.put("fabric", new JSONObject().put("version", fabricVersion));
                                }
                                String forgeVersion = Utils.getForgeVersion(headJSONObject);
                                if (!isEmpty(forgeVersion)) {
                                    headJSONObject.put("forge", new JSONObject().put("version", forgeVersion));
                                }
                                String liteloaderVersion = Utils.getLiteloaderVersion(headJSONObject);
                                if (!isEmpty(liteloaderVersion)) {
                                    headJSONObject.put("liteloader", new JSONObject().put("version", liteloaderVersion));
                                }
                                String optifineVersion = Utils.getOptifineVersion(headJSONObject);
                                if (!isEmpty(optifineVersion)) {
                                    headJSONObject.put("optifine", new JSONObject().put("version", optifineVersion));
                                }
                                String quiltVersion = Utils.getQuiltVersion(headJSONObject);
                                if (!isEmpty(quiltVersion)) {
                                    headJSONObject.put("quilt", new JSONObject().put("version", quiltVersion));
                                }

                            }, null);
                } catch (Exception e) {
                    System.out.println(e.getMessage());//legal
                    try {
                        Utils.writeFile(jsonFile, headJSONObjectICContent, false);
                        if (jarFileContent == null) {
                            jarFile.delete();
                        } else {
                            FileUtils.bytes2File(jarFile, jarFileContent, false);
                        }
                    } catch (Exception ignore) {
                    }
                }
                break;
            default:
                System.out.println(getString("CONSOLE_UNKNOWN_OPTION", key));
                break;
        }
    }

    public static void executeNotFound(List<Library> notFound) {
        for (Library library : notFound) {
            System.out.print("    ");
            System.out.println(library.libraryJSONObject.optString("name"));//legal
        }
        if (ConsoleUtils.yesOrNo(getString("CONSOLE_LACK_LIBRARIES_WHETHER_DOWNLOAD"))) {
            LibrariesDownloader.downloadLibraries(notFound);
        }
    }

    @Override
    public String getUsageName() {
        return "VERSION";
    }
}
