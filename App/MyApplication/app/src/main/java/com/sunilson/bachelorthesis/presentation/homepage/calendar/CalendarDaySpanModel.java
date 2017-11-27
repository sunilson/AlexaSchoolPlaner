package com.sunilson.bachelorthesis.presentation.homepage.calendar;

import com.sunilson.bachelorthesis.presentation.homepage.calendar.calendarDay.CalendarDayModel;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Linus Weiss
 */

@NoArgsConstructor
public class CalendarDaySpanModel {

    @Getter
    private List<CalendarDayModel> dayModels = new ArrayList<>();

    public void add(CalendarDayModel calendarDayModel) {
        this.dayModels.add(calendarDayModel);
    }

}
