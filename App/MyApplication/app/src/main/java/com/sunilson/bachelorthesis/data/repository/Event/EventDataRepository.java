package com.sunilson.bachelorthesis.data.repository.Event;

import android.util.Log;

import com.sunilson.bachelorthesis.data.model.EventEntity;
import com.sunilson.bachelorthesis.data.repository.database.ApplicationDatabase;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * @author Linus Weiss
 */


@Singleton
public class EventDataRepository implements com.sunilson.bachelorthesis.domain.repository.EventRepository {

    private EventRetrofitService eventRetrofitService;
    private ApplicationDatabase applicationDatabase;

    @Inject
    public EventDataRepository(EventRetrofitService eventRetrofitService, ApplicationDatabase applicationDatabase) {
        this.applicationDatabase = applicationDatabase;
        this.eventRetrofitService = eventRetrofitService;
    }

    @Override
    public Observable<List<EventEntity>> getEventList(DateTime from, DateTime to) {
        return eventRetrofitService.getEventSpan(from.getMillis(), to.getMillis()).doOnNext(new Consumer<List<EventEntity>>() {
                    @Override
                    public void accept(List<EventEntity> eventEntities) throws Exception {
                        if (eventEntities != null && eventEntities.size() > 0) {
                            applicationDatabase.applicationDao().addEvents(eventEntities);
                        }
                    }
                }
        );
    }

    @Override
    public Observable<List<EventEntity>> getOfflineEventList(DateTime from, DateTime to) {
        Log.d("Linus", "Getting offline events");
        DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
        return applicationDatabase.applicationDao().getEvents(formatter.print(from), formatter.print(to)).toObservable();
    }

    @Override
    public Observable<EventEntity> getSingleEvent(String id) {
        return applicationDatabase.applicationDao().getSingleEvent(id).toObservable().mergeWith(
                eventRetrofitService.getEvent(id).doOnNext(new Consumer<EventEntity>() {
                    @Override
                    public void accept(EventEntity eventEntity) throws Exception {
                        applicationDatabase.applicationDao().addEvent(eventEntity);
                    }
                })
        );
    }

    @Override
    public Observable<EventEntity> addEvent(Object body) {
        return eventRetrofitService.addEvent(body).doOnNext(new Consumer<EventEntity>() {
            @Override
            public void accept(EventEntity eventEntity) throws Exception {
                applicationDatabase.applicationDao().addEvent(eventEntity);
            }
        });
    }
}
