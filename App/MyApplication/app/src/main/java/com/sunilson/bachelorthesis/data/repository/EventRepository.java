package com.sunilson.bachelorthesis.data.repository;

import com.sunilson.bachelorthesis.data.model.EventEntity;

import org.joda.time.DateTime;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @author Linus Weiss
 */

public class EventRepository implements com.sunilson.bachelorthesis.domain.repository.EventRepository {
    @Override
    public Flowable<List<EventEntity>> getEventList(DateTime from, DateTime to) {
        return null;
    }

    @Override
    public Flowable<EventEntity> getSingleEvent(String id) {
        return null;
    }
}
