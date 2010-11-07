package com.madalla.webapp;

import java.io.Serializable;
import java.util.Enumeration;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
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
	private final static String FILE = "${catalina.base}/logs/";
	
	private String site;
	private String configType;
	
	public void init(){
		if (!Application.DEPLOYMENT.equalsIgnoreCase(configType)){
			setupLogging(FILE + site + "log");
		}
	}
	
	public static void setupLogging(String file){
		
     	Logger root = Logger.getRootLogger();
 		for (@SuppressWarnings("unchecked") Enumeration e = root.getAllAppenders(); e.hasMoreElements() ; ){
 			Object appender = e.nextElement();
 			if (appender instanceof FileAppender){
 				if (((FileAppender) appender).getName().equals("TOMCAT")){
 					((FileAppender) appender).setFile(file);
 				}
 			} else if (appender instanceof ConsoleAppender) {
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
}
