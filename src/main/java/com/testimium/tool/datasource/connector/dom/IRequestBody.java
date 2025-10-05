package com.testimium.tool.datasource.connector.dom;

import java.io.File;


/**
 * Interface provides methods to be implemented.
 * 
 * @author Sandeep Agrawal
 *
 */
public interface IRequestBody extends IServicePayload{

	/**
	 * Returns XML representation of the object as XML string.
	 * 
	 * @return xml text as String
	 */
	public String toXMLString();
	/**
	 * Returns JSON representation of the object as JSON string.
	 * 
	 * @return json text as String 
	 */
	public String toJSONString();
	
	/**
	 * Convert xml file to complex type object. 
	 * 
	 * @param xmlFile XML file
	 * @return IRequestBody
	 */
	public IRequestBody convertXMLToObject(File xmlFile);
}
