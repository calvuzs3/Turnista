package com.varonesoft.luca.turnista.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by luca on 14/04/16.
 */
public class DateTimeFormatter {

    // Default format String
    private static String DEF_FORMAT = "dd/MM/yyyy";

    /**
     * Return date in specified format.
     *
     * @param millis Date in milliseconds
     * @return String representing date in default format gg/MM/yyyy
     */
    public static String getDate(long millis) {
        return getDate(millis, DEF_FORMAT);
    }

    /**
     * Return date in specified format.
     *
     * @param millis Date in milliseconds
     * @param format Date format
     * @return String representing date in specified format
     */
    public static String getDate(long millis, String format) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return formatter.format(calendar.getTime());
    }
}
