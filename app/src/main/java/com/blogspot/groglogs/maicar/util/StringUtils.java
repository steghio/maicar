package com.blogspot.groglogs.maicar.util;

import java.text.DecimalFormat;
import java.util.Locale;

public class StringUtils {
    private static final DecimalFormat df1 = new DecimalFormat("#.#");

    public static String decimal2String1Precision(double d){
        return df1.format(d);
    }

    public static boolean isBlank(String s){
        return s == null || s.trim().isEmpty();
    }

    public static String formatIntegerWithThousandSeparator(int i){
        return String.format(Locale.getDefault(), "%,d", i);
    }
}
