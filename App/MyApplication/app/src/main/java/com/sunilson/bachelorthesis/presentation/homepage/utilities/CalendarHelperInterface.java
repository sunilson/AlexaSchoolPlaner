package com.sunilson.bachelorthesis.presentation.homepage.utilities;

import com.sunilson.bachelorthesis.presentation.homepage.exception.CalendarSettingsException;

import org.joda.time.DateTime;

import javax.annotation.Nullable;

/**
 * @author Linus Weiss
 */

public interface CalendarHelperInterface {

    void setCurrentCalendarDates(@Nullable DateTime from, @Nullable DateTime to);

    DateTime[] getCurrentCalendarDates() throws CalendarSettingsException;

    DateTime[] convertToBoundDates(int dayAmount)throws CalendarSettingsException;

}
