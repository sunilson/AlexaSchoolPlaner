package com.sunilson.bachelorthesis.presentation.homepage.dependencyInjection;

import com.sunilson.bachelorthesis.presentation.homepage.HomepageCalendarContainerFragment;
import com.sunilson.bachelorthesis.presentation.homepage.calendar.HomepageFragmentCalendar;

import dagger.Module;
import dagger.Provides;

/**
 * @author Linus Weiss
 */

@Module
public class HomepageFragmentModule {

    @Provides
    HomepageFragmentCalendar provideHomepageFragmentCalendar(HomepageFragmentCalendar homepageFragmentCalendar){
        return homepageFragmentCalendar;
    }

    @Provides
    HomepageCalendarContainerFragment provideHomepageCalendarContainerFragment(HomepageCalendarContainerFragment homepageCalendarContainerFragment){
        return homepageCalendarContainerFragment;
    }

}
