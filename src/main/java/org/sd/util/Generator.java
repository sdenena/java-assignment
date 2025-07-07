package org.sd.util;

import java.text.DecimalFormat;

public class Generator {
    // Format: ACC-0000000001
    private static final DecimalFormat formatter = new DecimalFormat("0000000000");

    public static String generateAccountNumber(Long number) {
        return "ACC-" + formatter.format(number);
    }
}
