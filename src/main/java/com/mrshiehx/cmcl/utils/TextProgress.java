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
package com.mrshiehx.cmcl.utils;

/**
 * 控制台的普通进度（当前/总共）
 *
 * @author MrShiehX
 **/
public class TextProgress extends PercentageTextProgress {
    public void setValue(int value) {
        if (value == this.value) return;
        int before = this.value;
        this.value = value;
        if (printed) {
            for (int j = 0; j < (before + "/" + maximum).length() + 2; j++) {
                System.out.print("\b");
            }
        }
        System.out.print("(" + value + "/" + maximum + ")");
        if (value == maximum) {
            System.out.println();
            done = true;
        }
        printed = true;
    }
}
