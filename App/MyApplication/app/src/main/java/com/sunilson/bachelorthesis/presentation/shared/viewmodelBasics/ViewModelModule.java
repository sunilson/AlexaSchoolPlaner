package com.sunilson.bachelorthesis.presentation.shared.viewmodelBasics;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.sunilson.bachelorthesis.presentation.addEvent.AddEventViewModel;
import com.sunilson.bachelorthesis.presentation.authentication.AuthenticationViewModel;
import com.sunilson.bachelorthesis.presentation.event.EventViewModel;
import com.sunilson.bachelorthesis.presentation.homepage.calendar.CalendarViewModel;
import com.sunilson.bachelorthesis.presentation.settings.SettingsViewModel;

import java.util.Set;

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
    @ViewModelKey(CalendarViewModel.class)
    abstract ViewModel bindHomepageViewModel(CalendarViewModel calendarViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EventViewModel.class)
    abstract ViewModel bindEventViewModel(EventViewModel eventViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AddEventViewModel.class)
    abstract ViewModel bindAddEventViewModel(AddEventViewModel addEventViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AuthenticationViewModel.class)
    abstract ViewModel bindAuthenticationViewModel(AuthenticationViewModel authenticationViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel.class)
    abstract ViewModel bindSettingsViewModel(SettingsViewModel settingsViewModel);

    @Binds
    abstract ViewModelProvider.Factory provideViewModelFactory(ViewModelFactory viewModelFactory);
}
