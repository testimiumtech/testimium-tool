package com.testimium.tool.datasource.connector.register;

import com.testimium.tool.context.TestContext;
import com.testimium.tool.datasource.CommonUtility;
import com.testimium.tool.datasource.connector.Connector;
import com.testimium.tool.datasource.connector.common.ConnectorUtil;
import com.testimium.tool.datasource.connector.common.HibernatePropertyEnum;
import com.testimium.tool.datasource.connector.dto.ConnectorBean;
import com.testimium.tool.datasource.connector.dto.ConnectorKey;
import com.testimium.tool.datasource.connector.dto.Connectorservices;
import com.testimium.tool.datasource.connector.dto.Service;
import com.testimium.tool.exception.DBConnectorException;
import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.utility.FileUtility;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Sandeep Agrawal
 */
public class DataSourceRegistry {

	private static Map<ConnectorKey, Object> connectorMap = new HashMap<ConnectorKey, Object>();

	/*public static Map<ConnectorKey, Object> getConnectorMap() {
		return connectorMap;
	}*/

	public static void reBuildConnectorMap() throws DBConnectorException {
		connectorMap = new HashMap<ConnectorKey, Object>();
		buildConnectorMap();
	}
	/**
	 * @return the sessionFactory
	 */
	public static Map<ConnectorKey, Object> getConnectorMap() throws DBConnectorException {
		if(null == connectorMap || connectorMap.size() <=0) {
			DataSourceRegistry.buildConnectorMap();
		}
		return DataSourceRegistry.connectorMap;
	}

	public static void buildConnectorMap() throws DBConnectorException {
		ConnectorKey connectorKey = null;
		Object connectorResp = null;
		try {
			Connectorservices connectorservices = (Connectorservices) CommonUtility
					.convertXMLToObject(FileUtility.getFile("datasource/DatasourceRegistry.xml"), Connectorservices.class);
			if (connectorservices == null)
				return;
			List<Service> services = connectorservices.getServices();
			if (services == null || services.isEmpty())
				return;
			for (Service service : services) {
				System.out.println("Into Service loop");
				List<ConnectorBean> connectorBeansList = service.getConnectors();
				if (connectorBeansList != null && !connectorBeansList.isEmpty()) {
					for (ConnectorBean connectorBean : connectorBeansList) {
						connectorKey = new ConnectorKey();
						connectorKey.setServiceName(service.getName());
						connectorKey.setConnectorName(connectorBean.getName());
						connectorKey.setConnectorUrl(ConnectorUtil.getConnectorURL(connectorBean));
						connectorKey.setDriver(connectorBean.getConfigurationProperty().getProperty(HibernatePropertyEnum.HIBERNATE_DRIVER_CLASS.getValue()));
						connectorKey.setDbUser(connectorBean.getConfigurationProperty().getProperty(HibernatePropertyEnum.HIBERNATE_USERNAME.getValue()));
						connectorKey.setPassword(connectorBean.getConfigurationProperty().getProperty(HibernatePropertyEnum.HIBERNATE_PASSWORD.getValue()));
						if (!connectorMap.containsKey(connectorKey)) {
							try {
								connectorResp = Connector.connect(connectorBean.getConfigurationProperty(), true);
								System.out.println("connectorResp not null");
							} catch (DBConnectorException e) {
							/*Collection<ServiceError> errorMessages = new ArrayList<ServiceError>();
							errorMessages.add(new ServiceError(null, e.getMessage(), null));*/
								//System.out.println("ConnectorException happened....");
								LogUtil.logToolErrorMsg("DBConnectorException happened: ", e);
							}
							connectorMap.put(connectorKey, connectorResp);
						}
					}
				}
			}
			LogUtil.logToolMsg("connectorMap size: " + connectorMap.size());
			LogUtil.logToolMsg("connectorMap : " + connectorMap.toString());
		} catch (IOException ex) {
			LogUtil.logToolErrorMsg("IOException happened: connectorMap - " + connectorMap.toString() , ex);
			throw new DBConnectorException("DBConnectorException: "+ ex.getMessage(), ex);
		}
	}

	public static Object getConnector(ConnectorKey connectorKey) throws DBConnectorException {
		if (null == connectorKey)
			return null;
		return getConnectorMap().get(connectorKey);
	}

	/**
	 *  Return the matched ConnectorKey
	 * @return
	 */
	public static ConnectorKey getMatchedConnectorKey() {
		Stream<ConnectorKey> connectorKeyStream = connectorMap
				.entrySet()
				.stream()
				.filter(entry -> TestContext.getTestContext("").getMainConnectorKey().equals(entry.getKey()))
				.map(Map.Entry::getKey);
		return connectorKeyStream.findFirst().get();
	}


	/**
	 *  Returns database Connection object
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static Connection getSqlConnectionObj() throws SQLException, ClassNotFoundException {
		ConnectorKey connectorKey = getMatchedConnectorKey();
		//Registering the Driver
		//DriverManager.registerDriver(new net.sourceforge.jtds.jdbc.Driver());
		Class.forName("net.sourceforge.jtds.jdbc.Driver");

		//Getting the connection
		Connection con = DriverManager.getConnection(connectorKey.getConnectorUrl(), connectorKey.getDbUser(), connectorKey.getPassword());
		System.out.println("Connection established......");
		return con;
	}

	/*public static void main(String[] args) throws DBConnectorException {
			buildConnectorMap();
		}*/

}
