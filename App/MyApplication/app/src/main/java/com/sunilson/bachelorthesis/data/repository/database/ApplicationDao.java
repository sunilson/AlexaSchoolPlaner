package com.sunilson.bachelorthesis.data.repository.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sunilson.bachelorthesis.data.model.EventEntity;
import com.sunilson.bachelorthesis.data.model.user.UserEntity;

import java.util.List;

import io.reactivex.Single;

/**
 * @author Linus Weiss
 */

@Dao
public interface ApplicationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUser(UserEntity userEntity);

    @Update
    void updateUser(UserEntity userEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addEvent(EventEntity eventEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addEvents(List<EventEntity> events);

    @Query("SELECT * FROM EVENTS WHERE EVENTS.`from` < :to AND EVENTS.`to` >= :from")
    Single<List<EventEntity>> getEvents(String from, String to);

    @Query("SELECT * FROM Events WHERE id = :id")
    Single<EventEntity> getSingleEvent(String id);

    @Query("SELECT * FROM Users WHERE id = 1")
    Single<UserEntity> getCurrentUser();
}
