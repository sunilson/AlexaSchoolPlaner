package com.sunilson.bachelorthesis.presentation.homepage.dependencyInjection;

import com.sunilson.bachelorthesis.presentation.homepage.CalendarViewPagerAdapter;
import com.sunilson.bachelorthesis.presentation.homepage.HomepageActivity;
import com.sunilson.bachelorthesis.presentation.homepage.HomepageCalendarContainerFragment;
import com.sunilson.bachelorthesis.presentation.homepage.calendar.HomepageFragmentCalendar;
import com.sunilson.bachelorthesis.presentation.homepage.utilities.HomepageCalendarHelper;

import dagger.Module;
import dagger.Provides;

/**
 * @author Linus Weiss
 */

@Module
public class HomepageFragmentModule {
    @Provides
    public CalendarViewPagerAdapter provideCalendarViewPagerAdapter (HomepageCalendarContainerFragment homepageCalendarContainerFragment, HomepageCalendarHelper homepageCalendarHelper) {
        return new CalendarViewPagerAdapter(homepageCalendarContainerFragment.getChildFragmentManager(), homepageCalendarHelper);
    }
}
