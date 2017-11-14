package com.sunilson.bachelorthesis.domain.interactors;

import android.support.v4.util.Preconditions;

import com.sunilson.bachelorthesis.data.model.EventEntity;
import com.sunilson.bachelorthesis.domain.model.DomainDay;
import com.sunilson.bachelorthesis.domain.mappers.EventListToDomainDayListMapper;
import com.sunilson.bachelorthesis.domain.repository.EventRepository;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * @author Linus Weiss
 */

public class GetDaySpanUseCase extends AbstractUseCase<List<DomainDay>, GetDaySpanUseCase.Params> {

    private final EventRepository eventRepository;
    private final EventListToDomainDayListMapper eventEntityListToDayListMapper;

    @Inject
    public GetDaySpanUseCase(EventRepository eventRepository, EventListToDomainDayListMapper eventEntityListToDayListMapper) {
        this.eventRepository = eventRepository;
        this.eventEntityListToDayListMapper = eventEntityListToDayListMapper;
    }

    @Override
    Flowable<List<DomainDay>> buildUseCaseFlowable(Params params) {
        Preconditions.checkNotNull(params);
        return this.eventRepository.getEventList(params.from, params.to).map(new Function<List<EventEntity>, List<DomainDay>>() {
            @Override
            public List<DomainDay> apply(List<EventEntity> eventEntities) throws Exception {
                return eventEntityListToDayListMapper.mapToDayList(eventEntities);
            }
        });
    }


    public static final class Params {
        private final DateTime from, to;

        private Params(DateTime from, DateTime to) {
            this.to = to;
            this.from = from;
        }

        public static Params forDaySpan(DateTime from, DateTime to) {
            return new Params(from, to);
        }
    }
}
