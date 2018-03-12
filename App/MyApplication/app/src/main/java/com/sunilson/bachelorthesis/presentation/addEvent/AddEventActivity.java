package com.sunilson.bachelorthesis.presentation.addEvent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.sunilson.bachelorthesis.R;
import com.sunilson.bachelorthesis.databinding.ActivityAddEventBinding;
import com.sunilson.bachelorthesis.domain.calendar.interactors.AddEventUseCase;
import com.sunilson.bachelorthesis.domain.calendar.model.DomainEvent;
import com.sunilson.bachelorthesis.presentation.event.EventModel;
import com.sunilson.bachelorthesis.presentation.event.mapper.DomainEventToEventModelMapper;
import com.sunilson.bachelorthesis.presentation.shared.baseClasses.BaseActivity;
import com.sunilson.bachelorthesis.presentation.shared.utilities.Constants;
import com.sunilson.bachelorthesis.presentation.shared.utilities.DisposableManager;
import com.sunilson.bachelorthesis.presentation.shared.utilities.ValidationHelper;
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

public class AddEventActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @BindView(R.id.activity_add_event_from_date_text)
    TextView fromDateText;
    @BindView(R.id.activity_add_event_to_date_text)
    TextView toDateText;
    @BindView(R.id.activity_add_event_description)
    EditText description;
    @BindView(R.id.activity_add_event_summary)
    EditText summary;
    @BindView(R.id.activity_add_event_location)
    EditText location;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    ValidationHelper validationHelper;
    @Inject
    DisposableManager disposableManager;
    @Inject
    AddEventUseCase addEventUseCase;
    @Inject
    DomainEventToEventModelMapper domainEventToEventModelMapper;
    private DateTime temp;
    private DateTime from = new DateTime();
    private DateTime to = from.plusHours(1);
    private AddEventViewModel addEventViewModel;
    private int eventType;
    private String pickerType;
    private ActivityAddEventBinding binding;

    /**
     * Creates an Intent that can be used to navigate to this Activity
     *
     * @param context
     * @return Intent to navigate to this Activity
     */
    public static Intent getCallingIntent(Context context, int eventType) {
        Intent intent = new Intent(context, AddEventActivity.class);
        intent.putExtra(Constants.INTENT_EVENT_TYPE, eventType);
        return intent;
    }

    @OnClick(R.id.activity_add_event_from_date_button)
    public void fromClick() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, this, from.getYear(), from.getMonthOfYear() - 1, from.getDayOfMonth());
        datePickerDialog.show();
        pickerType = "from";
    }

    @OnClick(R.id.activity_add_event_to_date_button)
    public void toClick() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, this, to.getYear(), to.getMonthOfYear() - 1, to.getDayOfMonth());
        datePickerDialog.show();
        pickerType = "to";
    }

    @OnClick(R.id.activity_add_event_submit)
    public void submit() {
        addEvent();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_event);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addEventViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddEventViewModel.class);
        this.eventType = getIntent().getIntExtra(Constants.INTENT_EVENT_TYPE, 0);

        binding.setFrom(from);
        binding.setTo(to);
        binding.setTyp(this.eventType);

        switch (this.eventType) {
            case Constants.DEADLINE:
                setTitle(getString(R.string.add_event_deadline));
                break;
            case Constants.SPECIALAPPOINTMENT:
                setTitle(getString(R.string.add_event_appointment));
                break;
        }

        ButterKnife.bind(this);
    }

    private void addEvent() {
        String summary = this.summary.getText().toString();
        String description = this.description.getText().toString();
        String location = this.location.getText().toString();

        if (!validationHelper.validateString(false, summary, null, null) ||
                !validationHelper.validateString(true, description, null, null) ||
                !validationHelper.validateString(true, location, null, null) ||
                !validationHelper.validateFromToDates(from, to)) {
            Toast.makeText(this, "Please fill out all inputs correctly!", Toast.LENGTH_SHORT).show();
            return;
        }

        EventModel eventModel = new EventModel();
        eventModel.setSummary(summary);
        eventModel.setDescription(description);
        eventModel.setLocation(location);
        eventModel.setFrom(from);
        eventModel.setTo(to);
        eventModel.setEventType(Constants.EVENT_TYPES[eventType]);

        disposableManager.add(addEventUseCase.execute(AddEventUseCase.Params
                .forDaySpan(domainEventToEventModelMapper.toDomainEvent(eventModel)))
                .subscribeWith(new DisposableObserver<DomainEvent>() {

                    @Override
                    public void onNext(DomainEvent domainEvent) {
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AddEventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );
    }

    private void openTimePicker() {
        DateTime startTime;
        if (pickerType.equals("from")) {
            startTime = from;
        } else {
            startTime = to;
        }
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this, startTime.getHourOfDay(), startTime.getMinuteOfHour(), true);
        timePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        temp = new DateTime(year, month + 1, day, 0, 0);
        openTimePicker();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        if (pickerType.equals("from")) {
            from = new DateTime(temp.getYear(), temp.getMonthOfYear(), temp.getDayOfMonth(), hour, minute);
            if (from.isAfter(to) || from.isEqual(to) || eventType == Constants.DEADLINE) {
                to = from.plusHours(1);
            }
            fromDateText.setText(from.toString());
        } else {
            to = new DateTime(temp.getYear(), temp.getMonthOfYear(), temp.getDayOfMonth(), hour, minute);
            if (to.isBefore(from) || to.isEqual(from)) {
                to = from.plusHours(1);
            }
            toDateText.setText(to.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposableManager.dispose();
    }
}
