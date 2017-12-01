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
        return this.eventRepository.getSingleEvent(params.id).map(new Function<EventEntity, DomainEvent>() {
            @Override
            public DomainEvent apply(EventEntity eventEntity) throws Exception {
                return entityToDomainEventMapper.mapToDomainEvent(eventEntity);
            }
        });
    }

    public static final class Params {
        private final String id;

        private Params(String id) {
            this.id = id;
        }

        public static Params forSingleEvent(String id) {
            return new Params(id);
        }
    }
}
