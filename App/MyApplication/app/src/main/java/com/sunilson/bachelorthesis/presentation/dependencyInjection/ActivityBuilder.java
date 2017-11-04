package com.sunilson.bachelorthesis.presentation.dependencyInjection;

import com.sunilson.bachelorthesis.presentation.homepage.HomepageActivity;
import com.sunilson.bachelorthesis.presentation.homepage.HomepageModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Linus Weiss
 */

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = HomepageModule.class)
    abstract HomepageActivity bindHomepageActivity();

}
