package com.sunilson.bachelorthesis.presentation.homepage.calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunilson.bachelorthesis.R;
import com.sunilson.bachelorthesis.presentation.homepage.calendar.calendarDay.CalendarDayModel;
import com.sunilson.bachelorthesis.presentation.homepage.calendar.calendarDay.CalendarDayView;
import com.sunilson.bachelorthesis.presentation.homepage.utilities.HomepageCalendarHelper;
import com.sunilson.bachelorthesis.presentation.shared.baseClasses.HasViewModel;
import com.sunilson.bachelorthesis.presentation.shared.utilities.DisposableManager;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import io.reactivex.observers.DisposableObserver;

/**
 * @author Linus Weiss
 */

public class HomepageFragmentCalendar extends Fragment {

    @BindView(R.id.fragment_homepage_day_view_scroll_view)
    NestedScrollView nestedScrollView;

    @BindView(R.id.fragment_homepage_day_container)
    LinearLayout dayContainer;

    @BindView(R.id.fragment_homepage_header_container)
    LinearLayout headerContainer;

    @Inject
    HomepageCalendarHelper homepageCalendarHelper;

    @Inject
    DisposableManager disposableManager;

    private Unbinder unbinder;
    private List<CalendarDayModel> days = new ArrayList<>();
    private DateTime[] dateTimes = new DateTime[0];

    public static HomepageFragmentCalendar newInstance(Long[] dates) {
        Bundle args = new Bundle();
        if (dates != null) {
            args.putLong("from", dates[0]);
            args.putLong("to", dates[1]);
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
        Long from = getArguments().getLong("from");
        Long to = getArguments().getLong("to");
        dateTimes = new DateTime[]{new DateTime(from), new DateTime(to)};
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage_day, container, false);
        unbinder = ButterKnife.bind(this, view);
        initializeHeader(inflater);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        disposableManager.dispose();
        super.onDestroy();
    }

    private void initializeHeader(LayoutInflater inflater) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("MMMM");
        DateTime[] storedDateTimes = homepageCalendarHelper.getCurrentCalendarDates();
        getActivity().setTitle(dateTimeFormatter.print(storedDateTimes[0]));

        //Get all days for the date interval between from and to and add header elements
        List<DateTime> days = new ArrayList<>();
        int dayAmount = Days.daysBetween(dateTimes[0], dateTimes[1]).getDays()+1;
        for (int i=0; i < dayAmount; i++) {
            DateTime d = dateTimes[0].plusDays(i);
            days.add(d);
        }

        //Format the dates of the DayViews and add them to the Header
        DateTimeFormatter fmt = DateTimeFormat.forPattern("E");
        for (DateTime dateTime : days) {
            LinearLayout header = (LinearLayout) inflater.inflate(R.layout.calendar_header_element, headerContainer, false);
            headerContainer.addView(header);
            ((TextView) header.getChildAt(0)).setText(Integer.toString(dateTime.getDayOfMonth()));
            ((TextView) header.getChildAt(1)).setText(fmt.print(dateTime));
        }
    }

    /**
     * Load event data from current dates from the server
     */
    public void loadData() {
        //Check if Activity implements HasViewModel interface and returns the correct type of ViewModel
        if (getActivity() instanceof HasViewModel && ((HasViewModel) getActivity()).getViewModel() instanceof CalendarViewModel) {
            //Show loading
            ((HasViewModel) getActivity()).loading(true);
            //Add Observer to disposables
            disposableManager.add(((CalendarViewModel) ((HasViewModel) getActivity()).getViewModel()).getDays(dateTimes[0], dateTimes[1]).subscribeWith(new EventListObserver()));
        }
    }

    /**
     * Observer used for listening to the network event requests
     */
    private final class EventListObserver extends DisposableObserver<CalendarDaySpanModel> {

        /**
         * Called when new data arrives
         *
         * @param days The data, formatted into a CalendarDaySpanModel object
         */
        @Override
        public void onNext(CalendarDaySpanModel days) {
            if (dayContainer != null) {
                ((HasViewModel) getActivity()).loading(false);
                CalendarDayView calendarDayView;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
                dayContainer.removeAllViews();

                //Iterate over days and add them to the fragment
                for (int i = 0; i < days.getDayModels().size(); i++) {
                    calendarDayView = new CalendarDayView(getContext());
                    calendarDayView.setLayoutParams(layoutParams);
                    calendarDayView.init(days.getDayModels().get(i));
                    dayContainer.addView(calendarDayView);
                }
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
