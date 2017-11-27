package com.sunilson.bachelorthesis.presentation.addEvent;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Linus Weiss
 */

@Module
public class AddEventModule {
    @Singleton
    @Provides
    AddEventActivity provideAddEventActivity(AddEventActivity addEventActivity){
        return addEventActivity;
    }
}
