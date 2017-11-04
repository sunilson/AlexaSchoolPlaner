package com.sunilson.bachelorthesis.presentation.homepage.day;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunilson.bachelorthesis.R;
import com.sunilson.bachelorthesis.presentation.models.CalendarDay;
import com.sunilson.bachelorthesis.presentation.models.Event;
import com.sunilson.bachelorthesis.presentation.utilities.ViewUtilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Linus Weiss
 */

public class DayView extends RelativeLayout {

    //Day with events. Events must be sorted by starting date
    private CalendarDay calendarDay = new CalendarDay();
    //List for events. Inner list are the connected groups
    private List<EventGroup> addedEvents = new ArrayList<>();
    private Date startPoint;
    private int groupCounter = 0;

    public DayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void addDay(CalendarDay calendarDay) {
        this.calendarDay = calendarDay;
        calculateFields();
    }

    private void calculateFields() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            calendarDay.getEventList().add(new Event(sdf.parse("20/12/1993 03:45"), sdf.parse("20/12/1993 08:55"), "Event 1"));
            calendarDay.getEventList().add(new Event(sdf.parse("20/12/1993 10:45"), sdf.parse("20/12/1993 18:58"), "Event 1"));
            calendarDay.getEventList().add(new Event(sdf.parse("20/12/1993 10:55"), sdf.parse("20/12/1993 11:55"), "Event 2"));
            calendarDay.getEventList().add(new Event(sdf.parse("20/12/1993 10:55"), sdf.parse("20/12/1993 11:55"), "Event 2"));
            calendarDay.getEventList().add(new Event(sdf.parse("20/12/1993 11:05"), sdf.parse("20/12/1993 12:25"), "Event 3"));
            calendarDay.getEventList().add(new Event(sdf.parse("20/12/1993 12:35"), sdf.parse("20/12/1993 13:35"), "Event 4"));
            calendarDay.getEventList().add(new Event(sdf.parse("20/12/1993 13:45"), sdf.parse("20/12/1993 15:55"), "Event 5"));
            calendarDay.getEventList().add(new Event(sdf.parse("20/12/1993 15:52"), sdf.parse("20/12/1993 16:55"), "Event 6"));
            calendarDay.getEventList().add(new Event(sdf.parse("20/12/1993 16:58"), sdf.parse("20/12/1993 18:55"), "Event 7"));
            calendarDay.getEventList().add(new Event(sdf.parse("20/12/1993 19:08"), sdf.parse("20/12/1993 20:55"), "Event 8"));
            startPoint = sdf.parse("20/12/1993 00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (Event event : calendarDay.getEventList()) {
            checkOverlappingEvents(event);
        }


        int groupIndex = 0;
        for (EventGroup eventGroup : addedEvents) {
            Log.d("Linus", "Group with index : " + Integer.toString(groupIndex));
            int columnIndex = 0;
            for (EventColumn eventColumn : eventGroup.eventColumns) {
                Log.d("Linus", "Column with index : " + Integer.toString(columnIndex));
                for (Event event : eventColumn.events) {
                    if (event instanceof RecurringEvent) {
                        Log.d("Linus", "Reccuring Event with title " + event.getTitle() + " and width " + Integer.toString(event.getWidth()));
                    } else {
                        Log.d("Linus", "Event with title " + event.getTitle() + " and width " + Integer.toString(event.getWidth()));
                    }
                }
                columnIndex++;
            }
            groupIndex++;
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


    public void init() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                calculateFields();
            }
        });
    }

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

        for (EventGroup eventGroup : addedEvents) {
            int horizontalIndex = 0;
            for (EventColumn eventColumn : eventGroup.eventColumns) {
                int width = eventGroup.eventColumns.size();
                for (Event event : eventColumn.events) {
                    if (!(event instanceof RecurringEvent)) {
                        addView(generateSingleField(width, event, horizontalIndex));
                    }
                }
                horizontalIndex++;
            }
        }
    }

    private View generateSingleField(int width, final Event event, int horizontalMargin) {
        //Setup container view
        LinearLayout container = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParamsContainer = new LinearLayout.LayoutParams(
                getWidth() / width * event.getWidth(),
                ViewUtilities.dateToHeight((int) getResources().getDimension(R.dimen.day_height), event.getFrom().getTime(), event.getTo().getTime()));
        layoutParamsContainer.setMargins(
                getWidth() / width * horizontalMargin,
                ViewUtilities.dateToHeight((int) getResources().getDimension(R.dimen.day_height), startPoint.getTime(), event.getFrom().getTime()),
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
        drawable.setColorFilter(new PorterDuffColorFilter(event.getColor(), PorterDuff.Mode.SRC_IN));
        content.setBackground(drawable);
        content.setGravity(Gravity.CENTER);


        //Add text to content
        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams layoutParamsTextView = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParamsTextView);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setMaxLines(1);
        textView.setText(event.getTitle());
        content.addView(textView);

        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), event.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        container.addView(content);

        return container;
    }

    private class RecurringEvent extends Event {

        public Event mainEvent;

        public RecurringEvent(Event event) {
            super(event.getFrom(), event.getTo(), event.getTitle());
            this.mainEvent = event;
        }
    }

    private class CalendarField {
        public int width;
        public String id;

        public CalendarField(int width, String id) {
            this.id = id;
            this.width = width;
        }
    }

    private class EventGroup {
        public List<EventColumn> eventColumns = new ArrayList<>();
        public long currentEnd = 0;
    }

    private class EventColumn {
        public List<Event> events = new ArrayList<>();
        public long currentEnd = 0;
    }

      /*
        //Draw events
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                if (fields[i][j] != null) {


                    //Check above and below, if this is a single event or a longer one, add respective padding
                    int paddingValue = (int) getResources().getDimension(R.dimen.day_field_padding);
                    int paddingTop = 0;
                    int paddingBottom = 0;
                    if (j != 0 && fields[i][j - 1] != null && fields[i][j - 1].id != fields[i][j].id) {
                        paddingTop = paddingValue;
                    }
                    if (j != fields[i].length - 1 && fields[i][j + 1] != null && fields[i][j + 1].id != fields[i][j].id) {
                        paddingBottom = paddingValue;
                    }
                    container.setPadding(paddingValue, paddingTop, paddingValue, paddingBottom);


                    //Add container to parent
                    addView(container);
                }
            }
        }
        */

}
