package com.testimium.tool.exception;

/**
 * @author Sandeep Agrawal
 * 
 */
public class DBException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public DBException() {
		super();
	}

	/**
	 */
	public DBException(Exception ex) {
		super(ex);
	}

	/**
	 * @param exceptionMsg
	 */
	public DBException(String exceptionMsg) {
		super(exceptionMsg);
	}

	/**
	 * @param exceptionMsg
	 */
	public DBException(String exceptionMsg, Exception ex) {
		super(exceptionMsg, ex);
	}

}
