package com.sunilson.bachelorthesis.domain.calendar.interactors;

import com.sunilson.bachelorthesis.data.repository.BodyModels.UrlForPostBody;
import com.sunilson.bachelorthesis.domain.repository.EventRepository;
import com.sunilson.bachelorthesis.domain.shared.AbstractUseCase;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * @author Linus Weiss
 */

public class ImportCalendarUseCase extends AbstractUseCase<Response<Void>, ImportCalendarUseCase.Params> {

    @Inject
    EventRepository eventRepository;

    @Inject
    public ImportCalendarUseCase() {

    }

    @Override
    protected Observable<Response<Void>> buildUseCaseObservable(Params params) {
        return eventRepository.importCalendar(new UrlForPostBody(params.url, params.type));
    }

    public static final class Params {

        private final String url;
        private final Integer type;

        private Params(String url, Integer type) {
            this.url = url;
            this.type = type;
        }

        public static Params forImport(String url, Integer type) {
            return new Params(url, type);
        }
    }
}
