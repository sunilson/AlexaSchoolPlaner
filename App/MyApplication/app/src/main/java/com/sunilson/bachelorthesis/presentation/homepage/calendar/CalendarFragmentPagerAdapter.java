package com.sunilson.bachelorthesis.presentation.homepage.calendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * @author Linus Weiss
 */

public class CalendarFragmentPagerAdapter extends FragmentStatePagerAdapter {

    public CalendarFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return HomepageFragmentCalendar.newInstance(null);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
