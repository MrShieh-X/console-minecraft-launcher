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

package com.mrshiehx.cmcl.utils.cmcl;

import com.mrshiehx.cmcl.constants.Constants;
import com.mrshiehx.cmcl.exceptions.NotSelectedException;
import com.mrshiehx.cmcl.functions.AccountFunction;
import com.mrshiehx.cmcl.modules.account.authentication.microsoft.MicrosoftAuthentication;
import com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.authlib.AuthlibInjectorAuthentication;
import com.mrshiehx.cmcl.modules.account.authentication.yggdrasil.nide8auth.Nide8AuthAuthentication;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.console.ConsoleUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static com.mrshiehx.cmcl.utils.Utils.getString;

public class AccountUtils {
    /*public static JSONObject getSelectedAccount(JSONObject config, boolean prompt) throws NotSelectedException {
        JSONArray accounts = config.optJSONArray("accounts");
        if (accounts == null || accounts.length() == 0) {
            if (prompt)
                System.out.println(getString("NOT_SELECTED_AN_ACCOUNT"));
            throw new NotSelectedException();
        }
        return accounts.toList()
                .stream()
                .filter(AccountUtils::isValidAccount)
                .map(x -> (JSONObject) x)
                .filter(x -> x.optBoolean("selected"))
                .findFirst()
                .orElseThrow(() -> {
                    if (prompt) System.out.println(getString("NOT_SELECTED_AN_ACCOUNT"));
                    return new NotSelectedException();
                });*/

    public static JSONObject getSelectedAccount(JSONObject config, boolean prompt) throws NotSelectedException {
        JSONArray accounts = config.optJSONArray("accounts");
        if (accounts == null || accounts.length() == 0) {
            if (prompt)
                System.out.println(getString("NOT_SELECTED_AN_ACCOUNT"));
            throw new NotSelectedException();
        }
        for (Object o : accounts) {
            if (!isValidAccount(o)) continue;
            JSONObject jsonObject = (JSONObject) o;
            if (jsonObject.optBoolean("selected")) {
                return jsonObject;
            }
        }
        if (prompt) System.out.println(getString("NOT_SELECTED_AN_ACCOUNT"));
        throw new NotSelectedException();
    }


    public static JSONObject getSelectedAccountIfNotLoginNow(JSONObject config) {
        JSONArray accounts = config.optJSONArray("accounts");
        int valid = 0;
        if (accounts != null) {
            for (Object o : accounts) {
                if (!isValidAccount(o)) continue;
                valid++;
                if (((JSONObject) o).optBoolean("selected")) {
                    return ((JSONObject) o);
                }
            }
        }
        if (accounts != null && valid > 0) {
            AtomicInteger i1 = new AtomicInteger();
            Utils.iteratorToStream(accounts.iterator())
                    .filter(AccountUtils::isValidAccount)
                    .map(object -> (JSONObject) object)
                    .forEach(account -> System.out.println(AccountFunction.formatAccountInfo(account, i1.getAndIncrement())));
            int order = ConsoleUtils.inputInt(getString("MESSAGE_SELECT_ACCOUNT", 0, valid - 1), 0, valid - 1);
            if (order == Integer.MAX_VALUE) return null;
            JSONObject account = accounts.optJSONObject(order);

            if (!isValidAccount(account)) {
                Utils.printfln(getString("ACCOUNT_INVALID"), order);
                return null;
            }

            account.put("selected", true);
            for (int i = 0; i < accounts.length(); i++) {
                if (i != order) {
                    JSONObject accountInFor = accounts.optJSONObject(i);
                    if (accountInFor != null) {
                        accountInFor.put("selected", false);
                    }
                }
            }
            Utils.saveConfig(config);
            return account;
        } else {
            System.out.println("[0]" + Utils.getString("ACCOUNT_TYPE_OFFLINE"));
            System.out.println("[1]" + Utils.getString("ACCOUNT_TYPE_MICROSOFT"));
            System.out.println("[2]" + Utils.getString("ACCOUNT_TYPE_OAS"));
            System.out.println("[3]" + Utils.getString("ACCOUNT_TYPE_NIDE8AUTH"));
            int sel = ConsoleUtils.inputInt(getString("MESSAGE_SELECT_ACCOUNT_TYPE", 0, 3), 0, 3);
            if (accounts == null) {
                config.put("accounts", accounts = new JSONArray());
            }
            switch (sel) {
                case 0: {
                    JSONObject account = new JSONObject().put("playerName", ConsoleUtils.inputString(getString("ACCOUNT_TIP_LOGIN_OFFLINE_PLAYERNAME"))).put("selected", true).put("loginMethod", 0);
                    accounts.put(account);
                    Utils.saveConfig(config);
                    return account;
                }
                case 1: {
                    try {
                        JSONObject account = MicrosoftAuthentication.loginMicrosoftAccount();
                        if (account == null) {
                            return null;
                        }
                        accounts.put(account.put("selected", true));
                        Utils.saveConfig(config);
                        return account;
                    } catch (Exception e) {
                        if (Constants.isDebug()) e.printStackTrace();
                        System.out.println(e.getMessage());
                    }
                }
                case 2: {
                    try {
                        JSONObject account = AuthlibInjectorAuthentication.authlibInjectorLogin(ConsoleUtils.inputString(getString("ACCOUNT_TIP_LOGIN_OAS_ADDRESS")), null, true);
                        if (account == null) {
                            return null;
                        }
                        accounts.put(account);
                        Utils.saveConfig(config);
                        return account;
                    } catch (Exception e) {
                        if (Constants.isDebug()) e.printStackTrace();
                        System.out.println(e.getMessage());
                    }
                }
                case 3: {
                    try {
                        JSONObject account = Nide8AuthAuthentication.nide8authLogin(ConsoleUtils.inputString(getString("ACCOUNT_TIP_LOGIN_NIDE8AUTH_SERVER_ID")), null, true);
                        if (account == null) {
                            return null;
                        }
                        accounts.put(account);
                        Utils.saveConfig(config);
                        return account;
                    } catch (Exception e) {
                        if (Constants.isDebug()) e.printStackTrace();
                        System.out.println(e.getMessage());
                    }
                }
                default:
                    return null;
            }
        }
    }


