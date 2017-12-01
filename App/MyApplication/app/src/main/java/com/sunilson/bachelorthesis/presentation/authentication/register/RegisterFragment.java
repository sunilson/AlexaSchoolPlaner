package com.sunilson.bachelorthesis.presentation.authentication.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunilson.bachelorthesis.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Linus Weiss
 */

public class RegisterFragment extends Fragment {

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        unbinder = ButterKnife.bind(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
