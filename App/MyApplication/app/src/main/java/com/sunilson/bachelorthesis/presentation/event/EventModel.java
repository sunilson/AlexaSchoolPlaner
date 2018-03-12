package com.sunilson.bachelorthesis.presentation.event;

import com.sunilson.bachelorthesis.presentation.shared.utilities.DateUtilities;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Linus Weiss
 *         <p>
 *         Base Model class of a single event for the Presentation layer
 */

@NoArgsConstructor
@RequiredArgsConstructor
public class EventModel {

    @Getter
    @Setter
    @NonNull
    private String id;

    @Getter
    @Setter
    @NonNull
    private DateTime from;

    @Getter
    @Setter
    @NonNull
    private DateTime to;

    @Getter
    @Setter
    @NonNull
    private String description;

    @Getter
    @Setter
    private String summary;

    @Getter
    @Setter
    private String location;

    @Getter
    @Setter
    private int width = 1;

    @Getter
    @Setter
    @NonNull
    private EventType eventType;

    public EventModel(EventModel eventModel) {
        this.from = eventModel.from;
        this.to = eventModel.to;
        this.description = eventModel.description;
        this.id = eventModel.id;
        this.summary = eventModel.summary;
        this.location = eventModel.location;
        this.eventType = eventModel.eventType;
    }

    public String formatDateTime() {
        String result = "Am " + DateTimeFormat.forPattern("dd.MM.yyyy").print(from) + " um " + DateTimeFormat.forPattern("HH:mm").print(from);

        if (from.withTimeAtStartOfDay().isEqual(to.withTimeAtStartOfDay())) {
            result += " bis um " + DateTimeFormat.forPattern("HH:mm").print(to);
        } else {
            result += " bis am " + DateTimeFormat.forPattern("dd.MM.yyyy").print(to) + " um " + DateTimeFormat.forPattern("HH:mm").print(to);
        }

        return result;
    }

    public String simpleFormatDateTime(DateTime dateTime) {
        return DateUtilities.formatDateTime(dateTime);
    }
}
