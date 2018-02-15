package com.sunilson.bachelorthesis.presentation.settings;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sunilson.bachelorthesis.R;
import com.sunilson.bachelorthesis.domain.authentication.model.DomainUser;
import com.sunilson.bachelorthesis.domain.calendar.interactors.ImportCalendarUseCase;
import com.sunilson.bachelorthesis.presentation.homepage.HomepageActivity;
import com.sunilson.bachelorthesis.presentation.homepage.calendar.CalendarDaySpanModel;
import com.sunilson.bachelorthesis.presentation.homepage.calendar.CalendarViewModel;
import com.sunilson.bachelorthesis.presentation.shared.baseClasses.BaseActivity;
import com.sunilson.bachelorthesis.presentation.shared.utilities.DisposableManager;
import com.sunilson.bachelorthesis.presentation.shared.utilities.Navigator;
import com.sunilson.bachelorthesis.presentation.shared.viewmodelBasics.ViewModelFactory;

import org.joda.time.DateTime;

import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.HasActivityInjector;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * @author Linus Weiss
 */

public class SettingsActivity extends BaseActivity {

    private SettingsViewModel settingsViewModel;

    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    DisposableManager disposableManager;

    @BindView(R.id.settings_activity_import_button)
    Button importButton;

    @OnClick(R.id.settings_activity_import_button)
    public void importCal(View button) {
        if(!importURL.getText().toString().isEmpty()) {
            disposableManager.add(
                    settingsViewModel
                            .importCalendar(importURL.getText().toString())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new ImportObserver()));
            button.setEnabled(false);
            Toast.makeText(this, R.string.importing_now, Toast.LENGTH_SHORT).show();
        }
    }

    @BindView(R.id.settings_activity_import_url)
    EditText importURL;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get ViewModel from factory
        settingsViewModel = ViewModelProviders.of(this, viewModelFactory).get(SettingsViewModel.class);
        disposableManager.add(settingsViewModel.getCurrentUser().subscribeWith(new UserObserver()));
    }

    /**
     * Creates an Intent that can be used to navigate to this Activity
     *
     * @param context
     * @return Intent to navigate to this Activity
     */
    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        return intent;
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposableManager.dispose();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private final class UserObserver extends DisposableObserver<DomainUser> {

        @Override
        public void onNext(DomainUser domainUser) {
            if (domainUser.getIcalurl() != null) {
                importURL.setText(domainUser.getIcalurl());
            }
        }

        @Override
        public void onError(Throwable e) {
            finish();
        }

        @Override
        public void onComplete() {

        }
    }

    private final class ImportObserver extends DisposableObserver<Response<Void>> {

        @Override
        public void onNext(Response<Void> response) {
            importButton.setEnabled(true);
            if(response.code() == 200) {
                Toast.makeText(SettingsActivity.this, R.string.import_success, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SettingsActivity.this, R.string.import_error, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.d("Linus", e.toString());
            importButton.setEnabled(true);
            Toast.makeText(SettingsActivity.this, R.string.import_error, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete() {

        }
    }
}
