package com.sunilson.bachelorthesis.presentation.shared.utilities;

import android.graphics.Color;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Linus Weiss
 */

public class ViewUtilities {

    public static int randomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public static int dateToHeight(int rowHeightScaled, long from, long to, long[] bounds) {

        if(from < bounds[0]) {
            from = bounds[0];
        }

        if(to > bounds[1]) {
            to = bounds[1];
        }

        long difference = to - from;
        double minuteHeight = (24d * (double) rowHeightScaled) / 1440d;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(difference);

        int result = (int)( minutes * minuteHeight);

        if(result > 0 ){
            return result;
        } else {
            return 0;
        }
    }

}
