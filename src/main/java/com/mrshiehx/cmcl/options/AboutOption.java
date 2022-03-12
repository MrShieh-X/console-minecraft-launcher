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
import com.mrshiehx.cmcl.utils.Utils;

public class AboutOption implements Option {
    @Override
    public void execute(Arguments arguments) {
        System.out.println("                  **");
        System.out.println("               **    **");
        System.out.println("            **          **");
        System.out.println("         **         ==     **          -------------------------------------------------------------");
        Utils.printfln("      **          ==  =       **        %s", String.format(Utils.getString("DIALOG_ABOUT_DESCRIPTION_1"), ConsoleMinecraftLauncher.CMCL_VERSION));
        Utils.printfln("   **           == + = == ==     **     %s", Utils.getString("DIALOG_ABOUT_DESCRIPTION_2"));
        Utils.printfln("**            =     +    ==         **  %s", Utils.getString("DIALOG_ABOUT_DESCRIPTION_3"));
        Utils.printfln("**  **        =   +  + ==       **  **  %s", Utils.getString("DIALOG_ABOUT_DESCRIPTION_4"));
        Utils.printfln("**      **    =========     **      **  %s", Utils.getString("DIALOG_ABOUT_DESCRIPTION_5"));
        Utils.printfln("**          **          **          **  %s", Constants.COPYRIGHT);
        System.out.println("**              **  **              **");
        Utils.printfln("**       ==       **       ======   **  %s", Utils.getString("DIALOG_ABOUT_DESCRIPTION_6"));
        System.out.println("**     ==  ==     **     == +  =    **     json");
        System.out.println("**   ==  ++  ==   **   ==  ++ =     **        Copyright (c) 2002 JSON.org");
        System.out.println("**     ==  ==     **     == +  =    **");
        System.out.println("**       ==       **       ======   **     nanohttpd");
        System.out.println("**                **                **        Copyright (C) 2012 - 2015 nanohttpd");
        System.out.println("    **            **            **            Licensed under the BSD-3-Clause License.");
        System.out.println("        **        **        **         -------------------------------------------------------------");
        System.out.println("            **    **    **");
        System.out.println("               ** ** **");
        System.out.println("                  **");
        //Utils.printfln(String.format(Utils.getString("DIALOG_ABOUT_DESCRIPTION"), ConsoleMinecraftLauncher.CMCL_VERSION, ConsoleMinecraftLauncher.CMCL_COPYRIGHT));
    }

    @Override
    public String getUsageName() {
        return null;
    }
}
