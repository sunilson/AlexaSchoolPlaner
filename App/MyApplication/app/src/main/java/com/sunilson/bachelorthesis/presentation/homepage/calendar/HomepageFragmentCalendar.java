package com.sunilson.bachelorthesis.presentation.homepage.calendar;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sunilson.bachelorthesis.R;
import com.sunilson.bachelorthesis.presentation.homepage.calendar.calendarDay.CalendarDayModel;
import com.sunilson.bachelorthesis.presentation.homepage.calendar.calendarDay.CalendarDayView;
import com.sunilson.bachelorthesis.presentation.homepage.exception.CalendarSettingsException;
import com.sunilson.bachelorthesis.presentation.homepage.utilities.HomepageCalendarHelper;
import com.sunilson.bachelorthesis.presentation.shared.baseClasses.HasViewModel;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;

/**
 * @author Linus Weiss
 */

public class HomepageFragmentCalendar extends Fragment {

    @BindView(R.id.fragment_homepage_day_view_scroll_view)
    NestedScrollView nestedScrollView;

    @BindView(R.id.fragment_homepage_day_container)
    LinearLayout dayContainer;

    @Inject
    HomepageCalendarHelper homepageCalendarHelper;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
            initializeCalendarDays();
        }


    private void initializeCalendarDays(){

        DateTime[] dateTimes = new DateTime[0];
        try {
            dateTimes = homepageCalendarHelper.convertToBoundDates(dayAmount);
        } catch (CalendarSettingsException e) {
            e.printStackTrace();
            //TODO Exception Handling
        }

        //Check if Activity implements HasViewModel interface and returns the correct type of ViewModel
        if (getActivity() instanceof HasViewModel && ((HasViewModel) getActivity()).getViewModel() instanceof CalendarViewModel) {

            //Show loading
            ((HasViewModel) getActivity()).loading(true);

            //Get data for calculated dates
            ((CalendarViewModel)((HasViewModel) getActivity()).getViewModel()).getDays(dateTimes[0], dateTimes[1]).observe(this, new Observer<CalendarDaySpanModel>() {
                @Override
                public void onChanged(@Nullable CalendarDaySpanModel days) {
                    Log.d("Linus", "Changed");
                    ((HasViewModel) getActivity()).loading(false);
                    CalendarDayView calendarDayView;
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
                    dayContainer.removeAllViews();
                    for (int i = 0; i < days.getDayModels().size(); i++) {
                        calendarDayView = new CalendarDayView(getContext());
                        calendarDayView.setLayoutParams(layoutParams);
                        calendarDayView.init(days.getDayModels().get(i));
                        dayContainer.addView(calendarDayView);
                    }
                }
            });
        } else {
            getActivity().finish();
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
