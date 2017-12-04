package com.sunilson.bachelorthesis.domain.authentication.interactors;

import com.auth0.android.jwt.JWT;
import com.sunilson.bachelorthesis.data.model.user.UserEntity;
import com.sunilson.bachelorthesis.domain.repository.AuthenticationRepository;
import com.sunilson.bachelorthesis.domain.shared.AbstractUseCase;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * @author Linus Weiss
 */

public class CheckLoginStatusUseCase extends AbstractUseCase<Boolean, Object> {

    private AuthenticationRepository authenticationRepository;
    private RefreshLoginUseCase refreshLoginUseCase;

    @Inject
    public CheckLoginStatusUseCase(AuthenticationRepository authenticationRepository, RefreshLoginUseCase refreshLoginUseCase) {
        this.authenticationRepository = authenticationRepository;
        this.refreshLoginUseCase = refreshLoginUseCase;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable(Object o) {
        return authenticationRepository.getCurrentUser().map(new Function<UserEntity, Boolean>() {
            @Override
            public Boolean apply(UserEntity userEntity) throws Exception {
                //If a userEntity has been found, check if the access token is still valid
                if (userEntity != null) {
                    String accessToken = userEntity.getTokens().getAccessToken();
                    JWT jwt = new JWT(accessToken);
                    if (jwt.getExpiresAt().getTime() < new Date().getTime() + 300000) {
                        return false;
                    } else {
                        return true;
                    }
                }
                throw  new Exception("No UserEntity defined");
            }
        });
    }
}
