package com.testimium.tool.exception;
/**
 * @author Sandeep Agrawal
 */
public class TaskNotFoundException extends Exception {

    public TaskNotFoundException(String task) {
        super(String.format("'%s' task is not found! ", task));
    }
}
