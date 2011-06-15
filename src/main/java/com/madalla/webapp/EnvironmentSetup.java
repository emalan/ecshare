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
import org.apache.wicket.Application;

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
	private String configType;
	private String logfile;

	public void init(){
		if (Application.DEPLOYMENT.equalsIgnoreCase(configType)){
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
		this.configType = configType;
	}

	public String getConfigType() {
		return configType;
	}

	public void setLogfile(String logfile) {
		this.logfile = logfile;
	}

	public String getLogfile() {
		return logfile;
	}
}
