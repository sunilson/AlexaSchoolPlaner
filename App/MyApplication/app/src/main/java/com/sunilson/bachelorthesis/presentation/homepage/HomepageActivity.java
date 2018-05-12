package com.sunilson.bachelorthesis.presentation.homepage;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.sunilson.bachelorthesis.R;
import com.sunilson.bachelorthesis.domain.authentication.interactors.CheckLoginStatusUseCase;
import com.sunilson.bachelorthesis.domain.authentication.interactors.GetCurrentUser;
import com.sunilson.bachelorthesis.domain.authentication.model.DomainUser;
import com.sunilson.bachelorthesis.presentation.homepage.calendar.CalendarViewModel;
import com.sunilson.bachelorthesis.presentation.homepage.calendar.HomepageFragmentCalendar;
import com.sunilson.bachelorthesis.presentation.homepage.utilities.HomepageCalendarHelper;
import com.sunilson.bachelorthesis.presentation.shared.baseClasses.BaseActivity;
import com.sunilson.bachelorthesis.presentation.shared.baseClasses.HasViewModel;
import com.sunilson.bachelorthesis.presentation.shared.utilities.Constants;
import com.sunilson.bachelorthesis.presentation.shared.utilities.Navigator;
import com.sunilson.bachelorthesis.presentation.shared.viewmodelBasics.ViewModelFactory;

import org.joda.time.DateTime;
import org.joda.time.Days;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

/**
 * @author Linus Weiss
 *
 * The main activity of the app. Here the fragments with the calendar data is displayed
 */

public class HomepageActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, HasViewModel<CalendarViewModel>, HasSupportFragmentInjector, DatePickerDialog.OnDateSetListener {

    @OnClick(R.id.activity_homepage_add_deadline)
    public void addDeadlineEvent() {
        Navigator.navigateToAddEvent(this, 2);
    }
    @OnClick(R.id.activity_homepage_add_appointment)
    public void addAppEvent() {
        Navigator.navigateToAddEvent(this, 1);
    }
    @BindView(R.id.activity_homepage_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.activity_homepage_navigation)
    NavigationView navigationView;

    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    HomepageCalendarHelper homepageCalendarHelper;
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;
    @Inject
    CheckLoginStatusUseCase checkLoginStatusUseCase;
    @Inject
    GetCurrentUser getCurrentUser;

    CalendarViewModel calendarViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_24dp);
        navigationView.setNavigationItemSelectedListener(this);

        //Decide which fragment should be loaded and only if this is no orientation change
        if (savedInstanceState == null) {
            changeContainerFragment();
        }

        //Get ViewModel from factory
        calendarViewModel = ViewModelProviders.of(this, viewModelFactory).get(CalendarViewModel.class);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_homepage, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.ADD_EVENT_REQUEST:
                changeContainerFragment();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_homepage_calendar:
                DateTime dateTime = homepageCalendarHelper.getCurrentCalendarDates()[0];
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        this, this, dateTime.getYear(), dateTime.getMonthOfYear() - 1, dateTime.getDayOfMonth());
                datePickerDialog.show();
                break;
            case R.id.menu_homepage_refresh:
                changeContainerFragment();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_homepage_day:
                homepageCalendarHelper.setDayAmount(0);
                changeContainerFragment();
                drawerLayout.closeDrawers();
                return true;
            case R.id.menu_homepage_three_days:
                homepageCalendarHelper.setDayAmount(2);
                changeContainerFragment();
                drawerLayout.closeDrawers();
                return true;
            case R.id.menu_homepage_week:
                homepageCalendarHelper.setDayAmount(6);
                changeContainerFragment();
                drawerLayout.closeDrawers();
                return true;
            case R.id.menu_homepage_log_out:
                //TODO Logout UseCase
                Navigator.navigateToLogin(this);
                return true;
            case R.id.menu_homepage_settings:
                Navigator.navigateToSettings(this);
                return true;
            case R.id.menu_homepage_checkLogin:
                getCurrentUser.execute(null).subscribeWith(new Observer<DomainUser>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DomainUser domainUser) {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
                return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CalendarViewModel getViewModel() {
        return this.calendarViewModel;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    /**
     * Creates an Intent that can be used to navigate to this Activity
     *
     * @param context
     * @return Intent to navigate to this Activity
     */
    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, HomepageActivity.class);
        return intent;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void loading(boolean value) {}

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        DateTime[] dateTimes = homepageCalendarHelper.getCurrentCalendarDates();
        int dayAmount = Days.daysBetween(dateTimes[0], dateTimes[1]).getDays();
        DateTime from = new DateTime(year, month + 1, dayOfMonth, 0, 0);
        DateTime to = from.plusDays(dayAmount);
        homepageCalendarHelper.setCurrentCalendarDates(from, to);
        changeContainerFragment();
    }

    private void changeContainerFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_homepage_framelayout, HomepageCalendarContainerFragment.newInstance()).commit();
    }
}
