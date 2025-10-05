/**
 *
 */
package com.testimium.tool.logging.custom;

import com.testimium.tool.logging.ILogger;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.varia.LevelRangeFilter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sandeep
 *
 */
public class ToolLogger extends ILogger {

	private static Map<String, ILogger> loggerMap = new HashMap<String, ILogger>();

	//private Logger logger;

	public static synchronized ILogger getInstance() {
		if(!loggerMap.containsKey("Tool-Log")) {
			//loggerMap.clear();
			ToolLogger toolLogger = new ToolLogger();
			toolLogger.createAppander();
			loggerMap.put("Tool-Log", toolLogger);
		}
		return loggerMap.get("Tool-Log");
	}

	private ToolLogger() {
	}

	private ToolLogger(String loggerName) {
		this.logger = Logger.getLogger(loggerName);
	}

	@SuppressWarnings("rawtypes")
	private ToolLogger(Class loggerClass)
	{
		this.loggerClass = loggerClass;
		this.loggerClassName = this.getClassName(loggerClass);
		this.logger = Logger.getLogger(loggerClass);
	}


	public Logger createAppander() {
		RollingFileAppender appender = new RollingFileAppender();
		//appender.setName(appenderName +"_"+rootNode);
		String appenderName = "Tool-Log";
		String loggerName = "Tool-Log";
		String rootDir = getRootLogDir();
		appender.setName(appenderName);
		appender.setFile(rootDir + File.separator + loggerName + ".log");
		//appender.setFile(PropertyUtility.getLoggerPath() + File.separator + rootNode+ File.separator+ loggerName+"_"+rootNode);
		appender.setMaxFileSize("1000KB");
		appender.setMaxBackupIndex(20);
		appender.setLayout(new PatternLayout("%d{HH:mm:ss} %5p [%-5c{1}] %m%n"));
		LevelRangeFilter levelRange = new LevelRangeFilter();
		levelRange.setLevelMax(Level.INFO);
		/*if(isErrorLevel)
			appender.setThreshold(Level.ERROR);
		else*/
		appender.setThreshold(Level.INFO);
		appender.activateOptions();
		//Logger logger = Logger.getLogger(loggerName+"_"+rootNode);
		Logger logger = Logger.getLogger(loggerName);
		logger.removeAllAppenders();
		logger.addAppender(appender);
		/*if(isErrorLevel)
			logger.setLevel(Level.ERROR);
		else*/
		logger.setLevel(Level.INFO);
		logger.setAdditivity(false);
		this.logger = logger;
		return logger;
	}

	@Override
	protected boolean removeAppander() {
		if(loggerMap.containsKey("Tool-Log")) {
			loggerMap.remove("Tool-Log");
			return true;
		}
		return false;
	}
}
