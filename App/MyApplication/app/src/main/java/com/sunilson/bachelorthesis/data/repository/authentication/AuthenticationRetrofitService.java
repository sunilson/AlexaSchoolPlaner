package com.sunilson.bachelorthesis.data.repository.authentication;

import com.sunilson.bachelorthesis.data.model.user.Tokens;
import com.sunilson.bachelorthesis.data.model.user.UserEntity;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author Linus Weiss
 *
 * Retrofit interface for everything authentication related
 */

public interface AuthenticationRetrofitService {

    @POST("auth/register")
    Observable<UserEntity> register(@Body Object body);

    @POST("auth/")
    Observable<UserEntity> signIn(@Body Object body);

    @POST("auth/")
    Observable<Tokens> refreshToken(@Body Object refreshtoken);
}
