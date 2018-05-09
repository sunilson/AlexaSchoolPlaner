package com.sunilson.bachelorthesis.data.interceptors;

import com.sunilson.bachelorthesis.domain.authentication.interactors.RefreshLoginUseCase;
import com.sunilson.bachelorthesis.presentation.shared.utilities.Constants;

import java.io.IOException;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * @author Linus Weiss
 */

@Singleton
public class TokenAuthenticator implements Authenticator {

    Lazy<RefreshLoginUseCase> refreshLoginUseCaseLazy;

    @Inject
    public TokenAuthenticator(Lazy<RefreshLoginUseCase> refreshLoginUseCaseLazy) {
        this.refreshLoginUseCaseLazy = refreshLoginUseCaseLazy;
    }

    @Nullable
    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        //Don't handle auth requests, because the user doesn't have to already be authenticated for them
        if (response.request().url().toString().startsWith(Constants.HOME_URL + "auth")) {
            return null;
        }

        //Refresh the current access token
        String accessToken = refreshLoginUseCaseLazy.get().execute(null).blockingFirst();

        //If refresh was successful set the header of the previous request, otherwise log out
        if (accessToken != null && !accessToken.isEmpty()) {
            return response.request().newBuilder().header("Authorization", "Bearer " + accessToken).build();
        } else {
            return null;
        }
    }
}

@Nullable
@Override
public Request authenticate(Route route, Response response) throws IOException {
    //Don't handle auth requests, because the user doesn't have to already be authenticated for them
    if (response.request().url().toString()
            .startsWith(Constants.HOME_URL + "auth")) {
        return null;
    }

    //Refresh the current access token
    String accessToken = refreshLoginUseCaseLazy
            .get().execute(null).blockingFirst();

    //If refresh was successful set the header of the previous request, otherwise log out
    if (accessToken != null && !accessToken.isEmpty()) {
        return response.request().newBuilder()
                .header("Authorization", "Bearer " + accessToken).build();
    } else {
        return null;
    }
}
