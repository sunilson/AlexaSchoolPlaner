package com.sunilson.bachelorthesis.presentation.models.events;

import com.sunilson.bachelorthesis.R;

/**
 * Created by linus_000 on 05.11.2017.
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
