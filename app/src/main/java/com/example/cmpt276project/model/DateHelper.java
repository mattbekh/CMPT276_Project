package com.example.cmpt276project.model;

import android.content.res.Resources;

import com.example.cmpt276project.App;
import com.example.cmpt276project.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This class helps with the conversion of GregorianCalendar dates to readable Strings, as well as
 * the construction of a GregorianCalendar date from a String.
 */
public class DateHelper {

    public static String getSmartDate(GregorianCalendar date) {
        final long ONE_DAY = 24 * 60 * 60 * 1000;
        final long THIRTY_DAYS = 30 * ONE_DAY;
        final long ONE_YEAR = 365 * ONE_DAY;
        long timeToday = new Date().getTime();
        long timeDifference = timeToday - date.getTimeInMillis();

        if (timeDifference > ONE_YEAR) {
            int year = date.get(Calendar.YEAR);
            String month = DateHelper.getMonthString(date.get(Calendar.MONTH));
            return String.format("%s %d", month, year);
        } else if (timeDifference > THIRTY_DAYS) {
            int day = date.get(Calendar.DAY_OF_MONTH);
            String month = DateHelper.getMonthString(date.get(Calendar.MONTH));
            return String.format("%s %d", month, day);
        } else {
            long numDaysAgo = timeDifference / ONE_DAY;
            String daysAgoText = App.resources().getString(R.string.Inspection_days_ago_text);
            return String.format("%d %s", numDaysAgo, daysAgoText);
        }
    }

    public static String getFullDate(GregorianCalendar date) {
        int year = date.get(Calendar.YEAR);
        int day = date.get(Calendar.DAY_OF_MONTH);
        String month = getMonthString(date.get(Calendar.MONTH));
        return String.format("%s %d, %d", month, day, year);
    }

    public static String getTimeString(GregorianCalendar date) {
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1;
        int day = date.get(Calendar.DAY_OF_MONTH);
        int hour = date.get(Calendar.HOUR_OF_DAY);
        int minute = date.get(Calendar.MINUTE);
        int second = date.get(Calendar.SECOND);
        return String.format("%04d%02d%02d%02d%02d%02d", year, month, day, hour, minute, second);
    }

    public static GregorianCalendar getDateFromString(String dateString) {
        if (dateString.length() != 8) {
            String errorMessage = String.format("Invalid date string [%s]", dateString);
            throw new IllegalArgumentException(errorMessage);
        }

        int year = Integer.parseInt(dateString.substring(0,4));
        int month = Integer.parseInt(dateString.substring(4,6)) - 1;
        int day = Integer.parseInt(dateString.substring(6,8));

        return new GregorianCalendar(year, month, day);
    }

    public static boolean isTwentyHoursSince(String timeString) {
        final long TWENTY_HOURS = 1000 * 60 * 60 * 20;
        GregorianCalendar lastUpdateDate = DateHelper.getDateFromTimeString(timeString);
        GregorianCalendar currentDate = new GregorianCalendar();
        long timeSinceUpdate = currentDate.getTimeInMillis() - lastUpdateDate.getTimeInMillis();

        return timeSinceUpdate > TWENTY_HOURS;
    }

    public static GregorianCalendar getDateFromTimeString(String timeString) {
        if (timeString.length() != 14) {
            String errorMessage = String.format("Invalid time string [%s]", timeString);
            throw new IllegalArgumentException(errorMessage);
        }

        String dateString = timeString.substring(0,8);
        int hours = Integer.parseInt(timeString.substring(8,10));
        int minutes = Integer.parseInt(timeString.substring(10,12));
        int seconds = Integer.parseInt(timeString.substring(12,14));

        GregorianCalendar calendar = getDateFromString(dateString);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);

        return calendar;
    }

    public static String getMonthString(int month) {
        Resources res = App.resources();
        switch (month) {
            case Calendar.JANUARY:
                return res.getString(R.string.Inspection_date_january);
            case Calendar.FEBRUARY:
                return res.getString(R.string.Inspection_date_february);
            case Calendar.MARCH:
                return res.getString(R.string.Inspection_date_march);
            case Calendar.APRIL:
                return res.getString(R.string.Inspection_date_april);
            case Calendar.MAY:
                return res.getString(R.string.Inspection_date_may);
            case Calendar.JUNE:
                return res.getString(R.string.Inspection_date_june);
            case Calendar.JULY:
                return res.getString(R.string.Inspection_date_july);
            case Calendar.AUGUST:
                return res.getString(R.string.Inspection_date_august);
            case Calendar.SEPTEMBER:
                return res.getString(R.string.Inspection_date_september);
            case Calendar.OCTOBER:
                return res.getString(R.string.Inspection_date_october);
            case Calendar.NOVEMBER:
                return res.getString(R.string.Inspection_date_november);
            case Calendar.DECEMBER:
                return res.getString(R.string.Inspection_date_december);
            default:
                throw new IllegalArgumentException("Invalid month");
        }
    }
}
