package com.sunilson.bachelorthesis.presentation.homepage.day;

import dagger.Module;
import dagger.Provides;

/**
 * Created by linus_000 on 12.11.2017.
 */

@Module
public class HomepageFragmentModule {

    @Provides
    HomepageFragmentCalendar provideHomepageFragmentCalendar(HomepageFragmentCalendar homepageFragmentCalendar){
        return homepageFragmentCalendar;
    }

}
