package com.testimium.tool.exception;
/**
 * Exception class is used by custom comparator classes to generate excetion with proper messages.
 * @author Sandeep Agrawal
 */
public class ToolComparatorException extends Exception {

    public ToolComparatorException() {
    }

    public ToolComparatorException(String message) {
        super(message);
    }

    public ToolComparatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToolComparatorException(Throwable cause) {
        super(cause);
    }

    public ToolComparatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
