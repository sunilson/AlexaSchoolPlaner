package com.sunilson.bachelorthesis.presentation.event;

import com.sunilson.bachelorthesis.R;

/**
 * @author Linus Weiss
 */

public enum EventType {
    SCHOOLAPPOINTMENT(R.color.schoolAppointment), SPECIALAPPOINTMENT(R.color.specialAppointment), DEADLINE(R.color.deadline);

    private int val;

    EventType(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
