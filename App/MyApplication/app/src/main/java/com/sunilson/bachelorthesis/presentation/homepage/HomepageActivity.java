package com.sunilson.bachelorthesis.presentation.homepage;

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
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sunilson.bachelorthesis.R;
import com.sunilson.bachelorthesis.presentation.baseClasses.BaseActivity;
import com.sunilson.bachelorthesis.presentation.baseClasses.HasViewModel;
import com.sunilson.bachelorthesis.presentation.homepage.day.HomepageFragmentCalendar;
import com.sunilson.bachelorthesis.presentation.homepage.exception.CalendarSettingsException;
import com.sunilson.bachelorthesis.presentation.utilities.Constants;
import com.sunilson.bachelorthesis.presentation.viewmodelBasics.ViewModelFactory;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * @author Linus Weiss
 */

public class HomepageActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, HasViewModel, HasSupportFragmentInjector {

    @BindView(R.id.activity_homepage_drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.activity_homepage_navigation)
    NavigationView navigationView;

    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    HomepageViewModel homepageViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        ButterKnife.bind(this);

        try {
            CurrentCalendarSettingsStorage.setDates(this, new DateTime(DateTimeZone.UTC), new DateTime(DateTimeZone.UTC).plusDays(1));
        } catch (CalendarSettingsException e) {
            e.printStackTrace();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_24dp);
        navigationView.setNavigationItemSelectedListener(this);

        //Decide which fragment should be loaded and only if this is no orientation change
        if (savedInstanceState == null) {
            changeFragment(HomepageFragmentCalendar.newInstance(2), Constants.FRAGMENT_TAG_DAY);
        }

        //Get ViewModel from factory
        homepageViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomepageViewModel.class);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_homepage_day:
                changeFragment(HomepageFragmentCalendar.newInstance(1), Constants.FRAGMENT_TAG_DAY);
                drawerLayout.closeDrawers();
                return true;
            case R.id.menu_homepage_three_days:
                changeFragment(HomepageFragmentCalendar.newInstance(3), Constants.FRAGMENT_TAG_DAY);
                drawerLayout.closeDrawers();
                return true;
            case R.id.menu_homepage_week:
                changeFragment(HomepageFragmentCalendar.newInstance(7), Constants.FRAGMENT_TAG_DAY);
                drawerLayout.closeDrawers();
                return true;
        }

        return false;
    }

    /**
     * Change current fragment in Acitvity
     * @param fragment Instance of new Fragment
     * @param tag Tag of new Fragment used to find it later
     */
    public void changeFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.activity_homepage_frame_layout, fragment, tag).commit();
    }

    @Override
    @SuppressWarnings("unchecked")
    public HomepageViewModel getViewModel() {
        return this.homepageViewModel;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    /**
     * Creates an Intent that can be used to navigate to this Activity
     *
     * @param context
     * @param fragmentTag Tag of fragment that should be loaded first
     * @return  Intent to navigate to this Activity
     */
    public static Intent getCallingIntent(Context context, String fragmentTag) {
        Intent intent = new Intent(context, HomepageActivity.class);
        intent.putExtra("fragmentTag", fragmentTag);
        return intent;
    }
}
