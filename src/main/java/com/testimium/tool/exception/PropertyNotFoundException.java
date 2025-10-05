package com.testimium.tool.exception;

/**
 * @author Sandeep Agrawal
 * 
 */
public class PropertyNotFoundException extends DBConnectorException {

	private static final long serialVersionUID = 1L;

	public PropertyNotFoundException() {
		super();
	}

	public PropertyNotFoundException(Exception ex) {
		super(ex);
	}

	public PropertyNotFoundException(String exceptionMsg) {
		super(exceptionMsg);
	}

	public PropertyNotFoundException(String exceptionMsg, Exception ex) {
		super(exceptionMsg, ex);
	}

}
