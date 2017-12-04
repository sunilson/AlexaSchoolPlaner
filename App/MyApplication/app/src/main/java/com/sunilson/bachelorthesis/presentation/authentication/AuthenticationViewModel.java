package com.sunilson.bachelorthesis.presentation.authentication;

import android.arch.lifecycle.ViewModel;

import com.sunilson.bachelorthesis.domain.authentication.interactors.GetCurrentUser;
import com.sunilson.bachelorthesis.domain.authentication.interactors.LoginUseCase;
import com.sunilson.bachelorthesis.domain.authentication.interactors.RegisterUseCase;
import com.sunilson.bachelorthesis.domain.authentication.model.DomainUser;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author Linus Weiss
 */

public class AuthenticationViewModel extends ViewModel {

    private LoginUseCase loginUseCase;
    private GetCurrentUser getCurrentUser;
    private RegisterUseCase registerUseCase;

    @Inject
    public AuthenticationViewModel(LoginUseCase loginUseCase, RegisterUseCase registerUseCase, GetCurrentUser getCurrentUser) {
        this.loginUseCase = loginUseCase;
        this.getCurrentUser = getCurrentUser;
        this.registerUseCase = registerUseCase;
    }

    public Observable<Boolean> login(String name, String password) {
      return this.loginUseCase.execute(LoginUseCase.Params.forLogin(name, password));
    }

    public Observable<Boolean> register(String name, String email, String password) {
        return this.registerUseCase.execute(RegisterUseCase.Params.forRegister(name, password, email));
    }

    public Observable<DomainUser> getCurrentUser() {
        return this.getCurrentUser.execute(null);
    }
}
