package com.sunilson.bachelorthesis.presentation.homepage.calendar.calendarDay;

import com.sunilson.bachelorthesis.presentation.event.EventModel;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.Getter;

/**
 * @author Linus Weiss
 *
 *Model class of a single calendar dayStartDate for the Presentation layer
 */
public class CalendarDayModel {

    @Getter
    private List<EventModel> eventList = new ArrayList<>();

    @Getter
    private DateTime dayStartDate;

    @Getter
    private DateTime dayEndDate;

    public CalendarDayModel(LocalDate dayStartDate) {
        this.dayStartDate = dayStartDate.toDateTimeAtStartOfDay();
        this.dayEndDate = this.dayStartDate.withTime(23, 59, 59, 999);
    }

    public void addEvent(EventModel event) {
        eventList.add(event);
    }

    public void sort() {
        Collections.sort(eventList, new Comparator<EventModel>() {
            @Override
            public int compare(EventModel e1, EventModel e2) {
                return e1.getFrom().compareTo(e2.getFrom());
            }
        });
    }
}
