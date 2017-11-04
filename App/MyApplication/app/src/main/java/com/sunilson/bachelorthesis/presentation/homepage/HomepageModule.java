package com.sunilson.bachelorthesis.presentation.homepage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Linus Weiss
 */

@Module
public class HomepageModule {
    @Singleton
    @Provides
    HomepageActivity provideHomepageActivity(HomepageActivity homepageActivity){
        return homepageActivity;
    }
}
