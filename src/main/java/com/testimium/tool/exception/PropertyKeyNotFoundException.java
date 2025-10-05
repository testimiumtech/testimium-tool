package com.testimium.tool.exception;
/**
 * @author Sandeep Agrawal
 */
public class PropertyKeyNotFoundException extends Exception {

    public PropertyKeyNotFoundException(String key) {
        super(String.format("%s Property key is not found", key));
    }
}
