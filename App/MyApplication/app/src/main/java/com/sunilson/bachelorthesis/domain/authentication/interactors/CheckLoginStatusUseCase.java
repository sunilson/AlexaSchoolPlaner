package com.sunilson.bachelorthesis.domain.authentication.interactors;

import android.util.Log;

import com.auth0.android.jwt.JWT;
import com.sunilson.bachelorthesis.data.model.user.UserEntity;
import com.sunilson.bachelorthesis.domain.authentication.exceptions.NoUserFoundException;
import com.sunilson.bachelorthesis.domain.repository.AuthenticationRepository;
import com.sunilson.bachelorthesis.domain.shared.AbstractUseCase;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * @author Linus Weiss
 */

public class CheckLoginStatusUseCase extends AbstractUseCase<String, Object> {

    private AuthenticationRepository authenticationRepository;
    private RefreshLoginUseCase refreshLoginUseCase;

    @Inject
    public CheckLoginStatusUseCase(AuthenticationRepository authenticationRepository, RefreshLoginUseCase refreshLoginUseCase) {
        this.authenticationRepository = authenticationRepository;
        this.refreshLoginUseCase = refreshLoginUseCase;
    }

    @Override
    protected Observable<String> buildUseCaseObservable(Object o) {
        return authenticationRepository.getCurrentUser().map(new Function<UserEntity, String>() {
            @Override
            public String apply(UserEntity userEntity) throws NoUserFoundException {
                //If a userEntity has been found, check if the access token is still valid
                if (userEntity != null) {
                    String accessToken = userEntity.getTokens().getAccessToken();
                    JWT jwt = new JWT(accessToken);
                    if (jwt.getExpiresAt().getTime() > new Date().getTime() + 300000) {
                        return accessToken;
                    } else {
                        return null;
                    }
                }
                throw  new NoUserFoundException("No UserEntity defined");
            }
        });
    }
}
