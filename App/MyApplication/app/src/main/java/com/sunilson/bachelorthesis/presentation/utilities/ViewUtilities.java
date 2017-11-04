package com.sunilson.bachelorthesis.presentation.utilities;

import android.graphics.Color;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by linus_000 on 04.11.2017.
 */

public class ViewUtilities {

    public static int randomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public static int dateToHeight(int rowHeightScaled, long from, long to) {
        long difference = to - from;
        double minuteHeight = (24d * (double) rowHeightScaled) / 1440d;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(difference);

        return (int)( minutes * minuteHeight);
    }

}
