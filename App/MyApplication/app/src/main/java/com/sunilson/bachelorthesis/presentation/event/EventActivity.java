package com.sunilson.bachelorthesis.presentation.event;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sunilson.bachelorthesis.R;
import com.sunilson.bachelorthesis.presentation.baseClasses.BaseActivity;
import com.sunilson.bachelorthesis.presentation.viewmodelBasics.ViewModelFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Created by linus_000 on 12.11.2017.
 */

public class EventActivity extends BaseActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    private EventViewModel eventViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eventViewModel = ViewModelProviders.of(this, viewModelFactory).get(EventViewModel.class);
    }

    /**
     * Creates an Intent that can be used to navigate to this Activity
     *
     * @param context
     * @return  Intent to navigate to this Activity
     */
    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, EventActivity.class);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
