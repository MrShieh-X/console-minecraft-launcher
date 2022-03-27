package com.mrshiehx.cmcl.server;

import com.mrshiehx.cmcl.exceptions.AuthenticationException;
import com.mrshiehx.cmcl.utils.NetworkUtils;
import com.mrshiehx.cmcl.utils.Utils;
import fi.iki.elonen.NanoHTTPD;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.isEmpty;

public class MicrosoftAuthenticationServer extends NanoHTTPD {
    private final int port;
    private final OnGotCode onGotCode;
    private final CompletableFuture<String> future = new CompletableFuture<>();


    public MicrosoftAuthenticationServer(int port, OnGotCode onGotCode) {
        super(port);

        this.port = port;
        this.onGotCode = onGotCode;
    }

    public String getRedirectURI() {
        return ("http%3A%2F%2Flocalhost%3A" + port + "%2Fauthentication-response");
    }

    public String getCode() throws InterruptedException, ExecutionException {
        return future.get();
    }

    @Override
    public NanoHTTPD.Response serve(IHTTPSession session) {
        if (session.getMethod() != Method.GET || !"/authentication-response".equals(session.getUri())) {
            return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_HTML, "");
        }
        String s = session.getQueryParameterString();
        if (isEmpty(s)) {
            return newFixedLengthResponse(Response.Status.BAD_REQUEST, MIME_HTML, "");
        }
        Map<String, String> query = Utils.mapOf(NetworkUtils.parseQuery(s));
        if (query.containsKey("code")) {
            String c = query.get("code");
            if (onGotCode != null) new Thread(() -> onGotCode.onGotCode(c, getRedirectURI())).start();
            future.complete(c);
        } else {
            future.completeExceptionally(new AuthenticationException("failed to authenticate"));
        }

        String html = "<!DOCTYPE html>\n" +
                "<html lang=\"en-US\">\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "    <title>" + getString("WEB_TITLE_LOGIN_MICROSOFT_ACCOUNT_RESPONSE") + "</title>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "    <div>" + getString("ON_AUTHENTICATED_PAGE_TEXT") + "</div>\n" +
                "\n" +
                "    <script>\n" +
                "        setTimeout(function() {open(\"about:blank\",\"_self\").close();}, 10000);\n" +
                "    </script>\n" +
                "</body>\n" +
                "\n" +
                "</html>";
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                stop();
            } catch (InterruptedException ignored) {
            }
        }).start();
        return newFixedLengthResponse(Response.Status.OK, "text/html; charset=UTF-8", html);
    }

    public interface OnGotCode {
        void onGotCode(String code, String redirect_uri);
    }
}