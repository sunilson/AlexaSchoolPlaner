package com.sunilson.bachelorthesis.domain.mappers;

import com.sunilson.bachelorthesis.data.model.EventEntity;
import com.sunilson.bachelorthesis.domain.model.DomainEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by linus_000 on 13.11.2017.
 */

@Singleton
public class EventEntityToDomainEventMapper {

    @Inject
    public EventEntityToDomainEventMapper() {

    }

    public DomainEvent mapToEntity(EventEntity eventEntity) {
        return new DomainEvent();
    }
}
