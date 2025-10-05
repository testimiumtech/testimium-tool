package com.testimium.tool.datasource.connector.factory;


import com.testimium.tool.datasource.connector.dto.ConnectorKey;
import com.testimium.tool.exception.DBConnectorException;
import com.testimium.tool.exception.DBException;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Sandeep Agrawal
 * 
 */
public class JdbcConnector implements DBConnector {

	/**
	 * Method will get data from database using provided property and
	 * return object
	 * 
	 * @return Object
	 */
	@Override
	public Object connect(Properties properties) throws DBConnectorException {

		return null;
	}

	@Override
	public void clean() {

	}

	@Override
	public int executeUpdate(String sql, ConnectorKey connectorKey) throws DBException {
		return 0;
	}

	@Override
	public List<Map<String, Object>> executeSQL(String sql, ConnectorKey connectorKey) throws DBException {
		return null;
	}

	@Override
	public void execute(String sqlQuery, ConnectorKey connectorKey) throws DBException, DBConnectorException {

	}

}
