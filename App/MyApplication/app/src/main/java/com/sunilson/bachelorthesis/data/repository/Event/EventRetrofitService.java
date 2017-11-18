package com.sunilson.bachelorthesis.data.repository.Event;

import retrofit2.http.GET;

/**
 * Created by linus_000 on 17.11.2017.
 */

public interface EventRetrofitService {

    @GET()
    Observable<> getEventSpan();

}
