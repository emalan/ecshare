package com.madalla.webapp;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class StartUtils {

    public static void setLogLevel(){
    	Logger root = Logger.getRootLogger();
    	ConsoleAppender appender = (ConsoleAppender)root.getAppender("STDOUT");
    	appender.setThreshold(Level.DEBUG);
    }
}
