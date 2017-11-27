package com.sunilson.bachelorthesis.presentation.event;

import org.joda.time.DateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Linus Weiss
 *
 * Base Model class of a single event for the Presentation layer
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
}
