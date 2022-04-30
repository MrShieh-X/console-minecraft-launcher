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
import com.mrshiehx.cmcl.bean.arguments.ValueArgument;
import com.mrshiehx.cmcl.utils.Utils;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;

public class SelectOption implements Option {
    @Override
    public void execute(Arguments arguments) {
        Argument argument = arguments.optArgument(0);
        if (!(argument instanceof ValueArgument)) {
            System.out.println(getString("CONSOLE_INCORRECT_USAGE"));
            return;
        }
        String select = ((ValueArgument) argument).value;
        if (Utils.versionExists(select)) {
            Utils.saveConfig(Utils.getConfig().put("selectedVersion", select));
        } else {
            System.out.println(getString("EXCEPTION_VERSION_NOT_FOUND"));
        }
    }

    @Override
    public String getUsageName() {
        return null;
    }
}
