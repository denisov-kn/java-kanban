package model;

import java.time.format.DateTimeFormatter;

public enum DateFormat {

    DATE_TIME_FORMAT("dd.MM.yyyy HH:mm");

    private final DateTimeFormatter formatter;

    DateFormat(String pattern) {
        this.formatter = DateTimeFormatter.ofPattern(pattern);

    }

    public  DateTimeFormatter getFormatter() {
        return formatter;
    }
}
