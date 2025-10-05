package com.testimium.tool.exception;
/**
 * @author Sandeep Agrawal
 */
public class CommandParamException extends Exception {

    public CommandParamException() {
    }

    public CommandParamException(String message) {
        super(message);
    }

    public CommandParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandParamException(Throwable cause) {
        super(cause);
    }

    public CommandParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
