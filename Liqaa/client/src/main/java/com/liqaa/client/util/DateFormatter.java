package com.liqaa.client.util;

import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    public static String formatDate(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a");
        return "on " + dateTime.format(formatter);
    }
}
