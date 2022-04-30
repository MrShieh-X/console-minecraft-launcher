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

import java.util.LinkedList;
import java.util.List;

public class Arguments {
    private final List<Argument> arguments;
    public final int size;

    public Arguments(String[] args) {
        this(args, false);
    }

    public Arguments(String[] args, boolean forImmersive) {
        this.arguments = new LinkedList<>();
        int length = args.length;
        if ((length >= 3) &&
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
                if (key.startsWith("--")) {
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
                } else if (!forImmersive && i == 0 && (!key.startsWith("--") && !key.startsWith("-") && !key.startsWith("/"))) {
                    arguments.add(new ValueArgument("b", key));
                }
            }
        }
        Utils.removeDuplicate(arguments);
        this.size = arguments.size();
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

    public int optInt(String name, int defaulT) {
        String s = opt(name, null);
        if (s != null) {
            try {
                return Integer.parseInt(s);
            } catch (Throwable ignore) {
            }
        }
        return defaulT;
    }

    public boolean optBoolean(String name) {
        return optBoolean(name, false);
    }

    public boolean optBoolean(String name, boolean defaulT) {
        String s = opt(name, null);
        if (s != null) {
            try {
                return Boolean.parseBoolean(s);
            } catch (Throwable ignore) {
            }
        }
        return defaulT;
    }

    public double optDouble(String name) {
        return optDouble(name, 0d);
    }

    public double optDouble(String name, double defaulT) {
        String s = opt(name, null);
        if (s != null) {
            try {
                return Double.parseDouble(s);
            } catch (Throwable ignore) {
            }
        }
        return defaulT;
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
}
