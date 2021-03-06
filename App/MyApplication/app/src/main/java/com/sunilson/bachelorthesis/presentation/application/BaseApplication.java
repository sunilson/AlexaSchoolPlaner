package com.sunilson.bachelorthesis.presentation.application;

import android.app.Activity;
import android.app.Application;


import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * @author Linus Weiss
 *
 * The base of the app
 */

public class BaseApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Override public void onCreate() {
        super.onCreate();
        if(LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        //LeakCanary for memory leak detection
        LeakCanary.install(this);
        this.initializeInjector();
    }

    private void initializeInjector() {
        //Entry point for dependency injection
        DaggerApplicationComponent.builder().application(this).build().inject(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }
}
