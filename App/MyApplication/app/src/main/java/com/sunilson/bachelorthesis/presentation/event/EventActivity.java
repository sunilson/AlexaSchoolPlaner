package com.sunilson.bachelorthesis.presentation.event;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.sunilson.bachelorthesis.R;
import com.sunilson.bachelorthesis.presentation.shared.baseClasses.BaseActivity;
import com.sunilson.bachelorthesis.presentation.shared.viewmodelBasics.ViewModelFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

/**
 * @author Linus Weiss
 */

public class EventActivity extends BaseActivity {

    @BindView(R.id.activity_event_header)
    LinearLayout event_header;

    @Inject
    ViewModelFactory viewModelFactory;

    private EventViewModel eventViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        eventViewModel = ViewModelProviders.of(this, viewModelFactory).get(EventViewModel.class);



        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                //supportFinishAfterTransition();
                finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
