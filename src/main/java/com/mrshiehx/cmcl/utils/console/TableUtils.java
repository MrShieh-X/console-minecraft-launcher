/*
 * Console Minecraft Launcher
 * Copyright (C) 2021-2024  MrShiehX
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

import com.mrshiehx.cmcl.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TableUtils {
    public static String toTable(@Nullable String[] headersStringArray, int @NotNull [] lengthsRatio, boolean splitSymbolForItems, @Nullable String[]... itemsStringArray) {
        int[] actualMaxLengths = new int[lengthsRatio.length];
        int windowWidth = 71;
        try (Terminal terminal = TerminalBuilder.terminal()) {
            windowWidth = terminal.getWidth();
            if (windowWidth == 0) windowWidth = 71;
        } catch (IOException ignore) {
        }
        int maximumLengthsRatioSum = 0;
        for (int i : lengthsRatio) {
            maximumLengthsRatioSum += i;
        }

        int[] maxLengths = new int[lengthsRatio.length];
        for (int i = 0; i < maxLengths.length; i++) {
            maxLengths[i] = Math.max(((int) (((float) (windowWidth - 1)) * (((float) (lengthsRatio[i])) / (float) maximumLengthsRatioSum))) - 3, 2);//"2" for a full-width character
        }


        TableString[] headers = null;
        TableString[][] items = null;
        if (headersStringArray != null) {
            headers = new TableString[headersStringArray.length];
            for (int i = 0; i < headersStringArray.length; i++) {
                headers[i] = new TableString(headersStringArray[i]);
            }
        }
        if (itemsStringArray != null) {
            items = new TableString[itemsStringArray.length][];
            for (int i = 0; i < itemsStringArray.length; i++) {
                String[] itemStringArray = itemsStringArray[i];
                if (items[i] == null) {
                    items[i] = new TableString[itemStringArray.length];
                }
                for (int j = 0; j < itemStringArray.length; j++) {
                    String value = itemStringArray[j];
                    items[i][j] = new TableString(value);
                }
            }

        }

        for (int i = 0; i < lengthsRatio.length; i++) {
            int maxLength = 0;
            if (headers != null)
                maxLength = headers[i].maxLengthForLines;
            if (items != null) {
                for (TableString[] item : items) {
                    int length = item[i].maxLengthForLines;
                    if (length > maxLength) {
                        maxLength = length;
                    }
                }
            }
            actualMaxLengths[i] = Math.min(maxLength, maxLengths[i]);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('+');
        for (int i = 0; i < lengthsRatio.length; i++) {
            for (int j = 0; j < actualMaxLengths[i] + 2; j++) {
                stringBuilder.append('-');
            }
            stringBuilder.append('+');
        }
        String split = stringBuilder.toString();
        stringBuilder.append('\n');


        if (headers != null) {
            printTableItem(stringBuilder, actualMaxLengths, headers);
            stringBuilder.append(split).append('\n');
        }

        if (items != null) {
            for (int i = 0; i < items.length; i++) {
                TableString[] item = items[i];
                printTableItem(stringBuilder, actualMaxLengths, item);
                if (splitSymbolForItems) {
                    stringBuilder.append(split);
                    if (i + 1 != items.length)
                        stringBuilder.append('\n');
                }
            }
        }
        if (!splitSymbolForItems)
            stringBuilder.append(split);
        return stringBuilder.toString();
    }

    private static void printTableItem(StringBuilder stringBuilder, int[] actualMaxLengths, TableString[] item) {
        stringBuilder.append('|');
        TableString[] rests = new TableString[actualMaxLengths.length];//为什么不用item.length？这是为了避免用户给多了，item的length比header数还大
        boolean shouldPrintNext = false;
        for (int i = 0; i < actualMaxLengths.length; i++) {//为什么不用item.length？这是为了避免用户给多了，item的length比header数还大
            stringBuilder.append(' ');
            int printedLength;
            TableString value = item[i];
            boolean multiLines = value.lines.size() > 1;
            if (multiLines) {
                value = new TableString(value.lines.get(0).toArray(new TableChar[0]));
            }
            int stringOccupiedSpacesLength;
            if ((stringOccupiedSpacesLength = value.length) <= actualMaxLengths[i]) {
                stringBuilder.append(value);
                rests[i] = null;
                printedLength = stringOccupiedSpacesLength;
            } else {
                if (value.tableChars[actualMaxLengths[i] - 1] instanceof TableHalfChar || value.tableChars[actualMaxLengths[i] - 1] instanceof TableFullCharRight) {
                    TableChar[] tableCharsTemp = new TableChar[actualMaxLengths[i]];
                    //for (int j = 0; j < actualMaxLengths[i]; j++) {
                    //    tableCharsTemp[j] = value.tableChars[j];
                    //}
                    if (actualMaxLengths[i] >= 0)
                        System.arraycopy(value.tableChars, 0, tableCharsTemp, 0, actualMaxLengths[i]);

                    stringBuilder.append(new TableString(tableCharsTemp));


                    TableChar[] tableCharsTemp2 = new TableChar[value.length - actualMaxLengths[i]];
                    //for (int j = 0; j < value.length - actualMaxLengths[i]; j++) {
                    //    tableCharsTemp2[j] = value.tableChars[j + actualMaxLengths[i]];
                    //}
                    if (value.length - actualMaxLengths[i] >= 0)
                        System.arraycopy(value.tableChars, actualMaxLengths[i], tableCharsTemp2, 0, value.length - actualMaxLengths[i]);


                    rests[i] = new TableString(tableCharsTemp2);
                    printedLength = actualMaxLengths[i];
                    shouldPrintNext = true;
                } else if (value.tableChars[actualMaxLengths[i] - 1] instanceof TableFullCharLeft) {
                    TableChar[] tableCharsTemp = new TableChar[actualMaxLengths[i] - 1];
                    //for (int j = 0; j < actualMaxLengths[i] - 1; j++) {
                    //    tableCharsTemp[j] = value.tableChars[j];
                    //}
                    if (actualMaxLengths[i] - 1 >= 0)
                        System.arraycopy(value.tableChars, 0, tableCharsTemp, 0, actualMaxLengths[i] - 1);

                    stringBuilder.append(new TableString(tableCharsTemp));

                    TableChar[] tableCharsTemp2 = new TableChar[value.length - (actualMaxLengths[i] - 1)];
                    //for (int j = 0; j < value.length - (actualMaxLengths[i] - 1); j++) {
                    //    tableCharsTemp2[j] = value.tableChars[j + (actualMaxLengths[i] - 1)];
                    //}
                    if (value.length - (actualMaxLengths[i] - 1) >= 0)
                        System.arraycopy(value.tableChars, (actualMaxLengths[i] - 1), tableCharsTemp2, 0, value.length - (actualMaxLengths[i] - 1));


                    rests[i] = new TableString(tableCharsTemp2);
                    printedLength = actualMaxLengths[i] - 1;
                    shouldPrintNext = true;
                } else {
                    //this situation will not happen
                    printedLength = 0;
                }
            }

            if (multiLines) {
                shouldPrintNext = true;
                List<List<TableChar>> list = new LinkedList<>(item[i].lines);
                if (rests[i] == null) {
                    list.remove(0);
                } else {
                    list.set(0, Arrays.asList(rests[i].tableChars));
                }
                rests[i] = new TableString(list);

            }
            if (rests[i] == null)
                rests[i] = new TableString("");//不设为null是因为打印其他value时这个也要读取

            for (int j = 0; j < actualMaxLengths[i] - printedLength + 1; j++) {
                stringBuilder.append(' ');
            }
            stringBuilder.append('|');
        }
        stringBuilder.append('\n');
        if (shouldPrintNext) printTableItem(stringBuilder, actualMaxLengths, rests);
    }

    static class TableString {
        static final TableChar LINE_SEPARATOR = new TableChar('\n') {
        };
        final TableChar[] tableChars;
        final int length;
        final List<List<TableChar>> lines;
        final List<Integer> lengthsForLines;
        final int maxLengthForLines;

        TableString(List<List<TableChar>> lines) {
            List<TableChar> tableCharsList = new LinkedList<>();
            for (int i = 0; i < lines.size(); i++) {
                List<TableChar> line = lines.get(i);
                tableCharsList.addAll(line);
                if (i + 1 != lines.size()) {
                    tableCharsList.add(LINE_SEPARATOR);
                }
            }
            tableChars = tableCharsList.toArray(new TableChar[0]);

            this.length = tableChars.length;
            this.lines = lines;
            this.lengthsForLines = Collections.unmodifiableList(this.lines.stream().map(List::size).collect(Collectors.toList()));
            this.maxLengthForLines = Collections.max(lengthsForLines);
        }

        TableString(TableChar[] tableChars) {
            this.tableChars = tableChars;
            this.length = tableChars.length;

            List<List<TableChar>> lines = new LinkedList<>();
            lines.add(new LinkedList<>());
            for (TableChar tableChar : tableChars) {
                if (tableChar == LINE_SEPARATOR) {
                    lines.add(new LinkedList<>());
                } else {
                    lines.get(lines.size() - 1).add(tableChar);
                }
            }
            this.lines = Collections.unmodifiableList(lines);
            this.lengthsForLines = Collections.unmodifiableList(this.lines.stream().map(List::size).collect(Collectors.toList()));
            this.maxLengthForLines = Collections.max(lengthsForLines);
        }

        TableString(String string) {
            string = String.valueOf(string).replace("\r\n", "\n").replace('\r', '\n');//此二者顺序不可变
            char[] charArray = string.toCharArray();
            List<TableChar> list = new LinkedList<>();
            for (char c : charArray) {
                if (c == '\n') {
                    list.add(LINE_SEPARATOR);
                } else {
                    if (Utils.isFullWidth(c)) {
                        list.add(new TableFullCharLeft(c));
                        list.add(new TableFullCharRight(c));
                    } else {
                        list.add(new TableHalfChar(c));
                    }
                }
            }
            tableChars = list.toArray(new TableChar[0]);
            length = list.size();


            List<List<TableChar>> lines = new LinkedList<>();
            lines.add(new LinkedList<>());
            for (TableChar tableChar : tableChars) {
                if (tableChar == LINE_SEPARATOR) {
                    lines.add(new LinkedList<>());
                } else {
                    lines.get(lines.size() - 1).add(tableChar);
                }
            }
            this.lines = Collections.unmodifiableList(lines);
            this.lengthsForLines = Collections.unmodifiableList(this.lines.stream().map(List::size).collect(Collectors.toList()));
            this.maxLengthForLines = Collections.max(lengthsForLines);
        }

        @Override
        public String toString() {
            List<Character> list = new LinkedList<>();
            for (int i = 0; i < tableChars.length; i++) {
                TableChar tableChar = tableChars[i];
                if (tableChar instanceof TableHalfChar || tableChar == LINE_SEPARATOR) {
                    list.add(tableChar.c);
                } else if (tableChar instanceof TableFullCharLeft) {
                    list.add(tableChar.c);
                    i++;
                } else if (tableChar instanceof TableFullCharRight) {
                    //ignore
                }
            }
            char[] chars = new char[list.size()];
            for (int i = 0; i < list.size(); i++) {
                chars[i] = list.get(i);
            }
            return new String(chars);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TableString that = (TableString) o;
            return Arrays.equals(tableChars, that.tableChars);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(tableChars);
        }
    }

    static abstract class TableChar {
        final char c;

        TableChar(char c) {
            this.c = c;
        }

        @Override
        public String toString() {
            return String.valueOf(c);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TableChar tableChar = (TableChar) o;
            return c == tableChar.c;
        }

        @Override
        public int hashCode() {
            return Objects.hash(c);
        }
    }

    static class TableHalfChar extends TableChar {
        TableHalfChar(char c) {
            super(c);
        }
    }

    static class TableFullCharLeft extends TableChar {
        TableFullCharLeft(char c) {
            super(c);
        }
    }

    static class TableFullCharRight extends TableChar {
        TableFullCharRight(char c) {
            super(c);
        }
    }
}
