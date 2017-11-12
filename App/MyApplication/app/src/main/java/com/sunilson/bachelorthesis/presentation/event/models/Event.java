package com.sunilson.bachelorthesis.presentation.event.models;

import org.joda.time.DateTime;

import javax.annotation.Nullable;

/**
 * @author Linus Weiss
 *
 * Base Model class of a single event for the Presentation layer
 */

public class Event {

    private DateTime from, to;
    private String description;
    private int width = 1;
    private EventType eventType;
    private Location location;

    /**
     *
     * @param from Start Date - required
     * @param to To Date - optional
     * @param description Description of the event
     * @param eventType Type of the event (combined with color code)
     * @param location Location of the event (String or/and geo location)
     */
    public Event(DateTime from, @Nullable DateTime to, String description, EventType eventType, @Nullable  Location location) {
        this.from = from;

        //If to date is null it is just addes as the from date + 1 hour
        if(to != null) {
            this.to = to;
        } else {
            this.to = new DateTime(this.from.getMillis()).plusHours(1);
        }

        this.description = description;
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

    public DateTime getFrom() {
        return from;
    }

    public void setFrom(DateTime from) {
        this.from = from;
    }

    public DateTime getTo() {
        return to;
    }

    public void setTo(DateTime to) {
        this.to = to;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
