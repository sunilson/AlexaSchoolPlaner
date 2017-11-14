package com.sunilson.bachelorthesis.domain.mappers;

import com.sunilson.bachelorthesis.data.model.EventEntity;
import com.sunilson.bachelorthesis.domain.model.DomainDay;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Linus Weiss
 */

@Singleton
public class EventListToDomainDayListMapper {

    private EventEntityToDomainEventMapper eventEntityToDomainEventMapper;

    @Inject
    public EventListToDomainDayListMapper(EventEntityToDomainEventMapper eventEntityToDomainEventMapper) {
        this.eventEntityToDomainEventMapper = eventEntityToDomainEventMapper;
    }

    public List<DomainDay> mapToDayList(List<EventEntity> eventEntityList) {
        return new ArrayList<>();
    }
}
