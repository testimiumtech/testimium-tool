package com.testimium.tool.datasource;

import com.testimium.tool.datasource.connector.dom.IRequestBody;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.Map;
/**
 * Class provides the API's utilities to manipulate on objects.
 * 
 * @author Sandeep Agrawal
 * 
 */
public class CommonUtility {

	private String SCHEMA_FILE_LOCATION 		= "/schema/datasourceConnector.xsd";
	/**
	 * Converts an object marked as @XmlRootElement to XML String.
	 * 
	 * @param classname as Class parameter, e.g. Response.class.
	 * @param object should be a class object , which need to be marshal.
	 * @return String
	 */
	public static <T> String objectToXMLString(Class<T> classname, Object object) {
		String xmlString = null;

		StringWriter sw = new StringWriter();
		try {
			JAXBContext jc = JAXBContext.newInstance(classname);
			Marshaller m = jc.createMarshaller();
			m.marshal(object, sw);
			xmlString = sw.toString();
			System.out.println("Json To XML: " + xmlString);
			sw.close();

		} catch (JAXBException ex) {
			System.out.println("JAXBException occurred under CommonUtil.objectToXMLString(Class classname, Object obj)"+ ex);
			try {
				sw.close();
			} catch (IOException e) {
				System.out.println("IOException occurred under CommonUtil.objectToXMLString(Class classname, Object obj)"+ e);
			}
		} catch (IOException ex) {
			System.out.println("IOException occurred under CommonUtil.objectToXMLString(Class classname, Object obj)"+ ex);
			try {
				sw.close();
			} catch (Exception e) {
				System.out.println("Exception occurred under CommonUtil.objectToXMLString(Class classname, Object obj)"+ e);
			}
		}
		return xmlString;
	}

	/**
	 * Converts an object marked as @XmlRootElement to XML String. it required to create Marshaller object of the 
	 * class to be marshalled and pass the Marshaller object.
	 * 
	 * @param object
	 * @param m object of the class to be marshalled.
	 * @return String
	 */
	public static String objectToXMLString(Object object, Marshaller m) {
		String xmlString = null;

		StringWriter sw = new StringWriter();
		try {
			m.marshal(object, sw);
			xmlString = sw.toString();
			sw.close();
		} catch (JAXBException ex) {
			System.out.println("JAXBException occurred under CommonUtil.objectToXMLString():"+ ex);
			try {
				sw.close();
			} catch (IOException e) {
				System.out.println("IOException occurred under CommonUtil.objectToXMLString():"+ e);
			}
		} catch (IOException ex) {
			System.out.println("IOException occurred under CommonUtil.objectToXMLString():"+ ex);
			try {
				sw.close();
			} catch (Exception e) {
				System.out.println("Exception occurred under CommonUtil.objectToXMLString():"+ e);
			}
		}
		System.out.println("Object to XML String: "+xmlString);
		return xmlString;

	}

