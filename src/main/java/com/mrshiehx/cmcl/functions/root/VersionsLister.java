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

import com.mrshiehx.cmcl.CMCL;
import com.mrshiehx.cmcl.utils.Utils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class VersionsLister {
    public static void execute(@Nullable String dir) {
        File parent = !Utils.isEmpty(dir) ? new File(dir) : CMCL.gameDir;
        File dir2 = !Utils.isEmpty(dir) ? new File(dir, "versions") : CMCL.versionsDir;
        System.out.println(Utils.getString("MESSAGE_BEFORE_LIST_VERSIONS", parent.getAbsolutePath()));
        List<String> list = CMCL.listVersions(dir2);
        Utils.addDoubleQuotationMark(list);
        System.out.println(Arrays.toString(list.toArray()));
    }
}
