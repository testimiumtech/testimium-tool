package com.testimium.tool.exception;
/**
 * @author Sandeep Agrawal
 */
public class InputParamNotFoundException extends Exception {

    public InputParamNotFoundException() {
        super(String.format("Input parameter is not found for command. "));
}
    }
