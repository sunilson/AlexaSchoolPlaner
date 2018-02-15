package com.sunilson.bachelorthesis.presentation.homepage.dependencyInjection;

import com.sunilson.bachelorthesis.presentation.homepage.HomepageCalendarContainerFragment;
import com.sunilson.bachelorthesis.presentation.homepage.calendar.HomepageFragmentCalendar;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Linus Weiss
 */

@Module
public abstract class HomepageFragmentBuilder {

    @ContributesAndroidInjector()
    abstract HomepageFragmentCalendar provideHomepageCalendarFragment();

    @ContributesAndroidInjector(modules = HomepageFragmentModule.class)
    abstract HomepageCalendarContainerFragment provideHomepageCalendarContainerFragment();
}
