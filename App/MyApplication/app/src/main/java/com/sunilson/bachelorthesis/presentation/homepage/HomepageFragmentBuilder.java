package com.sunilson.bachelorthesis.presentation.homepage;

import com.sunilson.bachelorthesis.presentation.homepage.calendar.HomepageFragmentCalendar;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Linus Weiss
 */

@Module
public abstract class HomepageFragmentBuilder {

    @ContributesAndroidInjector(modules = HomepageFragmentModule.class)
    abstract HomepageFragmentCalendar provideHomepageCalendarFragment();
}