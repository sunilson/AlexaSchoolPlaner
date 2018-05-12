package com.sunilson.bachelorthesis.domain.calendar.interactors;

import com.sunilson.bachelorthesis.data.model.EventEntity;
import com.sunilson.bachelorthesis.domain.calendar.mappers.EventEntityListToDomainDayListMapper;
import com.sunilson.bachelorthesis.domain.calendar.model.DomainDay;
import com.sunilson.bachelorthesis.domain.repository.EventRepository;
import com.sunilson.bachelorthesis.domain.shared.AbstractUseCase;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * @author Linus Weiss
 */

public class GetDaySpanUseCase extends AbstractUseCase<List<DomainDay>, GetDaySpanUseCase.Params> {

    private final EventRepository eventRepository;
    private final EventEntityListToDomainDayListMapper eventEntityListToDayListMapper;

    @Inject
    public GetDaySpanUseCase(EventRepository eventRepository, EventEntityListToDomainDayListMapper eventEntityListToDayListMapper) {
        this.eventRepository = eventRepository;
        this.eventEntityListToDayListMapper = eventEntityListToDayListMapper;
    }

    @Override
    protected Observable<List<DomainDay>> buildUseCaseObservable(final Params params) {
        Observable<List<EventEntity>> observable;

        if (params.offline) observable = this.eventRepository.getOfflineEventList(params.from, params.to);
        else observable =  this.eventRepository.getEventList(params.from, params.to);

        return observable.map(eventEntities -> eventEntityListToDayListMapper
                .mapToDayList(eventEntities,
                        params.from.toLocalDate(),
                        params.to.toLocalDate()));
    }


    public static final class Params {
        private final DateTime from, to;
        private final Boolean offline;

        private Params(DateTime from, DateTime to, Boolean offline) {
            this.to = to;
            this.from = from;
            this.offline = offline;
        }

        public static Params forDaySpan(DateTime from, DateTime to, Boolean offline) {
            return new Params(from, to, offline);
        }
    }
}
