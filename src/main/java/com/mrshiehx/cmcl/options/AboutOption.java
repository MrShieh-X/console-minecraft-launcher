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

import com.mrshiehx.cmcl.ConsoleMinecraftLauncher;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.constants.Constants;

import static com.mrshiehx.cmcl.utils.Utils.getString;

public class AboutOption implements Option {
    @Override
    public void execute(Arguments arguments) {
        System.out.println();
        System.out.println("                         _____   __  __    _____   _\n" +
                "                        / ____| |  \\/  |  / ____| | |\n" +
                "                       | |      | \\  / | | |      | |\n" +
                "                       | |      | |\\/| | | |      | |\n" +
                "                       | |____  | |  | | | |____  | |____\n" +
                "                        \\_____| |_|  |_|  \\_____| |______|\n" +
                "                                    \n");
        System.out.println("=====================================================================================");
        System.out.println(
                " - " + String.format(getString("MESSAGE_ABOUT_DESCRIPTION_1"), ConsoleMinecraftLauncher.CMCL_VERSION) + ": " + getString("MESSAGE_ABOUT_DESCRIPTION_2") + "\n" +
                        " - " + Constants.COPYRIGHT + "\n" +
                        " - " + getString("MESSAGE_ABOUT_DESCRIPTION_4") + Constants.SOURCE_CODE + "\n" +
                        "=====================================================================================\n" +
                        " - " + getString("MESSAGE_ABOUT_DESCRIPTION_MAIN_DEVELOPERS") + "\n" +
                        " --- MrShiehX\n" +
                        " ----- Github: https://github.com/MrShieh-X\n" +
                        " ----- Bilibili: https://space.bilibili.com/323674091\n" +
                        " --- Graetpro-X\n" +
                        " ----- Github: https://github.com/Graetpro\n" +
                        " ----- Bilibili: https://space.bilibili.com/122352984\n" +
                        "=====================================================================================\n" +
                        " - " + getString("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS") + "\n" +
                        " --- yushijinhun...............: " + getString("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS_AUTHLIB_INJECTOR") + "\n" +
                        " --- bangbang93................: " + getString("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS_BMCLAPI") + "\n" +
                        " --- " + getString("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS_MCBBS_NAME") + ": " + getString("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS_MCBBS") + "\n" +
                        "=====================================================================================\n" +
                        " - " + getString("MESSAGE_ABOUT_DESCRIPTION_6") + "\n" +
                        " --- json\n" +
                        " ----- Copyright (c) 2002 JSON.org\n" +
                        " --- nanohttpd\n" +
                        " ----- Copyright (C) 2012 - 2015 nanohttpd\n" +
                        " ----- Licensed under the BSD-3-Clause License.\n" +
                        " --- jansi\n" +
                        " ----- Copyright (C) 2009-2021 the original author(s).\n" +
                        " ----- Licensed under the Apache-2.0 License.\n" +
                        " --- Constant Pool Scanner\n" +
                        " ----- Copyright 1997-2010 Oracle and/or its affiliates.\n" +
                        " ----- Licensed under the GPL 2 or the CDDL.\n" +
                        "=====================================================================================");
    }

    @Override
    public String getUsageName() {
        return null;
    }
}
