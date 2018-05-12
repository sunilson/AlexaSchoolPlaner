package com.sunilson.bachelorthesis.presentation.addEvent;

import android.arch.lifecycle.ViewModel;

import com.sunilson.bachelorthesis.domain.calendar.interactors.AddEventUseCase;
import com.sunilson.bachelorthesis.presentation.event.EventModel;
import com.sunilson.bachelorthesis.presentation.event.mapper.DomainEventToEventModelMapper;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * @author Linus Weiss
 * <p>
 * ViewModel used to add a new Event
 */

@Singleton
public class AddEventViewModel extends ViewModel {

    private AddEventUseCase addEventUseCase;
    private DomainEventToEventModelMapper domainEventToEventModelMapper;

    @Inject
    public AddEventViewModel(
            AddEventUseCase addEventUseCase,
            DomainEventToEventModelMapper domainEventToEventModelMapper) {
        this.addEventUseCase = addEventUseCase;
        this.domainEventToEventModelMapper = domainEventToEventModelMapper;
    }

    public Observable<EventModel> addEvent(EventModel eventModel) {
        return addEventUseCase.execute(AddEventUseCase.Params
                .forDaySpan(domainEventToEventModelMapper.toDomainEvent(eventModel)))
                .map(value -> domainEventToEventModelMapper.toEvent(value));
    }
}
