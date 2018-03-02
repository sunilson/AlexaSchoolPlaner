package com.sunilson.bachelorthesis.presentation.event;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.sunilson.bachelorthesis.R;
import com.sunilson.bachelorthesis.databinding.ActivityEventBinding;
import com.sunilson.bachelorthesis.presentation.shared.baseClasses.BaseActivity;
import com.sunilson.bachelorthesis.presentation.shared.utilities.ConnectionManager;
import com.sunilson.bachelorthesis.presentation.shared.utilities.Constants;
import com.sunilson.bachelorthesis.presentation.shared.utilities.DisposableManager;
import com.sunilson.bachelorthesis.presentation.shared.viewmodelBasics.ViewModelFactory;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import io.reactivex.observers.DisposableObserver;

/**
 * @author Linus Weiss
 */

public class EventActivity extends BaseActivity {

    @BindView(R.id.activity_event_header)
    View event_header;
    @BindView(R.id.activity_event_fab)
    View fab;
    @BindView(R.id.activity_event_title)
    TextView title;
    @BindView(R.id.activity_event_from)
    TextView from;
    @BindView(R.id.activity_event_description)
    TextView description;
    @BindView(R.id.activity_event_content_container)
    View contentContainer;
    @BindView(R.id.activity_event_edit_container)
    View editContainer;
    @BindView(R.id.activity_event_show)
    View eventContainer;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    DisposableManager disposableManager;
    @Inject
    ConnectionManager connectionManager;
    private Animation fadeIn, fadeInMoveUp, scaleIn, slideUp;
    private EventViewModel eventViewModel;
    private int eventColor;
    private ActivityEventBinding binding;
    private EventModel currentEvent;

    /**
     * Creates an Intent that can be used to navigate to this Activity
     *
     * @param context
     * @return Intent to navigate to this Activity
     */
    public static Intent getCallingIntent(Context context, int eventColor) {
        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtra(Constants.INTENT_EVENT_TYPE, eventColor);
        return intent;
    }

    @OnClick(R.id.activity_event_close)
    public void close() {
        this.disposableManager.dispose();
        this.finish();
    }

    @OnClick(R.id.activity_event_edit_back)
    public void editBackButton() {
        closeEdit();
    }

    @OnClick(R.id.activity_event_fab)
    public void edit() {
        editContainer.setVisibility(View.VISIBLE);
        eventContainer.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.activity_event_edit_fab)
    public void commitEdit() {
        final ProgressDialog progressDoalog = new ProgressDialog(this);
        progressDoalog.setMessage(getString(R.string.loading));
        progressDoalog.setTitle(getString(R.string.edit_event));
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.setCancelable(false);
        progressDoalog.show();

        //TODO Current Event ändern

        disposableManager.add(eventViewModel.editEvent(currentEvent).subscribeWith(new DisposableObserver<EventModel>() {
            @Override
            public void onNext(EventModel eventModel) {
                currentEvent = eventModel;
                progressDoalog.cancel();
                closeEdit();
            }

            @Override
            public void onError(Throwable e) {
                progressDoalog.cancel();
                Toast.makeText(EventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                closeEdit();
            }

            @Override
            public void onComplete() {

            }
        }));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_event);
        eventViewModel = ViewModelProviders.of(this, viewModelFactory).get(EventViewModel.class);

        //Load animations
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeInMoveUp = AnimationUtils.loadAnimation(this, R.anim.fade_in_move_up);
        scaleIn = AnimationUtils.loadAnimation(this, R.anim.scale_in);
        slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        ButterKnife.bind(this);

        //Set color depending on event type
        eventColor = getResources().getColor(getIntent().getIntExtra(Constants.INTENT_EVENT_TYPE, 0));
        getWindow().setStatusBarColor(this.eventColor);
        event_header.setBackgroundColor(this.eventColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(this.eventColor));

        if (savedInstanceState != null) {
            disposableManager.add(eventViewModel.getSingleEvent(getIntent().getStringExtra(Constants.INTENT_EVENT_ID)).subscribeWith(new SingleEventObserver()));
        } else {
            //Listen to Activity Transition and start loading data after its done
            getWindow().getEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    //Load offline Event data first
                    disposableManager.add(eventViewModel.getOfflineSingleEvent(getIntent().getStringExtra(Constants.INTENT_EVENT_ID)).subscribeWith(new SingleEventObserver()));
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    protected void onDestroy() {
        //cleanup
        disposableManager.dispose();
        super.onDestroy();
    }

    private void closeEdit() {
        editContainer.setVisibility(View.INVISIBLE);
        eventContainer.setVisibility(View.VISIBLE);
    }

    private final class SingleEventObserverOnline extends DisposableObserver<EventModel> {
        @Override
        public void onNext(EventModel eventModel) {
            currentEvent = eventModel;
        }

        @Override
        public void onError(Throwable e) {
            //TODO If network error load offline
        }

        @Override
        public void onComplete() {

        }
    }

    private final class SingleEventObserver extends DisposableObserver<EventModel> {

        @Override
        public void onNext(EventModel eventModel) {
            currentEvent = eventModel;
            binding.setEvent(currentEvent);
            fab.startAnimation(scaleIn);
            title.startAnimation(fadeInMoveUp);
            contentContainer.startAnimation(slideUp);

            //Also get data from server if device is online
            if (connectionManager.isConnected()) {
                new Handler().postDelayed(() -> disposableManager.add(eventViewModel.getSingleEvent(getIntent().getStringExtra(Constants.INTENT_EVENT_ID)).subscribeWith(new SingleEventObserverOnline())), 500);
            }
        }

        @Override
        public void onError(Throwable e) {
        }

        @Override
        public void onComplete() {

        }
    }
}
