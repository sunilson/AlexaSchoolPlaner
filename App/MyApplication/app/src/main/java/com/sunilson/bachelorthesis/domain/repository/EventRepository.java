package com.sunilson.bachelorthesis.domain.repository;

import com.sunilson.bachelorthesis.data.model.EventEntity;
import com.sunilson.bachelorthesis.data.repository.BodyModels.EventForPostBody;
import com.sunilson.bachelorthesis.data.repository.BodyModels.UrlForPostBody;

import org.joda.time.DateTime;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * @author Linus Weiss
 */

public interface EventRepository {
    Observable<List<EventEntity>> getEventList(DateTime from, DateTime to);
    Observable<List<EventEntity>> getOfflineEventList(DateTime from, DateTime to);
    Observable<EventEntity> getSingleEvent(String id);
    Observable<EventEntity> getOfflineSingleEvent(String id);
    Observable<EventEntity> addEvent(EventForPostBody body);
    Observable<EventEntity> editEvent(EventForPostBody body);
    Observable<Response<Void>> importCalendar(UrlForPostBody body);
}
