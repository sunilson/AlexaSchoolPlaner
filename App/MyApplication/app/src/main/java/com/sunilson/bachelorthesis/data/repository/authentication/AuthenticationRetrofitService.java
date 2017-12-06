package com.sunilson.bachelorthesis.data.repository.authentication;

import com.sunilson.bachelorthesis.data.model.user.UserEntity;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author Linus Weiss
 */

public interface AuthenticationRetrofitService {

    @POST("auth/register")
    Observable<UserEntity> register(@Body Object body);

    @POST("auth/login")
    Observable<UserEntity> signIn(@Body Object body);

    @GET("auth/refreshToken")
    Observable<String> refreshToken(@Query("refreshToken") String refreshToken);
}
