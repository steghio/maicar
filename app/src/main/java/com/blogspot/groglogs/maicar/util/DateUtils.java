package com.blogspot.groglogs.maicar.util;

import java.time.LocalDate;

public class DateUtils {

    public static String getCurrentDateString(){
        return LocalDate.now().toString();
    }

    public static LocalDate getCurrentDate(){
        return LocalDate.now();
    }

    public static LocalDate from(int year, int month, int day){
        return LocalDate.of(year, month, day);
    }

    public static String stringFrom(int year, int month, int day){
        return LocalDate.of(year, month, day).toString();
    }
}
