package com.testimium.tool.logging;

import com.testimium.tool.utility.PropertyUtility;
import org.apache.log4j.Logger;

public abstract class ILogger {
	//protected static Map<String, ILogger> loggerM = new HashMap<String, ILogger>();
	/*protected abstract void info(String message);
	protected abstract  void debug(String message);
	protected abstract  void error(String message, Throwable throwable);*/
	protected Logger logger;
	protected Class loggerClass;
	protected String loggerClassName;

	protected abstract Logger createAppander();

	protected abstract boolean removeAppander();

	protected void info(String message) {
		this.logger.info(message);
	}

	protected void debug(String message) {
		this.logger.debug(message);

	}

	protected void error(String message, Throwable throwable) {
		if(null == throwable) {
			this.logger.error(message);
		} else {
			this.logger.error(message + " ERROR [" + getClassName(throwable) + "]", throwable);
		}
	}



	protected String getClassName(Throwable ex) {
		StackTraceElement[] stackTrace = ex.getStackTrace();
		String classname = stackTrace[0].getClassName() + "." + stackTrace[0].getMethodName();
		return classname;
	}

	@SuppressWarnings("rawtypes")
	protected String getClassName(Class cl) {
		String classname = cl.getName();
		return classname;
	}

	protected String getRootLogDir(){
		return  PropertyUtility.getLoggerPath();
	}

	protected void removeAllAppender(){
		if(null != logger)
			logger.removeAllAppenders();
	}
}
