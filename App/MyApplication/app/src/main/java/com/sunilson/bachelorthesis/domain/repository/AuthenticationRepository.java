package com.sunilson.bachelorthesis.domain.repository;

import com.sunilson.bachelorthesis.data.model.user.Tokens;
import com.sunilson.bachelorthesis.data.model.user.UserEntity;

import io.reactivex.Observable;

/**
 * @author Linus Weiss
 */

public interface AuthenticationRepository {
    Observable<UserEntity> signIn(Object domainLoginData);
    Observable<UserEntity> signUp(Object domainUser);
    Observable<Tokens> refreshToken(String refreshToken);
    Observable<UserEntity> getCurrentUser();
}
