package com.testimium.tool.exception;

/**
 * @author Sandeep Agrawal
 * 
 */
public class DBConnectorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DBConnectorException() {
		super();
	}

	/**
	 */
	public DBConnectorException(Exception ex) {
		super(ex);
	}

	/**
	 * @param exceptionMsg
	 */
	public DBConnectorException(String exceptionMsg) {
		super(exceptionMsg);
	}

	/**
	 * @param exceptionMsg
	 */
	public DBConnectorException(String exceptionMsg, Exception ex) {
		super(exceptionMsg, ex);
	}

}
