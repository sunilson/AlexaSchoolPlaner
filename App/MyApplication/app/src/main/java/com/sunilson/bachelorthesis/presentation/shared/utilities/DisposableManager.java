package com.sunilson.bachelorthesis.presentation.shared.utilities;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author Linus Weiss
 *
 * Injectable object used to dispose of Observers after Activity/Fragment is finished
 */
public class DisposableManager {

    private CompositeDisposable compositeDisposable;

    public void add(Disposable disposable) {
        getCompositeDisposable().add(disposable);
    }

    public void dispose() {
        getCompositeDisposable().dispose();
    }

    private CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        return compositeDisposable;
    }

    @Inject
    public DisposableManager() {}

}
