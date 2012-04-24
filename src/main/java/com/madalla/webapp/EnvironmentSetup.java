package com.madalla.webapp;

import java.io.IOException;
import java.io.Serializable;
import java.util.Enumeration;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.wicket.RuntimeConfigurationType;

/**
 * Sets up environment on startup.
 *
 * Spring configured with values from property files.
 *
 * @author Eugene Malan
 *
 */
public class EnvironmentSetup implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String LAYOUT_TOMCAT = "%d{ISO8601} %-5p - %-26.26c{1} - %m\n";

	private String site;
	private RuntimeConfigurationType configType;
	private String logfile;

	public void init(){
		if (RuntimeConfigurationType.DEPLOYMENT.equals(configType)){
			setupLogging(logfile);
		}
	}

	public static void setupLogging(String file){

     	Logger root = Logger.getRootLogger();

		Layout layout = new PatternLayout(LAYOUT_TOMCAT);
		DailyRollingFileAppender tomcat;
		try {
			tomcat = new DailyRollingFileAppender(layout, file ,"'.'yyyy-MM-dd");
			tomcat.setThreshold(Level.INFO);
			root.addAppender(tomcat);
		} catch (IOException e) {
			e.printStackTrace();
		}

 		for (@SuppressWarnings("rawtypes")Enumeration e = root.getAllAppenders(); e.hasMoreElements() ; ){
 			Object appender = e.nextElement();
 			if (appender instanceof ConsoleAppender) {
 				ConsoleAppender console = (ConsoleAppender) appender;
 				console.setThreshold(Level.WARN);
 			}

 		}


	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getSite() {
		return site;
	}

	public void setConfigType(String configType) {
		if ("DEVELOPMENT".equalsIgnoreCase(configType)) {
			this.configType = RuntimeConfigurationType.DEVELOPMENT;
		} else {
			this.configType = RuntimeConfigurationType.DEPLOYMENT;
		}
	}

	public RuntimeConfigurationType getRuntimeConfigType() {
		return configType;
	}

	public void setLogfile(String logfile) {
		this.logfile = logfile;
	}

	public String getLogfile() {
		return logfile;
	}
}
