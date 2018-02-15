package com.sunilson.bachelorthesis.domain.authentication.mappers;

import com.sunilson.bachelorthesis.data.model.user.UserEntity;
import com.sunilson.bachelorthesis.domain.authentication.model.DomainUser;

import javax.inject.Inject;

/**
 * @author Linus Weiss
 */

public class UserEntityToDomainUserMapper {

    @Inject
    public UserEntityToDomainUserMapper() {

    }

    public DomainUser toDomainUser(UserEntity userEntity) {
        return new DomainUser(userEntity.getUser().getUsername(), userEntity.getUser().getEmail(), null, userEntity.getUser().getIcalurl());
    }
}
