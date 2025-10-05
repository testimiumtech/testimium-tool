package com.testimium.tool.datasource.connector.common;

/**
 * @author Sandeep Agrawal
 *
 */
public enum ExcelPropertyEnum {

	APPLICATION_NAME("application_name"),
	DATABASE_TYPE("database_type");

	private String value;

	ExcelPropertyEnum(String value) {
		this.value = value;
	}

	/**
	 * @return value
	 */
	public String getValue() {
		return value;
	}
}
