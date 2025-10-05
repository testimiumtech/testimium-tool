package com.testimium.tool.datasource.connector;

import com.testimium.tool.datasource.connector.common.ConnectorValidator;
import com.testimium.tool.exception.DBConnectorException;
import com.testimium.tool.exception.PropertyNotFormattedProperlyException;
import com.testimium.tool.datasource.connector.factory.DBConnector;
import com.testimium.tool.datasource.connector.factory.DBConnectorFactory;

import java.util.Properties;


/**
 * @author Sandeep Agrawal
 * 
 */

public class Connector {

	/**
	 * connect method will get property as method argument and based on property
	 * it will connect to data source.
	 * 
	 * @param property
	 * @return Object
	 * @throws DBConnectorException
	 */
	public static Object connect(Properties property, boolean isCleanCache) throws DBConnectorException {
		Object connectorObj = null;

		if (!ConnectorValidator.validateProperty(property))
			throw new PropertyNotFormattedProperlyException();

		DBConnector connector = DBConnectorFactory.getConnector(property);
		try {
			if(isCleanCache)
				connector.clean();

			connectorObj = connector.connect(property);
		} catch (Exception e) {
			throw new DBConnectorException(e);
		}
		return connectorObj;
	}

}
