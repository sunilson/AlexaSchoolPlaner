package com.sunilson.bachelorthesis.presentation.homepage;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.ViewModel;

import com.sunilson.bachelorthesis.presentation.homepage.day.CalendarDaySpanModel;

import org.joda.time.DateTime;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * @author Linus Weiss
 */

public class HomepageViewModel extends ViewModel {

    @Inject
    public HomepageViewModel() {

    }

    public LiveData<CalendarDaySpanModel> getDays(DateTime from, DateTime to) {
       return LiveDataReactiveStreams.fromPublisher(Flowable.just(new CalendarDaySpanModel()));
    }
}
