package com.sunilson.bachelorthesis.presentation.navigation;

import android.content.Context;
import android.content.Intent;

import com.sunilson.bachelorthesis.presentation.homepage.HomepageActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by linus_000 on 06.11.2017.
 */

@Singleton
public class Navigator {

    @Inject
    public Navigator() {

    }

    public void navigateToHomepage(Context context, String fragmentTag) {
        if (context != null) {
            Intent intent = HomepageActivity.getCallingIntent(context, fragmentTag);
            context.startActivity(intent);
        }
    }

}
