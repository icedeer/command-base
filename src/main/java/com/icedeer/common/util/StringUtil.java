package com.icedeer.common.util;

public class StringUtil {
    public final static boolean isNullOrEmpty(String value) {
        if (value == null || value.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public final static boolean isNotEmpty(String value) {
        return value != null && value.trim().length() > 0;
    }

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

}
