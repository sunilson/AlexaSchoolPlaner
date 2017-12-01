package com.sunilson.bachelorthesis.presentation.dependencyInjection;

import com.sunilson.bachelorthesis.presentation.addEvent.AddEventActivity;
import com.sunilson.bachelorthesis.presentation.addEvent.AddEventModule;
import com.sunilson.bachelorthesis.presentation.event.EventActivity;
import com.sunilson.bachelorthesis.presentation.event.EventModule;
import com.sunilson.bachelorthesis.presentation.homepage.HomepageActivity;
import com.sunilson.bachelorthesis.presentation.homepage.dependencyInjection.HomepageModule;
import com.sunilson.bachelorthesis.presentation.homepage.dependencyInjection.HomepageFragmentBuilder;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Linus Weiss
 */

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {HomepageModule.class, HomepageFragmentBuilder.class})
    abstract HomepageActivity bindHomepageActivity();

    @ContributesAndroidInjector(modules = EventModule.class)
    abstract EventActivity bindEventActivity();

    @ContributesAndroidInjector(modules = AddEventModule.class)
    abstract AddEventActivity bindAddEventActivity();

}
