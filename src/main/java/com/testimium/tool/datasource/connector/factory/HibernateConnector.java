package com.testimium.tool.datasource.connector.factory;

import com.testimium.tool.datasource.connector.common.ConnectorConstant;
import com.testimium.tool.datasource.connector.dao.VividDataDaoImpl;
import com.testimium.tool.datasource.connector.dto.ConnectorKey;
import com.testimium.tool.datasource.connector.register.DataSourceRegistry;
import com.testimium.tool.exception.DBConnectorException;
import com.testimium.tool.exception.DBException;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Sandeep Agrawal
 * 
 */
public class HibernateConnector implements DBConnector {

	private static Map<String, Object> connectorMap = new HashMap<String, Object>();
	public void clean() {
		connectorMap = new HashMap<String, Object>();
	}

	/**
	 * Method will connect to data base using provided property and
	 * return hibernate connection factory
	 * 
	 * @return Object
	 */
	@Override
	public Object connect(Properties properties) throws DBConnectorException {
		Object factory = null;
		String applicationName = properties
				.getProperty(ConnectorConstant.APPLICATION_NAME);
		if (connectorMap.containsKey(applicationName))
			return connectorMap.get(applicationName);

		ServiceRegistry serviceRegistry = null;
		Configuration configuration = new Configuration();
		configuration.setProperties(properties);
		serviceRegistry = new ServiceRegistryBuilder().applySettings(
				configuration.getProperties()).buildServiceRegistry();
		factory = configuration.buildSessionFactory(serviceRegistry);
		return factory;
	}

	@Override
	public int executeUpdate(String sql, ConnectorKey connectorKey) throws DBException, DBConnectorException {
		System.out.println("executeUpdate SQLScript: "+ sql);
		int result;
		try{
			result = new VividDataDaoImpl().executeUpdate(sql, connectorKey) ;
		} catch (DBException ex) {
			if(ex.getMessage().contains("could not inspect JDBC autocommit mode")){
				DataSourceRegistry.reBuildConnectorMap();
				result = new VividDataDaoImpl().executeUpdate(sql, connectorKey) ;
			} else {
				throw new DBException(ex.getMessage());
			}
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> executeSQL(String sql, ConnectorKey connectorKey) throws DBException, DBConnectorException {
		System.out.println("executeSQL SQLScript: "+ sql);
		List<Map<String, Object>> result = null;
		try{
			result = new VividDataDaoImpl().executeQuey(sql, connectorKey) ;
		} catch (DBException ex) {
			if(ex.getMessage().contains("could not inspect JDBC autocommit mode")){
				DataSourceRegistry.reBuildConnectorMap();
				result = new VividDataDaoImpl().executeQuey(sql, connectorKey) ;
			} else {
				throw new DBException(ex.getMessage());
			}
		}
		return result;
	}

	@Override
	public void execute(String sqlQuery, ConnectorKey connectorKey) throws DBException, DBConnectorException {
		System.out.println("executeQuery SQLScript: "+ sqlQuery);
		try{
			new VividDataDaoImpl().execute(sqlQuery, connectorKey);
		} catch (DBException ex) {
			if(ex.getMessage().contains("could not inspect JDBC autocommit mode")){
				DataSourceRegistry.reBuildConnectorMap();
				new VividDataDaoImpl().execute(sqlQuery, connectorKey);
			} else {
				throw new DBException(ex.getMessage());
			}
		}
	}

	/*public static void main(String[] args) throws Exception {
		ConnectorKey key = new ConnectorKey();
		key.setServiceName("testimium");
		key.setConnectorName("sql.server.dev");
		DataSourceRegistry.buildConnectorMap();
		//new VividDataDaoImpl().executeExcelQuery("Employee", "", key);
		//List<Map<String, Object>> list = new VividDataDaoImpl().executeSQLQuey("Select * from TB_MODULE", key);
		List<Map<String, Object>> list = new HibernateConnector().executeSQL("SELECT FIRST_NAME, LAST_NAME FROM TB_CUSTOMERS", key);
		//List<Map<String, Object>> list = new VividDataDaoImpl().executeQuey("Update TB_MODULE set IS_USED='N' WHERE MODULE_NAME = 'MIN_MAX'", key);
		System.out.println("=================="+list.size());
		Thread.sleep(30000);
		list = new HibernateConnector().executeSQL("SELECT FIRST_NAME, LAST_NAME FROM TB_CUSTOMERS", key);
		//List<Map<String, Object>> list = new VividDataDaoImpl().executeQuey("Update TB_MODULE set IS_USED='N' WHERE MODULE_NAME = 'MIN_MAX'", key);
		System.out.println("=================="+list.size());
	}*/

}
