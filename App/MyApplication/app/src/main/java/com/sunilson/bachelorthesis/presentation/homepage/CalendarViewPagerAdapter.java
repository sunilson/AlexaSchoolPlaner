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
 */

public class CalendarViewPagerAdapter extends FragmentPagerAdapter {

    HomepageCalendarHelper homepageCalendarHelper;
    FragmentManager fm;
    int firstStart = 0;
    private DateTime[] middleDateTimes;
    private Integer prevFragmentPos;
    boolean direction;

    public CalendarViewPagerAdapter(FragmentManager fm, HomepageCalendarHelper homepageCalendarHelper) {
        super(fm);
        this.fm = fm;
        this.homepageCalendarHelper = homepageCalendarHelper;
        middleDateTimes = homepageCalendarHelper.getCurrentCalendarDates();
    }

    @Override
    public Fragment getItem(int position) {

        //1 rechts am Anfang
        //2 links am Anfang
        //Habe ich nach rechts oder nach links gewischt? Middle Date von links oder von rechts übernehmen

        DateTime[] tempDateTimes = middleDateTimes;
        int fragmentPos = position % 3;
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

            if (direction) {
                middleDateTimes = homepageCalendarHelper.addPeriodFromDateTimes(middleDateTimes);
                tempDateTimes = homepageCalendarHelper.addPeriodFromDateTimes(middleDateTimes);
            } else {
                middleDateTimes = homepageCalendarHelper.subtractPeriodFromDateTimes(middleDateTimes);
                tempDateTimes = homepageCalendarHelper.subtractPeriodFromDateTimes(middleDateTimes);
            }

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
