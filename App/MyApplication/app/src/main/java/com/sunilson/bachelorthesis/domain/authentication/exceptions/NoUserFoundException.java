package com.sunilson.bachelorthesis.domain.authentication.exceptions;

/**
 * Created by linus_000 on 21.12.2017.
 */

public class NoUserFoundException extends RuntimeException {

    public NoUserFoundException(String message) {
        super(message);
    }
}
