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
import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.Arguments;
import com.mrshiehx.cmcl.bean.arguments.ValueArgument;
import com.mrshiehx.cmcl.utils.Utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ListOption implements Option {
    @Override
    public void execute(Arguments arguments) {
        Argument f = arguments.optArgument(0);
        List<String> list = ConsoleMinecraftLauncher.listVersions(f instanceof ValueArgument ? new File(((ValueArgument) f).value, "versions") : ConsoleMinecraftLauncher.versionsDir);
        /*for (String s : list) {
            System.out.println(s);
        }*/
        Utils.addDoubleQuotationMark(list);
        System.out.println(Arrays.toString(list.toArray()));
    }

    @Override
    public String getUsageName() {
        return null;
    }
}
