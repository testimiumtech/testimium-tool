package com.testimium.tool.datasource.connector.common;

/**
 * @author Sandeep Agrawal
 *
 */
public enum HibernatePropertyEnum {

	HIBERNATE_DRIVER_CLASS("hibernate.connection.driver_class"), HIBERNATE_URL(
			"hibernate.connection.url"), HIBERNATE_USERNAME(
			"hibernate.connection.username"), HIBERNATE_PASSWORD(
			"hibernate.connection.password"), HIBERNATE_DIALECT(
			"hibernate.dialect");

	private String value;

	HibernatePropertyEnum(String value) {
		this.value = value;
	}

	/**
	 * @return value
	 */
	public String getValue() {
		return value;
	}
}
