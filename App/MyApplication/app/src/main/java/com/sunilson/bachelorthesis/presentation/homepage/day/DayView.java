package com.sunilson.bachelorthesis.presentation.homepage.day;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunilson.bachelorthesis.R;
import com.sunilson.bachelorthesis.presentation.models.events.Event;
import com.sunilson.bachelorthesis.presentation.models.events.EventType;
import com.sunilson.bachelorthesis.presentation.utilities.ViewUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Linus Weiss
 *
 * A view which takes in an object of the class {@link com.sunilson.bachelorthesis.presentation.homepage.day.CalendarDayModel} and renders its events on a hourly timeline
 */
public class DayView extends RelativeLayout {

    //TODO Make calculations async
    //TODO Make widths of events in rows evenly width

    //Day with events. Events must be sorted by starting date
    private CalendarDayModel calendarDayModel = new CalendarDayModel();
    //List for events. Inner list are the connected groups
    private List<EventGroup> addedEvents = new ArrayList<>();
    private int groupCounter = 0;
    private boolean ready = false;
    private View topView;

    public DayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void calculateFields() {

        //Iterate over all events and start the calculations
        for (Event event : calendarDayModel.getEventList()) {
            checkOverlappingEvents(event);
        }

        renderFields();
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
            eventColumn.currentEnd = event.getTo().getTime();
            return true;
        }

        //Check if event is overlapping current column
        if (eventColumn.currentEnd > event.getFrom().getTime()) {
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
            if (event.getTo().getTime() > eventColumn.currentEnd) {
                eventColumn.currentEnd = event.getTo().getTime();
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
     * @param index The index of the "old" column, which should be expanded to a new one
     */
    private void reverseExpand(Event event, EventGroup eventGroup, int index) {

        EventColumn previousColumn = eventGroup.eventColumns.get(index);
        EventColumn newColumn = eventGroup.eventColumns.get(index + 1);

        for (Event tempEvent : previousColumn.events) {
            if (tempEvent.getTo().getTime() < event.getFrom().getTime()) {
                if (tempEvent instanceof RecurringEvent) {
                    ((RecurringEvent) tempEvent).mainEvent.setWidth(((RecurringEvent) tempEvent).mainEvent.getWidth() + 1);
                } else {
                    tempEvent.setWidth(tempEvent.getWidth() + 1);
                }
                newColumn.events.add(new RecurringEvent(tempEvent));
            }
        }
    }

    /**
     * Render the events at their position with their calculated size
     */
    private void renderFields() {

        final float scale = getResources().getDisplayMetrics().density;

        //Draw seperator lines
        for (int i = 50; i < getHeight() * scale * 0.5f; i += 50) {
            View line = new View(getContext());
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
            layoutParams.setMargins(0, (int) (i * scale + 0.5f), 0, 0);
            line.setLayoutParams(layoutParams);
            line.setBackgroundColor(getResources().getColor(R.color.calendar_background_text));
            addView(line);
        }

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
                        if(topView == null) {
                            topView = view;
                        }
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
                ViewUtilities.dateToHeight((int) getResources().getDimension(R.dimen.day_height), event.getFrom().getTime(), event.getTo().getTime()));
        layoutParamsContainer.setMargins(
                getWidth() / width * horizontalMargin,
                ViewUtilities.dateToHeight((int) getResources().getDimension(R.dimen.day_height), calendarDayModel.getDayStartDate().getTime(), event.getFrom().getTime()),
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
        Drawable drawable = getResources().getDrawable(R.drawable.event_background);
        drawable.setColorFilter(new PorterDuffColorFilter(getResources().getColor(event.getEventType().getVal()), PorterDuff.Mode.SRC_IN));
        content.setBackground(drawable);
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
                Toast.makeText(getContext(), event.getDescription(), Toast.LENGTH_SHORT).show();
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
    public void addDay(CalendarDayModel calendarDayModel) {
        this.calendarDayModel = calendarDayModel;

        if (ready) {
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
    public void init() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ready = true;
            }
        });
    }

    public View getTopView() {
        return this.topView;
    }

}
