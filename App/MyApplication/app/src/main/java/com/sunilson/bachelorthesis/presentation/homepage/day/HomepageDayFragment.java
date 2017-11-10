package com.sunilson.bachelorthesis.presentation.homepage.day;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunilson.bachelorthesis.R;
import com.sunilson.bachelorthesis.presentation.baseClasses.BaseFragment;
import com.sunilson.bachelorthesis.presentation.models.events.Event;
import com.sunilson.bachelorthesis.presentation.models.events.EventType;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Linus Weiss
 */

public class HomepageDayFragment extends BaseFragment {

    @BindView(R.id.fragment_homepage_day_view)
    DayView dayView;

    @BindView(R.id.fragment_homepage_day_view_scroll_view)
    NestedScrollView nestedScrollView;

    private Unbinder unbinder;

    public static HomepageDayFragment newInstance() {
        Bundle args = new Bundle();
        HomepageDayFragment fragment = new HomepageDayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dayView.init();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CalendarDayModel calendarDayModel = new CalendarDayModel();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                try {
                    calendarDayModel.getEventList().add(new Event(sdf.parse("20/12/1993 03:45"), sdf.parse("20/12/1993 08:55"), "Event 1", EventType.SCHOOLAPPOINTMENT, null));
                    calendarDayModel.getEventList().add(new Event(sdf.parse("20/12/1993 10:45"), sdf.parse("20/12/1993 18:58"), "Event 1", EventType.SCHOOLAPPOINTMENT, null));
                    calendarDayModel.getEventList().add(new Event(sdf.parse("20/12/1993 10:55"), sdf.parse("20/12/1993 11:55"), "Event 2", EventType.SCHOOLAPPOINTMENT, null));
                    calendarDayModel.getEventList().add(new Event(sdf.parse("20/12/1993 10:55"), sdf.parse("20/12/1993 11:55"), "Event 2", EventType.SCHOOLAPPOINTMENT, null));
                    calendarDayModel.getEventList().add(new Event(sdf.parse("20/12/1993 11:05"), sdf.parse("20/12/1993 12:25"), "Event 3", EventType.SCHOOLAPPOINTMENT, null));
                    calendarDayModel.getEventList().add(new Event(sdf.parse("20/12/1993 12:35"), sdf.parse("20/12/1993 13:35"), "Event 4", EventType.SCHOOLAPPOINTMENT, null));
                    calendarDayModel.getEventList().add(new Event(sdf.parse("20/12/1993 13:45"), sdf.parse("20/12/1993 15:55"), "Event 5", EventType.SCHOOLAPPOINTMENT, null));
                    calendarDayModel.getEventList().add(new Event(sdf.parse("20/12/1993 15:52"), sdf.parse("20/12/1993 16:55"), "Event 6", EventType.SCHOOLAPPOINTMENT, null));
                    calendarDayModel.getEventList().add(new Event(sdf.parse("20/12/1993 16:58"), sdf.parse("20/12/1993 18:55"), "Event 7", EventType.SCHOOLAPPOINTMENT, null));
                    calendarDayModel.getEventList().add(new Event(sdf.parse("20/12/1993 19:08"), sdf.parse("20/12/1993 20:55"), "Event 8", EventType.SCHOOLAPPOINTMENT, null));
                    calendarDayModel.setDayStartDate(sdf.parse("20/12/1993 00:00"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dayView.addDay(calendarDayModel);
                nestedScrollView.post(new Runnable() {
                    public void run() {
                        nestedScrollView.scrollTo(0, dayView.getTopView().getTop());
                    }
                });
            }
        }, 2000);
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