    /**
     * 对返回值操作将不会影响到原 config
     **/
    /*public static JSONObject getSelectedAccountIfNotLoginNow(JSONObject config) {
        JSONArray accountsArray = config.optJSONArray("accounts");
        if (accountsArray == null)
            config.put("accounts", accountsArray = new JSONArray());

        List<JSONObject> accountsList = AccountFunction.getAllAccounts(accountsArray);

        Optional<JSONObject> optionalAccount = accountsList.stream().filter(jsonObject -> jsonObject.optBoolean("selected")).findFirst();

        if (optionalAccount.isPresent()) return optionalAccount.get();

        long valid = accountsList.size();
        if (valid > 0) {
            int order = ConsoleUtils.inputInt(getString("MESSAGE_SELECT_ACCOUNT", 0, valid - 1), 0, (int) (valid - 1));
            JSONObject account = accountsList.get(order);
            //已在AccountFunction.getAllAccounts判断
            *//*if (!isValidAccount(account)) {
                Utils.printfln(getString("ACCOUNT_INVALID"), order);
                return null;
            }*//*
            for (int i = 0; i < accountsList.size(); i++)
                accountsList.get(i).put("selected", i == order);

            config.put("accounts", new JSONArray(accountsList));
            Utils.saveConfig(config);
            return account;
        } else {
            System.out.println("[0]" + getString("ACCOUNT_TYPE_OFFLINE"));
            System.out.println("[1]" + getString("ACCOUNT_TYPE_MICROSOFT"));
            System.out.println("[2]" + getString("ACCOUNT_TYPE_OAS"));
            int sel = ConsoleUtils.inputInt(getString("MESSAGE_SELECT_ACCOUNT_TYPE", 0, 2), 0, 2);
            switch (sel) {
                case 0: {
                    JSONObject account = new JSONObject().put("playerName", ConsoleUtils.inputString(getString("ACCOUNT_TIP_LOGIN_OFFLINE_PLAYERNAME"))).put("selected", true).put("loginMethod", 0);
                    accountsArray.put(account);
                    Utils.saveConfig(config);
                    return account;
                }
                case 1:
                    try {
                        JSONObject account = MicrosoftAccounts.loginMicrosoftAccount();
                        if (account == null) {
                            return null;
                        }
                        accountsArray.put(account.put("selected", true));
                        Utils.saveConfig(config);
                        return account;
                    } catch (Exception e) {
                        if (Constants.isDebug()) e.printStackTrace();
                        System.out.println(e.getMessage());
                    }
                case 2: {
                    try {
                        JSONObject account = AuthlibAccounts.authlibInjectorLogin(ConsoleUtils.inputString(getString("ACCOUNT_TIP_LOGIN_OAS_ADDRESS")), true);
                        if (account == null) {
                            return null;
                        }
                        accountsArray.put(account);
                        Utils.saveConfig(config);
                        return account;
                    } catch (Exception e) {
                        if (Constants.isDebug()) e.printStackTrace();
                        System.out.println(e.getMessage());
                    }
                }
                default:
                    return null;
            }
        }
    }*/
    public static String getUUIDByName(String playerName) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(StandardCharsets.UTF_8)).toString().replace("-", "");
    }

    public static boolean isValidAccount(Object object) {
        return object instanceof JSONObject && isValidAccount((JSONObject) object);
    }

    public static boolean isValidAccount(JSONObject account) {
        return account != null && !Utils.isEmpty(account.optString("playerName")) && account.has("loginMethod");
    }
}
