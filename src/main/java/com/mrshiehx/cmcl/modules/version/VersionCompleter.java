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
package com.mrshiehx.cmcl.modules.version;

import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.cmcl.version.VersionModuleUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.isEmpty;

public class VersionCompleter {
    public static void execute(File jsonFile, File jarFile, String versionName) {
        JSONObject headJSONObjectIC;
        String headJSONObjectICContent;
        try {
            headJSONObjectICContent = FileUtils.readFileContent(jsonFile);
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

        if (jarFile.exists()) {
            try {
                jarFileContent = FileUtils.getBytes(jarFile);
            } catch (Exception ignore) {
            }
        }
        try {
            VersionInstaller.start(
                    inheritsFrom,
                    versionName,
                    new JSONObject(FileUtils.readFileContent(Utils.downloadVersionsFile())).optJSONArray("versions"),
                    true, true, true, null, Constants.DEFAULT_DOWNLOAD_THREAD_COUNT,
                    null, null, null, null, null,
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
                                Arguments arguments1 = new Arguments(hmca, false);
                                Arguments arguments2 = new Arguments(minecraftArguments, false);
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

                        String fabricVersion = VersionModuleUtils.getFabricVersion(headJSONObject);
                        if (!isEmpty(fabricVersion)) {
                            headJSONObject.put("fabric", new JSONObject().put("version", fabricVersion));
                        }
                        String forgeVersion = VersionModuleUtils.getForgeVersion(headJSONObject);
                        if (!isEmpty(forgeVersion)) {
                            headJSONObject.put("forge", new JSONObject().put("version", forgeVersion));
                        }
                        String liteloaderVersion = VersionModuleUtils.getLiteloaderVersion(headJSONObject);
                        if (!isEmpty(liteloaderVersion)) {
                            headJSONObject.put("liteloader", new JSONObject().put("version", liteloaderVersion));
                        }
                        String optifineVersion = VersionModuleUtils.getOptifineVersion(headJSONObject);
                        if (!isEmpty(optifineVersion)) {
                            headJSONObject.put("optifine", new JSONObject().put("version", optifineVersion));
                        }
                        String quiltVersion = VersionModuleUtils.getQuiltVersion(headJSONObject);
                        if (!isEmpty(quiltVersion)) {
                            headJSONObject.put("quilt", new JSONObject().put("version", quiltVersion));
                        }

                    }, null);
        } catch (Exception e) {
            System.out.println(e.getMessage());//legal
            try {
                FileUtils.writeFile(jsonFile, headJSONObjectICContent, false);
                if (jarFileContent == null) {
                    jarFile.delete();
                } else {
                    FileUtils.bytes2File(jarFile, jarFileContent, false);
                }
            } catch (Exception ignore) {
            }
        }
    }
}
