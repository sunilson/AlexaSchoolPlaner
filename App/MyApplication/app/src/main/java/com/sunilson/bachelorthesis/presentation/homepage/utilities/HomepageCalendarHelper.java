package com.sunilson.bachelorthesis.presentation.homepage.utilities;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.sunilson.bachelorthesis.presentation.shared.utilities.Constants;

import org.joda.time.DateTime;
import org.joda.time.Days;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Linus Weiss
 */

@Singleton
public class HomepageCalendarHelper {

    private Application application;

    @Inject
    public HomepageCalendarHelper(Application application) {
        this.application = application;
    }

    public void setCurrentCalendarDates(@Nullable DateTime from, @Nullable DateTime to) {
        SharedPreferences sharedPreferences = application.getSharedPreferences(Constants.SharedPrefsCalendarSettings, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (from != null) {
            editor.putLong(Constants.SharedPrefsCalendarSettingsFromDate, from.withTimeAtStartOfDay().getMillis());
        }

        if (to != null) {
            editor.putLong(Constants.SharedPrefsCalendarSettingsToDate, to.withTimeAtStartOfDay().withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).getMillis());
        }

        editor.commit();
    }

    public DateTime[] getCurrentCalendarDates() {
        DateTime[] result = new DateTime[2];
        SharedPreferences sharedPreferences = application.getSharedPreferences(Constants.SharedPrefsCalendarSettings, Context.MODE_PRIVATE);

        Long from = sharedPreferences.getLong(Constants.SharedPrefsCalendarSettingsFromDate, 0);
        Long to = sharedPreferences.getLong(Constants.SharedPrefsCalendarSettingsToDate, 0);

        if (from != 0 && to != 0) {
            result[0] = new DateTime(from);
            result[1] = new DateTime(to);
            return result;
        }

        result[0] = new DateTime();
        result[1] = result[0].plusDays(2);
        return result;
    }

    public Long[] getCurrentLongDates() {
        DateTime[] dateTimes = getCurrentCalendarDates();
        return new Long[]{
                dateTimes[0].getMillis(),
                dateTimes[1].getMillis()
        };
    }

    public Long[] convertDateTimesToLongDates(DateTime[] dateTimes) {
        return new Long[]{
                dateTimes[0].getMillis(),
                dateTimes[1].getMillis()
        };
    }

    public void setDayAmount(int dayAmount) {
        DateTime[] dateTimes = getCurrentCalendarDates();
        setCurrentCalendarDates(null, dateTimes[0].plusDays(dayAmount));
    }

    public DateTime[] addDayAmountToDateTimes(DateTime[] dateTimes, int dayAmount) {
        return new DateTime[]{
                dateTimes[0].plusDays(dayAmount),
                dateTimes[1].plusDays(dayAmount)
        };
    }

    public void addPeriod() {
        DateTime[] dateTimes = getCurrentCalendarDates();
        int dayAmount = Days.daysBetween(dateTimes[0], dateTimes[1]).getDays();
        setCurrentCalendarDates(dateTimes[0].plusDays(dayAmount + 1), dateTimes[1].plusDays(dayAmount + 1));
    }

    public void subtractPeriod() {
        DateTime[] dateTimes = getCurrentCalendarDates();
        int dayAmount = Days.daysBetween(dateTimes[0], dateTimes[1]).getDays();
        setCurrentCalendarDates(dateTimes[0].minusDays(dayAmount + 1), dateTimes[1].minusDays(dayAmount + 1));
    }

    public DateTime[] addPeriodFromDateTimes(DateTime[] dateTimes) {
        int dayAmount = Days.daysBetween(dateTimes[0], dateTimes[1]).getDays();
        return new DateTime[]{dateTimes[0].plusDays(dayAmount + 1), dateTimes[1].plusDays(dayAmount + 1)};
    }

    public DateTime[] subtractPeriodFromDateTimes(DateTime[] dateTimes) {
        int dayAmount = Days.daysBetween(dateTimes[0], dateTimes[1]).getDays();
        return new DateTime[]{dateTimes[0].minusDays(dayAmount + 1), dateTimes[1].minusDays(dayAmount + 1)};
    }

    /*

    @Override
    public DateTime[] convertToBoundDates(int dayAmount) {

        //Calculate from and to times
        DateTime[] dateTimes = null;
        //Get current times from local storage
        dateTimes = getCurrentCalendarDates();

        //Check if a day amount has been given, otherwise calculate it from the current from and to date
        if (dayAmount != 0) {
            //If day amount has been given, set new to date
            dateTimes[1] = dateTimes[0].plusDays(dayAmount - 1);
            setCurrentCalendarDates(null, dateTimes[1]);
        }

        return dateTimes;
    }

    */
}
