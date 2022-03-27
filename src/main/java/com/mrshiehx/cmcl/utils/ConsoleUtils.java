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

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.getString;
import static com.mrshiehx.cmcl.ConsoleMinecraftLauncher.isEmpty;

public class ConsoleUtils {
    public static boolean yesOrNo(String tip) {
        String text = tip + "(Y/N) ";
        return internalYesOrNo(text);
    }

    private static boolean internalYesOrNo(String text) {
        System.out.print(text);//legal
        Scanner scanner = new Scanner(System.in);
        String input;
        try {
            input = scanner.nextLine();
        } catch (NoSuchElementException ignore) {
            return false;
        }
        if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
            return true;
        } else if (input.equalsIgnoreCase("n") || input.equalsIgnoreCase("no")) {
            return false;
        }
        return internalYesOrNo(text);
    }

    public static int inputInt(String tip, int smallest, int biggest) {
        return internalInputInt(tip, "", smallest, biggest);
    }

    private static int internalInputInt(String origin, String text, int smallest, int biggest) {
        System.out.print(text + origin);//legal
        Scanner scanner = new Scanner(System.in);
        String input;
        try {
            input = scanner.nextLine();
        } catch (NoSuchElementException ignore) {
            return Integer.MAX_VALUE;
        }
        int value;
        try {
            value = Integer.parseInt(input);
        } catch (Exception e) {
            return internalInputInt(origin, Utils.getString("CONSOLE_INPUT_INT_WRONG"), smallest, biggest);
        }
        if (value < smallest || value > biggest) {
            return internalInputInt(origin, Utils.getString("CONSOLE_INPUT_INT_WRONG"), smallest, biggest);
        }
        return value;
    }

    public static String inputString(String tip, Collection<String> collection) {
        return internalInputString(tip, tip, collection);
    }

    private static String internalInputString(String text, String origin, Collection<String> collection) {
        System.out.print(text);//legal
        Scanner scanner = new Scanner(System.in);
        String input;
        try {
            input = scanner.nextLine();
        } catch (NoSuchElementException ignore) {
            return null;
        }
        if (collection.contains(input)) return input;

        if (isEmpty(input)) {
            return internalInputString(text, origin, collection);
        }
        return internalInputString(getString("CONSOLE_INPUT_STRING_NOT_FOUND", input) + origin, origin, collection);
    }

}
