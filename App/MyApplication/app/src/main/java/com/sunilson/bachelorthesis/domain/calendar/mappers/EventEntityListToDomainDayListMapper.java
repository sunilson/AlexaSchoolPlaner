package com.sunilson.bachelorthesis.domain.calendar.mappers;

import com.sunilson.bachelorthesis.data.model.EventEntity;
import com.sunilson.bachelorthesis.domain.calendar.model.DomainDay;
import com.sunilson.bachelorthesis.domain.calendar.model.DomainEvent;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Linus Weiss
 */

@Singleton
public class EventEntityListToDomainDayListMapper {

    private EventEntityToDomainEventMapper eventEntityToDomainEventMapper;

    @Inject
    public EventEntityListToDomainDayListMapper(EventEntityToDomainEventMapper eventEntityToDomainEventMapper) {
        this.eventEntityToDomainEventMapper = eventEntityToDomainEventMapper;
    }

    /**
     * Maps a list of EventEntities of the Data layer to a list of DomainDays
     *
     * @param eventEntityList List of events
     * @param from Start date of day range
     * @param to End date of day range
     * @return
     */
    public List<DomainDay> mapToDayList(List<EventEntity> eventEntityList, LocalDate from, LocalDate to) {

        List<DomainDay> result = new ArrayList<>();
        Map<String, DomainDay> tempMap = new HashMap<>();

        //Fill map with possible days
        for (LocalDate date = from; (date.isBefore(to) || date.isEqual(to)); date = date.plusDays(1)) {
            DomainDay domainDay = new DomainDay(date);
            tempMap.put(date.toString(), domainDay);
        }

        //Iterate over all Events
        for (EventEntity eventEntity : eventEntityList) {
            DomainEvent domainEvent = eventEntityToDomainEventMapper.mapToDomainEvent(eventEntity);

            //Iterate over all days that are between from and to of that specific event
            for (LocalDate date = domainEvent.getFrom().toLocalDate(); (date.isBefore(domainEvent.getTo().toLocalDate()) || date.isEqual(domainEvent.getTo().toLocalDate())); date = date.plusDays(1)) {
                if (tempMap.get(date.toString()) != null) {
                    tempMap.get(date.toString()).addEvent(domainEvent);
                }
            }
        }

        //Iterate over map and add Days to result list
        for (Map.Entry<String, DomainDay> entry : tempMap.entrySet()) {
            DomainDay day = entry.getValue();
            result.add(day);
        }

        //Sort day list by their localdates
        Collections.sort(result, new Comparator<DomainDay>() {
            @Override
            public int compare(DomainDay t1, DomainDay t2) {
                return t1.getLocalDate().compareTo(t2.getLocalDate());
            }
        });

        return result;
    }
}
