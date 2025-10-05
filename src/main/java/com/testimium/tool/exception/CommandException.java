package com.testimium.tool.exception;
/**
 * @author Sandeep Agrawal
 */
public class CommandException extends Exception {

    private boolean isApplyShutdown = false;

    public CommandException() {
    }
    public CommandException(boolean isApplyShutdown, String message) {
        super(message);
    }
    public CommandException(String message) {
        super(message);
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandException(Throwable cause) {
        super(cause);
    }

    public CommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public boolean isApplyShutdown() {
        return isApplyShutdown;
    }
}
