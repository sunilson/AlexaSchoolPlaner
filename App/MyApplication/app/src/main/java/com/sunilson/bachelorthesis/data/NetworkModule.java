package com.sunilson.bachelorthesis.data;

import com.sunilson.bachelorthesis.data.interceptors.AuthenticationInterceptor;

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
    OkHttpClient okHttpClient(AuthenticationInterceptor authenticationInterceptor) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(interceptor).addNetworkInterceptor(authenticationInterceptor).build();
    }

    @Provides
    @Singleton
    Retrofit retrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("http://172.16.16.127:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }

}
