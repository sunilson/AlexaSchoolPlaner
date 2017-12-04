package com.sunilson.bachelorthesis.data.repository.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sunilson.bachelorthesis.data.model.EventEntity;
import com.sunilson.bachelorthesis.data.model.user.UserEntity;

import io.reactivex.Flowable;

/**
 * @author Linus Weiss
 */

@Dao
public interface ApplicationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUser(UserEntity userEntity);

    @Update
    void changeAccessToken(UserEntity userEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addEvent(EventEntity eventEntity);

    @Query("SELECT * FROM Users WHERE id = 1")
    Flowable<UserEntity> getCurrentUser();
}
