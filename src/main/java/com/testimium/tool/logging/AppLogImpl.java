package com.testimium.tool.logging; /**
 * 
 *//*
package com.testimium.common.logging;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.varia.LevelRangeFilter;

*//**
 * @author sandeep
 *
 *//*
public class AppLogImpl implements AppLog {

	private static String logDir="c:/logs/PushClientTool/clients";
	private Class loggerClass;
	private String loggerClassName;
	private Logger logger;
	
	public AppLogImpl(String loggerName) {
		this.logger = Logger.getLogger(loggerName);
	}
	
	@SuppressWarnings("rawtypes")
	public AppLogImpl(Class loggerClass)
	{
		this.loggerClass = loggerClass;
		this.loggerClassName = this.getClassName(loggerClass);
		this.logger = Logger.getLogger(loggerClass);
	}
	
	private String getClassName(Throwable ex) {
		StackTraceElement[] stackTrace = ex.getStackTrace();
		String classname = stackTrace[0].getClassName() + "." + stackTrace[0].getMethodName();
		return classname;
	}
	
	@SuppressWarnings("rawtypes")
	private String getClassName(Class cl) {
		String classname = cl.getName();
		return classname;
	}
	
	public Logger getLogger() {
		return logger;
	}
	@Override
	public void info(String message, String loggerName) {
		logger = Logger.getLogger(loggerName);
		logger.info(message);
	}
	
	@Override
	public void debug(String message, String loggerName) {
		logger = Logger.getLogger(loggerName);
		logger.debug(message);
		
	}
	@Override
	public void error(String message, Throwable throwable, String loggerName) {
		logger = Logger.getLogger(loggerName);
		logger.error(message,throwable);
	}
	
	public static Logger createAppander(boolean isErrorLevel, String appenderName, String loggerName, String client) {
		RollingFileAppender appender = new RollingFileAppender();
		appender.setName(appenderName +"_"+client);
		appender.setFile(logDir + File.separator + client+ File.separator+ loggerName+"_"+client);
		appender.setMaxFileSize("1000KB");
		appender.setMaxBackupIndex(20);
		appender.setLayout(new PatternLayout("%d{HH:mm:ss} %5p [%-5c{1}] %m%n"));
		LevelRangeFilter levelRange = new LevelRangeFilter();
		levelRange.setLevelMax(Level.INFO);
		if(isErrorLevel)
			appender.setThreshold(Level.ERROR);
		else
			appender.setThreshold(Level.INFO);
		appender.activateOptions();
		Logger logger = Logger.getLogger(loggerName+"_"+client);
		logger.removeAllAppenders();
		logger.addAppender(appender);
		if(isErrorLevel)
			logger.setLevel(Level.ERROR);
		else
			logger.setLevel(Level.INFO);
		logger.setAdditivity(false);
		return logger;
	}
}
*/