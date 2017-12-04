package com.sunilson.bachelorthesis.domain.repository;

import com.sunilson.bachelorthesis.data.model.user.UserEntity;
import com.sunilson.bachelorthesis.domain.authentication.model.DomainUser;

import io.reactivex.Observable;

/**
 * @author Linus Weiss
 */

public interface AuthenticationRepository {
    Observable<UserEntity> signIn(String name, String password);
    Observable<UserEntity> signUp(DomainUser domainUser);
    Observable<String> refreshToken(String refreshToken);
    Observable<UserEntity> getCurrentUser();
}
