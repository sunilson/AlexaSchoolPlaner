package com.sunilson.bachelorthesis.presentation.models.events;

import com.sunilson.bachelorthesis.presentation.models.types.EventType;

import java.util.Date;

/**
 * @author Linus Weiss
 *
 * Base Model class of a single event for the Presentation layer
 */

public class Event {

    private Date from, to;
    private String title;
    private int width = 1;
    private EventType eventType;

    public Event(Date from, Date to, String title, EventType eventType) {
        this.from = from;
        this.to = to;
        this.title = title;
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
