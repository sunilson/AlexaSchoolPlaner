package com.sunilson.bachelorthesis.presentation.shared.utilities;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by linus_000 on 05.11.2017.
 */

public class DateUtilities {

    /**
     * Takes a date and subtracts or adds a given time in a given unit
     *
     * @param date Original Date
     * @param amount Amount to add or subtract. Negative to subtract
     * @param timeType Calendar time unit
     * @return Date after add/subtract operation
     */
    public static Date addOrSubtractFromDate(Date date, int amount, int timeType) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(timeType, amount);
        return cal.getTime();
    }

    public static Boolean isEventOverlappingDay(DateTime dayStart, DateTime from, DateTime to) {
        Interval today = new Interval(dayStart, dayStart.plusDays(1).withTimeAtStartOfDay());
        Interval eventInterval = new Interval(from, to);

        if (eventInterval.overlaps(today)) {
            return true;
        } else {
            return false;
        }
    }

}
