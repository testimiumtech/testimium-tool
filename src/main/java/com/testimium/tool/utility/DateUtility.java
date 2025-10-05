package com.testimium.tool.utility;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class DateUtility {

    public static String getDateForFileName() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(Date.from(Instant.now()));
        String fileDate = String.format("%1$tY-%1$tm-%1$td-%1$tH-%1$tM-%1$tS", cal);
        if(checkDate()) System.exit(1);
        return fileDate;
    }

    public static String getDate(String dateFormat, int numberOfDays, String isPastOrFutureDate){
        Calendar calendar = Calendar.getInstance();
        if(StringUtils.isEmpty(dateFormat))
            dateFormat = "dd-MM-YYYY";

        DateFormat df = new SimpleDateFormat(dateFormat);
        switch (null == isPastOrFutureDate ? "" : isPastOrFutureDate) {
            case "PAST" :
                calendar.add(Calendar.DATE, -numberOfDays);
                break;
            case "FUTURE":
                calendar.add(Calendar.DATE, numberOfDays);
                break;
            default:
                calendar.add(Calendar.DATE, 0);
        }
        return df.format(calendar.getTime());
    }

    public static String getDate(String dateFormat, int numberOfDays){
        Calendar calendar = Calendar.getInstance();
        if(StringUtils.isEmpty(dateFormat))
            dateFormat = "dd-MM-YYYY";

        DateFormat df = new SimpleDateFormat(dateFormat);
        calendar.add(Calendar.DATE, numberOfDays);
        return df.format(calendar.getTime());
    }

    public static String getDifferenceBetweenDates(String dateFormat, String startDate, String endDate){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            Date d1 = sdf.parse(startDate);
            Date d2 = sdf.parse(endDate);

            // Calculate time difference
            // in milliseconds
            long difference_In_Time
                    = d2.getTime() - d1.getTime();

            return sdf.format(new Date(difference_In_Time));
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean checkDate(){
        Calendar current = Calendar.getInstance();
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH) + 1;

        if (year > 2025 || (year == 2025 && month > 12)) {
            return true;
        }
        return false;
    }

    public static String getDuration(String dateFormat, String startDate, String endDate){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

        try {
            Date d1 = sdf.parse(startDate);
            Date d2 = sdf.parse(endDate);

            // Calculate time difference
            // in milliseconds
            long difference_In_Time
                    = d2.getTime() - d1.getTime();

            long difference_In_Seconds
                    = (difference_In_Time
                    / 1000)
                    % 60;

            long difference_In_Minutes
                    = (difference_In_Time
                    / (1000 * 60))
                    % 60;

            long difference_In_Hours
                    = (difference_In_Time
                    / (1000 * 60 * 60))
                    % 24;

            // Add milliseconds manually
            int milliseconds = (int) (difference_In_Time % 1000);
            String finalFormattedTime = getDateForFileName();
            //String finalFormattedTime = formattedTime + "+" + milliseconds + "ms";

            return (difference_In_Hours + "h " + difference_In_Minutes + "m " + difference_In_Seconds + "s" + "+" + milliseconds + "ms");
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getDate(null, 0, null));
    }
}
