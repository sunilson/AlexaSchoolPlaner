package com.sunilson.bachelorthesis.presentation.homepage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunilson.bachelorthesis.R;
import com.sunilson.bachelorthesis.presentation.homepage.utilities.HomepageCalendarHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;

/**
 * @author Linus Weiss
 */

public class HomepageCalendarContainerFragment extends Fragment {

    @BindView(R.id.fragment_calendar_container_viewpager)
    ViewPager viewPager;

    @Inject
    HomepageCalendarHelper homepageCalendarHelper;

    CalendarViewPagerAdapter calendarViewPagerAdapter;

    private Unbinder unbinder;

    public static HomepageCalendarContainerFragment newInstance() {
        return new HomepageCalendarContainerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_container, container, false);
        unbinder = ButterKnife.bind(this, view);
        initializeNewViewPager();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    private void initializeNewViewPager() {
        viewPager.setOffscreenPageLimit(1);
        calendarViewPagerAdapter = new CalendarViewPagerAdapter(getChildFragmentManager(), homepageCalendarHelper);
        viewPager.setAdapter(calendarViewPagerAdapter);
        viewPager.setCurrentItem((int)(Integer.MAX_VALUE/2));
    }
}
