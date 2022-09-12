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

package com.mrshiehx.cmcl.bean.arguments;

import com.mrshiehx.cmcl.utils.Utils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Arguments {
    private final List<Argument> arguments;
    private int size;

    public Arguments(String args, boolean forImmersive, boolean isForCMCL) {
        this(Utils.splitCommand(Utils.clearRedundantSpaces(args)).toArray(new String[0]), forImmersive, isForCMCL);
    }

    public Arguments(String[] args, boolean forImmersive, boolean isForCMCL) {
        this.arguments = new LinkedList<>();
        int length = args.length;
        if (isForCMCL && (length >= 3) &&
                (forImmersive ? ("config".equalsIgnoreCase(args[0]))
                        : ("-config".equalsIgnoreCase(args[0]) || "--config".equalsIgnoreCase(args[0]) ||
                        "/config".equalsIgnoreCase(args[0]))) &&
                !args[1].startsWith("--") &&
                !args[1].startsWith("-") &&
                !args[1].startsWith("/") &&
                !args[2].startsWith("--") &&
                !args[2].startsWith("-") &&
                !args[2].startsWith("/")) {
            arguments.add(new SingleArgument("config"));
            String key = args[1];
            arguments.add(new TextArgument(key.startsWith("\\-") ? key.substring(1) : key));
            String value = args[2];
            arguments.add(new TextArgument(value.startsWith("\\-") ? value.substring(1) : value));
            this.size = arguments.size();
            return;
        }

        if (isForCMCL && (length >= 3) &&
                (forImmersive ? ("vcfg".equalsIgnoreCase(args[0]))
                        : ("-vcfg".equalsIgnoreCase(args[0]) || "--vcfg".equalsIgnoreCase(args[0]) ||
                        "/vcfg".equalsIgnoreCase(args[0]))) &&
                !args[1].startsWith("--") &&
                !args[1].startsWith("-") &&
                !args[1].startsWith("/") &&
                !args[2].startsWith("--") &&
                !args[2].startsWith("-") &&
                !args[2].startsWith("/") /*&&
                (args.length < 4 || (
                        !args[3].startsWith("--") &&
                                !args[3].startsWith("-") &&
                                !args[3].startsWith("/")))*/) {
            String key = args[1];
            arguments.add(new ValueArgument("vcfg", key.startsWith("\\-") ? key.substring(1) : key));
            String value = args[2];
            arguments.add(new TextArgument(value.startsWith("\\-") ? value.substring(1) : value));
            if (args.length >= 4) {
                String value2 = args[3];
                if (!value2.startsWith("--") && !value2.startsWith("-") && !value2.startsWith("/")) {
                    arguments.add(new TextArgument(value2.startsWith("\\-") ? value2.substring(1) : value2));
                }
            }
            this.size = arguments.size();
            return;
        }
        for (int i = 0; i < length; i++) {
            String key = args[i];
            if (forImmersive && i == 0) {
                String next = (args.length > (i + 1)) ? args[i + 1] : null;
                if (!Utils.isEmpty(next) && !next.startsWith("--") && !next.startsWith("-") && !next.startsWith("/")) {
                    arguments.add(new ValueArgument(key, next.startsWith("\\-") ? next.substring(1) : next));
                } else {
                    arguments.add(new SingleArgument(key));
                }
            } else {
                int startLength = -1;
                if (key.startsWith("--")) {//必须先判断“--”，否则如果“--”开头的判定为“-”开头，则最终参数名将会以“-”开头
                    startLength = 2;
                } else if (key.startsWith("-") || key.startsWith("/")) {
                    startLength = 1;
                }
                if (startLength > 0) {
                    if (i + 1 >= length) {
                        arguments.add(new SingleArgument(key.substring(startLength)));
                    } else {
                        String next = args[i + 1];
                        if (!next.startsWith("--") && !next.startsWith("-") && !next.startsWith("/")) {
                            arguments.add(new ValueArgument(key.substring(startLength), next.startsWith("\\-") ? next.substring(1) : next));
                        } else {
                            arguments.add(new SingleArgument(key.substring(startLength)));
                        }
                    }
                } else if (isForCMCL && !forImmersive && i == 0 && (!key.startsWith("--") && !key.startsWith("-") && !key.startsWith("/"))) {
                    arguments.add(new ValueArgument("b", key));
                }
            }
        }
        if (isForCMCL) Utils.removeDuplicate(arguments);
        this.size = arguments.size();
    }

    public int getSize() {
        return size;
    }

    public Arguments removeDuplicate() {
        Utils.removeDuplicate(arguments);
        this.size = arguments.size();
        return this;
    }

    public boolean equals(int number, String target) {
        if (number >= 0 && number < size) {
            Argument s = optArgument(number);
            return s.equals(target);
        }
        return false;
    }

    public Argument opt(int number) {
        if (number >= 0 && number < size) {
            return optArgument(number);
        }
        return null;
    }

    public Argument optArgument(String name) {
        for (Argument argument : arguments) {
            if (argument.equals(name)) return argument;
        }
        return null;
    }

    public List<Argument> optArguments(String name) {
        List<Argument> list = new LinkedList<>();
        for (Argument argument : arguments) {
            if (argument.equals(name))
                list.add(argument);
        }
        return list;
    }

    public String opt(String name) {
        return opt(name, "");
    }

    public String opt(String name, String def) {
        Argument argument = optArgument(name);
        return argument instanceof ValueArgument ? ((ValueArgument) argument).value : def;
    }

    public int optInt(String name) {
        return optInt(name, 0);
    }

    public int optInt(String name, int defaultValue) {
        String s = opt(name, null);
        if (s != null) {
            try {
                return Integer.parseInt(s);
            } catch (Throwable ignore) {
            }
        }
        return defaultValue;
    }

    public boolean optBoolean(String name) {
        return optBoolean(name, false);
    }

    public boolean optBoolean(String name, boolean defaultValue) {
        String s = opt(name, null);
        if (s != null) {
            try {
                return Boolean.parseBoolean(s);
            } catch (Throwable ignore) {
            }
        }
        return defaultValue;
    }

    public double optDouble(String name) {
        return optDouble(name, 0d);
    }

    public double optDouble(String name, double defaultValue) {
        String s = opt(name, null);
        if (s != null) {
            try {
                return Double.parseDouble(s);
            } catch (Throwable ignore) {
            }
        }
        return defaultValue;
    }

    public boolean contains(String target) {
        for (Argument argument : arguments) {
            if (argument.equals(target)) return true;
        }
        return false;
    }

    public boolean contains(Argument target) {
        for (Argument argument : arguments) {
            if (argument.equals(target)) return true;
        }
        return false;
    }

    public Argument optArgument(int i) {
        if (i < size) {
            return arguments.get(i);
        }
        return null;
    }

    public List<Argument> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public String toString() {
        return toString("-");
    }

    public String toString(String argKeyStart) {
        if (Utils.isEmpty(argKeyStart)) argKeyStart = "-";
        final StringBuilder sb = new StringBuilder();
        int size = arguments.size();
        for (int i = 0; i < size; i++) {
            Argument argument = arguments.get(i);
            if (argument instanceof TextArgument) {
                sb.append(argument.key);
            } else if (argument instanceof SingleArgument) {
                sb.append(argKeyStart).append(argument.key);
            } else if (argument instanceof ValueArgument) {
                String value = ((ValueArgument) argument).value;
                if (value.contains(" ")) {
                    value = "\"" + value + "\"";
                }
                sb.append(argKeyStart)
                        .append(argument.key)
                        .append(' ')
                        .append(value);
            }
            if (i + 1 < size) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    public static Arguments valueOf(List<String> strings, boolean forImmersive, boolean isForCMCL) {
        return new Arguments(strings.toArray(new String[0]), forImmersive, isForCMCL);
    }

    public Arguments merge(Arguments arguments) {
        if (arguments == null || arguments.arguments.size() == 0) return this;
        for (Argument argument : arguments.arguments) {
            if (!this.arguments.contains(argument)) {
                this.arguments.add(argument);
            }
        }
        this.size = this.arguments.size();
        return this;
    }
}
