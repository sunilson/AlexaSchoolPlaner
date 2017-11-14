package com.sunilson.bachelorthesis.domain.repository;

import com.sunilson.bachelorthesis.data.model.EventEntity;

import org.joda.time.DateTime;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by linus_000 on 13.11.2017.
 */

public interface EventRepository {

    Flowable<List<EventEntity>> getEventList(DateTime from, DateTime to);

    Flowable<EventEntity> getSingleEvent(String id);

}
