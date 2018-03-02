package com.sunilson.bachelorthesis.data.repository.BodyModels;

import lombok.AllArgsConstructor;

/**
 * @author Linus Weiss
 */

@AllArgsConstructor
public class EventForPostBody {
    Integer type;
    String description, location, summary, id;
    Long  from, to;
}
