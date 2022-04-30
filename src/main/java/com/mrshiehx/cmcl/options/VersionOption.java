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
import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.bean.arguments.ValueArgument;
import com.mrshiehx.cmcl.modules.MinecraftLauncher;
import com.mrshiehx.cmcl.modules.modLoaders.fabric.FabricInstaller;
import com.mrshiehx.cmcl.modules.modLoaders.forge.ForgeInstaller;
import com.mrshiehx.cmcl.modules.version.LibrariesDownloader;
import com.mrshiehx.cmcl.modules.version.NativesDownloader;
import com.mrshiehx.cmcl.utils.ConsoleUtils;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
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


                    if (information.size() == 0) {
                        System.out.println(getString("VERSION_INFORMATION_NOTHING"));
                    } else {
                        System.out.println(value + ":");
                        for (Map.Entry<String, String> entry : information.entrySet()) {
                            System.out.print(entry.getKey());
                            System.out.println(entry.getValue());
                        }
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
                        JSONObject head = new JSONObject(Utils.readFileContent(jsonFile));
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

                    Pair<List<String>, List<Library>> pair = MinecraftLauncher.getLibraries(libraries, librariesFile);
                    List<Library> notFound = pair.getValue();
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
                new FabricInstaller().install(jsonFile, new File(dir, value + ".jar"));

                break;
            case "o":
                if (!jsonFile.exists()) {
                    System.out.println(getString("EXCEPTION_VERSION_NOT_FOUND"));
                    return;
                }
                new ForgeInstaller().install(jsonFile, new File(dir, value + ".jar"));

                break;
            default:
                System.out.println(getString("CONSOLE_UNKNOWN_OPTION", key));
                break;
        }
    }

    public static void executeNotFound(List<Library> notFound) {
        for (Library library : notFound) {
            System.out.print("    ");
            System.out.println(library.libraryJSONObject.optString("name"));
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
