package com.madalla;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public abstract class AbstractSpringWicketTester  extends AbstractDependencyInjectionSpringContextTests{

	protected Log log = LogFactory.getLog(this.getClass());
	
	public AppTester appTester;
    protected void onSetUp() throws Exception {
    	super.onSetUp();
    	appTester = new AppTester();
    }
    
    abstract protected List getTestConfigLocations();
    
    protected String[] getConfigLocations() {
        List configLocations = getTestConfigLocations();
        configLocations.add("classpath:com/madalla/applicationContext-test.xml");
        return (String[])configLocations.toArray(new String[configLocations.size()]);
    }
}
