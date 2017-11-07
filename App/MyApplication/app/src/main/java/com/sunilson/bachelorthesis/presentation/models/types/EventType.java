package com.sunilson.bachelorthesis.presentation.models.types;

import com.sunilson.bachelorthesis.R;

/**
 * Created by linus_000 on 05.11.2017.
 */

public enum EventType {
    APPOINTMENT(R.color.appointment), DEADLINE(R.color.deadline), PERIOD(R.color.period);

    private int val;

    EventType(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
