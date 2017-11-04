package com.sunilson.bachelorthesis.presentation.models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Linus Weiss
 *
 * Model class of a single calendar day for the Presentation layer
 */

public class CalendarDay {

    private List<Event> eventList = new ArrayList<>();

    public CalendarDay() {

    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }
}
