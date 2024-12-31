package com.blogspot.groglogs.maicar.util;

public class StringUtils {

    public static boolean isBlank(String s){
        return s == null || s.trim().isEmpty();
    }
}
