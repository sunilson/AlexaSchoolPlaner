package com.sunilson.bachelorthesis.presentation.homepage.utilities;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.sunilson.bachelorthesis.presentation.homepage.exception.CalendarSettingsException;
import com.sunilson.bachelorthesis.presentation.shared.utilities.Constants;

import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Linus Weiss
 */

@Singleton
public class HomepageCalendarHelper implements CalendarHelperInterface {

    private Application application;

    @Inject
    public HomepageCalendarHelper(Application application) {
        this.application = application;
    }

    @Override
    public void setCurrentCalendarDates(@Nullable DateTime from, @Nullable DateTime to) {
        SharedPreferences sharedPreferences = application.getSharedPreferences(Constants.SharedPrefsCalendarSettings, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (from != null) {
            editor.putLong(Constants.SharedPrefsCalendarSettingsFromDate, from.getMillis());
        }

        if (to != null) {
            editor.putLong(Constants.SharedPrefsCalendarSettingsToDate, to.getMillis());
            Log.d("Linus", to.toString());
        }

        editor.commit();
    }

    @Override
    public DateTime[] getCurrentCalendarDates() throws CalendarSettingsException {
        DateTime[] result = new DateTime[2];
        SharedPreferences sharedPreferences = application.getSharedPreferences(Constants.SharedPrefsCalendarSettings, Context.MODE_PRIVATE);

        Long from = sharedPreferences.getLong(Constants.SharedPrefsCalendarSettingsFromDate, 0);
        Long to = sharedPreferences.getLong(Constants.SharedPrefsCalendarSettingsToDate, 0);

        if (from != 0 && to != 0) {
            result[0] = new DateTime(from);
            result[1] = new DateTime(to);
            return result;
        }

        throw new CalendarSettingsException("No valid dates set!");
    }

    @Override
    public DateTime[] convertToBoundDates(int dayAmount) throws CalendarSettingsException {

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
}
