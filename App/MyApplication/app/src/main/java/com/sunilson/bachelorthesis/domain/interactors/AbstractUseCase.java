package com.sunilson.bachelorthesis.domain.interactors;

import io.reactivex.Flowable;

/**
 * @author Linus Weiss
 */

public abstract class AbstractUseCase<T, Params> {


    public AbstractUseCase() {
    }

    abstract Flowable<T> buildUseCaseFlowable(Params params);

    public Flowable<T> execute(Params params) {
        return this.buildUseCaseFlowable(params);
    }
}
