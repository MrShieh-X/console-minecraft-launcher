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
package com.mrshiehx.cmcl.bean;

import com.mrshiehx.cmcl.utils.Utils;

public class XDate {
    public final int year;
    public final int month;
    public final int day;

    public XDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public String toString() {
        return year + "/" + month + "/" + day;
    }

    public static XDate valueOf(String date) {
        if (Utils.isEmpty(date)) return null;
        String[] dates = date.split("/");
        if (dates == null || dates.length < 3) return null;
        try {
            return new XDate(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]));
        } catch (Throwable ignore) {
        }
        return null;
    }


    /**
     * 比较日期
     *
     * @return 0 first > second;
     * 1 first < second;
     * 2 first = second
     **/
    public static int compareDate(XDate first, XDate second) {
        if (first.year > second.year) return 0;
        if (first.year < second.year) return 1;
        if (first.month > second.month) return 0;
        if (first.month < second.month) return 1;
        if (first.day > second.day) return 0;
        if (first.day < second.day) return 1;
        return 2;
    }
}
