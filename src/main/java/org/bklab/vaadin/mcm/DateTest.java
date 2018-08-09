package org.bklab.vaadin.mcm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTest {
    public static void main(String[] args) {
        System.out.println(DateTest.TimeStamp2Date("1521959402", ""));
    }


    public static String TimeStamp2Date(String timestampString, String formats) {
        if (formats.isEmpty())
            formats = "yyyy-MM-dd HH:mm:ss";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }
}
