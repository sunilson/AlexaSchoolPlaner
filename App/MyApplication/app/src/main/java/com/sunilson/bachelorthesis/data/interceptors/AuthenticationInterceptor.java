package com.sunilson.bachelorthesis.data.interceptors;

import android.app.Application;
import android.arch.persistence.room.EmptyResultSetException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sunilson.bachelorthesis.domain.authentication.interactors.CheckLoginStatusUseCase;
import com.sunilson.bachelorthesis.presentation.shared.utilities.Navigator;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Linus Weiss
 */

@Singleton
public class AuthenticationInterceptor implements Interceptor {

    private Lazy<CheckLoginStatusUseCase> checkLoginStatusUseCase;
    private Application application;

    @Inject
    public AuthenticationInterceptor(Lazy<CheckLoginStatusUseCase> checkLoginStatusUseCase, Application application) {
        this.application = application;
        this.checkLoginStatusUseCase = checkLoginStatusUseCase;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String accessToken = null;

        if (!request.url().toString().contains("auth")) {
            try {
                accessToken = checkLoginStatusUseCase.get().execute(null).blockingFirst();
            } catch (Exception e) {
                //No access token was in the database (user was never logged in before),
                //go to login page if this was no Request to an authentication resource
                if (e instanceof EmptyResultSetException) {
                    //TODO Resolve this in presentation layer?
                    Navigator.navigateToLogin(application);
                }

                //If token is still null, a 401 error will be sent by the server. This will then be
                //handled in the TokenAuthenticator
            }
        }

        Log.d("Linus", "Request " + request.url().toString() + " with access token " + accessToken);

        if(accessToken != null) {
           request =  request.newBuilder().addHeader("Authorization", "Bearer " + accessToken).build();
        }

        Response response = chain.proceed(request);
        return response;
    }
}
