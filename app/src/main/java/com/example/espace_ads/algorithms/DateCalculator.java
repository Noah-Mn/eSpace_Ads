package com.example.espace_ads.algorithms;

import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import static java.lang.Math.subtractExact;

import java.time.format.DateTimeFormatter;

public class DateCalculator {

    public int daysBetweenDates(String date1, String date2) {

        return abs(getDays(date1) - getDays(date2));

    }

    private int getDays(String date) {

        date = "10/10/2020";
        int firstSlash = date.indexOf("/");
        int secondSlash = date.indexOf("/", firstSlash + 1);

        int month = Integer.parseInt (date.substring (0, firstSlash));
        int day = Integer.parseInt (date.substring(firstSlash+1, secondSlash));

        int year = parseInt(date.substring(0, 4));
//        int month = parseInt(date.substring(5, 2));
//        int day = parseInt(date.substring(8, 2));
        int ans = 0;
        for (int i = 1900; i < year; ++i) {
            if (isLeap(i)) {
                ans += 366;
            } else {
                ans += 365;
            }
        }
        for (int i = 1; i < month; ++i) {
            switch (i) {
                case 1:
                    ans += 31;
                    break;
                case 2:
                    ans += isLeap(year) ? 29 : 28;
                    break;
                case 3:
                    ans += 31;
                    break;
                case 4:
                    ans += 30;
                    break;
                case 5:
                    ans += 31;
                    break;
                case 6:
                    ans += 30;
                    break;
                case 7:
                    ans += 31;
                    break;
                case 8:
                    ans += 31;
                    break;
                case 9:
                    ans += 30;
                    break;
                case 10:
                    ans += 31;
                    break;
                case 11:
                    ans += 30;
                    break;
                case 12:
                    ans += 31;
                    break;
            }
        }
        return ans += day - 1;
    }

    boolean isLeap(int Y) {
        if (Y % 400 == 0) {
            return true;
        } else if (Y % 100 == 0) {
            return false;
        } else if (Y % 4 == 0) {
            return true;
        } else {
            return false;
        }
    }
}
