package com.sunilson.bachelorthesis.domain.repository;

import com.sunilson.bachelorthesis.data.model.EventEntity;

import org.joda.time.DateTime;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author Linus Weiss
 */

public interface EventRepository {

    Observable<List<EventEntity>> getEventList(DateTime from, DateTime to);

    Observable<EventEntity> getSingleEvent(String id);

    Observable<EventEntity> addEvent(Object body);

}
