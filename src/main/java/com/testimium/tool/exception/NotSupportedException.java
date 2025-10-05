package com.testimium.tool.exception;
/**
 * @author Sandeep Agrawal
 */
public class NotSupportedException extends Exception {

    public NotSupportedException(String msg) {
        super(String.format("'%s' is not supported by tool! ", msg));
    }
}
