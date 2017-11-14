package com.sunilson.bachelorthesis.presentation.homepage.calendar;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.sunilson.bachelorthesis.presentation.event.models.Event;
import com.sunilson.bachelorthesis.presentation.event.models.EventType;
import com.sunilson.bachelorthesis.presentation.homepage.calendar.calendarDay.CalendarDayModel;
import com.sunilson.bachelorthesis.presentation.homepage.exception.CalendarDayModelInvalidEventException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * @author Linus Weiss
 */

public class CalendarViewModel extends ViewModel {

    @Inject
    public CalendarViewModel() {

    }

    public LiveData<CalendarDaySpanModel> getDays(DateTime from, DateTime to) {
        Log.d("Linus", "getDays");
        int dayAmount = 3;
        CalendarDaySpanModel days = new CalendarDaySpanModel();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        CalendarDayModel calendarDayModel2 = new CalendarDayModel(formatter.parseDateTime("20/12/1993 10:45"));
        try {
            calendarDayModel2.add(new Event(formatter.parseDateTime("20/12/1993 03:45"), formatter.parseDateTime("20/12/1993 08:55"), "Event 1", EventType.SCHOOLAPPOINTMENT, null));

            calendarDayModel2.add(new Event(formatter.parseDateTime("20/12/1993 10:45"), formatter.parseDateTime("20/12/1993 18:58"), "Event 1", EventType.SCHOOLAPPOINTMENT, null));

            CalendarDayModel calendarDayModel = new CalendarDayModel(formatter.parseDateTime("20/12/1993 10:45"));
            calendarDayModel.add(new Event(formatter.parseDateTime("19/12/1993 10:45"), formatter.parseDateTime("21/12/1993 18:58"), "Event 1", EventType.SCHOOLAPPOINTMENT, null));
            calendarDayModel.add(new Event(formatter.parseDateTime("20/12/1993 10:55"), formatter.parseDateTime("20/12/1993 11:55"), "Event 2", EventType.SCHOOLAPPOINTMENT, null));
            calendarDayModel.add(new Event(formatter.parseDateTime("20/12/1993 10:55"), formatter.parseDateTime("20/12/1993 11:55"), "Event 2", EventType.SCHOOLAPPOINTMENT, null));
            calendarDayModel.add(new Event(formatter.parseDateTime("20/12/1993 11:05"), formatter.parseDateTime("20/12/1993 12:25"), "Event 3", EventType.SCHOOLAPPOINTMENT, null));
            calendarDayModel.add(new Event(formatter.parseDateTime("20/12/1993 12:35"), formatter.parseDateTime("20/12/1993 13:35"), "Event 4", EventType.SCHOOLAPPOINTMENT, null));
            calendarDayModel.add(new Event(formatter.parseDateTime("20/12/1993 13:45"), formatter.parseDateTime("20/12/1993 15:55"), "Event 5", EventType.SCHOOLAPPOINTMENT, null));
            calendarDayModel.add(new Event(formatter.parseDateTime("20/12/1993 15:50"), formatter.parseDateTime("20/12/1993 16:55"), "Event 6", EventType.SCHOOLAPPOINTMENT, null));
            calendarDayModel.add(new Event(formatter.parseDateTime("20/12/1993 16:58"), formatter.parseDateTime("20/12/1993 18:55"), "Event 7", EventType.SCHOOLAPPOINTMENT, null));
            calendarDayModel.add(new Event(formatter.parseDateTime("20/12/1993 19:08"), formatter.parseDateTime("20/12/1993 20:55"), "Event 8", EventType.SCHOOLAPPOINTMENT, null));

            for (int i = 0; i < dayAmount; i++) {
                if (i == 1) {
                    days.add(calendarDayModel2);
                } else {
                    days.add(calendarDayModel);
                }
            }
        } catch (CalendarDayModelInvalidEventException e) {
            e.printStackTrace();
        }

       return LiveDataReactiveStreams.fromPublisher(Flowable.just(days));
    }
}
