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

package com.mrshiehx.cmcl.bean;

import com.mrshiehx.cmcl.utils.Utils;

import java.io.File;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.librariesDir;

public class SplitLibraryName {
    public final String first;
    public final String second;
    public final String third;
    public final String classifier;
    public final String extension;


    public SplitLibraryName(String first, String second, String third) {
        this(first, second, third, null, ".jar");
    }

    public SplitLibraryName(String first, String second, String third, String classifier, String extension) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.classifier = classifier;
        this.extension = extension;
    }

    public static SplitLibraryName valueOf(String libraryName) {
        if (Utils.isEmpty(libraryName)) return null;
        String extension = ".jar";
        if (libraryName.contains("@")) {
            extension = "." + libraryName.substring(libraryName.indexOf("@") + 1);
            libraryName = libraryName.substring(0, libraryName.indexOf("@"));
        }
        String[] nameSplit = libraryName.split(":");
        if (nameSplit.length < 3) return null;

        return new SplitLibraryName(nameSplit[0], nameSplit[1], nameSplit[2], nameSplit.length >= 4 ? nameSplit[3] : null, extension);
    }

    public String getFileName() {
        return second + "-" + third + (!Utils.isEmpty(classifier) ? ("-" + classifier) : "") + extension;
    }

    public File getPhysicalFile() {
        String libraryFileName = getFileName();
        String libraryFileAndDirectoryName = Utils.getPathFromLibraryName(this);
        return new File(new File(librariesDir, libraryFileAndDirectoryName), libraryFileName);
    }
}
