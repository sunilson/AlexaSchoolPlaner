package com.sunilson.bachelorthesis.presentation.homepage.calendar.calendarDay;

import com.sunilson.bachelorthesis.presentation.event.models.Event;
import com.sunilson.bachelorthesis.presentation.homepage.exception.CalendarDayModelInvalidEventException;
import com.sunilson.bachelorthesis.presentation.shared.utilities.DateUtilities;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Linus Weiss
 *
 * Model class of a single calendar dayStartDate for the Presentation layer
 */
public class CalendarDayModel {

    private List<Event> eventList = new ArrayList<>();
    private DateTime dayStartDate, dayEndDate;

    public CalendarDayModel(DateTime dayStartDate) {
        this.dayStartDate = dayStartDate.withTimeAtStartOfDay();
        this.dayEndDate = this.dayStartDate.withTime(23, 59, 59, 999);
    }

    public void add(Event event) throws CalendarDayModelInvalidEventException {
        if(DateUtilities.isEventOverlappingDay(dayStartDate, event)) {
            eventList.add(event);
        } else {
            throw new CalendarDayModelInvalidEventException("Event is not valid for this day");
        }
    }

    public DateTime getDayEndDate() {
        return dayEndDate;
    }

    public DateTime getDayStartDate() {
        return dayStartDate;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void sort() {
        //TODO sort event list
    }
}
