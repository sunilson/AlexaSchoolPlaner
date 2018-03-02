package com.sunilson.bachelorthesis.domain.calendar.interactors;

import com.sunilson.bachelorthesis.data.model.EventEntity;
import com.sunilson.bachelorthesis.data.repository.BodyModels.EventForPostBody;
import com.sunilson.bachelorthesis.domain.calendar.mappers.EventEntityToDomainEventMapper;
import com.sunilson.bachelorthesis.domain.calendar.model.DomainEvent;
import com.sunilson.bachelorthesis.domain.repository.EventRepository;
import com.sunilson.bachelorthesis.domain.shared.AbstractUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * @author Linus Weiss
 */

public class EditEventUseCase extends AbstractUseCase<DomainEvent, EditEventUseCase.Params> {

    private EventRepository eventRepository;
    private EventEntityToDomainEventMapper eventEntityToDomainEventMapper;

    @Inject
    public EditEventUseCase(EventRepository eventRepository, EventEntityToDomainEventMapper eventEntityToDomainEventMapper) {
        this.eventRepository = eventRepository;
        this.eventEntityToDomainEventMapper = eventEntityToDomainEventMapper;
    }

    @Override
    protected Observable<DomainEvent> buildUseCaseObservable(Params params) {
        return eventRepository.editEvent(new EventForPostBody(
                params.domainEvent.getEventType(),
                params.domainEvent.getDescription(),
                params.domainEvent.getLocation(),
                params.domainEvent.getSummary(),
                params.domainEvent.getId(),
                params.domainEvent.getFrom().getMillis(),
                params.domainEvent.getTo().getMillis()
        )).map(eventEntity -> eventEntityToDomainEventMapper.mapToDomainEvent(eventEntity));
    }

    public static final class Params {
        private DomainEvent domainEvent;

        public Params(DomainEvent domainEvent) {
            this.domainEvent = domainEvent;
        }
    }
}
