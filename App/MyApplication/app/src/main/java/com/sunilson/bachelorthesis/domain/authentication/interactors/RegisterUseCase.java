package com.sunilson.bachelorthesis.domain.authentication.interactors;

import com.sunilson.bachelorthesis.data.model.user.UserEntity;
import com.sunilson.bachelorthesis.domain.authentication.model.DomainUser;
import com.sunilson.bachelorthesis.domain.repository.AuthenticationRepository;
import com.sunilson.bachelorthesis.domain.shared.AbstractUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * @author Linus Weiss
 */

public class RegisterUseCase extends AbstractUseCase<Boolean, RegisterUseCase.Params> {

    private AuthenticationRepository authenticationRepository;

    @Inject
    public RegisterUseCase(AuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable(Params params) {

        DomainUser domainUser = new DomainUser(params.name, params.email, params.password);

        return authenticationRepository.signUp(domainUser).map(new Function<UserEntity, Boolean>() {
            @Override
            public Boolean apply(UserEntity userEntity) throws Exception {
                return userEntity != null;
            }
        });
    }

    public static final class Params {
        String name, password, email;

        private Params(String name, String password, String email) {
            this.name = name;
            this.password = password;
            this.email = email;
        }

        public static Params forRegister(String name, String password, String email) {
            return new Params(name, password, email);
        }
    }
}
