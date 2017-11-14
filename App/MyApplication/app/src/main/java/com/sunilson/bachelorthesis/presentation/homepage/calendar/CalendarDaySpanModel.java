package com.sunilson.bachelorthesis.presentation.homepage.calendar;

import com.sunilson.bachelorthesis.presentation.homepage.calendar.calendarDay.CalendarDayModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Linus Weiss
 */

public class CalendarDaySpanModel {

    private List<CalendarDayModel> dayModels = new ArrayList<>();

    public CalendarDaySpanModel() {

    }

    public void add(CalendarDayModel calendarDayModel) {
        this.dayModels.add(calendarDayModel);
    }

    public List<CalendarDayModel> getDayModels() {
        return this.dayModels;
    }

}
