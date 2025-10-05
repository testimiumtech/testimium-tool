package com.testimium.tool.domain;

import com.testimium.tool.command.Command;

/**
 * @author Sandeep Agrawal
 */
public class CommandResponse {
    private String message;
    private Command nextCommand;
    private boolean isSuccess;
    private String executedCommand;
    private Exception exception;
    private int skipNumberOfNextTestCases;
    private int failNumberOfNextTestCases;

    public CommandResponse(String message) {
        this.message = message;
    }

    public CommandResponse(String message, Command nextCommand) {
        this.message = message;
        this.nextCommand = nextCommand;
    }
    public CommandResponse(String message, boolean isSuccess) {
        this.message = message;
        this.isSuccess = isSuccess;
    }

    public CommandResponse(String message, boolean isSuccess, String executedCommand) {
        this.message = message;
        this.isSuccess = isSuccess;
        this.executedCommand = executedCommand;
    }

    public CommandResponse(String message, boolean isSuccess, Exception exception) {
        this.message = message;
        this.isSuccess = isSuccess;
        this.exception = exception;
    }

    public CommandResponse(String message, Command nextCommand, boolean isSuccess) {
        this.message = message;
        this.nextCommand = nextCommand;
        this.isSuccess = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public Command getNextCommand() {
        return nextCommand;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getExecutedCommand() {
        return executedCommand;
    }

    public void setExecutedCommand(String executedCommand) {
        this.executedCommand = executedCommand;
    }

    public Exception getException() {
        return exception;
    }

    public int getSkipNumberOfNextTestCases() {
        return skipNumberOfNextTestCases;
    }

    public void setSkipNumberOfNextTestCases(int skipNumberOfNextTestCases) {
        this.skipNumberOfNextTestCases = skipNumberOfNextTestCases;
    }

    public int getFailNumberOfNextTestCases() {
        return failNumberOfNextTestCases;
    }

    public void setFailNumberOfNextTestCases(int failNumberOfNextTestCases) {
        this.failNumberOfNextTestCases = failNumberOfNextTestCases;
    }
}
