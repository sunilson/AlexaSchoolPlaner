package com.sunilson.bachelorthesis.presentation.event;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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
            fab.startAnimation(scaleIn);
        } else {
            //Listen to Activity Transition and start loading data after its done
            getWindow().getEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    //Load Event data
                    disposableManager.add(eventViewModel.getSingleEvent(getIntent().getStringExtra(Constants.INTENT_EVENT_ID)).subscribeWith(new SingleEventObserver()));
                    fab.startAnimation(scaleIn);
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

    private final class SingleEventObserver extends DisposableObserver<EventModel> {

        @Override
        public void onNext(EventModel eventModel) {
            binding.setEvent(eventModel);
            title.startAnimation(fadeInMoveUp);
            contentContainer.startAnimation(slideUp);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    }
}
