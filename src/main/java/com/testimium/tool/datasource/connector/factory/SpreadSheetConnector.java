package com.testimium.tool.datasource.connector.factory;

import com.testimium.tool.datasource.connector.dto.ConnectorKey;
import com.testimium.tool.exception.DBConnectorException;
import com.testimium.tool.exception.DBException;
//import com.codoid.products.fillo.Fillo;

import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * @author Sandeep Agrawal
 * 
 */
public class SpreadSheetConnector implements DBConnector {

	/**
	 * Method will extract spread sheet data using provided property
	 * and return response list
	 * 
	 * @return Object
	 */
	@Override
	public Object connect(Properties properties) throws DBConnectorException {
		return null;//new Fillo();
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
