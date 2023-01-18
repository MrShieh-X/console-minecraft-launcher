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
package com.mrshiehx.cmcl.functions;

import com.mrshiehx.cmcl.bean.Library;
import com.mrshiehx.cmcl.bean.ThreeReturns;
import com.mrshiehx.cmcl.bean.arguments.*;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.functions.mod.ModFunction;
import com.mrshiehx.cmcl.modSources.modrinth.ModrinthModManager;
import com.mrshiehx.cmcl.modules.MinecraftLauncher;
import com.mrshiehx.cmcl.modules.extra.ExtraInstaller;
import com.mrshiehx.cmcl.modules.extra.fabric.FabricInstaller;
import com.mrshiehx.cmcl.modules.extra.forge.ForgeInstaller;
import com.mrshiehx.cmcl.modules.extra.liteloader.LiteloaderInstaller;
import com.mrshiehx.cmcl.modules.extra.optifine.OptiFineInstaller;
import com.mrshiehx.cmcl.modules.extra.quilt.QuiltInstaller;
import com.mrshiehx.cmcl.modules.version.PrintVersionInfo;
import com.mrshiehx.cmcl.modules.version.VersionCompleter;
import com.mrshiehx.cmcl.modules.version.downloaders.AssetsDownloader;
import com.mrshiehx.cmcl.modules.version.downloaders.LibrariesDownloader;
import com.mrshiehx.cmcl.modules.version.downloaders.NativesDownloader;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.cmcl.version.VersionUtils;
import com.mrshiehx.cmcl.utils.console.ConsoleUtils;
import com.mrshiehx.cmcl.utils.json.JSONUtils;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.mrshiehx.cmcl.CMCL.*;

