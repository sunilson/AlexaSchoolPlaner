package com.sunilson.bachelorthesis.presentation.shared.baseClasses;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sunilson.bachelorthesis.R;

import butterknife.ButterKnife;

/**
 * @author Linus Weiss
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
