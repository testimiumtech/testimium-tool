package com.testimium.tool.exception;
/**
 * @author Sandeep Agrawal
 */
public class CommandNotFoundException extends Exception {

    public CommandNotFoundException(String command) {
        super(String.format("'%s' command is not found! ", command));
    }
}
