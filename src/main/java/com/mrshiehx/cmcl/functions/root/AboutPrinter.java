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
package com.mrshiehx.cmcl.functions.root;

import com.mrshiehx.cmcl.constants.Constants;

import static com.mrshiehx.cmcl.utils.Utils.getString;

public class AboutPrinter {
    public static void execute() {
        System.out.println();
        System.out.println("                         _____   __  __    _____   _\n" +
                "                        / ____| |  \\/  |  / ____| | |\n" +
                "                       | |      | \\  / | | |      | |\n" +
                "                       | |      | |\\/| | | |      | |\n" +
                "                       | |____  | |  | | | |____  | |____\n" +
                "                        \\_____| |_|  |_|  \\_____| |______|\n" +
                "                                    \n");
        System.out.println("=======================================================================================");
        System.out.println(
                " - " + String.format(getString("MESSAGE_ABOUT_DESCRIPTION_1"), Constants.CMCL_VERSION_NAME) + ": " + getString("MESSAGE_ABOUT_DESCRIPTION_2") + "\n" +
                        " - " + Constants.COPYRIGHT + "\n" +
                        " - " + getString("MESSAGE_ABOUT_DESCRIPTION_4") + Constants.SOURCE_CODE + "\n" +
                        "=======================================================================================\n" +
                        " - " + getString("MESSAGE_ABOUT_DESCRIPTION_MAIN_DEVELOPERS") + "\n" +
                        " --- MrShiehX\n" +
                        " ----- Github: https://github.com/MrShieh-X\n" +
                        " ----- Bilibili: https://space.bilibili.com/323674091\n" +
                        " --- Graetpro-X\n" +
                        " ----- Github: https://github.com/Graetpro\n" +
                        " ----- Bilibili: https://space.bilibili.com/122352984\n" +
                        "=======================================================================================\n" +
                        " - " + getString("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS") + "\n" +
                        " --- yushijinhun...............: " + getString("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS_AUTHLIB_INJECTOR") + "\n" +
                        " --- bangbang93................: " + getString("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS_BMCLAPI") + "\n" +
                        " --- " + getString("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS_MCBBS_NAME") + ": " + getString("MESSAGE_ABOUT_DESCRIPTION_SPECIAL_THANKS_MCBBS") + "\n" +
                        "=======================================================================================\n" +
                        " - " + getString("MESSAGE_ABOUT_DESCRIPTION_DISCLAIMER_TITLE") + "\n" +
                        " --- " + getString("MESSAGE_ABOUT_DESCRIPTION_DISCLAIMER_CONTENT_1") + "\n" +
                        " --- " + getString("MESSAGE_ABOUT_DESCRIPTION_DISCLAIMER_CONTENT_2") + "\n" +
                        "=======================================================================================\n" +
                        " - " + getString("MESSAGE_ABOUT_DESCRIPTION_6") + "\n" +
                        " --- json\n" +
                        " ----- Copyright (c) 2002 JSON.org\n" +
                        " ----- Licensed under the JSON License.\n" +
                        " --- nanohttpd\n" +
                        " ----- Copyright (C) 2012 - 2016 nanohttpd\n" +
                        " ----- Licensed under the BSD-3-Clause License.\n" +
                        " --- jansi\n" +
                        " ----- Copyright (C) 2009-2021 the original author(s).\n" +
                        " ----- Licensed under the Apache-2.0 License.\n" +
                        " --- Constant Pool Scanner\n" +
                        " ----- Copyright 1997-2010 Oracle and/or its affiliates.\n" +
                        " ----- Licensed under the GPL 2 or the CDDL.\n" +
                        " --- JLine\n" +
                        " ----- Copyright (C) 2022 the original author(s).\n" +
                        " ----- Distributed under the BSD-3-Clause License.\n" +
                        " --- Java Native Access (JNA)\n" +
                        " ----- Copyright (c) 2007-2019 Timothy Wall,\n" +
                        "       Wayne Meissner and Matthias Blasing\n" +
                        " ----- Licensed under the LGPL, version 2.1 or later, or (from\n" +
                        "       version 4.0 onward) the Apache License, version 2.0.\n" +
                        "=======================================================================================");
    }
}
