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
package com.mrshiehx.cmcl.functions;

import com.mrshiehx.cmcl.bean.arguments.*;
import com.mrshiehx.cmcl.exceptions.DescriptionException;
import com.mrshiehx.cmcl.exceptions.NotSelectedException;
import com.mrshiehx.cmcl.interfaces.filters.JSONObjectFilter;
import com.mrshiehx.cmcl.modules.account.authentication.AccountRefresher;
import com.mrshiehx.cmcl.modules.account.authentication.microsoft.MicrosoftAuthentication;
import com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.YggdrasilAuthentication;
import com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.authlib.AuthlibInjectorApiProvider;
import com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.authlib.AuthlibInjectorAuthentication;
import com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.nide8auth.Nide8AuthAuthentication;
import com.mrshiehx.cmcl.modules.account.skin.SkinDownloader;
import com.mrshiehx.cmcl.utils.FileUtils;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.cmcl.AccountUtils;
import com.mrshiehx.cmcl.utils.console.ConsoleUtils;
import com.mrshiehx.cmcl.utils.internet.NetworkUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.mrshiehx.cmcl.CMCL.getString;

public class AccountFunction implements Function {
    @Override
    public void execute(Arguments arguments) {
        if (!Function.checkArgs(arguments, 2, 1,
                ArgumentRequirement.ofSingle("l"),
                ArgumentRequirement.ofSingle("list"),
                ArgumentRequirement.ofSingle("r"),
                ArgumentRequirement.ofSingle("refresh"),
                ArgumentRequirement.ofSingle("cape"),
                ArgumentRequirement.ofSingle("skin"),
                ArgumentRequirement.ofSingle("s"),
                ArgumentRequirement.ofSingle("select"),
                ArgumentRequirement.ofValue("s"),
                ArgumentRequirement.ofValue("name"),
                ArgumentRequirement.ofValue("address"),
                ArgumentRequirement.ofValue("serverId"),
                ArgumentRequirement.ofValue("select"),
                ArgumentRequirement.ofValue("d"),
                ArgumentRequirement.ofValue("delete"),
                ArgumentRequirement.ofValue("cape"),
                ArgumentRequirement.ofValue("download-skin"),
                ArgumentRequirement.ofValue("skin"),
                ArgumentRequirement.ofValue("login"))) return;
        JSONObject config = Utils.getConfig();
        JSONArray accounts = config.optJSONArray("accounts");
        if (accounts == null) config.put("accounts", accounts = new JSONArray());

        if (arguments.optArgument(1) instanceof SingleArgument) {
            SingleArgument argument = (SingleArgument) arguments.optArgument(1);
            switch (argument.key) {
                case "l":
                case "list": {
                    AtomicInteger i = new AtomicInteger();
                    StringBuilder sb = new StringBuilder();
                    getAllAccounts(accounts).forEach(account -> sb.append(formatAccountInfo(account, i.getAndIncrement())).append('\n'));
                    System.out.print(sb);
                }
                break;
                case "r":
                case "refresh": {
                    JSONObject account = null;
                    try {
                        account = AccountUtils.getSelectedAccount(config, true);
                    } catch (NotSelectedException ignore) {
                    }

                    if (account == null) {
                        return;
                    }

                    try {
                        if (AccountRefresher.execute(account, accounts)) {
                            Utils.saveConfig(config);
                        }
                    } catch (DescriptionException e) {
                        e.print();
                    }
                }
                break;
                case "cape": {
                    JSONObject account;
                    try {
                        account = AccountUtils.getSelectedAccount(config, true);
                    } catch (NotSelectedException e) {
                        break;
                    }
                    int loginMethod = account.optInt("loginMethod");
                    if (loginMethod != 0) {
                        System.out.println(getString("ONLY_OFFLINE"));
                        break;
                    }
                    account.remove("cape");
                    Utils.saveConfig(config);
                }
                break;
                case "skin":
                    JSONObject account;
                    try {
                        account = AccountUtils.getSelectedAccount(config, true);
                    } catch (NotSelectedException e) {
                        break;
                    }
                    if (account.optInt("loginMethod") == 0) {
                        account.remove("providedSkin");
                        account.remove("offlineSkin");
                        account.remove("slim");
                        Utils.saveConfig(config);
                    } else {
                        System.out.println(getString("SKIN_CANCEL_ONLY_FOR_OFFLINE"));
                    }
                    break;
                default:
                    System.out.println(getString("CONSOLE_UNKNOWN_COMMAND_OR_MEANING", argument.originArray[0]));
                    break;

            }

        } else if (arguments.optArgument(1) instanceof ValueArgument) {
            Argument arg = arguments.optArgument(1);
            String value = ((ValueArgument) arg).value;
            switch (arg.key) {
                case "cape": {
                    JSONObject account;
                    try {
                        account = AccountUtils.getSelectedAccount(config, true);
                    } catch (NotSelectedException e) {
                        break;
                    }
                    int loginMethod = account.optInt("loginMethod");
                    if (loginMethod != 0) {
                        System.out.println(getString("ONLY_OFFLINE"));
                        break;
                    }
                    File file = new File(value);
                    if (!file.exists() || file.isDirectory()) {
                        System.out.println(getString("FILE_NOT_FOUND_OR_IS_A_DIRECTORY"));
                        break;
                    }
                    account.put("cape", file.getAbsolutePath());
                    Utils.saveConfig(config);
                }
                break;
                case "skin": {
                    JSONObject account;
                    try {
                        account = AccountUtils.getSelectedAccount(config, true);
                    } catch (NotSelectedException e) {
                        break;
                    }
                    int loginMethod = account.optInt("loginMethod");

                    if (!"steve".equals(value) && !"alex".equals(value)) {
                        if (loginMethod == 1) {
                            File file = new File(value);
                            if (!file.exists() || file.isDirectory()) {
                                System.out.println(getString("FILE_NOT_FOUND_OR_IS_A_DIRECTORY"));
                                break;
                            }
                            String suffix = "";
                            int var2 = file.getName().lastIndexOf("\\.");
                            if (var2 != -1) {
                                suffix = "/" + file.getName().substring(var2 + 1);
                            }
                            boolean slim = ConsoleUtils.yesOrNo(getString("SKIN_TYPE_DEFAULT_OR_SLIM"));
                            try {
                                YggdrasilAuthentication.uploadSkin(new AuthlibInjectorApiProvider(account.optString("url")), account.optString("uuid"), account.optString("accessToken"), file.getName(), suffix, FileUtils.getBytes(file), slim);
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println(getString("UNABLE_SET_SKIN"));
                            }
                        } else if (loginMethod == 0) {
                            File file = new File(value);
                            if (!file.exists() || file.isDirectory()) {
                                System.out.println(getString("FILE_NOT_FOUND_OR_IS_A_DIRECTORY"));
                                break;
                            }
                            if (ConsoleUtils.yesOrNo(getString("SKIN_TYPE_DEFAULT_OR_SLIM"))) {
                                account.put("slim", true);
                            } else {
                                account.remove("slim");
                            }
                            account.remove("providedSkin");
                            account.put("offlineSkin", file.getAbsolutePath());
                            Utils.saveConfig(config);

                        } else {
                            System.out.println(getString("UPLOAD_SKIN_ONLY_OAS_OR_OFFLINE"));

                        }

                    } else {
                        boolean steve = "steve".equals(value);
                        if (loginMethod == 0) {
                            account.remove("offlineSkin");
                            account.remove("slim");
                            account.put("providedSkin", steve ? "steve" : "alex");
                            Utils.saveConfig(config);
                        } else if (loginMethod == 1) {
                            String name;
                            byte[] skin;
                            boolean slim = false;
                            try {
                                if (steve) {
                                    name = "steve.png";
                                    InputStream is = AccountFunction.class.getResourceAsStream("/skin/steve.png");
                                    if (is == null) {
                                        System.out.println(getString("SKIN_STEVE_NOT_FOUND"));
                                        break;
                                    }
                                    skin = FileUtils.inputStream2ByteArray(is);
                                } else {
                                    name = "alex.png";
                                    InputStream is = AccountFunction.class.getResourceAsStream("/skin/alex.png");
                                    if (is == null) {
                                        System.out.println(getString("SKIN_ALEX_NOT_FOUND"));
                                        break;
                                    }
                                    skin = FileUtils.inputStream2ByteArray(is);
                                    slim = true;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println(getString(steve ? "SKIN_STEVE_UNABLE_READ" : "SKIN_ALEX_UNABLE_READ"));
                                break;
                            }

                            try {
                                YggdrasilAuthentication.uploadSkin(new AuthlibInjectorApiProvider(account.optString("url")), account.optString("uuid"), account.optString("accessToken"), name, "png", skin, slim);
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println(getString("UNABLE_SET_SKIN"));
                            }
                        } else {
                            System.out.println(getString("UPLOAD_SKIN_ONLY_OAS_OR_OFFLINE"));
                        }
                    }
                }
                break;
                case "s":
                case "select": {
                    int order;
                    try {
                        order = Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        Utils.printfln(getString("CONSOLE_UNSUPPORTED_VALUE"), value);
                        return;
                    }
                    if (order < 0) {
                        Utils.printfln(getString("ACCOUNT_NOT_EXISTS"), order);
                        return;
                    }
                    JSONObject account = accounts.optJSONObject(order);
                    if (account == null) {
                        Utils.printfln(getString("ACCOUNT_NOT_EXISTS"), order);
                        return;
                    }

                    if (!AccountUtils.isValidAccount(account)) {
                        Utils.printfln(getString("ACCOUNT_INVALID"), order);
                        return;
                    }

                    account.put("selected", true);
                    for (int i = 0; i < accounts.length(); i++) {
                        if (i != order) {
                            JSONObject acc = accounts.optJSONObject(i);
                            if (acc != null) {
                                acc.put("selected", false);
                            }
                        }
                    }
                    config.put("accounts", accounts);
                    Utils.saveConfig(config);
                }
                break;
                case "d":
                case "delete":
                    try {
                        int order = Integer.parseInt(value);
                        //accounts.remove(order);

                        //防止accounts内混入奇奇怪怪的东西
                        Map<Integer, Integer> map = new HashMap<>();
                        int accountCount = 0;
                        for (int i = 0; i < accounts.length(); i++) {
                            Object object = accounts.get(i);
                            if (AccountUtils.isValidAccount(object)) {
                                map.put(accountCount++, i);
                            }
                        }
                        Integer integer = map.get(order);
                        if (integer != null) {
                            accounts.remove(integer);
                            Utils.saveConfig(config);
                        }
                    } catch (NumberFormatException e) {
                        Utils.printfln(getString("CONSOLE_UNSUPPORTED_VALUE"), value);
                        return;
                    }
                    break;
                case "download-skin": {
                    JSONObject account;
                    try {
                        account = AccountUtils.getSelectedAccount(config, true);
                    } catch (NotSelectedException e) {
                        break;
                    }
                    int loginMethod = account.optInt("loginMethod");
                    File file = new File(value);
                    if ((loginMethod == 1 && !account.optString("url").isEmpty())
                            || (loginMethod == 2 && !account.optString("uuid").isEmpty() && !account.optString("url").isEmpty())
                            || (loginMethod == 3 && !account.optString("uuid").isEmpty() && !account.optString("serverId").isEmpty())) {
                        if (!file.exists())
                            SkinDownloader.start(file, account);
                        else
                            Utils.printfln(Utils.getString("CONSOLE_FILE_EXISTS"), file.getAbsolutePath());
                    } else {
                        System.out.println(getString("CONSOLE_ACCOUNT_UN_OPERABLE_MISSING_INFO"));
                    }
                }
                break;
                case "login":
                    boolean select = arguments.contains("s") || arguments.contains("select");
                    if ("offline".equalsIgnoreCase(value)) {
                        Argument nameArgument = Optional.ofNullable(arguments.optArgument("name")).orElseGet(() -> arguments.optArgument("n"));
                        if (!(nameArgument instanceof ValueArgument)) {
                            System.out.println(Utils.getString("ACCOUNT_LOGIN_NEED_NAME"));
                            break;
                        }
                        String name = ((ValueArgument) nameArgument).value;
                        int indexOf = -1;
                        for (int i = 0; i < accounts.length(); i++) {
                            JSONObject account = accounts.optJSONObject(i);
                            if (account == null) continue;
                            if (Objects.equals(account.optString("playerName"), name) && account.optInt("loginMethod") == 0) {
                                indexOf = i;
                            } else {
                                if (select)
                                    account.put("selected", false);
                            }

                        }
                        JSONObject account = new JSONObject();
                        account.put("playerName", name);
                        account.put("selected", select);
                        account.put("loginMethod", 0);
                        if (indexOf >= 0) {
                            if (ConsoleUtils.yesOrNo(String.format(getString("CONSOLE_REPLACE_LOGGED_ACCOUNT"), indexOf))) {
                                accounts.put(indexOf, account);
                                config.put("accounts", accounts);
                                Utils.saveConfig(config);
                            }
                        } else {
                            accounts.put(account);
                            Utils.saveConfig(config);
                        }
                    } else if ("microsoft".equalsIgnoreCase(value)) {
                        JSONObject account = MicrosoftAuthentication.loginMicrosoftAccount();
                        if (account == null) {
                            break;
                        }
                        addOrReplace(account, config, select, jsonObject1 -> jsonObject1.optInt("loginMethod") == 2 && Objects.equals(account.optString("id"), jsonObject1.optString("id")) && AccountUtils.isValidAccount(jsonObject1));
                    } else if ("authlib".equalsIgnoreCase(value)) {
                        Argument addressArgument = arguments.optArgument("address");
                        if (!(addressArgument instanceof ValueArgument)) {
                            System.out.println(Utils.getString("ACCOUNT_LOGIN_NEED_ADDRESS"));
                            break;
                        }
                        String address = ((ValueArgument) addressArgument).value;

                        JSONObject account;
                        try {
                            account = AuthlibInjectorAuthentication.authlibInjectorLogin(address, null, select);
                        } catch (Exception e) {
                            Utils.printfln(getString("FAILED_TO_LOGIN_OTHER_AUTHENTICATION_ACCOUNT"), e);
                            break;
                        }
                        if (account == null)
                            break;
                        addOrReplace(account, config, select, jsonObject -> jsonObject.optInt("loginMethod") == 1 &&
                                NetworkUtils.urlEqualsIgnoreSlash(account.optString("url"), jsonObject.optString("url")) &&
                                Objects.equals(account.optString("uuid"), jsonObject.optString("uuid")) && AccountUtils.isValidAccount(jsonObject));
                    } else if ("nide8auth".equalsIgnoreCase(value)) {
                        Argument serverIdArgument = arguments.optArgument("serverId");
                        if (!(serverIdArgument instanceof ValueArgument)) {
                            System.out.println(Utils.getString("ACCOUNT_LOGIN_NEED_SERVER_ID"));
                            break;
                        }
                        String serverId = ((ValueArgument) serverIdArgument).value;

                        JSONObject account;
                        try {
                            account = Nide8AuthAuthentication.nide8authLogin(serverId, null, select);
                        } catch (Exception e) {
                            Utils.printfln(getString("FAILED_TO_LOGIN_NIDE8AUTH_ACCOUNT"), e);
                            break;
                        }
                        if (account == null)
                            break;
                        addOrReplace(account, config, select, jsonObject -> jsonObject.optInt("loginMethod") == 3 &&
                                account.optString("serverId").equalsIgnoreCase(jsonObject.optString("serverId")) &&
                                Objects.equals(account.optString("uuid"), jsonObject.optString("uuid")) && AccountUtils.isValidAccount(jsonObject));
                    } else {
                        System.out.println(getString("ACCOUNT_LOGIN_UNKNOWN_LOGIN_METHOD", value));
                    }

                    break;
                default:
                    System.out.println(getString("CONSOLE_UNKNOWN_COMMAND_OR_MEANING", arguments.optArgument(1).originString));
                    break;

            }
        } else {
            System.out.println(getString("CONSOLE_ONLY_HELP", arguments.optArgument(1).originString));
        }
    }

    private static void addOrReplace(JSONObject account, JSONObject config, boolean select, JSONObjectFilter filter) {
        JSONArray accounts = config.optJSONArray("accounts");
        account.put("selected", select);
        int indexOf = -1;
        for (int i = 0; i < accounts.length(); i++) {
            JSONObject jsonObject1 = accounts.optJSONObject(i);
            if (jsonObject1 != null) {
                if (filter.accept(jsonObject1)) {
                    indexOf = i;
                } else {
                    if (select)
                        jsonObject1.put("selected", false);//如果加了--select，那就是要选择新登录的账号，其他账号一律变为未选择的
                }
            }
        }

        if (indexOf < 0) {
            accounts.put(account);
            config.put("accounts", accounts);
            Utils.saveConfig(config);
            System.out.println(getString("MESSAGE_LOGINED_TITLE"));
        } else {
            if (ConsoleUtils.yesOrNo(String.format(getString("CONSOLE_REPLACE_LOGGED_ACCOUNT"), indexOf))) {
                accounts.put(indexOf, account);
                config.put("accounts", accounts);
                Utils.saveConfig(config);
                System.out.println(getString("MESSAGE_LOGINED_TITLE"));
            }
        }
    }

    public static List<JSONObject> getAllAccounts(@NotNull JSONArray accounts) {
        return Utils.iteratorToStream(accounts.iterator()).filter(AccountUtils::isValidAccount).map(x -> (JSONObject) x).collect(Collectors.toList());
    }

    public static String formatAccountInfo(JSONObject account, int order) {
        return String.format("%d.%s (%s) %s",
                order,
                account.optString("playerName", "XPlayer"),
                getAccountType(account),
                account.optBoolean("selected") ? getString("ACCOUNT_SELECTED") : ""
        );
    }


    @Override
    public String getUsageName() {
        return "account";
    }

    public static String getAccountType(JSONObject account) {
        int loginMethod = account.optInt("loginMethod");
        if (loginMethod == 1) {
            return getString("ACCOUNT_TYPE_OAS_WITH_DETAIL", account.optString("serverName"), account.optString("url"));
        } else if (loginMethod == 2) {
            return getString("ACCOUNT_TYPE_MICROSOFT");
        } else if (loginMethod == 3) {
            return getString("ACCOUNT_TYPE_NIDE8AUTH_WITH_DETAIL", account.optString("serverName"), account.optString("serverId"));
        } else {
            return getString("ACCOUNT_TYPE_OFFLINE");
        }
    }
}
