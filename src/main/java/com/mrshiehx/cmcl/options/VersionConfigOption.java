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
 *
 */

package com.mrshiehx.cmcl.options;

import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.bean.arguments.ValueArgument;
import com.mrshiehx.cmcl.utils.Utils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.versionsDir;

public class VersionConfigOption implements Option {
    @Override
    public void execute(Arguments arguments) {
        Argument subOption = arguments.opt(1);
        if (subOption == null) {
            System.out.println(getString("CONSOLE_GET_USAGE"));
            return;
        }

        Argument firstArgument = arguments.optArgument(0);
        if (!(firstArgument instanceof ValueArgument)) {
            System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
            return;
        }

        String versionName = ((ValueArgument) firstArgument).value;
        if (!Utils.versionExists(versionName)) {
            System.out.println(getString("EXCEPTION_VERSION_NOT_FOUND"));
            return;
        }

        File versionDir = new File(versionsDir, versionName);
        File cfgFile = new File(versionDir, "cmclversion.json");
        JSONObject versionCfg = null;
        if (!cfgFile.exists()) {
            versionCfg = new JSONObject();
        } else {
            try {
                versionCfg = Utils.parseJSONObject(Utils.readFileContent(cfgFile));
            } catch (Throwable ignored) {
            }
            if (versionCfg == null) {
                versionCfg = new JSONObject();
            }
        }

        String key = subOption.key;
        switch (key.toLowerCase()) {
            case "workingdirectory": {
                versionCfg.put("workingDirectory", (subOption instanceof ValueArgument) ? ((ValueArgument) subOption).value : "");
                try {
                    Utils.writeFile(cfgFile, versionCfg.toString(), false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            break;
            default:
                Utils.printfln(getString("CONSOLE_UNKNOWN_OPTION"), key);
                break;
        }

    }

    @Override
    public String getUsageName() {
        return "VERSION_CONFIG";
    }
}
