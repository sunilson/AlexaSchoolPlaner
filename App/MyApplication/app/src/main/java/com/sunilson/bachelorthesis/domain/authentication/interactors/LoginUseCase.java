package com.sunilson.bachelorthesis.domain.authentication.interactors;

import com.sunilson.bachelorthesis.data.model.user.UserEntity;
import com.sunilson.bachelorthesis.domain.repository.AuthenticationRepository;
import com.sunilson.bachelorthesis.domain.shared.AbstractUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * @author Linus Weiss
 */

public class LoginUseCase extends AbstractUseCase<Boolean, LoginUseCase.Params> {

    AuthenticationRepository authenticationRepository;

    @Inject
    public LoginUseCase(AuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable(Params params) {
        return authenticationRepository.signIn(params.name, params.password).map(new Function<UserEntity, Boolean>() {
            @Override
            public Boolean apply(UserEntity userEntity) throws Exception {
                return userEntity != null;
            }
        });
    }

    public static final class Params {
        String name, password;

        private Params(String name, String password) {
            this.name = name;
            this.password = password;
        }

        public static Params forLogin(String name, String password) {
            return new Params(name, password);
        }
    }
}
