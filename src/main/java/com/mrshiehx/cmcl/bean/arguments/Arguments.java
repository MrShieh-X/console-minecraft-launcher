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

package com.mrshiehx.cmcl.bean.arguments;

import com.mrshiehx.cmcl.utils.Utils;
import com.mrshiehx.cmcl.utils.console.CommandUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Arguments {
    private final List<Argument> arguments;
    private int size;

    public Arguments(String args, boolean removeDuplicate) {
        this(CommandUtils.splitCommand(CommandUtils.clearRedundantSpaces(args)).toArray(new String[0]), removeDuplicate);
    }


    public Arguments(String[] args, boolean removeDuplicate) {
        this.arguments = new LinkedList<>();
        int length = args.length;
        for (int i = 0; i < length; i++) {
            String item = args[i];
            if (item.startsWith("--") && item.length() > 2) {
                String rear = item.substring(2);
                int index = rear.indexOf('=');
                if (index == -1) {
                    String value = null;
                    if (i + 1 != length) {
                        String next = args[i + 1];
                        if (!next.startsWith("-")) {
                            value = next;
                            i++;
                        }
                    }
                    arguments.add(value != null ? new ValueArgument(item + " " + value, new String[]{item, value}, rear, value) : new SingleArgument(item, new String[]{item}, rear));
                } else {
                    //如果index为0或大于0的处理方法相同
                    String key = rear.substring(0, index);
                    String value = rear.substring(index + 1);
                    arguments.add(new ValueArgument(item, new String[]{item}, key, value));
                }
            } else if (item.startsWith("-") && item.length() > 1) {
                if (item.length() == 2) {
                    String key = item.substring(1);
                    String value = null;
                    if (i + 1 != length) {
                        String next = args[i + 1];
                        if (!next.startsWith("-")) {
                            value = next;
                            i++;
                        }
                    }
                    arguments.add(value != null ? new ValueArgument(item + " " + value, new String[]{item, value}, key, value) : new SingleArgument(item, new String[]{item}, key));
                } else {
                    arguments.add(new ValueArgument(item, new String[]{item}, item.substring(1, 2), item.substring(2)));
                }

            } else {
                arguments.add(new TextArgument(item));
            }
        }
        if (removeDuplicate) Utils.removeDuplicate(arguments);
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
        if (i >= 0 && i < size) {
            return arguments.get(i);
        }
        return null;
    }

    public List<Argument> getArguments() {
        return Collections.unmodifiableList(arguments);
    }

    @Override
    public String toString() {
        return toString("--");
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

    public static Arguments valueOf(List<String> strings, boolean isForCMCL) {
        return new Arguments(strings.toArray(new String[0]), isForCMCL);
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

    /**
     * In order to prevent the user from inputting wrong options,
     * or entering parameters that should not exist,
     * the parameters that do not exist or have a wrong type in <code>arguments</code> will be filtered out,
     * and the original <code>Arguments</code> object is not changed.
     * Will compare the type and the key of the arguments.
     *
     * @return the filtered results, will not be empty
     */
    public List<Argument> exclude(ArgumentRequirement[] arguments, int offsetForOrigin) {
        if (arguments == null || arguments.length == 0) return Collections.unmodifiableList(this.arguments);
        if (this.size == 0) return Collections.emptyList();

        Map<String, List<Class<? extends Argument>>> map = new HashMap<>();
        for (ArgumentRequirement argReq : arguments) {
            if (map.get(argReq.key) != null) {
                map.get(argReq.key).add(argReq.clazz);
            } else {
                LinkedList<Class<? extends Argument>> list = new LinkedList<>();
                list.add(argReq.clazz);
                map.put(argReq.key, list);
            }
        }

        return this.arguments.subList(offsetForOrigin, this.arguments.size()).stream()
                .filter(argument -> !(argument instanceof TextArgument) && (map.get(argument.key) == null || !map.get(argument.key).contains(argument.getClass())))
                .collect(Collectors.toList());
    }
}
