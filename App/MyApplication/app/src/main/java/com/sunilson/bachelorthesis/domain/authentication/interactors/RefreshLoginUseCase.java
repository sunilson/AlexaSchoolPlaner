package com.sunilson.bachelorthesis.domain.authentication.interactors;

import com.sunilson.bachelorthesis.data.repository.database.ApplicationDatabase;
import com.sunilson.bachelorthesis.domain.authentication.exceptions.NoUserFoundException;
import com.sunilson.bachelorthesis.domain.repository.AuthenticationRepository;
import com.sunilson.bachelorthesis.domain.shared.AbstractUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author Linus Weiss
 */

public class RefreshLoginUseCase extends AbstractUseCase<String, RefreshLoginUseCase.Params> {

    private AuthenticationRepository authenticationRepository;
    private ApplicationDatabase applicationDatabase;

    @Inject
    public RefreshLoginUseCase(AuthenticationRepository authenticationRepository, ApplicationDatabase applicationDatabase) {
        this.authenticationRepository = authenticationRepository;
        this.applicationDatabase = applicationDatabase;
    }

    @Override
    protected Observable<String> buildUseCaseObservable(final Params params) {
        return applicationDatabase.applicationDao().getCurrentUser().toObservable().flatMap(userEntity -> {
            if (userEntity == null) {
                throw new NoUserFoundException("No UserEntity defined");
            }
            String refreshToken = userEntity.getTokens().getRefreshToken();
            return authenticationRepository.refreshToken(refreshToken).flatMap(tokens -> {
                if (tokens != null && tokens.getAccessToken().length() > 0) {
                    userEntity.getTokens().setAccessToken(tokens.getAccessToken());
                    applicationDatabase.applicationDao().updateUser(userEntity);
                    return Observable.just(tokens.getAccessToken());
                }
                return Observable.just(null);
            });
        });
    }

    public static final class Params {

        private Params() {

        }

        public static Params forRefreshLogin() {
            return new Params();
        }
    }
}
