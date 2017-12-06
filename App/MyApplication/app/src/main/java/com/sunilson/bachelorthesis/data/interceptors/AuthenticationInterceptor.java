package com.sunilson.bachelorthesis.data.interceptors;

import com.sunilson.bachelorthesis.domain.authentication.interactors.CheckLoginStatusUseCase;
import com.sunilson.bachelorthesis.domain.authentication.interactors.RefreshLoginUseCase;

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
    private Lazy<RefreshLoginUseCase> refreshLoginUseCaseLazy;

    @Inject
    public AuthenticationInterceptor(Lazy<CheckLoginStatusUseCase> checkLoginStatusUseCase,
                                     Lazy<RefreshLoginUseCase> refreshLoginUseCaseLazy) {
        this.checkLoginStatusUseCase = checkLoginStatusUseCase;
        this.refreshLoginUseCaseLazy = refreshLoginUseCaseLazy;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String accessToken = checkLoginStatusUseCase.get().execute(null).blockingFirst();
        if(accessToken != null) {
           request.newBuilder().header("Authorization", accessToken);
        }

        Response response = chain.proceed(request);
        return response;
    }
}
