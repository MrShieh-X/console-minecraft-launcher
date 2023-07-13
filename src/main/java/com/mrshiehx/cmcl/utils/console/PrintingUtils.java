package com.mrshiehx.cmcl.utils.console;

import com.mrshiehx.cmcl.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PrintingUtils {
    public static String oldNormalizeListItemsToText(List<String> list, boolean reserve) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        for (int i = reserve ? (list.size() - 1) : 0; reserve ? (i >= 0) : (i < list.size()); ) {
            String item = list.get(i);
            if (item.contains(" ")) item = "\"" + item + "\"";
            stringBuilder.append(item);//legal
            if (i > 0) {
                stringBuilder.append(", ");
            }
            if (reserve) i--;
            else i++;
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public static String normalizeListItemsToText(List<String> items, boolean reserve, int columnsNumIfFullLineCannot, int separatingSpaceLength, boolean tryFullLine) {
        int windowWidth = 0;
        if (tryFullLine) {
            try (Terminal terminal = TerminalBuilder.terminal()) {
                windowWidth = terminal.getWidth();
                if (windowWidth <= 0) tryFullLine = false;
            } catch (IOException ignore) {
                tryFullLine = false;
            }
        }
        if (!tryFullLine) {
            return normalizeListItemsToText(items, reserve, columnsNumIfFullLineCannot, separatingSpaceLength);
        }
        int i = 1;
        String previous = null;
        String now = null;
        int max = 0;
        while (max <= windowWidth) {
            previous = now;
            now = normalizeListItemsToText(items, reserve, i++, separatingSpaceLength);
            String[] split = now.split("\n");
            if (split.length == 1) {
                return now;
            }
            max = 0;
            for (String a : split) {
                int length;
                if ((length = Utils.stringOccupiedSpacesLength(a)) > max) {
                    max = length;
                }
            }
        }
        return previous != null ? previous : normalizeListItemsToText(items, reserve, columnsNumIfFullLineCannot, separatingSpaceLength);
    }

    public static String normalizeListItemsToText(List<String> items, boolean reserve, int columnsNum, int separatingSpaceLength) {
        int[] maxLength = new int[columnsNum];

        items = items.stream().map(item -> item.contains(" ") ? ("\"" + item + "\"") : item).collect(Collectors.toList());
        if (reserve) Collections.reverse(items);

        for (int i = 0; i < items.size(); i++) {
            int itemLength = Utils.stringOccupiedSpacesLength(items.get(i));
            int columnIndex = i % columnsNum;

            if (maxLength[columnIndex] < itemLength) {
                maxLength[columnIndex] = itemLength;
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            String item = items.get(i);
            stringBuilder.append(item);

            if ((i + 1) % columnsNum != 0) {
                for (int j = 0; j < maxLength[i % columnsNum] - Utils.stringOccupiedSpacesLength(item) + separatingSpaceLength; j++) {
                    stringBuilder.append(" ");
                }
            } else if (i + 1 != items.size()) {
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public static void printListItems(List<String> list, boolean reserved, int columnsNum, int separatingSpaceLength) {
        System.out.println(normalizeListItemsToText(list, reserved, columnsNum, separatingSpaceLength));
    }

    public static void printListItems(List<String> list, boolean reserved, int columnsNum, int separatingSpaceLength, boolean tryFullLine) {
        System.out.println(normalizeListItemsToText(list, reserved, columnsNum, separatingSpaceLength, tryFullLine));
    }

    public static void printTable(@Nullable String[] headers, int @NotNull [] lengthsRatio, boolean splitSymbolForItems, @Nullable String[]... items) {
        System.out.println(TableUtils.toTable(headers, lengthsRatio, splitSymbolForItems, items));
    }
}
