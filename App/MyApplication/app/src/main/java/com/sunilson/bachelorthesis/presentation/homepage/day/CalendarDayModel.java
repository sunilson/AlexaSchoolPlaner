package com.sunilson.bachelorthesis.presentation.homepage.day;

import com.sunilson.bachelorthesis.presentation.models.events.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Linus Weiss
 *
 * Model class of a single calendar dayStartDate for the Presentation layer
 */
public class CalendarDayModel {

    private List<Event> eventList = new ArrayList<>();
    private Date dayStartDate;

    public CalendarDayModel() {

    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public Date getDayStartDate() {
        return dayStartDate;
    }

    public void setDayStartDate(Date dayStartDate) {
        this.dayStartDate = dayStartDate;
    }

    public void sort() {
        //TODO sort event list
    }
}
