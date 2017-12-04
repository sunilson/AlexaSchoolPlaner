package com.sunilson.bachelorthesis.data.interceptors;

import android.util.Log;

import com.sunilson.bachelorthesis.domain.authentication.interactors.CheckLoginStatusUseCase;

import java.io.IOException;

import javax.inject.Inject;

import dagger.Lazy;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Linus Weiss
 */

public class AuthenticationInterceptor implements Interceptor {

    private Lazy<CheckLoginStatusUseCase> checkLoginStatusUseCase;

    @Inject
    public AuthenticationInterceptor(Lazy<CheckLoginStatusUseCase> checkLoginStatusUseCase) {
        this.checkLoginStatusUseCase = checkLoginStatusUseCase;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();



        Log.d("Linus", checkLoginStatusUseCase.get().execute(null).blockingFirst().toString());

        //TODO AccessToken auf gültigkeit prüfen. Wenn gülitg dann anfügen an den Request, ansonsten verscueh neuen zu holen

        Response response = chain.proceed(request);

        //TODO 401 Error abfangen und versuchen neu zu authentifizieren

        return response;
    }
}
