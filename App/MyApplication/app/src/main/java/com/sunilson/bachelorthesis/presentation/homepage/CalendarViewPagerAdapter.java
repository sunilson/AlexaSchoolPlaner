package com.sunilson.bachelorthesis.presentation.homepage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.sunilson.bachelorthesis.presentation.homepage.calendar.HomepageFragmentCalendar;
import com.sunilson.bachelorthesis.presentation.homepage.utilities.HomepageCalendarHelper;

import org.joda.time.DateTime;

import java.util.List;

/**
 * @author Linus Weiss
 *
 * This adapter is used with the main ViewPager. It doesnt end on either side, but instead just
 * starts over.
 */
public class CalendarViewPagerAdapter extends FragmentPagerAdapter {

    private HomepageCalendarHelper homepageCalendarHelper;
    private FragmentManager fm;
    private int firstStart = 0;
    private DateTime[] middleDateTimes;
    private Integer prevFragmentPos;
    private boolean direction;

    public CalendarViewPagerAdapter(FragmentManager fm, HomepageCalendarHelper homepageCalendarHelper) {
        super(fm);
        this.fm = fm;
        this.homepageCalendarHelper = homepageCalendarHelper;
        middleDateTimes = homepageCalendarHelper.getCurrentCalendarDates();
    }

    @Override
    public Fragment getItem(int position) {

        DateTime[] tempDateTimes = middleDateTimes;
        int fragmentPos = position % 3;

        //Check if this is the one of the first 3 times the view is requested,
        // if yes, we know where and with what time we need to add the fragment
        if (firstStart < 3) {
            firstStart++;
            if (fragmentPos == 1) {
                tempDateTimes = homepageCalendarHelper.addPeriodFromDateTimes(middleDateTimes);
            } else if (fragmentPos == 0) {

            } else if (fragmentPos == 2) {
                tempDateTimes = homepageCalendarHelper.subtractPeriodFromDateTimes(middleDateTimes);
            }

            switch (fragmentPos) {
                case 0:
                    return HomepageFragmentCalendar.newInstance(homepageCalendarHelper.getCurrentLongDates());
                case 1:
                case 2:
                    return HomepageFragmentCalendar.newInstance(homepageCalendarHelper.convertDateTimesToLongDates(tempDateTimes));
            }
        } else {
            //Else we don't know where to add the Fragment yet, so we need to
            //determine the direction of the swipe
            if (prevFragmentPos == null) {
                if (fragmentPos == 1) {
                    direction = false;
                }
                if (fragmentPos == 2) {
                    direction = true;
                }
            } else {
                if (prevFragmentPos == 1 && fragmentPos == 0) {
                    direction = false;
                }
                if (prevFragmentPos == 2 && fragmentPos == 0) {
                    direction = true;
                }

                if (prevFragmentPos == 0 && fragmentPos == 2) {
                    direction = false;
                }
                if (prevFragmentPos == 0 && fragmentPos == 1) {
                    direction = true;
                }

                if (prevFragmentPos == 2 && fragmentPos == 1) {
                    direction = false;
                }
                if (prevFragmentPos == 1 && fragmentPos == 2) {
                    direction = true;
                }

                if (prevFragmentPos == fragmentPos) {
                    direction = !direction;
                }
            }

            //Depending on the direction, add or subtract date period
            if (direction) {
                middleDateTimes = homepageCalendarHelper.addPeriodFromDateTimes(middleDateTimes);
                tempDateTimes = homepageCalendarHelper.addPeriodFromDateTimes(middleDateTimes);
            } else {
                middleDateTimes = homepageCalendarHelper.subtractPeriodFromDateTimes(middleDateTimes);
                tempDateTimes = homepageCalendarHelper.subtractPeriodFromDateTimes(middleDateTimes);
            }

            //Create new fragment and add it to the ViewPager
            prevFragmentPos = fragmentPos;
            homepageCalendarHelper.setCurrentCalendarDates(middleDateTimes[0], middleDateTimes[1]);
            return HomepageFragmentCalendar.newInstance(homepageCalendarHelper.convertDateTimesToLongDates(tempDateTimes));
        }
        return null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        HomepageFragmentCalendar fragmentCalendar = (HomepageFragmentCalendar) object;
        fm.beginTransaction().remove(fragmentCalendar).commit();
    }

    public void clear() {
        List<Fragment> fragments = fm.getFragments();
        if (fragments != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : fragments) {
                //You can perform additional check to remove some (not all) fragments:
                if (f instanceof HomepageFragmentCalendar) {
                    ft.remove(f);
                }
            }
            ft.commit();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
}
