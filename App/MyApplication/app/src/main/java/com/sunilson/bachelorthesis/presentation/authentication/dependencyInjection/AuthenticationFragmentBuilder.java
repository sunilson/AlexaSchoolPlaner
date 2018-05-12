package com.sunilson.bachelorthesis.presentation.authentication.dependencyInjection;

import com.sunilson.bachelorthesis.presentation.authentication.login.LoginFragment;
import com.sunilson.bachelorthesis.presentation.authentication.login.LoginFragmentModule;
import com.sunilson.bachelorthesis.presentation.authentication.register.RegisterFragment;
import com.sunilson.bachelorthesis.presentation.authentication.register.RegisterFragmentModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Linus Weiss
 *
 * Helper from Dagger for injecting fragments without having to write your own sub components
 */

@Module
public abstract class AuthenticationFragmentBuilder {
    @ContributesAndroidInjector(modules = RegisterFragmentModule.class)
    abstract RegisterFragment provideRegisterFragment();

    @ContributesAndroidInjector(modules = LoginFragmentModule.class)
    abstract LoginFragment provideLoginFragment();
}
