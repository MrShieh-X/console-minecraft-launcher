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
import com.mrshiehx.cmcl.exceptions.NotSelectedException;
import com.mrshiehx.cmcl.modules.account.loginner.AuthlibAccountLoginner;
import com.mrshiehx.cmcl.modules.account.loginner.MicrosoftAccountLoginner;
import com.mrshiehx.cmcl.modules.account.skin.SkinDownloader;
import com.mrshiehx.cmcl.utils.ConsoleUtils;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.authlib.AuthlibInjectorProvider;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.isEmpty;
import static java.nio.charset.StandardCharsets.UTF_8;

public class AccountOption implements Option {
    @Override
    public void execute(Arguments arguments) {
        JSONObject jsonObject = Utils.getConfig();
        JSONArray accounts = jsonObject.optJSONArray("accounts");
        if (accounts == null) jsonObject.put("accounts", accounts = new JSONArray());
        if (arguments.optArgument(0) instanceof SingleArgument) {
            Argument subOption = arguments.opt(1);
            if (subOption == null) {
                System.out.println(getString("CONSOLE_GET_USAGE"));
                return;
            }
            String key = subOption.key;
            switch (key.toLowerCase()) {
                case "p":
                    for (int i = 0; i < accounts.length(); i++) {
                        JSONObject account = accounts.optJSONObject(i);
                        if (account != null) {
                            Utils.printfln(
                                    "%d.%s (%s) %s",
                                    i,
                                    account.optString("playerName", "XPlayer"),
                                    getAccountType(account.optInt("loginMethod"), account.optString("serverName"), account.optString("url")),

                                    account.optBoolean("selected") ? getString("ACCOUNT_SELECTED") : ""
                            );
                        }
                    }
                    break;
                case "t":
                    if (subOption instanceof ValueArgument) {
                        String value = ((ValueArgument) subOption).value;
                        try {
                            int order = Integer.parseInt(value);
                            accounts.remove(order);
                            jsonObject.put("accounts", accounts);
                            Utils.saveConfig(jsonObject);
                        } catch (NumberFormatException e) {
                            Utils.printfln(getString("CONSOLE_UNSUPPORTED_VALUE"), value);
                            return;
                        }
                    } else {

                        System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                        return;
                    }
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
                        String name = valueArgument.value;
                        int indexOf = -1;
                        boolean select = arguments.contains("s");
                        for (int i = 0; i < accounts.length(); i++) {
                            JSONObject account = accounts.optJSONObject(i);
                            if (account != null) {
                                if (Objects.equals(account.optString("playerName", "XPlayer"), name) && account.optInt("loginMethod") == 0) {
                                    indexOf = i;
                                    break;
                                } else {
                                    if (select) account.put("selected", false);
                                }
                            }
                        }
                        JSONObject account = new JSONObject();
                        account.put("playerName", name);
                        account.put("selected", select);
                        account.put("loginMethod", 0);
                        if (indexOf >= 0) {
                            if (ConsoleUtils.yesOrNo(String.format(getString("CONSOLE_REPLACE_LOGGED_ACCOUNT"), indexOf))) {
                                accounts.remove(indexOf);
                                accounts.put(account);
                                jsonObject.put("accounts", accounts);
                                Utils.saveConfig(jsonObject);
                            }

                        } else {
                            accounts.put(account);
                            Utils.saveConfig(jsonObject);
                        }


                    } else if ("m".equalsIgnoreCase(loginMethod) && loginMethodArg instanceof SingleArgument) {
                        //SingleArgument singleArgument=(SingleArgument)loginMethodArg;
                        MicrosoftAccountLoginner.loginMicrosoftAccount(jsonObject, arguments.contains("s"));


                    } else if ("a".equalsIgnoreCase(loginMethod) && loginMethodArg instanceof SingleArgument) {
                        Argument sourceAddress = arguments.optArgument("d");

                        if (!(sourceAddress instanceof ValueArgument)) {
                            System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                            return;
                        }
                        String address = ((ValueArgument) sourceAddress).value;

                        System.out.print(getString("INPUT_ACCOUNT"));
                        String username = "";
                        try {
                            username = new Scanner(System.in).nextLine();
                        } catch (NoSuchElementException ignore) {
                            return;
                        }
                        Console console = System.console();
                        String password = "";
                        if (console != null) {
                            System.out.print(getString("INPUT_PASSWORD"));
                            char[] input = console.readPassword();
                            password = input != null ? new String(input) : "";
                        } else {
                            System.out.println(getString("WARNING_SHOWING_PASSWORD"));
                            System.out.print(getString("INPUT_PASSWORD"));
                            try {
                                password = new Scanner(System.in).nextLine();
                            } catch (NoSuchElementException ignore) {
                                return;
                            }
                        }
                        try {
                            AuthlibAccountLoginner.login(address, username, password, arguments.contains("s"), jsonObject);
                        } catch (Exception e) {
                            //e.printStackTrace();
                            Utils.printfln(getString("FAILED_TO_LOGIN_OTHER_AUTHENTICATION_ACCOUNT"), e);
                        }
                    } else {
                        System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                        return;
                    }
                    break;
                case "r": {
                    JSONObject account = null;
                    int indexOf = -1;
                    for (int i = 0; i < accounts.length(); i++) {
                        Object o = accounts.opt(i);
                        if (o instanceof JSONObject) {
                            JSONObject jsonObject1 = (JSONObject) o;
                            if (jsonObject1.optBoolean("selected")) {
                                account = jsonObject1;
                                indexOf = i;
                            }
                        }
                    }

                    if (account != null) {
                        String token;
                        if (account.optInt("loginMethod") == 2) {
                            if (!Utils.isEmpty((token = account.optString("accessToken")))) {

                                try {
                                    JSONObject mcSecond = Utils.parseJSONObject(Utils.getWithToken("https://api.minecraftservices.com/minecraft/profile", account.optString("tokenType", "Bearer"), token));

                                    if (mcSecond != null) {
                                        if (mcSecond.has("error") || mcSecond.has("errorMessage")) {
                                            String var2 = mcSecond.optString("errorMessage");
                                            System.out.println(getString("ERROR_WITH_MESSAGE", mcSecond.optString("error"), var2));
                                        } else {
                                            account.put("uuid", mcSecond.optString("id"));
                                            account.put("playerName", mcSecond.optString("name"));
                                            Utils.saveConfig(jsonObject);
                                            return;
                                        }
                                    } else {
                                        System.out.println(getString("MESSAGE_FAILED_REFRESH_TITLE") + ": " + getString("CONSOLE_FAILED_REFRESH_OFFICIAL_NO_RESPONSE"));
                                    }
                                } catch (Exception ee) {
                                    System.out.println(getString("MESSAGE_FAILED_REFRESH_TITLE") + ": " + ee);
                                }
                            } else {
                                System.out.println(getString("CONSOLE_ACCOUNT_UN_OPERABLE_ACCESS_TOKEN"));
                            }
                        } else if (account.optInt("loginMethod") == 1) {
                            String accessToken = account.optString("accessToken");
                            String clientToken = account.optString("clientToken");
                            String url = account.optString("url");
                            if (Utils.isEmpty((accessToken)) || Utils.isEmpty(clientToken) || Utils.isEmpty(url)) {
                                System.out.println(getString("CONSOLE_ACCOUNT_UN_OPERABLE_NEED_ACCESS_TOKEN_AND_CLIENT_TOKEN_AND_URL"));
                                return;
                            }
                            AuthlibInjectorProvider provider = new AuthlibInjectorProvider(url);
                            JSONObject request = new JSONObject();
                            request.put("accessToken", accessToken);
                            request.put("clientToken", clientToken);
                            try {
                                JSONObject response = Utils.parseJSONObject(Utils.post(provider.getRefreshmentURL(), request.toString()));
                                if (response == null) {
                                    System.out.println(getString("CONSOLE_FAILED_REFRESH_OFFICIAL_NO_RESPONSE"));
                                    return;
                                }
                                JSONObject selectedProfile = response.optJSONObject("selectedProfile");
                                account.put("accessToken", response.optString("accessToken"));
                                if (selectedProfile == null) {
                                    account.remove("playerName");
                                    account.remove("uuid");
                                    System.out.println(getString("WARNING_REFRESH_NOT_SELECTED"));
                                } else {
                                    account.put("playerName", selectedProfile.optString("name"));
                                    account.put("uuid", selectedProfile.optString("id"));
                                }
                                accounts.remove(indexOf);
                                accounts.put(indexOf, account);
                                jsonObject.put("accounts", accounts);
                                Utils.saveConfig(jsonObject);
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println(getString("MESSAGE_FAILED_REFRESH_TITLE") + ": " + e.toString());
                            }

                        }
                    }
                }
                break;
                case "s": {
                    Argument skinArg = arguments.opt(2);
                    if (skinArg == null) {
                        System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                        return;
                    }

                    JSONObject account;
                    try {
                        account = Utils.getSelectedAccount(jsonObject);
                    } catch (NotSelectedException e) {
                        return;
                    }

                    if ("d".equalsIgnoreCase(skinArg.key) && skinArg instanceof ValueArgument) {
                        File file = new File(((ValueArgument) skinArg).value);
                        if ((account.optInt("loginMethod") == 1 && !Utils.isEmpty(account.optString("url"))) || (account.optInt("loginMethod") == 2) && account.has("uuid")) {

                            if (!file.exists())
                                SkinDownloader.start(file, account);
                            else
                                Utils.printfln(Utils.getString("CONSOLE_FILE_EXISTS"), file.getAbsolutePath());
                        } else {
                            System.out.println(getString("CONSOLE_ACCOUNT_UN_OPERABLE_UUID"));
                        }
                    } else if ("u".equalsIgnoreCase(skinArg.key) && skinArg instanceof SingleArgument) {
                        account.remove("providedSkin");
                        account.remove("offlineSkin");
                        account.remove("slim");
                        Utils.saveConfig(jsonObject);
                    } else if ("u".equalsIgnoreCase(skinArg.key) && skinArg instanceof ValueArgument) {
                        if (account.optInt("loginMethod") == 1) {
                            File file = new File(((ValueArgument) skinArg).value);
                            if (!file.exists() || file.isDirectory()) {
                                System.out.println(getString("FILE_NOT_FOUND_OR_IS_A_DIRECTORY"));
                                return;
                            }
                            String suffix = "";
                            int var2 = file.getName().lastIndexOf("\\.");
                            if (var2 != -1) {
                                suffix = "/" + file.getName().substring(var2 + 1);
                            }
                            boolean slim = ConsoleUtils.yesOrNo(getString("SKIN_TYPE_DEFAULT_OR_SLIM"));
                            try {
                                upload(account, file.getName(), suffix, Utils.getBytes(file), slim);
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println(getString("UNABLE_SET_SKIN"));
                            }
                        } else if (account.optInt("loginMethod") == 0) {
                            String value = ((ValueArgument) skinArg).value;
                            if (!Utils.isEmpty(value)) {
                                File file = new File(value);
                                if (!file.exists() || file.isDirectory()) {
                                    System.out.println(getString("FILE_NOT_FOUND_OR_IS_A_DIRECTORY"));
                                    return;
                                }
                                if (ConsoleUtils.yesOrNo(getString("SKIN_TYPE_DEFAULT_OR_SLIM"))) {
                                    account.put("slim", true);
                                } else {
                                    account.remove("slim");
                                }
                                account.remove("providedSkin");
                                account.put("offlineSkin", file.getAbsolutePath());
                            } else {
                                account.remove("slim");
                                account.remove("providedSkin");
                                account.remove("offlineSkin");
                            }
                            Utils.saveConfig(jsonObject);

                        } else {
                            System.out.println(getString("UPLOAD_SKIN_ONLY_OAS_OR_OFFLINE"));

                        }

                    } else if (arguments.contains("e") || arguments.contains("x")) {
                        boolean steve;
                        if (arguments.contains("x") && !arguments.contains("e")) {
                            steve = false;
                        } else if (arguments.contains("e") && !arguments.contains("x")) {
                            steve = true;
                        } else {
                            System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                            return;
                        }

                        if (account.optInt("loginMethod") == 0) {
                            account.remove("offlineSkin");
                            account.remove("slim");
                            account.put("providedSkin", steve ? "steve" : "alex");
                            Utils.saveConfig(jsonObject);
                        } else if (account.optInt("loginMethod") == 1) {
                            String name;
                            byte[] skin;
                            boolean slim = false;
                            try {
                                if (steve) {
                                    name = "steve.png";
                                    InputStream is = AccountOption.class.getResourceAsStream("/skin/steve.png");
                                    if (is == null) {
                                        System.out.println(getString("SKIN_STEVE_NOT_FOUND"));
                                        return;
                                    }
                                    skin = Utils.inputStream2ByteArray(is);
                                } else {
                                    name = "alex.png";
                                    InputStream is = AccountOption.class.getResourceAsStream("/skin/alex.png");
                                    if (is == null) {
                                        System.out.println(getString("SKIN_ALEX_NOT_FOUND"));
                                        return;
                                    }
                                    skin = Utils.inputStream2ByteArray(is);
                                    slim = true;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println(getString(steve ? "SKIN_STEVE_UNABLE_READ" : "SKIN_ALEX_UNABLE_READ"));
                                return;
                            }

                            try {
                                upload(account, name, "png", skin, slim);
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println(getString("UNABLE_SET_SKIN"));
                            }
                        } else {
                            System.out.println(getString("UPLOAD_SKIN_ONLY_OAS_OR_OFFLINE"));
                        }
                    } else {
                        System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
                        return;
                    }
                }
                break;
                case "c":


                    JSONObject account;
                    try {
                        account = Utils.getSelectedAccount();
                    } catch (NotSelectedException e) {
                        return;
                    }
                    if (account.optInt("loginMethod") == 0) {
                        if ((subOption instanceof ValueArgument)) {

                            String value = ((ValueArgument) subOption).value;
                            if (isEmpty(value)) {
                                account.remove("cape");
                            } else {
                                File file = new File(value);
                                if (!file.exists() || file.isDirectory()) {
                                    System.out.println(getString("FILE_NOT_FOUND_OR_IS_A_DIRECTORY"));
                                    return;
                                }
                                account.put("cape", file.getAbsolutePath());
                            }
                        } else {
                            account.remove("cape");
                        }
                        Utils.saveConfig(jsonObject);
                    } else {
                        System.out.println(getString("ONLY_OFFLINE"));
                    }
                    break;
                default:
                    System.out.println(getString("CONSOLE_UNKNOWN_OPTION", key));
                    break;
            }
        } else if (arguments.optArgument(0) instanceof ValueArgument) {
            String value = ((ValueArgument) arguments.optArgument(0)).value;
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
            account.put("selected", true);
            for (int i = 0; i < accounts.length(); i++) {
                if (i != order) {
                    JSONObject acc = accounts.optJSONObject(i);
                    if (acc != null) {
                        acc.put("selected", false);
                    }
                }
            }
            jsonObject.put("accounts", accounts);
            Utils.saveConfig(jsonObject);
        }
    }

