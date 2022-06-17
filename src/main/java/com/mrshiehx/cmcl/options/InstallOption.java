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

import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.bean.XDate;
import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.bean.arguments.ValueArgument;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.modules.extra.fabric.FabricMerger;
import com.mrshiehx.cmcl.modules.extra.forge.ForgeMerger;
import com.mrshiehx.cmcl.modules.extra.liteloader.LiteloaderMerger;
import com.mrshiehx.cmcl.modules.extra.optifine.OptiFineMerger;
import com.mrshiehx.cmcl.modules.extra.quilt.QuiltMerger;
import com.mrshiehx.cmcl.modules.version.VersionInstaller;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.*;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.versionsDir;
import static com.mrshiehx.cmcl.utils.Utils.addDoubleQuotationMark;

public class InstallOption implements Option {
    @Override
    public void execute(Arguments arguments) {
        //JSONObject jsonObject = Utils.getConfig();
        Argument first = arguments.opt(0);

        if (first instanceof ValueArgument) {
            String version = ((ValueArgument) first).value;
            String storage = version;
            Argument name = arguments.optArgument("n");
            if ((name instanceof ValueArgument)) {
                String nameString = ((ValueArgument) name).value;
                storage = Utils.isEmpty(nameString) ? version : nameString;
            }
            if (/*new File(versionsDir, storage + "/" + storage + ".jar").exists() || */new File(versionsDir, storage + "/" + storage + ".json").exists()) {
                Utils.printfln(getString("MESSAGE_INSTALL_INPUT_NAME_EXISTS"), storage);
                return;
            }
            try {
                File versionsFile = Utils.downloadVersionsFile();
                JSONArray versions = new JSONObject(Utils.readFileContent(versionsFile)).optJSONArray("versions");
                int threadCount = arguments.optInt("t");
                boolean installFabric = arguments.contains("f");
                boolean installForge = arguments.contains("o");
                boolean installQuilt = arguments.contains("q");
                boolean installLiteLoader = arguments.contains("e");
                boolean installOptiFine = arguments.contains("p");
                VersionInstaller.InstallForgeOrFabric installForgeOrFabric = null;
                if (installFabric && installForge) {
                    System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                    return;
                }

                if (installFabric && installQuilt) {
                    System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                    return;
                }

                if (installQuilt && installForge) {
                    System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                    return;
                }

                if (installFabric && installLiteLoader) {
                    System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                    return;
                }

                if (installQuilt && installLiteLoader) {
                    System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                    return;
                }

                if (installFabric && installOptiFine) {
                    System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                    return;
                }

                if (installQuilt && installOptiFine) {
                    System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                    return;
                }


                if (installFabric) {
                    installForgeOrFabric = VersionInstaller.InstallForgeOrFabric.FABRIC;
                } else if (installForge) {
                    installForgeOrFabric = VersionInstaller.InstallForgeOrFabric.FORGE;
                } else if (installQuilt) {
                    installForgeOrFabric = VersionInstaller.InstallForgeOrFabric.QUILT;
                }


                try {
                    VersionInstaller.start(version,
                            storage,
                            versions,
                            !arguments.contains("na"),
                            !arguments.contains("nn"),
                            !arguments.contains("nl"),
                            installForgeOrFabric,
                            threadCount > 0 ? threadCount : Constants.DEFAULT_DOWNLOAD_THREAD_COUNT,
                            (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                                Pair<Boolean, List<JSONObject>> a = new FabricMerger().merge(minecraftVersion, headJSONObject, minecraftJarFile, askContinue, arguments.opt("f"));
                                if (askContinue && a != null && !a.getKey()) {
                                    Utils.deleteDirectory(minecraftJarFile.getParentFile());
                                }
                                return a;
                            },
                            (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                                Pair<Boolean, List<JSONObject>> a = new ForgeMerger().merge(minecraftVersion, headJSONObject, minecraftJarFile, askContinue, arguments.opt("o"));
                                if (askContinue && a != null && !a.getKey()) {
                                    Utils.deleteDirectory(minecraftJarFile.getParentFile());
                                }
                                return a;
                            },
                            (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                                Pair<Boolean, List<JSONObject>> a = new QuiltMerger().merge(minecraftVersion, headJSONObject, minecraftJarFile, askContinue, arguments.opt("q"));
                                if (askContinue && a != null && !a.getKey()) {
                                    Utils.deleteDirectory(minecraftJarFile.getParentFile());
                                }
                                return a;
                            }, installLiteLoader ? (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                                Pair<Boolean, List<JSONObject>> a = new LiteloaderMerger().merge(minecraftVersion, headJSONObject, minecraftJarFile, askContinue, arguments.opt("e"));
                                if (askContinue && a != null && !a.getKey()) {
                                    Utils.deleteDirectory(minecraftJarFile.getParentFile());
                                }
                                return a;
                            } : null,
                            installOptiFine ? (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                                Pair<Boolean, List<JSONObject>> a = new OptiFineMerger().merge(minecraftVersion, headJSONObject, minecraftJarFile, askContinue, arguments.opt("p"));
                                if (askContinue && a != null && !a.getKey()) {
                                    Utils.deleteDirectory(minecraftJarFile.getParentFile());
                                }
                                return a;
                            } : null,
                            () -> System.out.println(getString("MESSAGE_INSTALLED_NEW_VERSION")), null, null);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                //VersionInstaller.start(version, storage, versions, !arguments.contains("na"), !arguments.contains("nn"), !arguments.contains("nl"), installForgeOrFabric, installLiteLoader, installOptiFine, threadCount > 0 ? threadCount : Constants.DEFAULT_DOWNLOAD_THREAD_COUNT);
            } catch (Exception exception) {
                //exception.printStackTrace();
                Utils.printfln(getString("MESSAGE_FAILED_TO_INSTALL_NEW_VERSION"), exception);
                return;
            }
            return;
        }
        Argument subOption = arguments.opt(1);
        if (subOption == null) {
            System.out.println(getString("CONSOLE_GET_USAGE"));
            return;
        }
        String key = subOption.key;

        switch (key.toLowerCase()) {
            case "s":
                if (!(subOption instanceof ValueArgument)) {
                    System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                    return;
                }
                String typeString = ((ValueArgument) subOption).value;
                int typeInt;
                switch (typeString.toLowerCase()) {
                    case "a":
                        typeInt = 0;
                        break;
                    case "r":
                        typeInt = 1;
                        break;
                    case "s":
                        typeInt = 2;
                        break;
                    case "oa":
                        typeInt = 3;
                        break;
                    case "ob":
                        typeInt = 4;
                        break;
                    default:
                        System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                        return;
                }
                try {
                    XDate start = null;
                    XDate end = null;
                    String time = arguments.opt("i");
                    if (!Utils.isEmpty(time)) {
                        try {
                            String[] startAndEnd = time.split("/");
                            String[] starts = startAndEnd[0].split("-");
                            String[] ends = startAndEnd[1].split("-");
                            start = new XDate(Integer.parseInt(starts[0]), Integer.parseInt(starts[1]), Integer.parseInt(starts[2]));
                            end = new XDate(Integer.parseInt(ends[0]), Integer.parseInt(ends[1]), Integer.parseInt(ends[2]));
                            if (XDate.compareDate(start, end) == 0) {
                                System.out.println(getString("CONSOLE_INSTALL_SHOW_INCORRECT_TIME"));
                                System.out.println(getString("CONSOLE_GET_USAGE"));
                                return;
                            }

                        } catch (Throwable ignore) {
                            System.out.println(getString("CONSOLE_INSTALL_SHOW_INCORRECT_TIME"));
                            System.out.println(getString("CONSOLE_GET_USAGE"));
                            return;
                        }
                    }
                    List<String> versions = new LinkedList<>();
                    File versionsFile = Utils.downloadVersionsFile();
                    JSONArray versionsJSONArray = new JSONObject(Utils.readFileContent(versionsFile)).optJSONArray("versions");

                    for (int i = 0; i < versionsJSONArray.length(); i++) {
                        JSONObject jsonObject = versionsJSONArray.optJSONObject(i);
                        if (jsonObject == null) continue;
                        String id = jsonObject.optString("id");
                        String timeHere = jsonObject.optString("releaseTime");
                        boolean timeAllow;
                        if (!Utils.isEmpty(timeHere) && start != null/*&&end!=null*/) {

                            XDate thiz = null;
                            try {
                                String[] times = timeHere.substring(0, 4 + 1 + 2 + 1 + 2).split("-");
                                if (times.length > 0) {
                                    thiz = new XDate(Integer.parseInt(times[0]), Integer.parseInt(times[1]), Integer.parseInt(times[2]));
                                }
                            } catch (Throwable ignore) {
                            }
                            if (thiz != null) {
                                /* 0 first > second
                                 * 1 first < second
                                 * 2 first = second
                                 **/
                                int compareOfStart = XDate.compareDate(start, thiz);
                                int compareOfEnd = XDate.compareDate(end, thiz);
                                if (compareOfStart == 1 || compareOfStart == 2) {
                                    timeAllow = compareOfEnd == 0 || compareOfEnd == 2;
                                } else {
                                    timeAllow = false;
                                }
                            } else {
                                timeAllow = false;
                            }
                        } else timeAllow = true;
                        if (typeInt == 0) {
                            if (timeAllow) versions.add(id);
                        } else {
                            String type = jsonObject.optString("type");
                            switch (typeInt) {
                                case 1:
                                    if ("release".equals(type))
                                        if (timeAllow) versions.add(id);
                                    break;
                                case 2:
                                    if ("snapshot".equals(type))
                                        if (timeAllow) versions.add(id);
                                    break;
                                case 3:
                                    if ("old_alpha".equals(type))
                                        if (timeAllow) versions.add(id);
                                    break;
                                case 4:
                                    if ("old_beta".equals(type))
                                        if (timeAllow) versions.add(id);
                                    break;
                            }
                        }

                    }
                    System.out.println(Arrays.toString(addDoubleQuotationMark(versions).toArray()));
                } catch (Exception exception) {
                    exception.printStackTrace();
                    Utils.printfln(getString("CONSOLE_FAILED_LIST_VERSIONS"), exception);
                    return;
                }
                break;
            default:
                System.out.println(getString("CONSOLE_UNKNOWN_OPTION", key));
                break;
        }

    }


    @Override
    public String getUsageName() {
        return "INSTALL";
    }
}
