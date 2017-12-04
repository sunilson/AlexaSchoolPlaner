package com.sunilson.bachelorthesis.data.dependencyInjection;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.sunilson.bachelorthesis.data.repository.database.ApplicationDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Linus Weiss
 */

@Module
public class LocalStorageModule {

    @Provides
    @Singleton
    ApplicationDatabase provideRoomDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), ApplicationDatabase.class, "application_db").fallbackToDestructiveMigration().build();
    }

}
