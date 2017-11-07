package com.sunilson.bachelorthesis.presentation.models.events;

import com.sunilson.bachelorthesis.presentation.models.types.EventType;

import java.util.Date;

/**
 * @author Linus Weiss
 */

public class PeriodEvent extends Event {
    private SchoolSubject schoolSubject;
    private Location location;

    public PeriodEvent(Date from, Date to, String title, SchoolSubject schoolSubject, Location location) {
        super(from, to, title, EventType.PERIOD);

        this.schoolSubject = schoolSubject;
        this.location = location;
    }

}
