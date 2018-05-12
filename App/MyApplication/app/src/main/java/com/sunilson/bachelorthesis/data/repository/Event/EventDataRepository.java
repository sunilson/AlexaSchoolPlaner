package com.sunilson.bachelorthesis.data.repository.Event;

import android.util.Log;

import com.sunilson.bachelorthesis.data.model.EventEntity;
import com.sunilson.bachelorthesis.data.model.user.UserEntity;
import com.sunilson.bachelorthesis.data.repository.BodyModels.EventForPostBody;
import com.sunilson.bachelorthesis.data.repository.BodyModels.UrlForPostBody;
import com.sunilson.bachelorthesis.data.repository.database.ApplicationDatabase;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * @author Linus Weiss
 *
 * Repository implementation for everything event related
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

    /**
     * Get a list of events between two dates
     *
     * @param from
     * @param to
     * @return List of EventEntities
     */
    @Override
    public Observable<List<EventEntity>> getEventList(DateTime from, DateTime to) {
        return eventRetrofitService
                .getEventSpan(from.getMillis(), to.getMillis())
                .doOnNext(eventEntities -> {
                    //Save all returned events to local database
                    if (eventEntities != null && eventEntities.size() > 0) {
                        applicationDatabase.applicationDao().addEvents(eventEntities);
                    }
                });
    }

    /**
     * Get events between two dates from local database
     *
     * @param from
     * @param to
     * @return List of EventEntities
     */
    @Override
    public Observable<List<EventEntity>> getOfflineEventList(DateTime from, DateTime to) {
        DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
        return applicationDatabase
                .applicationDao()
                .getEvents(formatter.print(from), formatter.print(to))
                .toObservable();
    }

    @Override
    public Observable<EventEntity> getOfflineSingleEvent(String id) {
        return applicationDatabase
                .applicationDao()
                .getSingleEvent(id)
                .toObservable();
    }

    /**
     * Get single event from server and store it locally
     *
     * @param id
     * @return
     */
    @Override
    public Observable<EventEntity> getSingleEvent(String id) {
        return eventRetrofitService
                .getEvent(id)
                .doOnNext(eventEntity -> applicationDatabase.applicationDao().addEvent(eventEntity));
    }

    @Override
    public Observable<EventEntity> addEvent(EventForPostBody body) {
        return eventRetrofitService.addEvent(body).doOnNext(eventEntity -> {
            if (eventEntity != null) applicationDatabase.applicationDao().addEvent(eventEntity);
        });
    }

    @Override
    public Observable<EventEntity> editEvent(EventForPostBody body) {
        return eventRetrofitService.editEvent(body).doOnNext(eventEntity -> {
            if (eventEntity != null) applicationDatabase.applicationDao().updateEvent(eventEntity);
        });
    }

    @Override
    public Observable<Response<Void>> importCalendar(final UrlForPostBody body) {
        return eventRetrofitService.importCalendar(body).doOnNext(response -> {
            if (response.code() == 200) {
                applicationDatabase.applicationDao().getCurrentUser().subscribe(userEntity -> {
                    userEntity.getUser().setIcalurl(body.getUrl());
                    userEntity.getUser().setIcaltype(body.getType());
                    applicationDatabase.applicationDao().updateUser(userEntity);
                });
            }
        });
    }
}
