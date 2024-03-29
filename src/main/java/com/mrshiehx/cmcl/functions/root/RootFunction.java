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
import com.mrshiehx.cmcl.bean.arguments.*;
import com.mrshiehx.cmcl.functions.Function;
import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.cmcl.version.VersionUtils;

import static com.mrshiehx.cmcl.CMCL.isEmpty;
import static com.mrshiehx.cmcl.utils.Utils.getString;

public class RootFunction implements Function {
    @Override
    public void execute(Arguments arguments) {
        execute(arguments, false);
    }

    public static void execute(Arguments arguments, boolean isImmersive) {
        Argument firstArgument = arguments.optArgument(0);
        String key = firstArgument.key;
        String originString = firstArgument.originString;
        String[] originArray = firstArgument.originArray;
        if (!isImmersive) {
            if (firstArgument instanceof SingleArgument) {
                switch (key) {
                    case "h":
                    case "help":
                        printHelp();
                        break;
                    case "a":
                    case "about":
                        AboutPrinter.execute();
                        break;
                    /*case "i":
                    case "immersive":
                        ImmersiveMode.getIn();
                        break;*/
                    case "c":
                    case "check-for-updates":
                        UpdatesChecker.execute();
                        break;
                    case "l":
                    case "list":
                        VersionsLister.execute(null);
                        break;
                    case "p":
                    case "print": {
                        String selectedVersion = Utils.getConfig().optString("selectedVersion");
                        if (isEmpty(selectedVersion)) {
                            System.out.println(CMCL.getString("MESSAGE_TO_SELECT_VERSION"));
                            return;
                        }
                        LaunchCommands.print(selectedVersion);
                    }
                    break;
                    default:
                        tryToStartVersion(originArray[0]);
                        break;
                }
            } else if (firstArgument instanceof ValueArgument) {
                String value = ((ValueArgument) firstArgument).value;
                switch (key) {
                    case "l":
                    case "list":
                        VersionsLister.execute(value);
                        break;
                    case "p":
                    case "print":
                        LaunchCommands.print(value);
                        break;
                    case "s":
                    case "select":
                        VersionSelector.execute(value);
                        break;
                    case "h":
                    case "help":
                        System.out.println(getString("CONSOLE_HELP_WRONG_WRITE", originString));
                        break;
                    default:
                        tryToStartVersion(originArray[0]);
                        break;
                }
            } else {
                tryToStartVersion(originArray[0]);
            }
        } else {
            if (!(firstArgument instanceof TextArgument)) {
                System.out.println(getString("CONSOLE_IMMERSIVE_WRONG", firstArgument.originString));
                return;
            }
            Argument secondArgument = arguments.optArgument(1);
            switch (key) {
                case "help":
                    printHelp();
                    break;
                case "about":
                    AboutPrinter.execute();
                    break;
                case "immersive":
                    ImmersiveMode.getIn();
                    break;
                case "check-for-updates":
                    UpdatesChecker.execute();
                    break;
                case "list":
                    String dir = null;
                    if (secondArgument != null) {
                        dir = secondArgument.originArray[0];
                    }
                    VersionsLister.execute(dir);
                    break;
                case "print":
                    String versionToPrint = null;
                    if (secondArgument != null) {
                        versionToPrint = secondArgument.originArray[0];
                    }
                    LaunchCommands.print(versionToPrint);
                    break;
                case "select":
                    if (secondArgument == null) {
                        System.out.println(getString("CONSOLE_IMMERSIVE_MISSING_PARAMETER"));
                        return;
                    }
                    VersionSelector.execute(secondArgument.originArray[0]);
                    break;
                case "start":
                    String version = null;
                    if (secondArgument != null) {
                        version = secondArgument.originArray[0];
                    }
                    VersionStarter.execute(version);
                    break;
                default:
                    System.out.println(getString("CONSOLE_IMMERSIVE_NOT_FOUND", key));
                    break;
            }
        }
    }


    private static void tryToStartVersion(String versionName) {
        if (VersionUtils.versionExists(versionName)) {
            VersionStarter.execute(versionName);
        } else {
            System.out.println(getString("CONSOLE_NOT_FOUND_VERSION_OR_OPTION", versionName));
        }
        //如果版本不存在，提示打错命令或者目标版本不存在
    }

    @Override
    public String getUsageName() {
        return null;
    }

    public static void printHelp() {
        System.out.println(CMCL.getHelpDocumentation("ROOT"));
    }
}
