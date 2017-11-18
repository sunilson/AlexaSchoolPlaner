package com.sunilson.bachelorthesis.data.repository.Event;

import com.sunilson.bachelorthesis.data.model.EventEntity;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import retrofit2.Retrofit;

/**
 * @author Linus Weiss
 */


@Singleton
public class EventRepository implements com.sunilson.bachelorthesis.domain.repository.EventRepository {

    @Inject
    public EventRepository(Retrofit retrofit) {

    }

    @Override
    public Flowable<List<EventEntity>> getEventList(DateTime from, DateTime to) {
        return null;
    }

    @Override
    public Flowable<EventEntity> getSingleEvent(String id) {
        return null;
    }
}
