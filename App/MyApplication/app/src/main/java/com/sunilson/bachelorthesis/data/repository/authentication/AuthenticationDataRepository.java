package com.sunilson.bachelorthesis.data.repository.authentication;

import com.sunilson.bachelorthesis.data.model.user.Tokens;
import com.sunilson.bachelorthesis.data.model.user.UserEntity;
import com.sunilson.bachelorthesis.data.repository.BodyModels.RefreshtokenForPostBody;
import com.sunilson.bachelorthesis.data.repository.database.ApplicationDatabase;
import com.sunilson.bachelorthesis.domain.authentication.model.DomainUser;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * @author Linus Weiss
 *
 * Repository implementation for everything authentication related
 */

@Singleton
public class AuthenticationDataRepository implements com.sunilson.bachelorthesis.domain.repository.AuthenticationRepository {

    private AuthenticationRetrofitService authenticationRetrofitService;
    private ApplicationDatabase applicationDatabase;

    @Inject
    public AuthenticationDataRepository(AuthenticationRetrofitService authenticationRetrofitService, ApplicationDatabase applicationDatabase) {
        this.authenticationRetrofitService = authenticationRetrofitService;
        this.applicationDatabase = applicationDatabase;
    }

    @Override
    public Observable<UserEntity> signIn(Object body) {
        return authenticationRetrofitService
                .signIn(body)
                .doOnNext(userEntity -> applicationDatabase.applicationDao().addUser(userEntity));
    }

    @Override
    public Observable<UserEntity> signUp(Object domainUser) {
        return this.authenticationRetrofitService
                .register(domainUser)
                .doOnNext(userEntity -> applicationDatabase.applicationDao().addUser(userEntity));
    }

    @Override
    public Observable<Tokens> refreshToken(String refreshToken) {
        return authenticationRetrofitService
                .refreshToken(new RefreshtokenForPostBody(refreshToken));
    }

    @Override
    public Observable<UserEntity> getCurrentUser() {
        return applicationDatabase
                .applicationDao()
                .getCurrentUser()
                .toObservable();
    }
}
