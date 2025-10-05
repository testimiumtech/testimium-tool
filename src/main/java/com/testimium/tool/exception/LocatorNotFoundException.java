package com.testimium.tool.exception;

/**
 * @author Sandeep Agrawal
 */
public class LocatorNotFoundException extends Exception {

	private static final long serialVersionUID = 5606191897859235048L;
	private String locatorName;

    public LocatorNotFoundException(String locatorName) {
        super(String.format("Locator '%s' is not found or not properly configured in Object Factory", locatorName));
        this.locatorName = locatorName;
    }

    public String getLocatorName() {
        return locatorName;
    }

}
