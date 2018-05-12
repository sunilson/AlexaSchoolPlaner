package com.sunilson.bachelorthesis.presentation.event;

import android.arch.lifecycle.ViewModel;

import com.sunilson.bachelorthesis.domain.calendar.interactors.EditEventUseCase;
import com.sunilson.bachelorthesis.domain.calendar.interactors.GetSingleEventUseCase;
import com.sunilson.bachelorthesis.domain.calendar.model.DomainEvent;
import com.sunilson.bachelorthesis.presentation.event.mapper.DomainEventToEventModelMapper;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * @author Linus Weiss
 *
 * ViewModel for the event activity
 */

public class EventViewModel extends ViewModel {

    private GetSingleEventUseCase getSingleEventUseCase;
    private EditEventUseCase editEventUseCase;
    private DomainEventToEventModelMapper domainEventToEventModelMapper;

    @Inject
    public EventViewModel(GetSingleEventUseCase getSingleEventUseCase, DomainEventToEventModelMapper domainEventToEventModelMapper, EditEventUseCase editEventUseCase) {
        this.editEventUseCase = editEventUseCase;
        this.getSingleEventUseCase = getSingleEventUseCase;
        this.domainEventToEventModelMapper = domainEventToEventModelMapper;
    }

    public Observable<EventModel> getSingleEvent(String id) throws IllegalArgumentException {
        if(id == null) throw new IllegalArgumentException("ID can't be null!");
        return getSingleEventUseCase.execute(GetSingleEventUseCase.Params.forSingleEvent(id, false)).map(domainEvent -> domainEventToEventModelMapper.toEvent(domainEvent));
    }

    public Observable<EventModel> editEvent(EventModel eventModel) {
        return editEventUseCase.execute(new EditEventUseCase.Params(domainEventToEventModelMapper.toDomainEvent(eventModel))).map(domainEvent -> domainEventToEventModelMapper.toEvent(domainEvent));
    }

    public Observable<EventModel> getOfflineSingleEvent(String id) throws IllegalArgumentException {
        if(id == null) throw new IllegalArgumentException("ID can't be null!");
        return getSingleEventUseCase.execute(GetSingleEventUseCase.Params.forSingleEvent(id, true)).map(domainEvent -> domainEventToEventModelMapper.toEvent(domainEvent));
    }
}
