package com.sunilson.bachelorthesis.domain.authentication.interactors;

import com.sunilson.bachelorthesis.domain.authentication.model.DomainUser;
import com.sunilson.bachelorthesis.domain.shared.AbstractUseCase;

import io.reactivex.Observable;

/**
 * @author Linus Weiss
 */

public class LoginUseCase extends AbstractUseCase<Boolean, LoginUseCase.Params> {

    @Override
    protected Observable<Boolean> buildUseCaseObservable(Params params) {
        return null;
    }

    public static final class Params {
        DomainUser domainUser;

        private Params(DomainUser domainUser) {
            this.domainUser = domainUser;
        }

        public static Params forLogin(DomainUser domainUser) {
            return new Params(domainUser);
        }
    }
}
