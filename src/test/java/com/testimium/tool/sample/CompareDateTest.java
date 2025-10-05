package com.testimium.tool.sample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CompareDateTest {
    public static void main(String[] args) {

        String dateStr1 = "15/12/2022 03:33:44:099";
        String dateStr2 = "15/12/22 03:33:44:098";

        try {
            Date date1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss:ms", Locale.ENGLISH)
                    .parse(dateStr1);
            Date date2 = new SimpleDateFormat("dd/MM/yy hh:mm:ss:ms", Locale.ENGLISH)
                    .parse(dateStr2);

            System.out.println(date1);
            System.out.println(date2);

            if (date1.compareTo(date2) > 0) {
                System.out.println("start is after end");
            } else if (date1.compareTo(date2) < 0) {
                System.out.println("start is before end");
            } else if (date1.compareTo(date2) == 0) {
                System.out.println("start is equal to end");
            } else {
                System.out.println("Something weird happened...");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
