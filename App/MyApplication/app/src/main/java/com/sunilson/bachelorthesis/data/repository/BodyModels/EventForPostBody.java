package com.sunilson.bachelorthesis.data.repository.BodyModels;

import lombok.AllArgsConstructor;

/**
 * @author Linus Weiss
 */

@AllArgsConstructor
public class EventForPostBody {
    String description, location, summary;
    Long  from, to;
}
