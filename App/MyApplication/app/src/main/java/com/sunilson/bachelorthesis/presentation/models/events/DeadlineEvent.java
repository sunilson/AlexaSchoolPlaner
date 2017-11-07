package com.sunilson.bachelorthesis.presentation.models.events;

import com.sunilson.bachelorthesis.presentation.models.types.EventType;
import com.sunilson.bachelorthesis.presentation.utilities.DateUtilities;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Linus Weiss
 */

public class DeadlineEvent extends Event {

    private Date deadline;
    private SchoolSubject subject;

    public DeadlineEvent(Date deadline, String title, SchoolSubject schoolSubject) {
        super(DateUtilities.addOrSubtractFromDate(deadline, -1, Calendar.HOUR), deadline, title, EventType.DEADLINE);

        this.deadline = deadline;
        this.subject = schoolSubject;
    }
}
