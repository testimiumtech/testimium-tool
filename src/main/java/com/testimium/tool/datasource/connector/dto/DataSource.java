package com.testimium.tool.datasource.connector.dto;

import java.util.List;
import java.util.Properties;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "datasource")
public class DataSource {

	/**
	 * This field contains data source name
	 */
	private String name;
	/**
	 * This field contains list of data source properties
	 */
	private List<Property> properties;

	/**
	 * @return the name
	 */
	@XmlAttribute
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the properties
	 */
	@XmlElementWrapper(name = "properties")
	@XmlElement(name = "property")
	public List<Property> getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	/**
	 * This method will return list of properties available
	 * 
	 * @return Properties
	 */
	public Properties getConfigurationProperty() {
		Properties prop = new Properties();
		for (Property property : this.properties) {
			prop.put(
					property.getKey(),
					(property.getValue().equals("true") || property.getValue()
							.equals("false")) ? Boolean.getBoolean(property
							.getValue()) : property.getValue());
		}
		return prop;
	}
}
