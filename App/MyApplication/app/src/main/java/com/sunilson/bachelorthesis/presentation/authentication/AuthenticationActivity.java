package com.sunilson.bachelorthesis.presentation.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.sunilson.bachelorthesis.R;
import com.sunilson.bachelorthesis.presentation.shared.baseClasses.BaseActivity;

import butterknife.ButterKnife;

/**
 * @author Linus Weiss
 */

public class AuthenticationActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);
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
}
