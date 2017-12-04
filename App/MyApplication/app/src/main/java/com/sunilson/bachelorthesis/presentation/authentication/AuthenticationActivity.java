package com.sunilson.bachelorthesis.presentation.authentication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.sunilson.bachelorthesis.R;
import com.sunilson.bachelorthesis.presentation.authentication.register.RegisterFragment;
import com.sunilson.bachelorthesis.presentation.shared.baseClasses.BaseActivity;
import com.sunilson.bachelorthesis.presentation.shared.baseClasses.HasViewModel;
import com.sunilson.bachelorthesis.presentation.shared.utilities.Constants;
import com.sunilson.bachelorthesis.presentation.shared.viewmodelBasics.ViewModelFactory;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * @author Linus Weiss
 */

public class AuthenticationActivity extends BaseActivity implements HasViewModel<AuthenticationViewModel>, HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Inject
    ViewModelFactory viewModelFactory;

    private AuthenticationViewModel authenticationViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);

        if(savedInstanceState == null) {
            changeFragment(RegisterFragment.newInstance(), Constants.FRAGMENT_TAG_Register);
        }

        authenticationViewModel = ViewModelProviders.of(this, viewModelFactory).get(AuthenticationViewModel.class);
    }

    /**
     * Replace the fragment in this activity
     *
     * @param fragment New Fragment
     * @param tag Tag to later find this fragment
     */
    private void changeFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_authentication_framelayout, fragment, tag).commit();
    }

    /**
     * Add new fragment to this activity with transition
     *
     * @param fragment New fragment
     * @param tag Tag to later find this fragment and for backstack
     */
    private void addFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().add(R.id.activity_authentication_framelayout, fragment, tag).addToBackStack(tag).commit();
    }

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, AuthenticationActivity.class);
        return intent;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    public AuthenticationViewModel getViewModel() {
        return this.authenticationViewModel;
    }

    @Override
    public void loading(boolean value) {

    }
}
