package org.sd.util;

import java.util.List;
import java.lang.reflect.Field;

public class Table {
    public static void displayUserTable(List<?> objects) {
        if (objects == null || objects.isEmpty()) {
            System.out.println("No records found.");
            return;
        }

        Class<?> clazz = objects.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();

        // Calculate max width for each column
        int[] columnWidths = new int[fields.length];
        String[] headers = new String[fields.length];

        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            headers[i] = fields[i].getName();
            columnWidths[i] = headers[i].length(); // Start with header width
        }

        // Compute max width for each field based on values
        for (Object obj : objects) {
            for (int i = 0; i < fields.length; i++) {
                try {
                    Object value = fields[i].get(obj);
                    String strValue = value != null ? value.toString() : "N/A";
                    if (strValue.length() > columnWidths[i]) {
                        columnWidths[i] = Math.min(strValue.length(), 30); // Optional limit
                    }
                } catch (IllegalAccessException e) {
                    // Ignore
                }
            }
        }

        // Print header separator
        printSeparator(columnWidths);

        // Print header row
        for (int i = 0; i < headers.length; i++) {
            System.out.printf("| %-" + columnWidths[i] + "s ", headers[i]);
        }
        System.out.println("|");

        // Print header separator
        printSeparator(columnWidths);

        // Print rows
        for (Object obj : objects) {
            for (int i = 0; i < fields.length; i++) {
                try {
                    Object value = fields[i].get(obj);
                    String strValue = value != null ? value.toString() : "N/A";
                    strValue = truncateString(strValue, columnWidths[i]);
                    System.out.printf("| %-" + columnWidths[i] + "s ", strValue);
                } catch (IllegalAccessException e) {
                    System.out.printf("| %-" + columnWidths[i] + "s ", "ERR");
                }
            }
            System.out.println("|");
        }

        // Footer
        printSeparator(columnWidths);
        System.out.println("Total: " + objects.size());
    }

    private static void printSeparator(int[] columnWidths) {
        StringBuilder sb = new StringBuilder();
        for (int width : columnWidths) {
            sb.append("+").append("-".repeat(width + 2));
        }
        sb.append("+");
        System.out.println(sb);
    }

    private static String truncateString(String str, int maxLength) {
        if (str == null) return "";
        return str.length() <= maxLength ? str : str.substring(0, maxLength - 3) + "...";
    }
}
