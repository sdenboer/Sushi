package com.sushi.components.utils;

public class StringUtils {

    public static String getFirstLine(String string) {
        return string.substring(0, string.indexOf("\n"));
    }
}
