package com.testimium.tool.datasource.connector.factory;

import java.util.Properties;

import com.testimium.tool.context.TestContext;
import com.testimium.tool.datasource.connector.common.ConnectorConstant;
import com.testimium.tool.exception.PropertyNotFoundException;

/**
 * @author Sandeep Agrawal
 * 
 */
public class DBConnectorFactory {

	private static HibernateConnector hibernateConnector;

	/**
	 * Method return appropriate connection factory depending upon received property.
	 * 
	 * @param property
	 * @return
	 * @throws PropertyNotFoundException
	 */
	public static DBConnector getConnector(Properties property)
			throws PropertyNotFoundException {
		DBConnector connector = null;
		String dbType = property.getProperty(ConnectorConstant.DATABASE_TYPE);
		if (dbType == null)
			throw new PropertyNotFoundException(ConnectorConstant.DATABASE_TYPE
					+ " property not found");
		if(null != property.getProperty("isMainDataSource") && "YES".equalsIgnoreCase(property.getProperty("isMainDataSource"))) {
			TestContext.getTestContext("").setMainDBConnector(getConnector(dbType));
			return getConnector(dbType);
		}
		return getConnector(dbType);
	}


	/**
	 * Method return appropriate connection factory depending upon received property.
	 *
	 * @param dbType
	 * @return
	 * @throws PropertyNotFoundException
	 */
	public static DBConnector getConnector(String dbType) throws PropertyNotFoundException {
		DBConnector connector = null;
		if (dbType == null)
			throw new PropertyNotFoundException(ConnectorConstant.DATABASE_TYPE + " property not found");

		switch(dbType.trim()) {
			case "SQLServer":
			case "Oracle":
			case "MySQL":
			case "PostgreSQL":
				connector = getHibernateConnector();
				break;
			default:
				return null;

		}
		/*if (ConnectorConstant.SPREAD_SHEET_DB.equalsIgnoreCase(dbType))
			connector = new SpreadSheetConnector();*/
		return connector;
	}

	private static HibernateConnector getHibernateConnector() {
		if(null == hibernateConnector)
			hibernateConnector = new HibernateConnector();
		return hibernateConnector;
	}
}
