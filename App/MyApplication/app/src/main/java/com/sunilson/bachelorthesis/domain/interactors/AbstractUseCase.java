package com.sunilson.bachelorthesis.domain.interactors;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Linus Weiss
 */

public abstract class AbstractUseCase<T, Params> {

    public AbstractUseCase() {
    }

    protected abstract Observable<T> buildUseCaseObservable(Params params);

    public Observable<T> execute(Params params) {
        Observable<T> observable = this.buildUseCaseObservable(params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return observable;
    }
}
