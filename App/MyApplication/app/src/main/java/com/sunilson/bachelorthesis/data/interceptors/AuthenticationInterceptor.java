package com.sunilson.bachelorthesis.data.interceptors;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Linus Weiss
 */

@Singleton
public class AuthenticationInterceptor implements Interceptor {

    @Inject
    public AuthenticationInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        return response;
    }
}
