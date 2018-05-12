package com.sunilson.bachelorthesis.presentation.homepage.calendar;

import android.arch.lifecycle.ViewModel;

import com.sunilson.bachelorthesis.domain.calendar.interactors.GetDaySpanUseCase;
import com.sunilson.bachelorthesis.domain.calendar.model.DomainDay;
import com.sunilson.bachelorthesis.presentation.homepage.calendar.mapper.DomainDayListToCalendarDaySpanModelMapper;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * @author Linus Weiss
 *
 * This is the ViewModel of the calendar. It provides data for whole days of the calendar
 */

public class CalendarViewModel extends ViewModel {

    GetDaySpanUseCase getDaySpanUseCase;
    DomainDayListToCalendarDaySpanModelMapper daySpanModelMapper;

    @Inject
    public CalendarViewModel(GetDaySpanUseCase getDaySpanUseCase, DomainDayListToCalendarDaySpanModelMapper daySpanModelMapper) {
        this.getDaySpanUseCase = getDaySpanUseCase;
        this.daySpanModelMapper = daySpanModelMapper;
    }

    /**
     * Loads whole days for a certain time range. A day is a holder for multiple events
     *
     * @param from
     * @param to
     * @param offline Set this to true if data should be loaded from local database
     * @return A DaySpan which contains multiple days which contain multiple events
     * @throws IllegalArgumentException
     */
    public Observable<CalendarDaySpanModel> getDays(DateTime from, DateTime to, Boolean offline) throws  IllegalArgumentException{
        if(from == null || to == null) throw new IllegalArgumentException("Dates cannot be null!");
       return getDaySpanUseCase.execute(GetDaySpanUseCase.Params.forDaySpan(from, to, offline)).map(
               domainDays -> daySpanModelMapper.toCalendarDaySpanModel(domainDays)
       );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
