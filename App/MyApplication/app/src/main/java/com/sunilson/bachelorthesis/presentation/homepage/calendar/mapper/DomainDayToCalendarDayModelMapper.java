package com.sunilson.bachelorthesis.presentation.homepage.calendar.mapper;

import com.sunilson.bachelorthesis.domain.model.DomainDay;
import com.sunilson.bachelorthesis.domain.model.DomainEvent;
import com.sunilson.bachelorthesis.presentation.event.EventModel;
import com.sunilson.bachelorthesis.presentation.event.mapper.DomainEventToEventModelMapper;
import com.sunilson.bachelorthesis.presentation.homepage.calendar.calendarDay.CalendarDayModel;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Linus Weiss
 */

@Singleton
public class DomainDayToCalendarDayModelMapper {

    private DomainEventToEventModelMapper domainEventToEventModelMapper;

    @Inject
    public DomainDayToCalendarDayModelMapper(DomainEventToEventModelMapper domainEventToEventModelMapper) {
    this.domainEventToEventModelMapper = domainEventToEventModelMapper;
    }

    public CalendarDayModel toCalendarDay(DomainDay domainDay) {

        CalendarDayModel calendarDayModel = new CalendarDayModel(domainDay.getLocalDate());

        for (DomainEvent domainEvent : domainDay.getDomainEvents()) {
            EventModel eventModel = this.domainEventToEventModelMapper.toEvent(domainEvent);
            calendarDayModel.addEvent(eventModel);
        }

        return calendarDayModel;
    }
}
