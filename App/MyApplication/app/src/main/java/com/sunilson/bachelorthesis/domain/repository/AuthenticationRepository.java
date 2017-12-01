package com.sunilson.bachelorthesis.domain.repository;

import com.sunilson.bachelorthesis.data.model.UserEntity;
import com.sunilson.bachelorthesis.domain.authentication.model.DomainUser;

import io.reactivex.Observable;

/**
 * @author Linus Weiss
 */

public interface AuthenticationRepository {
    Observable<UserEntity> signIn(DomainUser domainUser);
    Observable<UserEntity> signUp(DomainUser domainUser);
    Observable<UserEntity> refreshToken(DomainUser domainUser);
}
