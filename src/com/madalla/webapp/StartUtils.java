package com.madalla.webapp;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class StartUtils {
	
	private static final String LOCAL_LAYOUT = "%-5p - %-26.26c{1} - %m\n";
	
	private StartUtils(){};
	
	public static void setLogForLocal(){
		Logger root = Logger.getRootLogger();
    	ConsoleAppender appender = (ConsoleAppender)root.getAppender("STDOUT");
    	if (appender != null){
    		appender.setThreshold(Level.DEBUG);
    		Layout layout = new PatternLayout(LOCAL_LAYOUT);
    		appender.setLayout(layout);
    	}
		
	}


}
