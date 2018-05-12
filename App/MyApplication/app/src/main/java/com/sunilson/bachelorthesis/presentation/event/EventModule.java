package com.sunilson.bachelorthesis.presentation.event;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Linus Weiss
 */

@Module
public class EventModule {
    @Singleton
    @Provides
    EventActivity provideEventActivity(EventActivity eventActivity){
        return eventActivity;
    }
}