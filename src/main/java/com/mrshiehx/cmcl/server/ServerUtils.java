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

package com.mrshiehx.cmcl.server;

import com.mrshiehx.cmcl.constants.Constants;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONArray;
import org.json.JSONObject;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class ServerUtils {
    public static String errorHtml(String error) {
        return "\n" +
                "<html>\n" +
                "<head><title>" + error + " - CMCL " + Constants.CMCL_VERSION_NAME + "</title></head>\n" +
                "<body>\n" +
                "<center><h1>" + error + "</h1></center>\n" +
                "<hr><center>CMCL " + Constants.CMCL_VERSION_NAME + "</center>\n" +
                "</body>\n" +
                "</html>";
    }

    protected static NanoHTTPD.Response ok(JSONObject response) {
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/json", response.toString(2));
    }

    protected static NanoHTTPD.Response ok(JSONArray response) {
        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/json", response.toString(2));
    }

    protected static NanoHTTPD.Response notFound() {
        return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, NanoHTTPD.MIME_HTML, errorHtml("404 Not Found"));
    }

    protected static NanoHTTPD.Response noContent() {
        return newFixedLengthResponse(NanoHTTPD.Response.Status.NO_CONTENT, NanoHTTPD.MIME_HTML, "");
    }

    protected static NanoHTTPD.Response badRequest() {
        return newFixedLengthResponse(NanoHTTPD.Response.Status.BAD_REQUEST, NanoHTTPD.MIME_HTML, errorHtml("400 Bad Request"));
    }

    protected static NanoHTTPD.Response internalError() {
        return newFixedLengthResponse(NanoHTTPD.Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_HTML, errorHtml("500 internal error"));
    }
}
