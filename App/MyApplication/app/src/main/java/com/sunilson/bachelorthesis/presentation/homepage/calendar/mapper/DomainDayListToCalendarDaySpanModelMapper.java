package com.sunilson.bachelorthesis.presentation.homepage.calendar.mapper;

import com.sunilson.bachelorthesis.domain.calendar.model.DomainDay;
import com.sunilson.bachelorthesis.presentation.homepage.calendar.CalendarDaySpanModel;
import com.sunilson.bachelorthesis.presentation.homepage.calendar.calendarDay.CalendarDayModel;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Linus Weiss
 */

public class DomainDayListToCalendarDaySpanModelMapper {

    private DomainDayToCalendarDayModelMapper calendarDayModelMapper;

    @Inject
    public DomainDayListToCalendarDaySpanModelMapper(DomainDayToCalendarDayModelMapper calendarDayModelMapper) {
        this.calendarDayModelMapper = calendarDayModelMapper;
    }

    public CalendarDaySpanModel toCalendarDaySpanModel(List<DomainDay> domainDays) {

        CalendarDaySpanModel calendarDaySpanModel = new CalendarDaySpanModel();

        for (DomainDay domainDay : domainDays) {
            CalendarDayModel calendarDayModel = this.calendarDayModelMapper.toCalendarDay(domainDay);
            calendarDaySpanModel.add(calendarDayModel);
        }

        return calendarDaySpanModel;
    }
}
