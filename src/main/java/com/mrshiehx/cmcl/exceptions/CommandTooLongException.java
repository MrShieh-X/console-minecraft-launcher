package com.mrshiehx.cmcl.exceptions;

public class CommandTooLongException extends Exception {
    public CommandTooLongException() {
        super();
    }

    public CommandTooLongException(String message) {
        super(message);
    }

    public CommandTooLongException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandTooLongException(Throwable cause) {
        super(cause);
    }

    protected CommandTooLongException(String message, Throwable cause,
                                      boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
