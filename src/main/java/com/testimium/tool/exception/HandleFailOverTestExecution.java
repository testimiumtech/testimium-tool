package com.testimium.tool.exception;

import com.testimium.tool.domain.CommandParam;

/**
 * @author Sandeep Agrawal
 */
public class HandleFailOverTestExecution extends Exception {

	private String message;
	private CommandParam commandParam;

    public HandleFailOverTestExecution(String message) {
        super(String.format("Handling the fail over test execution: ", message));
        this.message = message;
    }


    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public HandleFailOverTestExecution(String message, CommandParam commandParam) {
        super(String.format("Handling the fail over test execution: ", message));
        this.message = message;
        this.commandParam = commandParam;
    }

    public String getMessage() {
        return message;
    }

    public CommandParam getCommandParam() {
        return commandParam;
    }
}
