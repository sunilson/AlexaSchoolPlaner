package com.sunilson.bachelorthesis.data.repository.Event;

import com.sunilson.bachelorthesis.data.model.EventEntity;
import com.sunilson.bachelorthesis.data.repository.BodyModels.EventForPostBody;
import com.sunilson.bachelorthesis.data.repository.BodyModels.UrlForPostBody;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Linus Weiss
 *
 * Retrofit Interface to the Resource server for Event requests
 */

public interface EventRetrofitService {

    @GET("events")
    Observable<List<EventEntity>> getEventSpan(@Query("from") Long from, @Query("to") Long to);

    @GET("events/{event_id}")
    Observable<EventEntity> getEvent(@Path("event_id") String event_id);

    @POST("events/new")
    Observable<EventEntity> addEvent(@Body EventForPostBody body);

    @POST("events/import")
    Observable<Response<Void>> importCalendar(@Body UrlForPostBody body);
}
