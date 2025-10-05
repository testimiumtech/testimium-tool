package com.testimium.tool.datasource.connector.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * @author Sandeep Agrawal
 * 
 */
@XmlRootElement(name = "connectorKey")
@XmlType(propOrder={"serviceName","connectorName","connectorUrl"})
@JsonSerialize(include=Inclusion.NON_NULL)
public class ConnectorKey {

	/**
	 * This field contains service name of connector
	 */
	private String serviceName;
	/**This filed contains connector name
	 * 
	 */
	private String connectorName;
	/**
	 * This filed contains data source connection URL
	 */
	private String connectorUrl;

	private String driver;
	private String dbUser;
	private String password;

	/**
	 * @return connectorUrl
	 */
	@XmlElement(name = "connectorUrl")
	public String getConnectorUrl() {
		return connectorUrl;
	}

	/**
	 * @param connectorUrl
	 */
	public void setConnectorUrl(String connectorUrl) {
		this.connectorUrl = connectorUrl;
	}

	/**
	 * @return
	 */
	@XmlElement(name = "serviceName")
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @return
	 */
	@XmlElement(name = "connectorName")
	public String getConnectorName() {
		return connectorName;
	}

	/**
	 * @param connectorName
	 */
	public void setConnectorName(String connectorName) {
		this.connectorName = connectorName;
	}


	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		ConnectorKey connectorKey = null;
		if (obj instanceof ConnectorKey) {
			connectorKey = (ConnectorKey) obj;
			if (this.connectorName.equalsIgnoreCase(connectorKey
					.getConnectorName())
					&& this.serviceName.equalsIgnoreCase(connectorKey
							.getServiceName())) {
				return true;
			}

		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int initialNum = 11;
		int hashcode = 0;
		hashcode = initialNum
				* (serviceName.hashCode() + connectorName.hashCode());
		return hashcode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConnectorKey [serviceName=" + serviceName + ", connectorName="
				+ connectorName + ", connectorUrl=" + connectorUrl + "]";
	}

	
}
