package com.sunilson.bachelorthesis.domain.mappers;

import com.sunilson.bachelorthesis.data.model.EventEntity;
import com.sunilson.bachelorthesis.domain.model.DomainEvent;

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
