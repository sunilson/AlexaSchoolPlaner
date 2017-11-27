package com.sunilson.bachelorthesis.domain.model;

import lombok.AllArgsConstructor;

/**
 * @author Linus Weiss
 */

@AllArgsConstructor
public class EventForPostBody {
    String description, location, summary;
    Long  from, to;
}
