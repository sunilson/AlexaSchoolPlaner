package com.sunilson.bachelorthesis.presentation.homepage;

import com.sunilson.bachelorthesis.presentation.homepage.utilities.CalendarHelperInterface;
import com.sunilson.bachelorthesis.presentation.homepage.utilities.HomepageCalendarHelper;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

/**
 * @author Linus Weiss
 */

@Module
public abstract class HomepageModule {

    @Binds
    @Singleton
    public abstract HomepageActivity provideHomepageActivity(HomepageActivity homepageActivity);

    @Binds
    @Singleton
    public abstract CalendarHelperInterface bindCalendarHelper(HomepageCalendarHelper homepageCalendarHelper);
}
