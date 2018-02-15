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
        return eventRetrofitService
                .getEventSpan(from.getMillis(), to.getMillis())
                .doOnNext(new Consumer<List<EventEntity>>() {
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
        DateTimeFormatter formatter = ISODateTimeFormat.dateTime();
        return applicationDatabase.applicationDao().getEvents(formatter.print(from), formatter.print(to)).toObservable();
    }

    @Override
    public Observable<EventEntity> getOfflineSingleEvent(String id) {
        return applicationDatabase.applicationDao().getSingleEvent(id).toObservable();
    }

    @Override
    public Observable<EventEntity> getSingleEvent(String id) {
        return eventRetrofitService.getEvent(id).doOnNext(new Consumer<EventEntity>() {
            @Override
            public void accept(EventEntity eventEntity) throws Exception {
                applicationDatabase.applicationDao().addEvent(eventEntity);
            }
        });
    }

    @Override
    public Observable<EventEntity> addEvent(EventForPostBody body) {
        return eventRetrofitService.addEvent(body).doOnNext(new Consumer<EventEntity>() {
            @Override
            public void accept(EventEntity eventEntity) throws Exception {
                applicationDatabase.applicationDao().addEvent(eventEntity);
            }
        });
    }

    @Override
    public Observable<Response<Void>> importCalendar(final UrlForPostBody body) {
        return eventRetrofitService.importCalendar(new UrlForPostBody(body.getUrl())).doOnNext(new Consumer<Response<Void>>() {
            @Override
            public void accept(Response<Void> response) throws Exception {
                if (response.code() == 200) {
                    applicationDatabase.applicationDao().getCurrentUser().subscribe(new Consumer<UserEntity>() {
                        @Override
                        public void accept(UserEntity userEntity) throws Exception {
                            userEntity.getUser().setIcalurl(body.getUrl());
                            applicationDatabase.applicationDao().updateUser(userEntity);
                        }
                    });
                }
            }
        });
    }
}
