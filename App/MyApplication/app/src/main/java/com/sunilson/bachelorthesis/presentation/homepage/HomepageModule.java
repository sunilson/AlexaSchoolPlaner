package com.sunilson.bachelorthesis.presentation.homepage;

import com.sunilson.bachelorthesis.presentation.homepage.utilities.HomepageCalendarHelper;

import dagger.Module;
import dagger.Provides;

/**
 * @author Linus Weiss
 */

@Module
public class HomepageModule {
    @Provides
    public CalendarViewPagerAdapter provideCalendarViewPagerAdapter (HomepageActivity homepageActivity, HomepageCalendarHelper homepageCalendarHelper) {
        return new CalendarViewPagerAdapter(homepageActivity.getSupportFragmentManager(), homepageCalendarHelper);
    }
}
