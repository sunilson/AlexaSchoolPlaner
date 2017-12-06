package com.sunilson.bachelorthesis.data.repository.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.sunilson.bachelorthesis.data.model.EventEntity;
import com.sunilson.bachelorthesis.data.model.user.UserEntity;

/**
 * @author Linus Weiss
 */

@Database(entities = {UserEntity.class, EventEntity.class}, version = 4)
@TypeConverters({com.sunilson.bachelorthesis.data.repository.database.TypeConverters.class})
public abstract class ApplicationDatabase extends RoomDatabase {
    public abstract ApplicationDao applicationDao();
}
