package com.sunilson.bachelorthesis.presentation.shared.utilities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.sunilson.bachelorthesis.presentation.addEvent.AddEventActivity;
import com.sunilson.bachelorthesis.presentation.authentication.AuthenticationActivity;
import com.sunilson.bachelorthesis.presentation.event.EventActivity;
import com.sunilson.bachelorthesis.presentation.homepage.HomepageActivity;
import com.sunilson.bachelorthesis.presentation.shared.baseClasses.BaseActivity;

/**
 * @author Linus Weiss
 */

public class Navigator {


    public static void navigateToLogin(AppCompatActivity context) {
        if(context != null) {
            Intent intent = AuthenticationActivity.getCallingIntent(context);
            context.startActivity(intent);
            context.finish();
        }
    }

    public static void navigateToHomepage(Context context) {
        if(context != null) {
            Intent intent = HomepageActivity.getCallingIntent(context);
            context.startActivity(intent);
        }
    }

    public static void navigateToEvent(Context context, String id, int eventColor) {
        if(context != null) {
            Intent intent = EventActivity.getCallingIntent(context, eventColor);
            intent.putExtra(Constants.INTENT_EVENT_ID, id);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation((AppCompatActivity) context);
            context.startActivity(intent, options.toBundle());
        }
    }

    public static void navigateToAddEvent(BaseActivity activity, int eventType) {
        if(activity != null) {
            Intent intent = AddEventActivity.getCallingIntent(activity, eventType);
            activity.startActivityForResult(intent, Constants.ADD_EVENT_REQUEST);
        }
    }
}
