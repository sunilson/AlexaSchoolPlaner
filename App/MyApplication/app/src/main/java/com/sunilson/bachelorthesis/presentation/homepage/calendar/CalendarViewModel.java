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
 */

public class CalendarViewModel extends ViewModel {

    GetDaySpanUseCase getDaySpanUseCase;
    DomainDayListToCalendarDaySpanModelMapper daySpanModelMapper;

    @Inject
    public CalendarViewModel(GetDaySpanUseCase getDaySpanUseCase, DomainDayListToCalendarDaySpanModelMapper daySpanModelMapper) {
        this.getDaySpanUseCase = getDaySpanUseCase;
        this.daySpanModelMapper = daySpanModelMapper;
    }

    public Observable<CalendarDaySpanModel> getDays(DateTime from, DateTime to, Boolean offline) throws  IllegalArgumentException{

        if(from == null || to == null) {
            throw new IllegalArgumentException("Dates cannot be null!");
        }

       return getDaySpanUseCase.execute(GetDaySpanUseCase.Params.forDaySpan(from, to, offline)).map(
               domainDays -> daySpanModelMapper.toCalendarDaySpanModel(domainDays)
       );
    }

    @Override
    protected void onCleared() {
        super.onCleared();


    }
}
