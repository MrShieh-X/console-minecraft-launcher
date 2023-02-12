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

import com.mrshiehx.cmcl.bean.Pair;
import com.mrshiehx.cmcl.bean.XDate;
import com.mrshiehx.cmcl.bean.arguments.*;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.functions.mod.ModFunction;
import com.mrshiehx.cmcl.modSources.modrinth.ModrinthModManager;
import com.mrshiehx.cmcl.modules.extra.fabric.FabricMerger;
import com.mrshiehx.cmcl.modules.extra.forge.ForgeMerger;
import com.mrshiehx.cmcl.modules.extra.liteloader.LiteloaderMerger;
import com.mrshiehx.cmcl.modules.extra.optifine.OptiFineMerger;
import com.mrshiehx.cmcl.modules.extra.quilt.QuiltMerger;
import com.mrshiehx.cmcl.modules.version.VersionInstaller;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.mrshiehx.cmcl.CMCL.*;
import static com.mrshiehx.cmcl.utils.Utils.addDoubleQuotationMark;

public class InstallFunction implements Function {
    @Override
    public void execute(Arguments arguments) {
        Argument firstArg = arguments.optArgument(1);
        if (firstArg instanceof TextArgument) {
            if (!Function.checkArgs(arguments, 2, 1,
                    ArgumentRequirement.ofSingle("s"),
                    ArgumentRequirement.ofSingle("select"),
                    ArgumentRequirement.ofSingle("api"),
                    ArgumentRequirement.ofSingle("no-assets"),
                    ArgumentRequirement.ofSingle("no-libraries"),
                    ArgumentRequirement.ofSingle("no-natives"),
                    ArgumentRequirement.ofSingle("fabric"),
                    ArgumentRequirement.ofSingle("forge"),
                    ArgumentRequirement.ofSingle("liteloader"),
                    ArgumentRequirement.ofSingle("optifine"),
                    ArgumentRequirement.ofSingle("quilt"),
                    ArgumentRequirement.ofValue("n"),
                    ArgumentRequirement.ofValue("name"),
                    ArgumentRequirement.ofValue("t"),
                    ArgumentRequirement.ofValue("thread"),
                    ArgumentRequirement.ofValue("fabric"),
                    ArgumentRequirement.ofValue("forge"),
                    ArgumentRequirement.ofValue("liteloader"),
                    ArgumentRequirement.ofValue("optifine"),
                    ArgumentRequirement.ofValue("quilt"))) return;
            String version = firstArg.originString;
            String storage = arguments.opt("n", arguments.opt("name", version));
            if (new File(versionsDir, storage + "/" + storage + ".json").exists()) {
                Utils.printfln(getString("MESSAGE_INSTALL_INPUT_NAME_EXISTS"), storage);
                return;
            }
            try {
                File versionsFile = Utils.downloadVersionsFile();
                JSONArray versions = new JSONObject(FileUtils.readFileContent(versionsFile)).optJSONArray("versions");
                int threadCount = arguments.optInt("t", arguments.optInt("thread", Constants.DEFAULT_DOWNLOAD_THREAD_COUNT));
                boolean installFabric = arguments.contains("fabric");
                boolean installForge = arguments.contains("forge");
                boolean installQuilt = arguments.contains("quilt");
                boolean installLiteLoader = arguments.contains("liteloader");
                boolean installOptiFine = arguments.contains("optifine");

                if (!installFabric && arguments.contains("api")) {
                    System.out.println(getString("INSTALL_FABRIC_API_WITHOUT_FABRIC"));
                    return;
                }

                VersionInstaller.InstallForgeOrFabricOrQuilt installForgeOrFabricOrQuilt = null;
                if (installFabric && installForge) {
                    System.out.println(getString("INSTALL_COEXIST", "Fabric", "Forge"));
                    return;
                }

                if (installFabric && installLiteLoader) {
                    System.out.println(getString("INSTALL_COEXIST", "Fabric", "LiteLoader"));
                    return;
                }
                if (installFabric && installOptiFine) {
                    System.out.println(getString("INSTALL_COEXIST", "Fabric", "OptiFine"));
                    return;
                }

                if (installFabric && installQuilt) {
                    System.out.println(getString("INSTALL_COEXIST", "Fabric", "Quilt"));
                    return;
                }
                if (installQuilt && installForge) {
                    System.out.println(getString("INSTALL_COEXIST", "Quilt", "Forge"));
                    return;
                }

                if (installQuilt && installLiteLoader) {
                    System.out.println(getString("INSTALL_COEXIST", "Quilt", "LiteLoader"));
                    return;
                }


                if (installQuilt && installOptiFine) {
                    System.out.println(getString("INSTALL_COEXIST", "Quilt", "OptiFine"));
                    return;
                }


                if (installFabric) {
                    installForgeOrFabricOrQuilt = VersionInstaller.InstallForgeOrFabricOrQuilt.FABRIC;
                } else if (installForge) {
                    installForgeOrFabricOrQuilt = VersionInstaller.InstallForgeOrFabricOrQuilt.FORGE;
                } else if (installQuilt) {
                    installForgeOrFabricOrQuilt = VersionInstaller.InstallForgeOrFabricOrQuilt.QUILT;
                }


                VersionInstaller.start(version,
                        storage,
                        versions,
                        !arguments.contains("no-assets"),
                        !arguments.contains("no-natives"),
                        !arguments.contains("no-libraries"),
                        installForgeOrFabricOrQuilt,
                        threadCount > 0 ? threadCount : Constants.DEFAULT_DOWNLOAD_THREAD_COUNT,
                        (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                            Pair<Boolean, List<JSONObject>> a = new FabricMerger().merge(minecraftVersion, headJSONObject, minecraftJarFile, askContinue, arguments.opt("fabric"));
                            if (askContinue && a != null && !a.getKey()) {
                                FileUtils.deleteDirectory(minecraftJarFile.getParentFile());
                            }
                            return a;
                        },
                        (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                            Pair<Boolean, List<JSONObject>> a = new ForgeMerger().merge(minecraftVersion, headJSONObject, minecraftJarFile, askContinue, arguments.opt("forge"));
                            if (askContinue && a != null && !a.getKey()) {
                                FileUtils.deleteDirectory(minecraftJarFile.getParentFile());
                            }
                            return a;
                        },
                        (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                            Pair<Boolean, List<JSONObject>> a = new QuiltMerger().merge(minecraftVersion, headJSONObject, minecraftJarFile, askContinue, arguments.opt("quilt"));
                            if (askContinue && a != null && !a.getKey()) {
                                FileUtils.deleteDirectory(minecraftJarFile.getParentFile());
                            }
                            return a;
                        }, installLiteLoader ? (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                            Pair<Boolean, List<JSONObject>> a = new LiteloaderMerger().merge(minecraftVersion, headJSONObject, minecraftJarFile, askContinue, arguments.opt("liteloader"));
                            if (askContinue && a != null && !a.getKey()) {
                                FileUtils.deleteDirectory(minecraftJarFile.getParentFile());
                            }
                            return a;
                        } : null,
                        installOptiFine ? (minecraftVersion, headJSONObject, minecraftJarFile, askContinue) -> {
                            Pair<Boolean, List<JSONObject>> a = new OptiFineMerger().merge(minecraftVersion, headJSONObject, minecraftJarFile, askContinue, arguments.opt("optifine"));
                            if (askContinue && a != null && !a.getKey()) {
                                FileUtils.deleteDirectory(minecraftJarFile.getParentFile());
                            }
                            return a;
                        } : null,
                        () -> {
                            System.out.println(getString("MESSAGE_INSTALLED_NEW_VERSION"));
                            if (arguments.contains("s") || arguments.contains("select")) {
                                Utils.saveConfig(Utils.getConfig().put("selectedVersion", storage));
                            }
                            if (installFabric && arguments.contains("api")) {
                                String url = new ModrinthModManager().getDownloadLink("P7dR8mSH", "Fabric API", version.replace(" Pre-Release ", "-pre"), null, ModFunction.MOD_MR_DEPENDENCY_INSTALLER);
                                if (!isEmpty(url)) {
                                    ModFunction.downloadMod(url);
                                }
                            }
                        }, null, null);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        } else if (firstArg instanceof ValueArgument) {
            if (!Function.checkArgs(arguments, 2, 1,
                    ArgumentRequirement.ofValue("show"),
                    ArgumentRequirement.ofValue("t"),
                    ArgumentRequirement.ofValue("time"))) return;

            if (!firstArg.key.equals("show")) {
                System.out.println(getString("CONSOLE_UNKNOWN_USAGE", firstArg.key));
                return;
            }
            String typeString = ((ValueArgument) firstArg).value;
            int typeInt;
            switch (typeString.toLowerCase()) {
                case "a":
                case "all":
                    typeInt = 0;
                    break;
                case "r":
                case "release":
                    typeInt = 1;
                    break;
                case "s":
                case "snapshot":
                    typeInt = 2;
                    break;
                case "oa":
                case "oldalpha":
                    typeInt = 3;
                    break;
                case "ob":
                case "oldbeta":
                    typeInt = 4;
                    break;
                default:
                    System.out.println(getString("INSTALL_SHOW_UNKNOWN_TYPE", typeString));
                    return;
            }
            try {
                XDate start = null;
                XDate end = null;
                String time = arguments.opt("t", arguments.opt("time"));
                if (!Utils.isEmpty(time)) {
                    try {
                        String[] startAndEnd = time.split("/");
                        String[] starts = startAndEnd[0].split("-");
                        String[] ends = startAndEnd[1].split("-");
                        start = new XDate(Integer.parseInt(starts[0]), Integer.parseInt(starts[1]), Integer.parseInt(starts[2]));
                        end = new XDate(Integer.parseInt(ends[0]), Integer.parseInt(ends[1]), Integer.parseInt(ends[2]));
                        if (XDate.compareDate(start, end) == 0) {
                            System.out.println(getString("CONSOLE_INSTALL_SHOW_INCORRECT_TIME", time));
                            return;
                        }

                    } catch (Throwable ignore) {
                        System.out.println(getString("CONSOLE_INSTALL_SHOW_INCORRECT_TIME", time));
                        return;
                    }
                }
                List<String> versions = new LinkedList<>();
                File versionsFile = Utils.downloadVersionsFile();
                JSONArray versionsJSONArray = new JSONObject(FileUtils.readFileContent(versionsFile)).optJSONArray("versions");

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
                            if (times.length > 1) {
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
                Collections.reverse(versions);
                System.out.println(Arrays.toString(addDoubleQuotationMark(versions).toArray()));
            } catch (Exception exception) {
                exception.printStackTrace();
                Utils.printfln(getString("CONSOLE_FAILED_LIST_VERSIONS"), exception);
            }

        } else {
            System.out.println(Utils.getString("CONSOLE_UNKNOWN_COMMAND_OR_MEANING", firstArg.originString));
        }
    }


    @Override
    public String getUsageName() {
        return "install";
    }
}
