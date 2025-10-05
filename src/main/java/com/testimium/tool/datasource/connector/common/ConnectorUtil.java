package com.testimium.tool.datasource.connector.common;
import com.testimium.tool.datasource.connector.dto.ConnectorBean;

import java.util.Properties;

/**
 * @author Sandeep Agrawal
 * 
 */
public class ConnectorUtil {

	/**
	 * getExistingConnectorServices() method will get existing connector
	 * services present in connectorServiceRegistry.xml file
	 * 
	 * @return Connectorservices
	 */
	/*public static Connectorservices getExistingConnectorServices() throws IOException {
		*//*InputSource source = new InputSource(
				ConnectorUtil.class
						.getResourceAsStream("/connectorRegistry/ConnectorServiceRegistry.xml"));*//*
		Connectorservices connectorservices = (Connectorservices) CommonUtility
				.convertXMLInputSourceToObject(FileUtility.getFile("datasource/DatasourceRegistry.xml"), Connectorservices.class);
		return connectorservices;
	}*/

	/**
	 * writeConnectionRegistry() method will add new connecservice in existing
	 * ConnectorServiceRegistry.xml
	 * 
	 * @param connectorservices
	 */
	/*public static void writeConnectionRegistry(
			Connectorservices connectorservices) {
		BufferedWriter writer = null;
		FileWriter fileWriter = null;
		String xmlStr = CommonUtility.objectToXMLString(Connectorservices.class,
				connectorservices);
		URL url = ConnectorUtil.class
				.getResource("/ConnectorServiceRegistry.xml");
		File file = null;
		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		try {

			fileWriter = new FileWriter(file);
			writer = new BufferedWriter(fileWriter);
			writer.write(xmlStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}*/

	/**
	 * getConnectorURL() method will provide connection URL of data source for
	 * specified connectorBean
	 * 
	 * @param connectorBean
	 * @return String
	 */
	public static String getConnectorURL(ConnectorBean connectorBean) {
		String connectionUrl = "";
		Properties properties = connectorBean.getConfigurationProperty();
		String databaseType = properties.getProperty(ConnectorConstant.DATABASE_TYPE);
		//TODO Fix Me for NullPointerException
		switch(databaseType) {
			case "SQLServer":
			case "Oracle":
			case "MySQL":
			case "PostgreSQL":
				connectionUrl = properties.getProperty(HibernatePropertyEnum
						.valueOf(ConnectorConstant.HIBERNATE_DATABASE_URL)
						.getValue());
				break;
			default:
				return null;
		}

		/*if (ConnectorConstant.RELATIONAL_DB.equalsIgnoreCase(databaseType)) {
			connectionUrl = properties.getProperty(HibernatePropertyEnum
					.valueOf(ConnectorConstant.HIBERNATE_DATABASE_URL)
					.getValue());
		}*/
		/*if (ConnectorConstant.SAP_DB.equalsIgnoreCase(databaseType)) {
			connectionUrl = properties.getProperty(SAPPropertyEnum.valueOf(
					ConnectorConstant.SAP_CONNECTION_URL).getValue());
		}*/
		return connectionUrl;
	}
}
