package com.openclassrooms.mddapi.utils;

import java.time.format.DateTimeFormatter;

public class DateFormatter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public static DateTimeFormatter getFormatter() {
        return FORMATTER;
    }
}

