package com.sunilson.bachelorthesis.presentation.dependencyInjection;

import com.sunilson.bachelorthesis.presentation.addEvent.AddEventActivity;
import com.sunilson.bachelorthesis.presentation.addEvent.AddEventModule;
import com.sunilson.bachelorthesis.presentation.authentication.AuthenticationActivity;
import com.sunilson.bachelorthesis.presentation.authentication.dependencyInjection.AuthenticationFragmentBuilder;
import com.sunilson.bachelorthesis.presentation.authentication.dependencyInjection.AuthenticationModule;
import com.sunilson.bachelorthesis.presentation.event.EventActivity;
import com.sunilson.bachelorthesis.presentation.event.EventModule;
import com.sunilson.bachelorthesis.presentation.homepage.HomepageActivity;
import com.sunilson.bachelorthesis.presentation.homepage.dependencyInjection.HomepageFragmentBuilder;
import com.sunilson.bachelorthesis.presentation.homepage.dependencyInjection.HomepageModule;
import com.sunilson.bachelorthesis.presentation.settings.SettingsActivity;
import com.sunilson.bachelorthesis.presentation.settings.dependencyInjection.SettingsModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Linus Weiss
 *
 * This is a helper from Dagger. It generates Sub-Components for every activity and injects it
 * with the given modules
 */

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {HomepageModule.class, HomepageFragmentBuilder.class})
    abstract HomepageActivity bindHomepageActivity();

    @ContributesAndroidInjector(modules = EventModule.class)
    abstract EventActivity bindEventActivity();

    @ContributesAndroidInjector(modules = AddEventModule.class)
    abstract AddEventActivity bindAddEventActivity();

    @ContributesAndroidInjector(modules = SettingsModule.class)
    abstract SettingsActivity bindSettingsActivity();

    @ContributesAndroidInjector(modules = {AuthenticationModule.class, AuthenticationFragmentBuilder.class})
    abstract AuthenticationActivity bindAuthenticationActivity();

}
