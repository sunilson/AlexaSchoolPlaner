package com.sunilson.bachelorthesis.domain.model;

import org.joda.time.DateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Linus Weiss
 */

@NoArgsConstructor
public class DomainEvent {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private DateTime from, to;

    @Getter
    @Setter
    private String description, location, summary;

    @Getter
    @Setter
    private int eventType;

}
