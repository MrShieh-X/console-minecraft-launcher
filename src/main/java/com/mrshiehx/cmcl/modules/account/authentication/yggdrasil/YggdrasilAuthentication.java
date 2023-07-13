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
package com.mrshiehx.cmcl.modules.account.authentication.yggdrasil;

import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.console.InteractionUtils;
import com.mrshiehx.cmcl.utils.internet.NetworkUtils;
import com.mrshiehx.cmcl.utils.json.JSONUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import static com.mrshiehx.cmcl.CMCL.getString;
import static com.mrshiehx.cmcl.CMCL.isEmpty;
import static java.nio.charset.StandardCharsets.UTF_8;

public class YggdrasilAuthentication {
    public static void uploadSkin(YggdrasilAuthenticationApiProvider apiProvider, String uuid, String accessToken, String fileName, String suffix, byte[] skin, boolean slim) {
        if (Utils.isEmpty(uuid) || Utils.isEmpty(accessToken)) {
            System.out.println(getString("CONSOLE_ACCOUNT_UN_OPERABLE_NEED_UUID_AND_URL_AND_TOKEN"));
            return;
        }
        try {
            String skinUploadURL = apiProvider.getSkinUploadURL(uuid);
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

            String resultString = NetworkUtils.httpURLConnection2String(connection);
            if (!Utils.isEmpty(resultString) && resultString.startsWith("{")) {
                JSONObject result = new JSONObject(resultString);
                Utils.printfln(getString("ERROR_WITH_MESSAGE"), result.optString("error"), result.optString("errorMessage"));
            } else {
                System.out.println(getString("SUCCESSFULLY_SET_SKIN"));
            }
        } catch (ProtocolException | MalformedURLException e) {
            e.printStackTrace();
            Utils.printfln(getString("UNABLE_SET_SKIN"));
        } catch (IOException e) {
            e.printStackTrace();
            if (Utils.getConfig().optBoolean("proxyEnabled"))
                System.err.println(Utils.getString("EXCEPTION_NETWORK_WRONG_PLEASE_CHECK_PROXY"));
            Utils.printfln(getString("UNABLE_SET_SKIN"));
        }

    }

    public static JSONObject selectCharacter(List<JSONObject> availableProfiles) {
        for (int i = 0; i < availableProfiles.size(); i++) {
            JSONObject jsonObject = availableProfiles.get(i);
            System.out.println((i + 1) + "." + jsonObject.optString("name"));
        }
        int number = InteractionUtils.inputInt(Utils.getString("MESSAGE_YGGDRASIL_LOGIN_SELECT_PROFILE", 1, availableProfiles.size()), 1, availableProfiles.size());

        if (number != Integer.MAX_VALUE) {
            return availableProfiles.get(number - 1);
        } else {
            return null;
        }
    }

    public static JSONObject validate(YggdrasilAuthenticationApiProvider provider, String accessToken, String clientToken) throws IOException {
        String validationURL = provider.getValidationURL();
        JSONObject request = new JSONObject().put("accessToken", accessToken).put("clientToken", clientToken);
        String response = NetworkUtils.post(validationURL, request.toString());
        if (isEmpty(response))
            return null;
        return JSONUtils.parseJSONObject(response);
    }

    public static JSONObject refresh(YggdrasilAuthenticationApiProvider provider, String accessToken, String clientToken) throws IOException {
        String refreshmentURL = provider.getRefreshmentURL();
        JSONObject request = new JSONObject().put("accessToken", accessToken).put("clientToken", clientToken);
        return new JSONObject(NetworkUtils.post(refreshmentURL, request.toString()));
    }
}
