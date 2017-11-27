package com.sunilson.bachelorthesis.presentation.shared.utilities;

import com.sunilson.bachelorthesis.presentation.event.EventType;

/**
 * @author Linus Weiss
 */

public class Constants {

    public final static String FRAGMENT_TAG_DAY = "homepageFragmentDay";

    public final static int SCHOOLAPPOINTMENT = 0;
    public final static int SPECIALAPPOINTMENT = 1;
    public final static int DEADLINE = 2;
    public final static EventType[] EVENT_TYPES = {
            EventType.SCHOOLAPPOINTMENT,
            EventType.SPECIALAPPOINTMENT,
            EventType.DEADLINE
    };
    public final static String INTENT_EVENT_TYPE = "eventType";

    public final static String SharedPrefsCalendarSettings = "calendarSettings";
    public final static String SharedPrefsCalendarSettingsFromDate = "fromDate";
    public final static String SharedPrefsCalendarSettingsToDate = "toDate";
    public final static String SharedPrefsCalendarSettingsDayAmount = "dayAmount";

    public final static int ADD_EVENT_REQUEST = 1;

    public final static String INTENT_EVENT_ID = "id";
}
