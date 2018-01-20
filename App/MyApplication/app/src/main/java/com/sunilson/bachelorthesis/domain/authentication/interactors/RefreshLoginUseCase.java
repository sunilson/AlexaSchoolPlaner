package com.sunilson.bachelorthesis.domain.authentication.interactors;

import com.sunilson.bachelorthesis.data.model.user.UserEntity;
import com.sunilson.bachelorthesis.data.repository.database.ApplicationDatabase;
import com.sunilson.bachelorthesis.domain.authentication.exceptions.NoUserFoundException;
import com.sunilson.bachelorthesis.domain.repository.AuthenticationRepository;
import com.sunilson.bachelorthesis.domain.shared.AbstractUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

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
        return applicationDatabase.applicationDao().getCurrentUser().toObservable().flatMap(new Function<UserEntity, Observable<String>>() {
            @Override
            public Observable<String> apply(final UserEntity userEntity) throws NoUserFoundException {

                if (userEntity == null) {
                    throw new NoUserFoundException("No UserEntity defined");
                }

                String refreshToken = userEntity.getTokens().getRefreshToken();
                return authenticationRepository.refreshToken(refreshToken).flatMap(new Function<String, Observable<String>>() {
                    @Override
                    public Observable<String> apply(String s) throws Exception {
                        if(s != null && s.length() > 0) {
                            userEntity.getTokens().setAccessToken(s);
                            applicationDatabase.applicationDao().changeAccessToken(userEntity);
                            return Observable.just(s);
                        }

                        return Observable.just(null);
                    }
                });
            }
        });
    }

    public static final class Params {
        String refreshToken;

        private Params(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public static Params forRefreshLogin(String refreshToken) {
            return new Params(refreshToken);
        }
    }
}
