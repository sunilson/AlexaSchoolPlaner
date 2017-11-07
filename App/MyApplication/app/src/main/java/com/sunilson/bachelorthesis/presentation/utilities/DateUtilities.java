package com.sunilson.bachelorthesis.presentation.utilities;

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

}
