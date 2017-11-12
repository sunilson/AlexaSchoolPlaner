package com.sunilson.bachelorthesis.presentation.utilities;

import com.sunilson.bachelorthesis.presentation.event.models.EventType;

/**
 * Created by linus_000 on 06.11.2017.
 */

public class Constants {

    public final static String FRAGMENT_TAG_DAY = "homepageFragmentDay";
    public final static EventType[] EVENT_TYPES = {
            EventType.SCHOOLAPPOINTMENT,
            EventType.SPECIALAPPOINTMENT,
            EventType.DEADLINE
    };
    public final static String SharedPrefsCalendarSettings = "calendarSettings";
    public final static String SharedPrefsCalendarSettingsFromDate = "fromDate";
    public final static String SharedPrefsCalendarSettingsToDate = "toDate";
}
