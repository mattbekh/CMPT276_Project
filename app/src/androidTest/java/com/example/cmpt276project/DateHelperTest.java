package com.example.cmpt276project;

import android.content.res.Resources;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.cmpt276project.model.DataUpdateChecker;
import com.example.cmpt276project.model.DateHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the functionality of the DateHelper class
 */
@RunWith(AndroidJUnit4.class)
public class DateHelperTest {

    @Test
    public void shouldGetNumberOfDaysAgoForDateWithinThirtyDays() {
        int numDaysAgo = 10;
        GregorianCalendar pastCalendar = numDaysAgoCalendar(numDaysAgo);
        Resources res = App.resources();
        String daysAgoText = res.getString(R.string.Inspection_days_ago_text);
        String correctResult = String.format("%d %s", numDaysAgo, daysAgoText);
        String actualResult = DateHelper.getSmartDate(pastCalendar);

        assertEquals(correctResult, actualResult);
    }

    @Test
    public void shouldGetMonthAndDayForDateWithinOneYear() {
        GregorianCalendar pastCalendar = numDaysAgoCalendar(50);
        String month = DateHelper.getMonthString(pastCalendar.get(Calendar.MONTH));
        int day = pastCalendar.get(Calendar.DAY_OF_MONTH);
        String correctResult = String.format("%s %d", month, day);
        String actualResult = DateHelper.getSmartDate(pastCalendar);

        assertEquals(correctResult, actualResult);
    }

    @Test
    public void shouldGetMonthAndYearForDateWithinOneYear() {
        GregorianCalendar pastCalendar = numDaysAgoCalendar(400);
        String month = DateHelper.getMonthString(pastCalendar.get(Calendar.MONTH));
        int year = pastCalendar.get(Calendar.YEAR);
        String correctResult = String.format("%s %d", month, year);
        String actualResult = DateHelper.getSmartDate(pastCalendar);

        assertEquals(correctResult, actualResult);
    }

    @Test
    public void shouldBeAbleToCreateDateFromString() {
        String dateString = "20181029";
        GregorianCalendar date = DateHelper.getDateFromString(dateString);
        assertEquals(2018, date.get(Calendar.YEAR));
        assertEquals(9, date.get(Calendar.MONTH));
        assertEquals(29, date.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    public void shouldBeAbleToGetFullDateString() {
        String dateString = "20181029";
        GregorianCalendar date = DateHelper.getDateFromString(dateString);
        String fullDate = DateHelper.getFullDate(date);
        String expectedFullDate = "October 29, 2018";
        assertEquals(expectedFullDate, fullDate);
    }

    @Test
    public void ShouldBeAbleToTellIfUpdateWasLessThanTwentyHoursAgo() {
        String timeString = DateHelper.getTimeString(numHoursAgoCalendar(19));
        assertFalse(DateHelper.isTwentyHoursSince(timeString));
    }

    @Test
    public void ShouldBeAbleToTellIfUpdateWasMoreThanTwentyHoursAgo() {
        String timeString = DateHelper.getTimeString(numHoursAgoCalendar(20));
        assertTrue(DateHelper.isTwentyHoursSince(timeString));
    }

    private GregorianCalendar numHoursAgoCalendar(long numHours) {
        final long MILLISECONDS_PER_HOUR = 1000 * 60 * 60;
        return pastCalendar(numHours, MILLISECONDS_PER_HOUR);
    }

    private GregorianCalendar numDaysAgoCalendar(long numDays) {
        final long MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;
        return pastCalendar(numDays, MILLISECONDS_PER_DAY);
    }

    private GregorianCalendar pastCalendar(long amount, long unit) {
        long timeNumDaysAgo = new Date().getTime() - amount * unit;
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(timeNumDaysAgo));
        return calendar;
    }
}