public class VersionFunction implements Function {
    @Override
    public void execute(Arguments arguments) {
        if (!Function.checkArgs(arguments, 3, 2,
                ArgumentRequirement.ofSingle("info"),
                ArgumentRequirement.ofSingle("d"),
                ArgumentRequirement.ofSingle("delete"),
                ArgumentRequirement.ofValue("rename"),
                ArgumentRequirement.ofSingle("complete"),
                ArgumentRequirement.ofValue("complete"),
                ArgumentRequirement.ofValue("t"),
                ArgumentRequirement.ofValue("thread"),
                ArgumentRequirement.ofValue("config"),
                ArgumentRequirement.ofSingle("api"),
                ArgumentRequirement.ofSingle("fabric"),
                ArgumentRequirement.ofSingle("forge"),
                ArgumentRequirement.ofSingle("liteloader"),
                ArgumentRequirement.ofSingle("optifine"),
                ArgumentRequirement.ofSingle("quilt"),
                ArgumentRequirement.ofValue("fabric"),
                ArgumentRequirement.ofValue("forge"),
                ArgumentRequirement.ofValue("liteloader"),
                ArgumentRequirement.ofValue("optifine"),
                ArgumentRequirement.ofValue("quilt"))) return;
        Argument versionArg = arguments.optArgument(1);
        if (versionArg.originArray.length > 1) {
            System.out.println(getString("CONSOLE_VERSION_UNCLEAR_MEANING", versionArg.originString));
            return;
        }
        String versionName = versionArg.originString;
        File versionDir = new File(versionsDir, versionName);
        File jsonFile = new File(versionDir, versionName + ".json");
        File jarFile = new File(versionDir, versionName + ".jar");
        if (!jsonFile.exists()) {
            System.out.println(getString("EXCEPTION_VERSION_NOT_FOUND", versionName));
            return;
        }
        Argument secondArg = arguments.optArgument(2);
        if (secondArg instanceof SingleArgument) {
            String key = secondArg.key;
            switch (key) {
                case "info":
                    PrintVersionInfo.execute(jsonFile, jarFile, versionDir, versionName);
                    break;
                case "d":
                case "delete":
                    FileUtils.deleteDirectory(versionDir);
                    break;
                case "complete":
                    VersionCompleter.execute(jsonFile, jarFile, versionName);
                    break;
                case "fabric":
                    installFabric(jsonFile, jarFile, null, arguments.contains("api"));
                    break;
                case "forge":
                    installExtra(jsonFile, jarFile, new ForgeInstaller(), null);
                    break;
                case "liteloader":
                    installExtra(jsonFile, jarFile, new LiteloaderInstaller(), null);
                    break;
                case "optifine":
                    installExtra(jsonFile, jarFile, new OptiFineInstaller(), null);
                    break;
                case "quilt":
                    installExtra(jsonFile, jarFile, new QuiltInstaller(), null);
                    break;
                default:
                    System.out.println(getString("CONSOLE_UNKNOWN_COMMAND_OR_MEANING", secondArg.originString));
                    break;
            }
        } else if (secondArg instanceof ValueArgument) {
            String key = secondArg.key;
            String secondValue = ((ValueArgument) secondArg).value;
            switch (key) {
                case "rename":
                    if (VersionUtils.versionExists(secondValue)) {
                        System.out.println(getString("MESSAGE_INSTALL_INPUT_NAME_EXISTS", secondValue));
                        return;
                    }
                    try {
                        JSONObject head = new com.mrshiehx.cmcl.utils.json.XJSONObject(FileUtils.readFileContent(jsonFile));
                        head.put("id", secondValue);
                        FileUtils.writeFile(jsonFile, head.toString(2), false);
                    } catch (Exception e) {
                        System.out.println(getString("MESSAGE_FAILED_RENAME_VERSION", e));
                        return;
                    }
                    File newFile = new File(versionsDir, secondValue);
                    File file2 = new File(newFile, versionName + ".jar");
                    File file3 = new File(newFile, versionName + ".json");
                    versionDir.renameTo(newFile);
                    file2.renameTo(new File(newFile, secondValue + ".jar"));
                    file3.renameTo(new File(newFile, secondValue + ".json"));
                    break;
                case "complete":
                    if ("assets".equalsIgnoreCase(secondValue)) {
                        try {
                            AssetsDownloader.start(
                                    new JSONObject(FileUtils.readFileContent(jsonFile)),
                                    arguments.optInt("t", arguments.optInt("thread", Constants.DEFAULT_DOWNLOAD_THREAD_COUNT)),
                                    null);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    } else if ("libraries".equalsIgnoreCase(secondValue)) {
                        try {
                            JSONArray libraries = new JSONObject(FileUtils.readFileContent(jsonFile)).optJSONArray("libraries");
                            ThreeReturns<List<Library>, List<Library>, Boolean> pair = MinecraftLauncher.getLibraries(libraries);
                            List<Library> notFound = pair.second;
                            if (notFound.size() == 0) {
                                System.out.println(getString("VERSION_COMPLETE_LIBRARIES_NO_NEED_TO"));
                                return;
                            }
                            executeNotFound(notFound);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if ("natives".equalsIgnoreCase(secondValue)) {
                        try {
                            JSONArray libraries = new JSONObject(FileUtils.readFileContent(jsonFile)).optJSONArray("libraries");
                            System.out.println(getString("MESSAGE_INSTALL_DOWNLOADING_LIBRARIES"));
                            NativesDownloader.download(versionDir, JSONUtils.jsonArrayToJSONObjectList(libraries));
                            System.out.println(getString("MESSAGE_REDOWNLOADED_NATIVES"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println(getString("VERSION_UNKNOWN_COMPLETING", secondValue));
                    }
                    break;
                case "config": {
                    String name = secondValue;
                    Argument third = arguments.optArgument(3);

                    File cfgFile = new File(versionDir, "cmclversion.json");
                    JSONObject versionCfg;
                    if (cfgFile.exists()) {
                        try {
                            versionCfg = new JSONObject(FileUtils.readFileContent(cfgFile));
                        } catch (Throwable ignored) {
                            versionCfg = new JSONObject();
                        }
                    } else {
                        versionCfg = new JSONObject();
                    }

                    if (third != null)
                        versionCfg.put(name, third.originArray[0]);
                    else
                        versionCfg.remove(name);
                    try {
                        FileUtils.writeFile(cfgFile, versionCfg.toString(), false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
                case "fabric":
                    installFabric(jsonFile, jarFile, secondValue, arguments.contains("api"));
                    break;
                case "forge":
                    installExtra(jsonFile, jarFile, new ForgeInstaller(), secondValue);
                    break;
                case "liteloader":
                    installExtra(jsonFile, jarFile, new LiteloaderInstaller(), secondValue);
                    break;
                case "optifine":
                    installExtra(jsonFile, jarFile, new OptiFineInstaller(), secondValue);
                    break;
                case "quilt":
                    installExtra(jsonFile, jarFile, new QuiltInstaller(), secondValue);
                    break;
                default:
                    System.out.println(getString("CONSOLE_UNKNOWN_COMMAND_OR_MEANING", secondArg.originString));
                    break;
            }

        } else {
            System.out.println(getString("CONSOLE_ONLY_HELP"));
        }
    }

    private static boolean installFabric(File jsonFile, File jarFile, @Nullable String fabricVersion, boolean installApi) {
        boolean success = new FabricInstaller().install(jsonFile, jarFile, fabricVersion);
        if (success && installApi) {
            String ver = null;
            try {
                ver = VersionUtils.getGameVersion(new JSONObject(FileUtils.readFileContent(jsonFile)), jarFile).id.replace(" Pre-Release ", "-pre");
            } catch (Exception e) {
                e.printStackTrace();
            }
            String url = new ModrinthModManager().getDownloadLink("P7dR8mSH", "Fabric API", ver);
            if (!isEmpty(url)) {
                ModFunction.downloadMod(url);
            }
        }
        return success;
    }

    private static boolean installExtra(File jsonFile, File jarFile, ExtraInstaller extraInstaller, @Nullable String extraVersion) {
        return extraInstaller.install(jsonFile, jarFile, extraVersion);
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
        return "version";
    }
}
