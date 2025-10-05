package com.testimium.tool.reader;

import com.testimium.tool.logging.LogUtil;
import com.testimium.tool.logging.LogsComprassor;
import com.testimium.tool.utility.FileUtility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
/**
 * @author Sandeep Agrawal
 */
public class PropertyReader {

	private static Properties properties = new Properties();

	static {
		try {
			//LogUtil.logToolMsg("Initializing Tool properties...........");
			//properties.load(new FileInputStream("conf/testing.properties"));
			load(new String[]{"conf/testing.properties"});
			String envProfile = properties.getProperty("tool.active.profile");
			//LogUtil.logToolMsg("Loading profile " + envProfile + ".........");
			//properties.load(new FileInputStream("conf/" + envProfile + "-env.properties"));
			load(new String[]{"conf/" + envProfile + "-env.properties"});

			try {
				if("TRUE".equalsIgnoreCase(properties.getProperty("enable.logging.compressor"))) {
					new LogsComprassor().compress();
				}

			} catch (IOException ex) {
				LogUtil.logToolErrorMsg("IOException - make zip for previous logs ", ex);
			}

			load(FileUtility.getAllFiles(properties.getProperty("project.properties.path")));
		} catch (IOException ex) {
			LogUtil.logToolErrorMsg("IOException - Failed to load tool profile/locators: ", ex);
		}
	}

	/**
	 *
	 * @param propFiles
	 */
	public static void load(String[] propFiles) throws IOException {
		//LogUtil.logToolMsg("Loading Locators......... ");
		try {
			for (String fileName: propFiles) {
				//LogUtil.logToolMsg(fileName);
				properties.load(new FileInputStream(fileName));
			}
		} catch (FileNotFoundException ex) {
			LogUtil.logToolErrorMsg("FileNotFoundException - Failed to load tool profile/locators: ", ex);
		}
	}

	/*public Properties getObjectRepository() throws IOException{
		//Read object repository file
		//InputStream stream = new FileInputStream(new File(System.getProperty("user.dir")+"\\src\\objects\\object.properties"));
		InputStream stream = new FileInputStream("D:\\SandeepAgrawal\\testimium\\POC\\Selenium\\codebase\\smarttestingtool\\Selenium_POC\\src\\main\\resources\\config\\"+ "testing.properties");
		//load all objects
		properties.load(stream);
		return properties;
	}*/

	public static String getProperty(String key) {
		//TODO Implement Exception handling in case key not found.
		/*if(key.trim().contains("="))
			key = key.replace("=", "&&");*/
		return properties.getProperty(key);
	}

	public static void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}
}
