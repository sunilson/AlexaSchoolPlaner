package com.sunilson.bachelorthesis.presentation.application;

import android.app.Application;
import android.content.Context;

import com.sunilson.bachelorthesis.presentation.viewmodelBasics.ViewModelModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Linus Weiss
 */

@Module(includes = ViewModelModule.class)
public class ApplicationModule {

    @Provides
    @Singleton
    Context provideApplicationContext(Application application) {
        return application;
    }

}
