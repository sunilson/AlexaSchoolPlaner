package com.sunilson.bachelorthesis.presentation.models.events;

import com.sunilson.bachelorthesis.presentation.models.types.EventType;

import java.util.Date;

/**
 * @author Linus Weiss
 */

public class AppointmentEvent extends Event {
    public AppointmentEvent(Date from, Date to, String title, EventType eventType) {
        super(from, to, title, eventType);
    }
}
