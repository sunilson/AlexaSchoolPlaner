package com.sunilson.bachelorthesis.domain.authentication.interactors;

import android.util.Log;

import com.sunilson.bachelorthesis.data.model.user.UserEntity;
import com.sunilson.bachelorthesis.domain.authentication.mappers.UserEntityToDomainUserMapper;
import com.sunilson.bachelorthesis.domain.authentication.model.DomainUser;
import com.sunilson.bachelorthesis.domain.repository.AuthenticationRepository;
import com.sunilson.bachelorthesis.domain.shared.AbstractUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * @author Linus Weiss
 */

public class GetCurrentUser extends AbstractUseCase<DomainUser, GetCurrentUser.Params> {

    private AuthenticationRepository authenticationRepository;
    private UserEntityToDomainUserMapper userEntityToDomainUserMapper;

    @Inject
    public GetCurrentUser(AuthenticationRepository authenticationRepository, UserEntityToDomainUserMapper userEntityToDomainUserMapper) {
        this.authenticationRepository = authenticationRepository;
        this.userEntityToDomainUserMapper = userEntityToDomainUserMapper;
    }

    @Override
    protected Observable<DomainUser> buildUseCaseObservable(Params params) {
        return authenticationRepository.getCurrentUser().map(userEntity -> {
            Log.d("Linus", userEntity.getTokens().getExpiresIn().toString());
            return userEntityToDomainUserMapper.toDomainUser(userEntity);
        });
    }

    public final static class Params{

    }
}
