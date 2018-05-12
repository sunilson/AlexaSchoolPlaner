package com.sunilson.bachelorthesis.domain.calendar.interactors;

import com.sunilson.bachelorthesis.data.model.EventEntity;
import com.sunilson.bachelorthesis.domain.shared.AbstractUseCase;
import com.sunilson.bachelorthesis.domain.calendar.mappers.EventEntityToDomainEventMapper;
import com.sunilson.bachelorthesis.domain.calendar.model.DomainEvent;
import com.sunilson.bachelorthesis.domain.repository.EventRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * @author Linus Weiss
 *
 * Use case for getting the data of a single event
 */
public class GetSingleEventUseCase extends AbstractUseCase<DomainEvent, GetSingleEventUseCase.Params> {

    private EventRepository eventRepository;
    private EventEntityToDomainEventMapper entityToDomainEventMapper;

    @Inject
    public GetSingleEventUseCase(EventRepository eventRepository, EventEntityToDomainEventMapper entityToDomainEventMapper) {
        this.eventRepository = eventRepository;
        this.entityToDomainEventMapper = entityToDomainEventMapper;
    }

    @Override
    protected Observable<DomainEvent> buildUseCaseObservable(GetSingleEventUseCase.Params params) {
        //Differentiate between offline and online status, because data should not be loaded twice
        if (params.offline) {
            return this.eventRepository.getOfflineSingleEvent(params.id)
                    .map(eventEntity -> entityToDomainEventMapper.mapToDomainEvent(eventEntity));
        } else {
            return this.eventRepository.getSingleEvent(params.id)
                    .map(eventEntity -> entityToDomainEventMapper.mapToDomainEvent(eventEntity));
        }
    }

    public static final class Params {
        private final String id;
        private final boolean offline;

        private Params(String id, boolean offline) {
            this.id = id;
            this.offline = offline;
        }

        public static Params forSingleEvent(String id, boolean offline) {
            return new Params(id, offline);
        }
    }
}
