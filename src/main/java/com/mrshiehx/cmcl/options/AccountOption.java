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

import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.bean.arguments.SingleArgument;
import com.mrshiehx.cmcl.bean.arguments.ValueArgument;
import com.mrshiehx.cmcl.modules.account.loginner.MicrosoftAccountLoginner;
import com.mrshiehx.cmcl.modules.account.skin.SkinDownloader;
import com.mrshiehx.cmcl.utils.ConsoleUtils;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONObject;

import java.io.File;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;

public class AccountOption implements Option {
    @Override
    public void execute(Arguments arguments) {
        Argument subOption = arguments.opt(1);
        if (subOption == null) {
            System.out.println(getString("CONSOLE_GET_USAGE"));
            return;
        }
        String key = subOption.key;
        JSONObject jsonObject = Utils.getConfig();
        boolean hasAccessToken = jsonObject.has("accessToken");
        switch (key.toLowerCase()) {
            case "t":
                jsonObject.remove("loginMethod");
                jsonObject.remove("accessToken");
                jsonObject.remove("uuid");
                jsonObject.remove("emailAddress");
                jsonObject.remove("tokenType");
                Utils.saveConfig(jsonObject);
                break;
            case "l":
                Argument loginMethodArg = arguments.opt(2);
                if (loginMethodArg == null) {
                    System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                    return;
                }
                String loginMethod = loginMethodArg.key;
                if ("o".equalsIgnoreCase(loginMethod) && loginMethodArg instanceof ValueArgument) {
                    ValueArgument valueArgument = (ValueArgument) loginMethodArg;
                    if (replace(hasAccessToken)) {
                        jsonObject.put("loginMethod", 0);
                        jsonObject.remove("accessToken");
                        jsonObject.remove("uuid");
                        jsonObject.remove("emailAddress");
                        jsonObject.remove("tokenType");
                        jsonObject.put("playerName", valueArgument.value);
                        Utils.saveConfig(jsonObject);
                    } else return;
                } else if ("m".equalsIgnoreCase(loginMethod) && loginMethodArg instanceof SingleArgument) {
                    //SingleArgument singleArgument=(SingleArgument)loginMethodArg;
                    if (replace(hasAccessToken)) {
                        MicrosoftAccountLoginner.loginMicrosoftAccount(jsonObject);
                    } else return;

                } else {
                    System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                    return;
                }
                break;
            case "r":
                String token;
                if (jsonObject.optInt("loginMethod") == 2 && !Utils.isEmpty((token = jsonObject.optString("accessToken")))) {

                    try {
                        JSONObject mcSecond = Utils.parseJSONObject(Utils.get("https://api.minecraftservices.com/minecraft/profile", jsonObject.optString("tokenType", "Bearer"), token));

                        if (mcSecond != null) {
                            if (mcSecond.has("error") || mcSecond.has("errorMessage")) {
                                String var2 = mcSecond.optString("errorMessage");
                                System.out.println(String.format(getString("DIALOG_OFFICIAL_LOGIN_FAILED_TEXT"), mcSecond.optString("error"), var2));
                            } else {
                                jsonObject.put("loginMethod", 2);
                                jsonObject.put("uuid", mcSecond.optString("id"));
                                jsonObject.put("playerName", mcSecond.optString("name"));
                                Utils.saveConfig(jsonObject);
                                return;
                            }
                        } else {
                            System.out.println(getString("DIALOG_OFFICIAL_FAILED_REFRESH_TITLE") + ": " + getString("CONSOLE_FAILED_REFRESH_OFFICIAL_NO_RESPONSE"));
                        }
                    } catch (Exception ee) {
                        System.out.println(getString("DIALOG_OFFICIAL_FAILED_REFRESH_TITLE") + ": " + ee);
                    }
                } else {
                    System.out.println(getString("CONSOLE_ACCOUNT_UN_OPERABLE_ACCESS_TOKEN"));
                }
                break;
            case "s":
                Argument skinArg = arguments.opt(2);
                if (skinArg == null) {
                    System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                    return;
                }
                if ("d".equalsIgnoreCase(skinArg.key) && skinArg instanceof ValueArgument) {
                    if (jsonObject.optInt("loginMethod") != 0 && jsonObject.has("uuid")) {
                        File file = new File(((ValueArgument) skinArg).value);
                        if (!file.exists())
                            SkinDownloader.start(file, jsonObject);
                        else
                            Utils.printfln(Utils.getString("CONSOLE_FILE_EXISTS"), file.getAbsolutePath());
                    } else {
                        System.out.println(getString("CONSOLE_ACCOUNT_UN_OPERABLE_UUID"));
                    }
                } else {
                    System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                    return;
                }
                break;
            default:
                System.out.println(String.format(getString("CONSOLE_UNKNOWN_OPTION"), key));
                break;
        }
    }

    private boolean replace(boolean hasAccessToken) {
        if (hasAccessToken) {
            return ConsoleUtils.yesOrNo(getString("CONSOLE_REPLACE_ACCOUNT"));
        } else return true;
    }

    @Override
    public String getUsageName() {
        return "ACCOUNT";
    }
}