    private void upload(JSONObject account, String fileName, String suffix, byte[] skin, boolean slim) {
        String url = account.optString("url");
        String uuid = account.optString("uuid");
        String accessToken = account.optString("accessToken");
        if (!Utils.isEmpty(url) && !Utils.isEmpty(uuid) && !Utils.isEmpty(accessToken)) {
            try {
                String skinUploadURL = new AuthlibInjectorProvider(url).getSkinUploadURL(uuid);
                HttpURLConnection connection = (HttpURLConnection) new URL(skinUploadURL).openConnection();
                connection.setUseCaches(false);
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Accept-Language", Locale.getDefault().toString());
                connection.setRequestProperty("Authorization", "Bearer " + accessToken);
                connection.setDoOutput(true);
                connection.setRequestProperty("Accept", "*/*");
                String boundary = "~~~~~~~~~~~~~~~~~~~~~~~~~";
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                connection.setDoOutput(true);


                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                byte[] sl = "\r\n".getBytes(UTF_8);
                byteArrayOutputStream.write(("--" + boundary).getBytes(UTF_8));
                byteArrayOutputStream.write(sl);

                byteArrayOutputStream.write("Content-Disposition: form-data; name=\"model\"".getBytes(UTF_8));
                byteArrayOutputStream.write(sl);
                byteArrayOutputStream.write(sl);

                String model = "";
                if (slim) {
                    model = "slim";
                }
                byteArrayOutputStream.write(model.getBytes(UTF_8));
                byteArrayOutputStream.write(sl);

                byteArrayOutputStream.write(("--" + boundary).getBytes(UTF_8));
                byteArrayOutputStream.write(sl);
                byteArrayOutputStream.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"").getBytes(UTF_8));
                byteArrayOutputStream.write(sl);
                byteArrayOutputStream.write(("Content-Type: image" + suffix).getBytes(UTF_8));
                byteArrayOutputStream.write(sl);
                byteArrayOutputStream.write(sl);

                byteArrayOutputStream.write(skin);


                byteArrayOutputStream.write(sl);
                byteArrayOutputStream.write(sl);
                //byteArrayOutputStream.write(("--" + boundary + "--").getBytes(UTF_8));
                connection.setRequestProperty("Content-Length", String.valueOf(byteArrayOutputStream.size()));


                OutputStream outputStream = connection.getOutputStream();

                outputStream.write(byteArrayOutputStream.toByteArray());
                byteArrayOutputStream.close();

                String var = Utils.httpURLConnection2String(connection);
                                    /*System.out.println("---------------------163------------------------");
                                    for(Map.Entry<String, List<String>>entry:connection.getHeaderFields().entrySet()){
                                        System.out.println(entry.getKey()+": "+ Arrays.toString(entry.getValue().toArray()));
                                    }
                                    System.out.println("---------------------163------------------------");*/


                if (!Utils.isEmpty(var) && var.startsWith("{")) {
                    JSONObject result = new JSONObject(var);
                    Utils.printfln(getString("ERROR_WITH_MESSAGE"), result.optString("error"), result.optString("errorMessage"));
                } else {
                    System.out.println(getString("SUCCESSFULLY_SET_SKIN"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Utils.printfln(getString("UNABLE_SET_SKIN"));
            }
        } else {
            System.out.println(getString("CONSOLE_ACCOUNT_UN_OPERABLE_NEED_UUID_AND_URL_AND_TOKEN"));
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

    public static String getAccountType(int loginMethod, String ifOASName, String ifOASURL) {
        if (loginMethod == 2) {
            return getString("ACCOUNT_TYPE_MICROSOFT");
        } else if (loginMethod == 1) {
            return String.format(getString("ACCOUNT_TYPE_OAS"), ifOASName, ifOASURL);
        } else {
            return getString("ACCOUNT_TYPE_OFFLINE");
        }
    }
}
