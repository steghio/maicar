package com.blogspot.groglogs.maicar.util;

import java.text.DecimalFormat;

public class StringUtils {
    private static final DecimalFormat df4 = new DecimalFormat("#.####");
    private static final DecimalFormat df1 = new DecimalFormat("#.#");

    public static String decimal2String4Precision(double d){
        return df4.format(d);
    }

    public static String decimal2String1Precision(double d){
        return df1.format(d);
    }

    public static boolean isBlank(String s){
        return s == null || s.trim().isEmpty();
    }
}
