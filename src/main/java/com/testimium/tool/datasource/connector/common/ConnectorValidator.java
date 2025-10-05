package com.testimium.tool.datasource.connector.common;


import com.testimium.tool.exception.DBConnectorException;
import com.testimium.tool.exception.PropertyNotFoundException;

import java.util.Properties;
import java.util.Set;

/**
 * @author Sandeep Agrawal
 * 
 */
public class ConnectorValidator {

	/**
	 * validateProperty() method will validate property object whether it is
	 * valid or not
	 * 
	 * @param property
	 * @return
	 * @throws DBConnectorException
	 */
	public static boolean validateProperty(Properties property)
			throws DBConnectorException {

		if (property == null)
			throw new PropertyNotFoundException("No property found");

		String dbType = property.getProperty(ConnectorConstant.DATABASE_TYPE);
		if (dbType == null)
			throw new PropertyNotFoundException(ConnectorConstant.DATABASE_TYPE
					+ " property not found");
		dbType = dbType.trim();
		if (ConnectorConstant.SQLServer.equalsIgnoreCase(dbType))
			return validateHibernateProperty(property);
		if (ConnectorConstant.SPREAD_SHEET_DB.equalsIgnoreCase(dbType))
			return validateSpreadSheetProperty(property);

		throw new PropertyNotFoundException(ConnectorConstant.DATABASE_TYPE
				+ " property not found");
	}

	/**
	 * validateHibernateProperty() method will validate Hibernate related
	 * properties
	 * 
	 * @param property
	 * @return
	 * @throws DBConnectorException
	 */
	private static boolean validateHibernateProperty(Properties property)
			throws DBConnectorException {

		Set<Object> keySet = property.keySet();
		for (HibernatePropertyEnum hibernatePropertyEnum : HibernatePropertyEnum
				.values())
			if (!keySet.contains(hibernatePropertyEnum.getValue()))
				throw new PropertyNotFoundException(
						hibernatePropertyEnum.getValue()
								+ " property not found");
		return true;
	}

	/**
	 * validateSpreadSheetProperty() method will validate Spread sheet related
	 * properties
	 * 
	 * @param property
	 * @return
	 * @throws DBConnectorException
	 */
	private static boolean validateSpreadSheetProperty(Properties property)
			throws DBConnectorException {
		Set<Object> keySet = property.keySet();
		for (ExcelPropertyEnum excelPropertyEnum : ExcelPropertyEnum
				.values())
			if (!keySet.contains(excelPropertyEnum.getValue()))
				throw new PropertyNotFoundException(
						excelPropertyEnum.getValue()
								+ " property not found");
		return true;
	}
}
