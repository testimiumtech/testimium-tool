package com.testimium.tool.datasource.connector.dto;

import com.testimium.tool.datasource.CommonUtility;
import com.testimium.tool.datasource.connector.dom.IRequestBody;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.List;


/**
 * @author Sandeep Agrawal
 *
 */
@XmlRootElement(name="connectorservices")
public class Connectorservices implements IRequestBody {

	/**
	 * This filed contains list of service present
	 */
	private List<Service> services;

	/**
	 * @return the services
	 */
	@XmlElementWrapper(name = "services")
	@XmlElement(name = "service")
	public List<Service> getServices() {
		return services;
	}

	/**
	 * @param services the services to set
	 */
	public void setServices(List<Service> services) {
		this.services = services;
	}

	/**
	 * This method will convert Object into XML String return String
	 */
	@Override
	public String toXMLString() {
		return CommonUtility.objectToXMLString(this.getClass(), this);
	}


	/**
	 * This method will convert Object into JSON string return Json String
	 */
	@Override
	public String toJSONString() {
		return CommonUtility.objectToJSONString(this);
	}


	/**
	 * This method will convert XML into Object return IRequestBody
	 */
	@Override
	public IRequestBody convertXMLToObject(File xmlFile) {
		return CommonUtility.convertXMLToObject(xmlFile, this.getClass());
	}
}
