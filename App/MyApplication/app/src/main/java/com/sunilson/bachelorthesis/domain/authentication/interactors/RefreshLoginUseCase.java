package com.sunilson.bachelorthesis.domain.authentication.interactors;

import com.sunilson.bachelorthesis.data.repository.database.ApplicationDatabase;
import com.sunilson.bachelorthesis.domain.authentication.exceptions.NoUserFoundException;
import com.sunilson.bachelorthesis.domain.repository.AuthenticationRepository;
import com.sunilson.bachelorthesis.domain.shared.AbstractUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author Linus Weiss
 *
 * Use case for refreshing the access token of the current user
 */

public class RefreshLoginUseCase extends AbstractUseCase<String, RefreshLoginUseCase.Params> {

    private AuthenticationRepository authenticationRepository;
    private ApplicationDatabase applicationDatabase;

    @Inject
    public RefreshLoginUseCase(AuthenticationRepository authenticationRepository, ApplicationDatabase applicationDatabase) {
        this.authenticationRepository = authenticationRepository;
        this.applicationDatabase = applicationDatabase;
    }

    @Override
    protected Observable<String> buildUseCaseObservable(final Params params) {
        //First load the current user
        return applicationDatabase.applicationDao().getCurrentUser().toObservable().flatMap(userEntity -> {
            if (userEntity == null) throw new NoUserFoundException("No UserEntity defined");
            String refreshToken = userEntity.getTokens().getRefreshToken();
            //Refresh the token with the repository
            return authenticationRepository.refreshToken(refreshToken).flatMap(tokens -> {
                //Check if tokens were returned
                if (tokens != null && tokens.getAccessToken().length() > 0) {
                    //Set new access tokens and store user to local database
                    userEntity.getTokens().setAccessToken(tokens.getAccessToken());
                    applicationDatabase.applicationDao().updateUser(userEntity);
                    return Observable.just(tokens.getAccessToken());
                }
                return Observable.just(null);
            });
        });
    }

    public static final class Params {
        private Params() { }
        public static Params forRefreshLogin() {
            return new Params();
        }
    }
}
