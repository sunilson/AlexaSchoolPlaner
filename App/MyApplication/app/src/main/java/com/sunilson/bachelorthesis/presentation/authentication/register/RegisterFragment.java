package com.sunilson.bachelorthesis.presentation.authentication.register;

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
import com.sunilson.bachelorthesis.presentation.shared.baseClasses.HasViewModel;
import com.sunilson.bachelorthesis.presentation.shared.utilities.DisposableManager;
import com.sunilson.bachelorthesis.presentation.shared.utilities.Navigator;
import com.sunilson.bachelorthesis.presentation.shared.utilities.ValidationHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * @author Linus Weiss
 */

public class RegisterFragment extends Fragment {

    private Unbinder unbinder;

    @Inject
    DisposableManager disposableManager;

    @Inject
    ValidationHelper validationHelper;

    @BindView(R.id.fragment_register_email)
    EditText email;
    @BindView(R.id.fragment_register_password)
    EditText password;
    @BindView(R.id.fragment_register_repeat_password)
    EditText repeatPassword;
    @BindView(R.id.fragment_register_username)
    EditText username;

    @OnClick(R.id.fragment_register_submit)
    public void register() {
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        String repeatPassword = this.repeatPassword.getText().toString();
        String username = this.username.getText().toString();

        if(validationHelper.validateUsername(username)
                && validationHelper.validatePassword(password)
                && validationHelper.validateEmail(email)
                && password.equals(repeatPassword)) {
            if (getActivity() instanceof HasViewModel && ((HasViewModel) getActivity()).getViewModel() instanceof AuthenticationViewModel) {
                AuthenticationViewModel authenticationViewModel = ((AuthenticationViewModel) ((HasViewModel) getActivity()).getViewModel());
                disposableManager.add(authenticationViewModel.register(username, email, password).subscribeWith(new RegisterObserver()));
            }
        } else {
            Toast.makeText(getContext(), "Invalid data!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        disposableManager.dispose();
        super.onDestroy();
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    private final class RegisterObserver extends DisposableObserver<Boolean> {

        @Override
        public void onNext(Boolean o) {
            if(o) {
                Navigator.navigateToHomepage(getActivity());
                getActivity().finish();
            }
        }

        @Override
        public void onError(Throwable e) {

            if(e instanceof HttpException) {
                HttpException httpException = (HttpException) e;
                Toast.makeText(getContext(), "Error: " + Integer.toString(httpException.code()), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error. Please make sure that you have an internet connection!", Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        public void onComplete() {

        }
    }
}
