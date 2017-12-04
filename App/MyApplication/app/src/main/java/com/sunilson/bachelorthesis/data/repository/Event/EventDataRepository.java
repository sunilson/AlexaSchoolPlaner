package com.sunilson.bachelorthesis.data.repository.Event;

import com.sunilson.bachelorthesis.data.model.EventEntity;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * @author Linus Weiss
 */


@Singleton
public class EventDataRepository implements com.sunilson.bachelorthesis.domain.repository.EventRepository {

    private EventRetrofitService eventRetrofitService;

    @Inject
    public EventDataRepository(EventRetrofitService eventRetrofitService) {
        this.eventRetrofitService = eventRetrofitService;
    }

    @Override
    public Observable<List<EventEntity>> getEventList(DateTime from, DateTime to) {
        return eventRetrofitService.getEventSpan(from.getMillis(), to.getMillis()).cache();
    }

    @Override
    public Observable<EventEntity> getSingleEvent(String id) {
        return eventRetrofitService.getEvent(id);
    }

    @Override
    public Observable<EventEntity> addEvent(Object body) {
        return eventRetrofitService.addEvent(body);
    }
}
