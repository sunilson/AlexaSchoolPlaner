package com.sunilson.bachelorthesis.presentation.viewmodelBasics;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.sunilson.bachelorthesis.presentation.event.EventViewModel;
import com.sunilson.bachelorthesis.presentation.homepage.HomepageViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * @author Linus Weiss
 */

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomepageViewModel.class)
    abstract ViewModel bindHomepageViewModel(HomepageViewModel homepageViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EventViewModel.class)
    abstract ViewModel bindEventViewModel(EventViewModel eventViewModel);

    @Binds
    abstract ViewModelProvider.Factory provideViewModelFactory(ViewModelFactory viewModelFactory);
}
