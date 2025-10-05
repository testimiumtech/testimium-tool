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
public class ToolErrorLogger extends ILogger {

	private static Map<String, ILogger> loggerMap = new HashMap<String, ILogger>();

	public static synchronized ILogger getInstance() {
		if(!loggerMap.containsKey("Tool-Error-Log")) {
			//loggerMap.clear();
			ToolErrorLogger toolErrorLogger = new ToolErrorLogger();
			toolErrorLogger.createAppander();
			loggerMap.put("Tool-Error-Log", toolErrorLogger);
		}
		return loggerMap.get("Tool-Error-Log");
	}

	private ToolErrorLogger() {
	}

	@SuppressWarnings("rawtypes")
	private ToolErrorLogger(Class loggerClass)
	{
		this.loggerClass = loggerClass;
		this.loggerClassName = this.getClassName(loggerClass);
		this.logger = Logger.getLogger(loggerClass);
	}

	public Logger createAppander() {
		RollingFileAppender appender = new RollingFileAppender();
		//appender.setName(appenderName +"_"+rootNode);
		String appenderName = "Tool-Error-Log";
		String loggerName = "Tool-Error-Log";
		String rootDir = getRootLogDir();
		appender.setName(appenderName);
		appender.setFile(rootDir + File.separator + loggerName + ".log");
		//appender.setFile(PropertyUtility.getLoggerPath() + File.separator + rootNode+ File.separator+ loggerName+"_"+rootNode);
		appender.setMaxFileSize("1000KB");
		appender.setMaxBackupIndex(20);
		appender.setLayout(new PatternLayout("%d{HH:mm:ss} %5p [%-5c{1}] %m%n"));
		LevelRangeFilter levelRange = new LevelRangeFilter();
		levelRange.setLevelMax(Level.INFO);
		appender.setThreshold(Level.ERROR);
		appender.activateOptions();
		//Logger logger = Logger.getLogger(loggerName+"_"+rootNode);
		Logger logger = Logger.getLogger(loggerName);
		logger.removeAllAppenders();
		logger.addAppender(appender);
		logger.setLevel(Level.ERROR);
		logger.setAdditivity(false);
		this.logger = logger;
		return logger;
	}

	@Override
	protected boolean removeAppander() {
		if(loggerMap.containsKey("Tool-Error-Log")) {
			loggerMap.remove("Tool-Error-Log");
			return true;
		}
		return false;
	}
}
