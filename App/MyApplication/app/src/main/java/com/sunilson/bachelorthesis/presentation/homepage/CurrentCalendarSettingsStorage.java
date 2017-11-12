package com.sunilson.bachelorthesis.presentation.homepage;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.sunilson.bachelorthesis.presentation.homepage.exception.CalendarSettingsException;
import com.sunilson.bachelorthesis.presentation.utilities.Constants;

import org.joda.time.DateTime;

import javax.annotation.Nullable;

/**
 * @Author Linus Weiss
 */

public class CurrentCalendarSettingsStorage {


    public static void setDates(Context context, @Nullable DateTime from, @Nullable DateTime to) throws CalendarSettingsException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SharedPrefsCalendarSettings, Context.MODE_PRIVATE);
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

    public static DateTime[] getDates(Context context) throws CalendarSettingsException {
        DateTime[] result = new DateTime[2];
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SharedPrefsCalendarSettings, Context.MODE_PRIVATE);

        Long from = sharedPreferences.getLong(Constants.SharedPrefsCalendarSettingsFromDate, 0);
        Long to = sharedPreferences.getLong(Constants.SharedPrefsCalendarSettingsToDate, 0);

        if (from != 0 && to != 0) {
            result[0] = new DateTime(from);
            result[1] = new DateTime(to);
            return result;
        }

        throw new CalendarSettingsException("No valid dates set!");
    }
}
