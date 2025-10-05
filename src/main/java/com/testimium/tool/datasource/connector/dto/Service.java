package com.testimium.tool.datasource.connector.dto;

import java.io.File;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import com.testimium.tool.datasource.connector.dom.IRequestBody;

/**
 * @author Sandeep Agrawal
 *
 */
@XmlRootElement(name="service")
public class Service implements IRequestBody {

	private String name;
	private List<ConnectorBean> connectors;
	/**
	 * @return the name
	 */
	@XmlAttribute
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the connectors
	 */
	@XmlElementWrapper(name = "connectors")
	@XmlElement(name = "connector")
	public List<ConnectorBean> getConnectors() {
		return connectors;
	}
	/**
	 * @param connectors the connectors to set
	 */
	public void setConnectors(List<ConnectorBean> connectors) {
		this.connectors = connectors;
	}
	@Override
	public String toXMLString() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String toJSONString() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public IRequestBody convertXMLToObject(File xmlFile) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
