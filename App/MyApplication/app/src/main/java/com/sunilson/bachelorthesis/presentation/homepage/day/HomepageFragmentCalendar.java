package com.sunilson.bachelorthesis.presentation.homepage.day;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sunilson.bachelorthesis.R;
import com.sunilson.bachelorthesis.presentation.baseClasses.BaseFragment;
import com.sunilson.bachelorthesis.presentation.homepage.CurrentCalendarSettingsStorage;
import com.sunilson.bachelorthesis.presentation.homepage.HomepageActivity;
import com.sunilson.bachelorthesis.presentation.homepage.exception.CalendarDayModelInvalidEventException;
import com.sunilson.bachelorthesis.presentation.homepage.exception.CalendarSettingsException;
import com.sunilson.bachelorthesis.presentation.event.models.Event;
import com.sunilson.bachelorthesis.presentation.event.models.EventType;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;

/**
 * @author Linus Weiss
 */

public class HomepageFragmentCalendar extends BaseFragment {

    @BindView(R.id.fragment_homepage_day_view_scroll_view)
    NestedScrollView nestedScrollView;

    @BindView(R.id.fragment_homepage_day_container)
    LinearLayout dayContainer;

    private Unbinder unbinder;
    private Integer dayAmount;
    private List<CalendarDayModel> days = new ArrayList<>();

    /**
     * Create a new instance of the HomepageFragmentCalendar
     *
     * @param dayAmount Amount of days that should be displayed. Can be null, then the currently saved dates will be used
     * @return Instance of HomepageFragmentCalendar
     */
    public static HomepageFragmentCalendar newInstance(@Nullable Integer dayAmount) {
        Bundle args = new Bundle();
        if (dayAmount != null) {
            args.putInt("dayAmount", dayAmount);
        }
        HomepageFragmentCalendar fragment = new HomepageFragmentCalendar();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dayAmount = getArguments().getInt("dayAmount");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeCalendarDays();
    }

    private void initializeCalendarDays() {
        DateTime[] dateTimes = null;
        try {
            //Get current times from local storage
            dateTimes = CurrentCalendarSettingsStorage.getDates(getContext());

            //Formatter for title in Toolbar
            DateTimeFormatter dtf = DateTimeFormat.forPattern("dd.MM.yyyy");

            //Check if a day amount has been given, otherwise calculate it from the current from and to date
            if (dayAmount == 0) {
                dayAmount = Days.daysBetween(dateTimes[0].toLocalDate(), dateTimes[1].toLocalDate()).getDays() + 1;
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(dtf.print(dateTimes[0]) + " - " + dtf.print(dateTimes[1]));
            } else {
                //If day amount has been given, set new to date
                dateTimes[1] = dateTimes[0].plusDays(dayAmount - 1);
                CurrentCalendarSettingsStorage.setDates(getContext(), null, dateTimes[1]);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(dtf.print(dateTimes[0]) + " - " + dtf.print(dateTimes[1]));
            }
        } catch (CalendarSettingsException e) {
            e.printStackTrace();
            //TODO error handling
        }

        ((HomepageActivity) getActivity()).getViewModel().getDays(dateTimes[0], dateTimes[1]).observe(this, new Observer<CalendarDaySpanModel>() {
            @Override
            public void onChanged(@Nullable CalendarDaySpanModel calendarDaySpanModel) {
                Log.d("Linus", "bla");
                if (calendarDaySpanModel != null) {
                    Log.d("Linus", calendarDaySpanModel.toString());
                }
            }
        });

        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");

        CalendarDayModel calendarDayModel2 = new CalendarDayModel(formatter.parseDateTime("20/12/1993 10:45"));
        try {
            calendarDayModel2.add(new Event(formatter.parseDateTime("20/12/1993 03:45"), formatter.parseDateTime("20/12/1993 08:55"), "Event 1", EventType.SCHOOLAPPOINTMENT, null));

            calendarDayModel2.add(new Event(formatter.parseDateTime("20/12/1993 10:45"), formatter.parseDateTime("20/12/1993 18:58"), "Event 1", EventType.SCHOOLAPPOINTMENT, null));

            CalendarDayModel calendarDayModel = new CalendarDayModel(formatter.parseDateTime("20/12/1993 10:45"));
            calendarDayModel.add(new Event(formatter.parseDateTime("19/12/1993 10:45"), formatter.parseDateTime("21/12/1993 18:58"), "Event 1", EventType.SCHOOLAPPOINTMENT, null));
            calendarDayModel.add(new Event(formatter.parseDateTime("20/12/1993 10:55"), formatter.parseDateTime("20/12/1993 11:55"), "Event 2", EventType.SCHOOLAPPOINTMENT, null));
            calendarDayModel.add(new Event(formatter.parseDateTime("20/12/1993 10:55"), formatter.parseDateTime("20/12/1993 11:55"), "Event 2", EventType.SCHOOLAPPOINTMENT, null));
            calendarDayModel.add(new Event(formatter.parseDateTime("20/12/1993 11:05"), formatter.parseDateTime("20/12/1993 12:25"), "Event 3", EventType.SCHOOLAPPOINTMENT, null));
            calendarDayModel.add(new Event(formatter.parseDateTime("20/12/1993 12:35"), formatter.parseDateTime("20/12/1993 13:35"), "Event 4", EventType.SCHOOLAPPOINTMENT, null));
            calendarDayModel.add(new Event(formatter.parseDateTime("20/12/1993 13:45"), formatter.parseDateTime("20/12/1993 15:55"), "Event 5", EventType.SCHOOLAPPOINTMENT, null));
            calendarDayModel.add(new Event(formatter.parseDateTime("20/12/1993 15:50"), formatter.parseDateTime("20/12/1993 16:55"), "Event 6", EventType.SCHOOLAPPOINTMENT, null));
            calendarDayModel.add(new Event(formatter.parseDateTime("20/12/1993 16:58"), formatter.parseDateTime("20/12/1993 18:55"), "Event 7", EventType.SCHOOLAPPOINTMENT, null));
            calendarDayModel.add(new Event(formatter.parseDateTime("20/12/1993 19:08"), formatter.parseDateTime("20/12/1993 20:55"), "Event 8", EventType.SCHOOLAPPOINTMENT, null));

            for (int i = 0; i < dayAmount; i++) {
                if (i == 1) {
                    days.add(calendarDayModel2);
                } else {
                    days.add(calendarDayModel);
                }
            }
        } catch (CalendarDayModelInvalidEventException e) {
            e.printStackTrace();
        }

        DayView dayView;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        for (int i = 0; i < days.size(); i++) {
            dayView = new DayView(getContext());
            dayView.setLayoutParams(layoutParams);
            dayView.init(days.get(i));
            this.dayContainer.addView(dayView);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage_day, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
