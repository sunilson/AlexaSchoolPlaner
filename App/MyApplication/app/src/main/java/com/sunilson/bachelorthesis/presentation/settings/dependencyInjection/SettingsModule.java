package com.sunilson.bachelorthesis.presentation.settings.dependencyInjection;

import com.sunilson.bachelorthesis.presentation.addEvent.AddEventActivity;
import com.sunilson.bachelorthesis.presentation.settings.SettingsActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Linus Weiss
 */

@Module
public class SettingsModule {
    @Singleton
    @Provides
    SettingsActivity provideSettingsActivity(SettingsActivity settingsActivity){
        return settingsActivity;
    }
}
