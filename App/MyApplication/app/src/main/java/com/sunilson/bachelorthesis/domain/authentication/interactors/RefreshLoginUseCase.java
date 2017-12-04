package com.sunilson.bachelorthesis.domain.authentication.interactors;

import com.sunilson.bachelorthesis.data.model.user.UserEntity;
import com.sunilson.bachelorthesis.data.repository.database.ApplicationDatabase;
import com.sunilson.bachelorthesis.domain.repository.AuthenticationRepository;
import com.sunilson.bachelorthesis.domain.shared.AbstractUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
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
        return authenticationRepository.refreshToken(params.refreshToken).flatMap(new Function<String, Observable<Object>>() {
            @Override
            public Observable<Object> apply(final String s) throws Exception {
                if (s != null && s.length() > 0) {
                    //Get current user and and set access token to the newly generated one
                    return applicationDatabase.applicationDao().getCurrentUser().toObservable().map(new Function<UserEntity, Object>() {
                        @Override
                        public UserEntity apply(UserEntity userEntity) throws Exception {
                            if (userEntity == null) {
                                throw new Exception("No UserEntity defined");
                            }
                            userEntity.getTokens().setAccessToken(s);
                            return userEntity;
                        }
                    });
                } else {
                    return Observable.just(new Object());
                }
            }
        }).flatMap(new Function<Object, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Object userEntity) throws Exception {
                //If no UserEntity is returned, there was no fresh access token generated. Return empty string
                if (!(userEntity instanceof UserEntity)) {
                    return Observable.just("");
                }

                //UserEntity was returned. Update the database entry and return the access token
                UserEntity tempUserEntity = (UserEntity) userEntity;
                applicationDatabase.applicationDao().changeAccessToken(tempUserEntity);
                return Observable.just(tempUserEntity.getTokens().getAccessToken());
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
