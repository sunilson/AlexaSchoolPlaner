package com.sunilson.bachelorthesis.presentation.event;

import android.arch.lifecycle.ViewModel;

import com.sunilson.bachelorthesis.domain.calendar.interactors.GetSingleEventUseCase;
import com.sunilson.bachelorthesis.domain.calendar.model.DomainEvent;
import com.sunilson.bachelorthesis.presentation.event.mapper.DomainEventToEventModelMapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;

/**
 * @author Linus Weiss
 */

public class EventViewModel extends ViewModel {

    GetSingleEventUseCase getSingleEventUseCase;
    DomainEventToEventModelMapper domainEventToEventModelMapper;

    @Inject
    public EventViewModel(GetSingleEventUseCase getSingleEventUseCase, DomainEventToEventModelMapper domainEventToEventModelMapper) {
        this.getSingleEventUseCase = getSingleEventUseCase;
        this.domainEventToEventModelMapper = domainEventToEventModelMapper;
    }

    public io.reactivex.Observable<EventModel> getSingleEvent(String id) {
        return getSingleEventUseCase.execute(GetSingleEventUseCase.Params.forSingleEvent(id)).map(new Function<DomainEvent, EventModel>() {
            @Override
            public EventModel apply(DomainEvent domainEvent) throws Exception {
                return domainEventToEventModelMapper.toEvent(domainEvent);
            }
        });
    }

    /*
    public LiveData<EventModel> getSingleEvent(String id) {
        return LiveDataReactiveStreams.fromPublisher(getSingleEventUseCase.execute(GetSingleEventUseCase.Params.forSingleEvent(id)).map(new Function<DomainEvent, EventModel>() {
            @Override
            public EventModel apply(DomainEvent domainEvent) throws Exception {
                return domainEventToEventModelMapper.toEvent(domainEvent);
            }
        }));
    }
*/
}
