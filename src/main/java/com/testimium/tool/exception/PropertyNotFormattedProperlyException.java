package com.testimium.tool.exception;

/**
 * @author Sandeep Agrawal
 * 
 */
public class PropertyNotFormattedProperlyException extends DBConnectorException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PropertyNotFormattedProperlyException() {
		super();
	}

	/**
	 * @param ex
	 */
	public PropertyNotFormattedProperlyException(Exception ex) {
		super(ex);
	}

	/**
	 * @param exceptionMsg
	 */
	public PropertyNotFormattedProperlyException(String exceptionMsg) {
		super(exceptionMsg);
	}

	/**
	 * @param exceptionMsg
	 * @param ex
	 */
	public PropertyNotFormattedProperlyException(String exceptionMsg,
			Exception ex) {
		super(exceptionMsg, ex);
	}
}
