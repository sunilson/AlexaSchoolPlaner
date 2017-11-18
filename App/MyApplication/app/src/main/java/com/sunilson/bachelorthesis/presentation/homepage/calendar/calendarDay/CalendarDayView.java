package com.sunilson.bachelorthesis.presentation.homepage.calendar.calendarDay;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunilson.bachelorthesis.R;
import com.sunilson.bachelorthesis.presentation.event.models.Event;
import com.sunilson.bachelorthesis.presentation.event.models.EventType;
import com.sunilson.bachelorthesis.presentation.navigation.Navigator;
import com.sunilson.bachelorthesis.presentation.shared.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Linus Weiss
 *         <p>
 *         A view which takes in an object of the class {@link com.sunilson.bachelorthesis.presentation.homepage.calendar.calendarDay.CalendarDayModel} and renders its events on a hourly timeline
 */
public class CalendarDayView extends RelativeLayout {

    //TODO Make calculations async
    //TODO Make widths of events in rows evenly width

    private CalendarDayModel calendarDayModel;
    private List<EventGroup> addedEvents = new ArrayList<>();
    private int groupCounter = 0;
    private boolean ready = false;
    private View topView;

    public CalendarDayView(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.day_view, this, true);
    }

    private void calculateFields() {

        //Make calculations for DayView in background
        Observable
                .fromCallable(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {

                        //Iterate over all events and start the calculations
                        for (Event event : calendarDayModel.getEventList()) {
                            checkOverlappingEvents(event);
                        }


                        return true;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        renderFields();
                    }
                });
    }

    private void checkOverlappingEvents(Event event) {

        //Check if list is empty, if yes, add first list
        if (addedEvents.size() == 0) {
            addedEvents.add(new EventGroup());
        }

        checkGroups(addedEvents, event);
    }

    /**
     * Checks the group at the current index if the event overlaps any column. If not, add it to a new group,
     * otherwise add it to the current group
     *
     * @param groups All groups
     * @param event  The event to be added
     * @return
     */
    private boolean checkGroups(List<EventGroup> groups, Event event) {

        EventGroup group = groups.get(groupCounter);

        //Check if group is empty, if yes add the first column
        if (group.eventColumns.size() == 0) {
            group.eventColumns.add(new EventColumn());
        }

        //Iterate over all EventColumns
        if (!checkColumns(group, 0, event, true, false)) {
            groupCounter++;
            //Check if next group exists, otherwise add it
            if (groups.size() == groupCounter) {
                groups.add(new EventGroup());
            }
            return checkGroups(groups, event);
        } else {
            return true;
        }
    }


    /**
     * Checks if the given event overlaps in the column anywhere. If yes, the event is added to all
     * empty fields to the right
     *
     * @param eventGroup  Group containing the columns
     * @param index       Index of the current column
     * @param event       The event to be added
     * @param overlapping If the event has overlapped anything yet
     * @return If the event has been added to the group or not
     */
    private boolean checkColumns(EventGroup eventGroup, int index, Event event, boolean firstEvent, boolean overlapping) {

        if (index == eventGroup.eventColumns.size()) {
            if (overlapping) {
                return true;
            } else {
                return false;
            }
        }

        EventColumn eventColumn = eventGroup.eventColumns.get(index);

        //If first EventColumn is empty, add Event to it, as this is the first event
        if (eventColumn.events.size() == 0) {
            eventColumn.events.add(event);
            eventColumn.currentEnd = event.getTo().getMillis();
            return true;
        }

        //Check if event is overlapping current column
        if (eventColumn.currentEnd > event.getFrom().getMillis()) {
            //TODO Error bei Overlapping - Not Overlapping - Overlapping - Not Overlapping
            overlapping = true;

            //Check if next column exists, otherwise add a new one to make space for the event
            if (eventGroup.eventColumns.size() == index + 1) {
                eventGroup.eventColumns.add(new EventColumn());
                //Expand previous Fields to new column if they do not collide with new event
                reverseExpand(event, eventGroup, index);
            }
            return checkColumns(eventGroup, index + 1, event, firstEvent, overlapping);
        } else if (overlapping) {
            //Event is overlapping a field before, but not here, so add it
            if (firstEvent) {
                firstEvent = false;
                eventColumn.events.add(event);
            } else {
                event.setWidth(event.getWidth() + 1);
                eventColumn.events.add(new RecurringEvent(event));
            }

            //Set new end to column
            if (event.getTo().getMillis() > eventColumn.currentEnd) {
                eventColumn.currentEnd = event.getTo().getMillis();
            }
        }

        return checkColumns(eventGroup, index + 1, event, firstEvent, overlapping);
    }


    /**
     * Traverses a column in a group at a specific index upwards from a specific event and expands
     * all views in that column to the next one, if available
     *
     * @param event      The event which starts the expanding
     * @param eventGroup The group in which the column is
     * @param index      The index of the "old" column, which should be expanded to a new one
     */
    private void reverseExpand(Event event, EventGroup eventGroup, int index) {

        EventColumn previousColumn = eventGroup.eventColumns.get(index);
        EventColumn newColumn = eventGroup.eventColumns.get(index + 1);

        //Iterate over all Events of the previous column
        for (Event tempEvent : previousColumn.events) {
            //Only look at events that are before the given event
            if (tempEvent.getTo().getMillis() < event.getFrom().getMillis()) {

                //Checking if the event is a recurring event or not
                if (tempEvent instanceof RecurringEvent) {
                    //If it is a recurring event, add 1 width to the main Event
                    ((RecurringEvent) tempEvent).mainEvent.setWidth(((RecurringEvent) tempEvent).mainEvent.getWidth() + 1);
                } else {
                    //If it is a main event, add 1 event to it
                    tempEvent.setWidth(tempEvent.getWidth() + 1);
                }
                //Add new recurring event to the newly created Column
                newColumn.events.add(new RecurringEvent(tempEvent));
            }
        }
    }


    /**
     * Render the events at their position with their calculated size
     */
    private void renderFields() {
        //Go through all created event groups
        for (EventGroup eventGroup : addedEvents) {
            int horizontalIndex = 0;
            for (EventColumn eventColumn : eventGroup.eventColumns) {
                int width = eventGroup.eventColumns.size();
                for (Event event : eventColumn.events) {
                    //Only render "real" events, not placeholders
                    if (!(event instanceof RecurringEvent)) {
                        View view = generateSingleField(width, event, horizontalIndex);
                        addView(view);
                        if (topView == null) {
                            topView = view;
                        }
                    } else {
                    }

                }
                horizontalIndex++;
            }
        }
    }

    /**
     * Generates a View for a single Event
     *
     * @param width            Width of Event in its group
     * @param event            The event itself
     * @param horizontalMargin The margin which defines the horizontal position of the event
     * @return A Linearlayout containing the name of the Event and a click listener
     */
    private View generateSingleField(int width, final Event event, int horizontalMargin) {
        //Setup container view
        LinearLayout container = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParamsContainer = new LinearLayout.LayoutParams(
                getWidth() / width * event.getWidth(),
                ViewUtilities.dateToHeight((int) getResources().getDimension(R.dimen.day_height), event.getFrom().getMillis(), event.getTo().getMillis(), new long[] {calendarDayModel.getDayStartDate().getMillis(), calendarDayModel.getDayEndDate().getMillis()}));
        layoutParamsContainer.setMargins(
                getWidth() / width * horizontalMargin,
                ViewUtilities.dateToHeight((int) getResources().getDimension(R.dimen.day_height), calendarDayModel.getDayStartDate().getMillis(), event.getFrom().getMillis(),  new long[] {calendarDayModel.getDayStartDate().getMillis(), calendarDayModel.getDayEndDate().getMillis()}),
                0,
                0);

        container.setPadding((int) getResources().getDimension(R.dimen.day_field_container_padding),
                (int) getResources().getDimension(R.dimen.day_field_container_padding),
                (int) getResources().getDimension(R.dimen.day_field_container_padding),
                (int) getResources().getDimension(R.dimen.day_field_container_padding));

        container.setLayoutParams(layoutParamsContainer);

        //Setup content view
        LinearLayout content = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParamsContent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        content.setPadding((int) getResources().getDimension(R.dimen.day_field_content_padding),
                (int) getResources().getDimension(R.dimen.day_field_content_padding),
                (int) getResources().getDimension(R.dimen.day_field_content_padding),
                (int) getResources().getDimension(R.dimen.day_field_content_padding));
        content.setLayoutParams(layoutParamsContent);
        content.setBackgroundColor(getResources().getColor(event.getEventType().getVal()));
        content.setGravity(Gravity.CENTER);

        //Add text to content and content to container
        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams layoutParamsTextView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParamsTextView);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setMaxLines(1);
        textView.setText(event.getDescription());
        textView.setTextColor(getResources().getColor(R.color.white));
        content.addView(textView);
        container.addView(content);

        //Set click listener to view
        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigator.navigateToEvent(getContext(), v);
            }
        });

        return container;
    }

    /**
     * Placeholder class for events that are wider than one column. Contains all the information needed to expand it
     */
    private class RecurringEvent extends Event {

        Event mainEvent;

        RecurringEvent(Event event) {
            super(event.getFrom(), event.getTo(), event.getDescription(), EventType.SCHOOLAPPOINTMENT, null);
            this.mainEvent = event;
        }
    }

    /**
     * Describes a group of events that are overlapping with others in the group and are arranged in columns
     */
    private class EventGroup {
        public List<EventColumn> eventColumns = new ArrayList<>();
        public long currentEnd = 0;
    }

    /**
     * Describes a column of events that don't overlap but are in the same group
     */
    private class EventColumn {
        public List<Event> events = new ArrayList<>();
        public long currentEnd = 0;
    }


    /**
     * Adds a new day to the View and re-renders it
     *
     * @param calendarDayModel The new day to be added and rendered
     */
    public void renderDay(@Nullable CalendarDayModel calendarDayModel) {
        if (calendarDayModel != null) {
            this.calendarDayModel = calendarDayModel;
        }

        if (ready && this.calendarDayModel != null) {
            //Sort model to be sure it is sorted correctly for rendering
            this.calendarDayModel.sort();
            calculateFields();
        } else {
            //TODO Error Handling oder Listener implementieren
        }
    }

    /**
     * Sets up a layout listener and signalizes if the View is ready for altering. If ready, the listener is removed
     */
    public void init(@Nullable  final CalendarDayModel dayModel) {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);

                //Store dayModel
                if (dayModel != null) {
                    calendarDayModel = dayModel;
                }

                //Signal that view is ready and render day
                ready = true;
                renderDay(null);
            }
        });
    }

    public View getTopView() {
        return this.topView;
    }

}
