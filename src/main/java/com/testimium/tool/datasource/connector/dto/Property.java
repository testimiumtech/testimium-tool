package com.testimium.tool.datasource.connector.dto;

import com.testimium.tool.datasource.connector.dom.IRequestBody;

import java.io.File;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author Sandeep Agrawal
 * 
 */
@XmlRootElement(name = "property")
public class Property implements IRequestBody {

	/**
	 * This field contains property key
	 */
	private String key;
	/**
	 * This field contains property value
	 */
	private String value;

	/**
	 * @return the key
	 */
	@XmlAttribute
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	@XmlAttribute
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * This method will convert object into xml return String
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
