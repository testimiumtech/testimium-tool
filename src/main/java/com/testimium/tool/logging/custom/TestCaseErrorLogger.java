/**
 *
 */
package com.testimium.tool.logging.custom;

import com.testimium.tool.context.TestContext;
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
public class TestCaseErrorLogger extends ILogger {

	private static Map<String, ILogger> loggerMap = new HashMap<String, ILogger>();

	/*private Class loggerClass;
	private String loggerClassName;
	private Logger logger;*/

	public static synchronized ILogger getInstance() {
		if(!loggerMap.containsKey(TestContext.getTestContext("").getTestCaseName())) {
			//loggerMap.clear();
			TestCaseErrorLogger testCaseLogger = new TestCaseErrorLogger();
			testCaseLogger.createAppander();
			loggerMap.put(TestContext.getTestContext("").getTestCaseName(), testCaseLogger);
		}
		return loggerMap.get(TestContext.getTestContext("").getTestCaseName());
	}

	private TestCaseErrorLogger() {
	}

	/*private TestCaseLogger(boolean isErrorLog,
						  String appenderName, String loggerName, String rootDir) {
			createAppander(isErrorLog, appenderName, loggerName, PropertyReader.getProperty("tool.active.profile") + File.separator + rootDir);
			getLogger(isErrorLog, appenderName,loggerName);
	}*/

	/*private TestCaseErrorLogger(String loggerName) {
		this.logger = Logger.getLogger(loggerName);
	}*/

	@SuppressWarnings("rawtypes")
	private TestCaseErrorLogger(Class loggerClass)
	{
		this.loggerClass = loggerClass;
		this.loggerClassName = this.getClassName(loggerClass);
		this.logger = Logger.getLogger(loggerClass);
	}

	/*private String getClassName(Throwable ex) {
		StackTraceElement[] stackTrace = ex.getStackTrace();
		String classname = stackTrace[0].getClassName() + "." + stackTrace[0].getMethodName();
		return classname;
	}

	@SuppressWarnings("rawtypes")
	private String getClassName(Class cl) {
		String classname = cl.getName();
		return classname;
	}*/

	/*public void getLogger(String loggerName) {
		*//*synchronized (this.logger) {
			if(!loggerMap.containsKey(loggerName+"_"+client)) {
				loggerMap.put(loggerName+"_"+client, createAppander(
						isErrorLog, appenderName, loggerName, client));
			}*//*
		this.logger = Logger.getLogger(loggerName);
	}*/

	public Logger createAppander() {
		RollingFileAppender appender = new RollingFileAppender();
		//appender.setName(appenderName +"_"+rootNode);
		String appenderName = TestContext.getTestContext("").getTestCaseName();
		String loggerName = TestContext.getTestContext("").getTestCaseName();
		if(null != loggerName) {
			String rootDir = getRootLogDir() + File.separator + TestContext.getTestContext("").getTestSuiteName() + File.separator + "Error";
			appender.setName(appenderName);
			appender.setFile(rootDir + File.separator + loggerName + "-error.log");
			//TestContext.getTestContext("").setTestErrorLog(rootDir + File.separator + loggerName + "-error.log");
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
		}
		return logger;
	}

	@Override
	protected boolean removeAppander() {
		if(loggerMap.containsKey(TestContext.getTestContext("").getTestCaseName())) {
			loggerMap.remove(TestContext.getTestContext("").getTestCaseName());
			return true;
		}
		return false;
	}

	/*protected void info(String message) {
		this.logger.info(message);
	}

	protected void debug(String message) {
		this.logger.debug(message);

	}
	protected void error(String message, Throwable throwable) {
		this.logger.error(message +" ERROR ["+getClassName(throwable)+"]",throwable);
	}

	protected  void destroy() {
		//loggerMap = null;
		//logDir = null;
		logger = null;
		loggerClass = null;
		loggerClassName = null;
		//clientLog = null;
	}*/
}
