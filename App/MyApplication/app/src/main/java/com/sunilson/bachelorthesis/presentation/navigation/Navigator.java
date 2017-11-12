package com.sunilson.bachelorthesis.presentation.navigation;

import android.content.Context;
import android.content.Intent;

import com.sunilson.bachelorthesis.presentation.event.EventActivity;
import com.sunilson.bachelorthesis.presentation.homepage.HomepageActivity;

/**
 * @author Linus Weiss
 */

public class Navigator {


    public static void navigateToEvent(Context context) {
        if(context != null) {
            Intent intent = EventActivity.getCallingIntent(context);
            context.startActivity(intent);
        }
    }

    public static void navigateToHomepage(Context context, String fragmentTag) {
        if (context != null) {
            Intent intent = HomepageActivity.getCallingIntent(context, fragmentTag);
            context.startActivity(intent);
        }
    }
}
