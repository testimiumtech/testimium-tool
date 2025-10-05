package com.testimium.tool.datasource.connector.dao;

import com.testimium.tool.datasource.connector.dto.ConnectorKey;
import com.testimium.tool.exception.DBConnectorException;
import com.testimium.tool.exception.DBException;

import java.util.List;
import java.util.Map;

/**
 * Generic DAO Interface to execute hibernate SQL queries.
 * @author Sandeep grawal
 */
public interface IVividDataDao {

	/**
	 * Execute Only Select SQL query and the Store procedures based on database connector configure in system.
	 * 
	 * @param sqlQuery String
	 * @param connectorKey ConnectorKey
	 * @return List of map
	 * @throws DBException if any database exception
	 * @throws DBConnectorException if DB connection issue
	 */
	List<Map<String, Object>> executeQuey(String sqlQuery, ConnectorKey connectorKey) throws DBException, DBConnectorException;

	/**
	 * Execute Only Insert and Update DML SQL query based on database connector configure in system.
	 * @param sqlQuery input param
	 * @param connectorKey input param
	 * @return number
	 * @throws DBException if any database exception
	 * @throws DBConnectorException if DB connection issue
	 */
	int executeUpdate(String sqlQuery, ConnectorKey connectorKey) throws DBException, DBConnectorException;
	//public List<Object[]> getDataList(String queryString, ConnectorKey connectorKey) throws Exception;
	//TODO remove this and implement ExcelDaoImpl by implementing IVividDataDao and use the same executeQuery() method
	//List<Map<String, Object>> executeExcelQuery(String sheetName, String sqlQuery, ConnectorKey connectorKey) throws Exception;

	void execute(String sqlQuery, ConnectorKey connectorKey) throws DBException, DBConnectorException;
}
