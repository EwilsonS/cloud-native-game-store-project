package com.evanco.levelupservice.exception;

/**
 * Exception class to handle multiple values returned from
 * the database when only one return value is expected,
 * such as when finding points by customer id, but there are
 * multiple rows with the same customer id
 */
public class AmbiguousResultException extends RuntimeException {

    public AmbiguousResultException() {
    }

    public AmbiguousResultException(String message) {
        super(message);
    }
}
