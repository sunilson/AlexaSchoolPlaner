package com.sunilson.bachelorthesis.domain.calendar.mappers;

import com.sunilson.bachelorthesis.data.model.EventEntity;
import com.sunilson.bachelorthesis.domain.calendar.model.DomainEvent;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Linus Weiss
 */

@Singleton
public class EventEntityToDomainEventMapper {

    @Inject
    public EventEntityToDomainEventMapper() {

    }

    /**
     * Transforms given eventEntity into model class of domain section
     *
     * @param eventEntity
     * @return Transformed DomainEvent
     */
    public DomainEvent mapToDomainEvent(EventEntity eventEntity) {
        DomainEvent domainEvent = new DomainEvent();

        domainEvent.setDescription(eventEntity.getDescription());
        domainEvent.setEventType(eventEntity.getType());
        domainEvent.setLocation(eventEntity.getLocation());
        domainEvent.setId(eventEntity.getId());
        domainEvent.setSummary(eventEntity.getSummary());

        DateTimeFormatter parser = ISODateTimeFormat.dateTime();
        domainEvent.setFrom(parser.parseDateTime(eventEntity.getFrom()));
        domainEvent.setTo(parser.parseDateTime(eventEntity.getTo()));

        return domainEvent;
    }
}
