package com.blogspot.groglogs.maicar.util;

import java.time.LocalDate;

public class DateUtils {

    public static LocalDate fromString(String s){
        String[] split = s.split("-");
        return LocalDate.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
    }

    public static String stringFrom(int year, int month, int day){
        return LocalDate.of(year, month, day).toString();
    }
}
