package com.sunilson.bachelorthesis.presentation.settings;

import android.arch.lifecycle.ViewModel;

import com.sunilson.bachelorthesis.data.model.user.User;
import com.sunilson.bachelorthesis.domain.authentication.interactors.GetCurrentUser;
import com.sunilson.bachelorthesis.domain.authentication.model.DomainUser;
import com.sunilson.bachelorthesis.domain.calendar.interactors.ImportCalendarUseCase;


import javax.inject.Inject;

import io.reactivex.Observable;
import okhttp3.Response;

/**
 * @author Linus Weiss
 */

public class SettingsViewModel extends ViewModel {

    ImportCalendarUseCase importCalendarUseCase;
    GetCurrentUser getCurrentUser;

    @Inject
    public SettingsViewModel(ImportCalendarUseCase importCalendarUseCase, GetCurrentUser getCurrentUser) {
        this.importCalendarUseCase = importCalendarUseCase;
        this.getCurrentUser = getCurrentUser;
    }

    /**
     * Load the current user from the local database. A mapping is not needed here
     *
     * @return Observable that returns a DomainUser Object
     */
    public Observable<DomainUser> getCurrentUser() {
        return this.getCurrentUser.execute(null);
    }

    public Observable<retrofit2.Response<Void>> importCalendar(String url, Integer type) {
        return importCalendarUseCase.execute(ImportCalendarUseCase.Params.forImport(url, type));
    }

}
