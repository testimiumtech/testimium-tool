package com.testimium.tool.datasource.connector.dto;


import com.testimium.tool.datasource.connector.dom.IRequestBody;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * @author Sandeep Agrawal
 * 
 */
@XmlRootElement(name = "connector")
public class ConnectorBean implements IRequestBody {

	/**
	 * This field contains connector name
	 */
	private String name;
	/**
	 * This field contains list of property
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
	 * This method will return all configuration properties present
	 * 
	 * @return Properties
	 */
	public Properties getConfigurationProperty() {
		//TODO Put this in cache
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

	/**
	 * This method will convert object into XML return String
	 */
	@Override
	public String toXMLString() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method will convert object into JSON string return String
	 */
	@Override
	public String toJSONString() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method will convert XML into Object return IRequestBody
	 */
	@Override
	public IRequestBody convertXMLToObject(File xmlFile) {
		// TODO Auto-generated method stub
		return null;
	}
}
