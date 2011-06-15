package com.madalla;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public abstract class AbstractSpringWicketTester  extends AbstractDependencyInjectionSpringContextTests{

	protected Log log = LogFactory.getLog(this.getClass());

	public AbstractSpringWicketTester(){
		super();
		setAutowireMode(AUTOWIRE_BY_NAME);
	}

	public AppTester appTester;
    @Override
	protected void onSetUp() throws Exception {
    	super.onSetUp();
    	appTester = new AppTester();

    }

    abstract protected List<String> getTestConfigLocations();

    @Override
	protected String[] getConfigLocations() {
        List<String> configLocations = getTestConfigLocations();
        configLocations.add("classpath:com/madalla/applicationContext-test.xml");
        return configLocations.toArray(new String[configLocations.size()]);
    }
}
