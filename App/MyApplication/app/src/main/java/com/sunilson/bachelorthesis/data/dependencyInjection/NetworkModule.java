package com.sunilson.bachelorthesis.data.dependencyInjection;

import com.sunilson.bachelorthesis.data.interceptors.TokenAuthenticator;
import com.sunilson.bachelorthesis.data.repository.Event.EventRetrofitService;
import com.sunilson.bachelorthesis.data.repository.authentication.AuthenticationRetrofitService;
import com.sunilson.bachelorthesis.presentation.shared.utilities.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Linus Weiss
 */

@Module()
public class NetworkModule {

    @Provides
    @Singleton
    OkHttpClient okHttpClient(TokenAuthenticator tokenAuthenticator) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().authenticator(tokenAuthenticator).addInterceptor(interceptor)./*addNetworkInterceptor(authenticationInterceptor). */build();
    }

    @Provides
    @Singleton
    Retrofit retrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(Constants.HOME_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    AuthenticationRetrofitService authenticationRetrofitService(Retrofit retrofit) {
        return retrofit.create(AuthenticationRetrofitService.class);
    }

    @Provides
    @Singleton
    EventRetrofitService eventRetrofitService(Retrofit retrofit) {
        return retrofit.create(EventRetrofitService.class);
    }

}
