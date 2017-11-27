package com.sunilson.bachelorthesis.presentation.homepage.calendar;

import android.arch.lifecycle.ViewModel;

import com.sunilson.bachelorthesis.domain.interactors.calendar.GetDaySpanUseCase;
import com.sunilson.bachelorthesis.domain.model.DomainDay;
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

    public Observable<CalendarDaySpanModel> getDays(DateTime from, DateTime to) {
       return getDaySpanUseCase.execute(GetDaySpanUseCase.Params.forDaySpan(from, to)).map(
               new Function<List<DomainDay>, CalendarDaySpanModel>() {
                   @Override
                   public CalendarDaySpanModel apply(List<DomainDay> domainDays) throws Exception {
                       return daySpanModelMapper.toCalendarDaySpanModel(domainDays);
                   }
               }
       );
    }

    /*
    public LiveData<CalendarDaySpanModel> getDays(DateTime from, DateTime to) {

        Log.d("Linus", "Get Days");

        Flowable<List<DomainDay>> flowable = getDaySpanUseCase.execute(GetDaySpanUseCase.Params.forDaySpan(from, to));

        return LiveDataReactiveStreams.fromPublisher(flowable.map(
                new Function<List<DomainDay>, CalendarDaySpanModel>() {
                    @Override
                    public CalendarDaySpanModel apply(List<DomainDay> domainDays) throws Exception {
                        return daySpanModelMapper.toCalendarDaySpanModel(domainDays);
                    }
                }
        ));
    }
    */

    @Override
    protected void onCleared() {
        super.onCleared();


    }
}
