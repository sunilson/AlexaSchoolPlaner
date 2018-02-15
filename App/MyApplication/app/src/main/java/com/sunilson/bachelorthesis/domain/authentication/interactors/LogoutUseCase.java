package com.sunilson.bachelorthesis.domain.authentication.interactors;

import com.sunilson.bachelorthesis.data.repository.authentication.AuthenticationDataRepository;
import com.sunilson.bachelorthesis.data.repository.database.ApplicationDatabase;
import com.sunilson.bachelorthesis.domain.repository.AuthenticationRepository;
import com.sunilson.bachelorthesis.domain.shared.AbstractUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by linus on 04.02.2018.
 */

public class LogoutUseCase extends AbstractUseCase<Void, Void> {

    private AuthenticationRepository authenticationRepository;
    private ApplicationDatabase applicationDatabase;

    @Inject
    public LogoutUseCase(AuthenticationRepository authenticationRepository, ApplicationDatabase applicationDatabase) {
        this.authenticationRepository = authenticationRepository;
        this.applicationDatabase = applicationDatabase;
    }

    @Override
    protected Observable<Void> buildUseCaseObservable(Void aVoid) {
        return null;
    }

}
