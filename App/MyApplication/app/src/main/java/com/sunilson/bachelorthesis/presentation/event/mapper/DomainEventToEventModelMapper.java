package com.sunilson.bachelorthesis.presentation.event.mapper;

import com.sunilson.bachelorthesis.domain.model.DomainEvent;
import com.sunilson.bachelorthesis.presentation.event.EventModel;
import com.sunilson.bachelorthesis.presentation.shared.utilities.Constants;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Linus Weiss
 */

@Singleton
public class DomainEventToEventModelMapper {

    @Inject
    public DomainEventToEventModelMapper() {

    }

    public DomainEvent toDomainEvent(EventModel eventModel) {
        DomainEvent domainEvent = new DomainEvent();
        domainEvent.setSummary(eventModel.getSummary());
        domainEvent.setDescription(eventModel.getDescription());
        domainEvent.setFrom(eventModel.getFrom());
        domainEvent.setTo(eventModel.getTo());
        domainEvent.setEventType(Arrays.asList(Constants.EVENT_TYPES).indexOf(eventModel.getEventType()));
        return domainEvent;
    }

    public EventModel toEvent(DomainEvent domainEvent) {
        EventModel eventModel = new EventModel(
                domainEvent.getId(),
                domainEvent.getFrom(),
                domainEvent.getTo(),
                domainEvent.getDescription(),
                Constants.EVENT_TYPES[domainEvent.getEventType()]);

        if(domainEvent.getSummary() != null) {
            eventModel.setSummary(domainEvent.getSummary());
        }

        return eventModel;
    }
}
