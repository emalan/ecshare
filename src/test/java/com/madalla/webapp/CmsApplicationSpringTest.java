package com.madalla.webapp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * Spring Integration Test to test Spring start up when using the eccms package.
 *
 * @author Eugene Malan
 *
 */
public class CmsApplicationSpringTest extends AbstractDependencyInjectionSpringContextTests{

	private CmsApplication wicketApplication;

	public CmsApplicationSpringTest() {
		super();
		setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
    @Override
	protected String[] getConfigLocations() {
    	List<String> configLocations = new ArrayList<String>();
		configLocations.add("classpath:com/madalla/webapp/applicationContext-app.xml");
        configLocations.add("classpath:com/madalla/webapp/applicationContext-test.xml");
        return configLocations.toArray(new String[configLocations.size()]);
    }
	
	public void testSpringConfig() {
		assertNotNull(wicketApplication);
	}

	public void setWicketApplication(CmsApplication wicketApplication) {
		this.wicketApplication = wicketApplication;
	}
	


}
