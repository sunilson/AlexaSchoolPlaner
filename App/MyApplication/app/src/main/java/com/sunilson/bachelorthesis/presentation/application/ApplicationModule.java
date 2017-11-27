package com.sunilson.bachelorthesis.presentation.application;

import android.app.Application;
import android.content.Context;

import com.sunilson.bachelorthesis.data.repository.Event.EventDataRepository;
import com.sunilson.bachelorthesis.domain.repository.EventRepository;
import com.sunilson.bachelorthesis.presentation.shared.viewmodelBasics.ViewModelModule;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

/**
 * @author Linus Weiss
 */

@Module(includes = ViewModelModule.class)
public abstract class ApplicationModule {

    @Binds
    @Singleton
    abstract Context provideApplicationContext(Application application);

    @Binds
    @Singleton
    abstract EventRepository provideEventRepository(EventDataRepository eventDataRepository);
}
