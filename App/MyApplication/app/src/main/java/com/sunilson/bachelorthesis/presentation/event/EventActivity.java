package com.sunilson.bachelorthesis.presentation.event;

import android.animation.AnimatorSet;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import dagger.android.AndroidInjection;
import io.reactivex.observers.DisposableObserver;

/**
 * @author Linus Weiss
 */

public class EventActivity extends BaseActivity {

    @BindView(R.id.activity_event_header)
    RelativeLayout event_header;

    @BindView(R.id.activity_event_fab)
    View fab;

    @BindView(R.id.activity_event_title)
    TextView title;

    @BindView(R.id.activity_event_from)
    TextView from;

    @BindView(R.id.activity_event_to)
    TextView to;

    @BindView(R.id.activity_event_description)
    TextView description;

    @BindView(R.id.activity_event_content_container)
    View contentContainer;

    @Inject
    ViewModelFactory viewModelFactory;

    private Animation fadeIn, fadeInMoveUp, scaleIn, slideUp;
    private EventViewModel eventViewModel;
    private int eventColor;
    private ActivityEventBinding binding;

    @Inject
    DisposableManager disposableManager;

    @Inject
    ConnectionManager connectionManager;

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
        eventColor = getResources().getColor(getIntent().getIntExtra(Constants.INTENT_EVENT_TYPE, 0));
        getWindow().setStatusBarColor(this.eventColor);
        event_header.setBackgroundColor(this.eventColor);

        if(savedInstanceState != null) {
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

    /**
     * Creates an Intent that can be used to navigate to this Activity
     *
     * @param context
     * @return  Intent to navigate to this Activity
     */
    public static Intent getCallingIntent(Context context, int eventType) {
        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtra(Constants.INTENT_EVENT_TYPE, eventType);
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

    @Override
    protected void onDestroy() {
        disposableManager.dispose();
        super.onDestroy();
    }

    private final class SingleEventObserverOnline extends DisposableObserver<EventModel> {
        @Override
        public void onNext(EventModel eventModel) {
            binding.setEvent(eventModel);
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
            binding.setEvent(eventModel);
            fab.startAnimation(scaleIn);
            title.startAnimation(fadeInMoveUp);
            contentContainer.startAnimation(slideUp);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Load online data
                    disposableManager.add(eventViewModel.getSingleEvent(getIntent().getStringExtra(Constants.INTENT_EVENT_ID)).subscribeWith(new SingleEventObserverOnline()));
                }
            }, 500);

        }

        @Override
        public void onError(Throwable e) {
            //TODO If network error load offline
        }

        @Override
        public void onComplete() {

        }
    }
}
