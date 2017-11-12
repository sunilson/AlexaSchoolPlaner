package com.sunilson.bachelorthesis.presentation.homepage.day;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Linus Weiss
 */

@Module
public abstract class HomepageFragmentBuilder {

    @ContributesAndroidInjector(modules = HomepageFragmentModule.class)
    abstract HomepageFragmentCalendar provideHomepageDayFragment();
}
