package com.sunilson.bachelorthesis.presentation.navigation;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sunilson.bachelorthesis.presentation.event.EventActivity;
import com.sunilson.bachelorthesis.presentation.homepage.HomepageActivity;

/**
 * @author Linus Weiss
 */

public class Navigator {


    public static void navigateToEvent(Context context, View view) {
        if(context != null) {
            Intent intent = EventActivity.getCallingIntent(context);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation((AppCompatActivity) context);
            context.startActivity(intent, options.toBundle());
        }
    }

    public static void navigateToHomepage(Context context, String fragmentTag) {
        if (context != null) {
            Intent intent = HomepageActivity.getCallingIntent(context, fragmentTag);
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((AppCompatActivity) context).toBundle());
        }
    }
}
