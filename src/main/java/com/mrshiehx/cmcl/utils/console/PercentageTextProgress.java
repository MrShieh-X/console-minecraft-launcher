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
package com.mrshiehx.cmcl.utils.console;

/**
 * 控制台的百分比进度
 *
 * @author MrShiehX
 **/
public class PercentageTextProgress {
    protected int maximum;
    protected int value;
    public boolean printed;
    public boolean done;

    public PercentageTextProgress() {
    }

    public int getMaximum() {
        return maximum;
    }

    public int getValue() {
        return value;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public void setValue(int value) {
        if (value == this.value) return;
        int before = this.value;
        this.value = value;
        if (printed) {
            for (int j = 0; j < String.valueOf(((int) ((((double) before) / ((double) maximum)) * 100))).length() + 3; j++) {
                System.out.print("\b");
            }
        }
        System.out.print("(" + ((int) ((((double) value) / ((double) maximum)) * 100)) + "%)");
        if (value == maximum) {
            System.out.println();
            done = true;
        }
        printed = true;
    }
}
