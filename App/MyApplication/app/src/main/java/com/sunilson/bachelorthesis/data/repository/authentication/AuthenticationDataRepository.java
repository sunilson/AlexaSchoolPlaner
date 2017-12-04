package com.sunilson.bachelorthesis.data.repository.authentication;

import com.sunilson.bachelorthesis.data.model.user.UserEntity;
import com.sunilson.bachelorthesis.data.repository.database.ApplicationDatabase;
import com.sunilson.bachelorthesis.domain.authentication.model.DomainUser;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * @author Linus Weiss
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
    public Observable<UserEntity> signIn(String name, String password) {
        return authenticationRetrofitService.signIn(name, password).doOnNext(new Consumer<UserEntity>() {
            @Override
            public void accept(UserEntity userEntity) throws Exception {
                applicationDatabase.applicationDao().addUser(userEntity);
            }
        });
    }

    @Override
    public Observable<UserEntity> signUp(DomainUser domainUser) {
        return this.authenticationRetrofitService.register(domainUser).doOnNext(new Consumer<UserEntity>() {
            @Override
            public void accept(UserEntity userEntity) throws Exception {
                applicationDatabase.applicationDao().addUser(userEntity);
            }
        });
    }

    @Override
    public Observable<String> refreshToken(String refreshToken) {
        return authenticationRetrofitService.refreshToken(refreshToken);
    }

    @Override
    public Observable<UserEntity> getCurrentUser() {
        return applicationDatabase.applicationDao().getCurrentUser().toObservable();
    }
}
