package com.sunilson.bachelorthesis.presentation.event;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sunilson.bachelorthesis.R;
import com.sunilson.bachelorthesis.databinding.ActivityEventBinding;
import com.sunilson.bachelorthesis.presentation.shared.baseClasses.BaseActivity;
import com.sunilson.bachelorthesis.presentation.shared.utilities.ConnectionManager;
import com.sunilson.bachelorthesis.presentation.shared.utilities.Constants;
import com.sunilson.bachelorthesis.presentation.shared.utilities.DisposableManager;
import com.sunilson.bachelorthesis.presentation.shared.viewmodelBasics.ViewModelFactory;

import org.joda.time.DateTime;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import io.reactivex.observers.DisposableObserver;

/**
 * @author Linus Weiss
 */

public class EventActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    //ButterKnife view binding
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
    @BindView(R.id.activity_event_edit_description)
    EditText editDescription;
    @BindView(R.id.activity_event_edit_summary)
    EditText editSummary;
    @BindView(R.id.activity_event_edit_location)
    EditText editLocation;
    @BindView(R.id.activity_event_show)
    View eventContainer;

    //Dagger injection
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    DisposableManager disposableManager;
    @Inject
    ConnectionManager connectionManager;

    private Animation fadeIn, fadeInMoveUp, scaleIn, slideUp;
    private EventViewModel eventViewModel;
    private ActivityEventBinding binding;
    private EventModel currentEvent;
    private EventModel editEvent;
    private DateTime editTempDate;
    private String pickType;

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

    //Butterknife OnClick Bindings
    @OnClick(R.id.activity_event_edit_from_button)
    public void editFromDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, this, currentEvent.getFrom().getYear(), currentEvent.getFrom().getMonthOfYear() - 1, currentEvent.getFrom().getDayOfMonth());
        datePickerDialog.show();
        pickType = "from";
    }

    @OnClick(R.id.activity_event_edit_to_button)
    public void editToDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, this, currentEvent.getTo().getYear(), currentEvent.getTo().getMonthOfYear() - 1, currentEvent.getTo().getDayOfMonth());
        datePickerDialog.show();
        pickType = "to";
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
        editEvent = new EventModel(currentEvent);
        binding.setEditEvent(editEvent);
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

        editEvent.setDescription(editDescription.getText().toString());
        if (!editSummary.getText().toString().isEmpty())
            editEvent.setSummary(editSummary.getText().toString());
        editEvent.setLocation(editLocation.getText().toString());

        disposableManager.add(eventViewModel.editEvent(editEvent).subscribeWith(new DisposableObserver<EventModel>() {
            @Override
            public void onNext(EventModel eventModel) {
                currentEvent = eventModel;
                binding.setEvent(currentEvent);
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
        int eventColor = getResources().getColor(getIntent().getIntExtra(Constants.INTENT_EVENT_TYPE, 0));
        getWindow().setStatusBarColor(eventColor);
        event_header.setBackgroundColor(eventColor);
        fab.setBackgroundTintList(ColorStateList.valueOf(eventColor));

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
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        //Stop all observables
        disposableManager.dispose();
        super.onDestroy();
    }

    /**
     * Toggle edit mode of the event
     */
    private void closeEdit() {
        editContainer.setVisibility(View.INVISIBLE);
        eventContainer.setVisibility(View.VISIBLE);
    }

    private void openTimePicker() {
        DateTime startTime;
        if (pickType.equals("from")) {
            startTime = currentEvent.getFrom();
        } else {
            startTime = currentEvent.getTo();
        }
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, startTime.getHourOfDay(), startTime.getMinuteOfHour(), true);
        timePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        editTempDate = new DateTime(year, month + 1, day, 0, 0);
        openTimePicker();
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        if (pickType.equals("from")) {
            editEvent.setFrom(new DateTime(editTempDate.getYear(), editTempDate.getMonthOfYear(), editTempDate.getDayOfMonth(), hour, minute));
            if (editEvent.getFrom().isAfter(editEvent.getTo()) || editEvent.getFrom().isEqual(editEvent.getTo()) || editEvent.getEventType() == EventType.DEADLINE) {
                editEvent.setTo(editEvent.getFrom().plusHours(1));
            }
        } else {
            editEvent.setTo(new DateTime(editTempDate.getYear(), editTempDate.getMonthOfYear(), editTempDate.getDayOfMonth(), hour, minute));
            if (editEvent.getTo().isBefore(editEvent.getFrom()) || editEvent.getTo().isEqual(editEvent.getFrom())) {
                editEvent.setTo(editEvent.getFrom().plusHours(1));
            }
        }
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
            //Update event databinding. We could also use bindable properties but we always get
            //the whole event back, so it doesn't really matter if we update the whole thing
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
