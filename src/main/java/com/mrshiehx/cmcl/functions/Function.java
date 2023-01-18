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
package com.mrshiehx.cmcl.functions;

import com.mrshiehx.cmcl.bean.arguments.Argument;
import com.mrshiehx.cmcl.bean.arguments.ArgumentRequirement;
import com.mrshiehx.cmcl.bean.arguments.Arguments;

import java.util.List;

import static com.mrshiehx.cmcl.utils.Utils.getString;

public interface Function {
    void execute(Arguments arguments);

    String getUsageName();

    static boolean checkArgs(Arguments arguments, int leastCount, int offsetForOrigin, ArgumentRequirement... argumentRequirements) {
        if (arguments.getSize() >= leastCount) {
            List<Argument> unruly = arguments.exclude(argumentRequirements, offsetForOrigin);
            if (unruly.size() == 1) {
                System.out.println(getString("CONSOLE_ARG_CHECKING_ONE", unruly.get(0).originString));
                return false;
            } else if (unruly.size() > 1) {
                StringBuilder sb = new StringBuilder();
                for (Argument argument : unruly) {
                    sb.append(argument.originString).append('\n');
                }
                System.out.print(getString("CONSOLE_ARG_CHECKING_PLURAL", sb));
                return false;
            }
        }
        return true;
    }
}
