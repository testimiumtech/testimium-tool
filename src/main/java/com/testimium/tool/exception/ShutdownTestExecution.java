package com.testimium.tool.exception;

/**
 * @author Sandeep Agrawal
 */
public class ShutdownTestExecution extends Exception {

	private String message;

    public ShutdownTestExecution(String message) {
        super(String.format("\nStoping the test execution because of the exception: ", message));
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
