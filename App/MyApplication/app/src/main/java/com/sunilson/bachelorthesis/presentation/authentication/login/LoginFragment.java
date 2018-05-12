package com.sunilson.bachelorthesis.presentation.authentication.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.sunilson.bachelorthesis.R;
import com.sunilson.bachelorthesis.presentation.authentication.AuthenticationViewModel;
import com.sunilson.bachelorthesis.presentation.authentication.register.RegisterFragment;
import com.sunilson.bachelorthesis.presentation.shared.baseClasses.HasFragments;
import com.sunilson.bachelorthesis.presentation.shared.baseClasses.HasViewModel;
import com.sunilson.bachelorthesis.presentation.shared.utilities.Constants;
import com.sunilson.bachelorthesis.presentation.shared.utilities.DisposableManager;
import com.sunilson.bachelorthesis.presentation.shared.utilities.Navigator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.observers.DisposableObserver;

/**
 * @author Linus Weiss
 *
 * Fragment with login form and validation of that form
 */

public class LoginFragment extends Fragment {

    @BindView(R.id.fragment_login_name)
    EditText name;

    @BindView(R.id.fragment_login_password)
    EditText password;

    @Inject
    DisposableManager disposableManager;

    @OnClick(R.id.fragment_login_register)
    public void register(View v) {
        if(getActivity() instanceof HasFragments) {
            ((HasFragments) getActivity()).changeFragment(RegisterFragment.newInstance(), Constants.FRAGMENT_TAG_Register);
        }
    }

    @OnClick(R.id.fragment_login_login)
    public void login(View v) {

        String name = this.name.getText().toString();
        String password = this.password.getText().toString();

        //Validate inputs
        if(!name.isEmpty() && !password.isEmpty()) {
            if(getActivity() instanceof  HasViewModel) {
                AuthenticationViewModel authenticationViewModel = ((AuthenticationViewModel) ((HasViewModel) getActivity()).getViewModel());
                disposableManager.add(authenticationViewModel.login(name, password).subscribeWith(new LoginObserver()));
            }
        } else Toast.makeText(getContext(), "Please fill out all fields!", Toast.LENGTH_SHORT).show();
    }

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        this.disposableManager.dispose();
        super.onDestroy();
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    private class LoginObserver extends DisposableObserver<Boolean> {
        @Override
        public void onNext(Boolean aBoolean) {
            if(aBoolean) {
                Navigator.navigateToHomepage(getContext());
                getActivity().finish();
            }
        }

        @Override
        public void onError(Throwable e) {
            Toast.makeText(getContext(), "Login error! Please try again.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete() {
        }
    }
}
