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
public interface DBConnector {

	Object connect(Properties properties) throws DBConnectorException;
	void clean();
	int executeUpdate(String sql, ConnectorKey connectorKey) throws DBException, DBConnectorException;
	List<Map<String, Object>> executeSQL(String sql, ConnectorKey connectorKey) throws DBException, DBConnectorException;
	void execute(String sqlQuery, ConnectorKey connectorKey) throws DBException, DBConnectorException;
}