	/**
	 * Converts an object to JSON String.
	 * 
	 * @param object 
	 * @return JSON format as String
	 */
	public static String objectToJSONString(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(object);
		} catch (JsonGenerationException e) {
			System.out.println("JsonGenerationException occurred under CommonUtil.objectToJSONString(): "+ e);
		} catch (JsonMappingException e) {
			System.out.println("JsonMappingException occurred under CommonUtil.objectToJSONString(): "+ e);
		} catch (IOException e) {
			System.out.println("IOException occurred under CommonUtil.objectToJSONString(): "+ e);
		}
		System.out.println("Object to JSON String: "+json);
		return json;
	}
	/**
	 * Convert XML file to complex type of any object.
	 *  
	 * @param xmlFile
	 * @param clazz
	 * @return Object
	 */
	public static <T> Object externalXMLToObject(File xmlFile, Class<T> clazz) {
		Object unMarshallObj = null;
		StringWriter sw = new StringWriter();
		try {
			JAXBContext jc = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unMarshallObj = (Object) unmarshaller.unmarshal(xmlFile);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(unMarshallObj, sw);
			System.out.println(sw.toString());
			sw.close();
		} catch (JAXBException e) {
			System.out.println("JAXBException occurred under CommonUtil.externalXMLToObject()"+ e);
			try {
				sw.close();
			} catch (IOException e1) {
				System.out.println("IOException occurred under CommonUtil.externalXMLToObject()"+ e1);
			}
		} catch (IOException e) {
			System.out.println("IOException occurred under CommonUtil.externalXMLToObject()"+ e);
		}

		return unMarshallObj;
	}
	/**
	 * Convert XML file to complex type of object IRequestBody.
	 * 
	 * @param xmlFile
	 * @param clazz
	 * @return IRequestBody
	 */
	public static <T> IRequestBody convertXMLToObject(File xmlFile, Class<T> clazz) {
		IRequestBody unMarshallObj = null;
		StringWriter sw = new StringWriter();
		try {
			JAXBContext jc = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unMarshallObj = (IRequestBody) unmarshaller.unmarshal(xmlFile);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(unMarshallObj, sw);
			System.out.println("convertXML To Object : "+sw.toString());
			sw.close();
		} catch (JAXBException e) {
			System.out.println("JAXBException occurred under CommonUtil.convertXMLToObject()"+ e);
			e.printStackTrace();
			try {
				sw.close();
			} catch (IOException e1) {
				System.out.println("IOException occurred under CommonUtil.convertXMLToObject()"+ e1);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException occurred under CommonUtil.convertXMLToObject()"+ e);
		}

		return unMarshallObj;
	}
	
	/**
	 * Convert XML file as input stream to complex type of object IRequestBody.
	 * 
	 * @param inputStream
	 * @param clazz
	 * @return IRequestBody
	 */
	public static <T> IRequestBody convertXMLInputSourceToObject(InputSource inputStream, Class<T> clazz) {
		IRequestBody unMarshallObj = null;
		StringWriter sw = new StringWriter();
		try {
			JAXBContext jc = JAXBContext.newInstance(clazz);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unMarshallObj = (IRequestBody) unmarshaller.unmarshal(inputStream);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(unMarshallObj, sw);
			System.out.println("convertXML To Object : "+sw.toString());
			sw.close();
		} catch (JAXBException e) {
			System.out.println("JAXBException occurred under CommonUtil.convertXMLToObject()"+ e);
			e.printStackTrace();
			try {
				sw.close();
			} catch (IOException e1) {
				System.out.println("IOException occurred under CommonUtil.convertXMLToObject()"+ e1);
			}
		} catch (IOException e) {
			System.out.println("IOException occurred under CommonUtil.convertXMLToObject()"+ e);
			e.printStackTrace();
		}

		return unMarshallObj;
	}

	/**
	 * Check if key is present in Map.
	 * 
	 * @param key
	 * @param map
	 * @return boolean true or false
	 */
	public static boolean isMapKeyPresent(String key, Map<String, String> map){
		if(null != map.get(key))
			return true;

		return false;
	}
	
	 
	
	/*public boolean isValidXML(String xmlFilePath)  {
		Source xmlFile = null;
		try{
			URL schemaFile = new URL(SCHEMA_FILE_LOCATION);
			xmlFile = new StreamSource(new File(xmlFilePath));
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(schemaFile);
			Validator validator = schema.newValidator();
			validator.validate(xmlFile);
		}catch (SAXException e) {
			System.out.println(xmlFilePath+ "\n"+xmlFile.getSystemId() + " is NOT valid");
			System.out.println("SAXException occurred under CommonUtil.isValidXML()"+ e.getMessage());
			//String message = e.getMessage();
			//message = message.substring((message.indexOf("cvc-complex-type")+7), message.length());
			////contextPathAware.addErrors("", message);
			return false;
		}  catch (IOException e) {
			System.out.println("IOException occurred under CommonUtil.isValidXML()"+ e.getMessage());
			////contextPathAware.addErrors(""+ e.getMessage());
			return false;
		}
		return true;
	}*/
	/**
	 * Validate XML against system defined schema.
	 *  
	 * @param xmlFilePath XML file name along with path 
	 * @return boolean true if XML is valid else false
	 */
	public boolean isValidXML(String xmlFilePath) {
		Source xmlFile = null;
		System.out.println("xmlFilePath: "+xmlFilePath);
		System.out.println("schemaFilePath: "+SCHEMA_FILE_LOCATION);
		//System.out.println(xmlFilePath+"    "+schemaFilePath);
		try {
			SchemaFactory schemaFactory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(new SAXSource(new InputSource(CommonUtility.class.getResourceAsStream(SCHEMA_FILE_LOCATION))));
			Validator validator = schema.newValidator();
			File file = new File(xmlFilePath);
			System.out.println("Xml File Absolute Path in COmmonUtil.isValidXML(): "+file.getAbsolutePath());
			xmlFile = new StreamSource(file);
			validator.validate(xmlFile);
			System.out.println(xmlFile.getSystemId() + " is valid");
		} catch (SAXException e) {
			System.out.println("SAXException occurred under CommonUtil.isValidXML()"+ e);
			System.out.println(xmlFilePath+ "\n"+xmlFile.getSystemId() + " is NOT valid"+ e);
			System.out.println("Reason: " + e.getLocalizedMessage()+ e);
			String message = e.getMessage();
			//message = message.substring((message.indexOf("cvc-complex-type")+7), message.length());
			////contextPathAware.addErrors("", message);
			return false;
		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException occurred under CommonUtil.isValidXML()"+ e);
			//contextPathAware.addErrors(""+ e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException occurred under CommonUtil.isValidXML()"+ e);
			//contextPathAware.addErrors(""+ e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception occurred under CommonUtil.isValidXML()"+ e);
			//contextPathAware.addErrors(""+ e.getMessage());
		}
		return true;
	}
}
