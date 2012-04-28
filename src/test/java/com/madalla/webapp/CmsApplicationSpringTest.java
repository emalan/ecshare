package com.madalla.webapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.RuntimeConfigurationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.madalla.AbstractSpringWicketTester;
import com.madalla.BuildInformation;
import com.madalla.email.IEmailSender;
import com.madalla.service.IDataService;
import com.madalla.service.IRepositoryAdminService;

/**
 * Spring Integration Test to test Spring start up when using the eccms package.
 *
 * @author Eugene Malan
 *
 */
public class CmsApplicationSpringTest extends AbstractDependencyInjectionSpringContextTests{

	private static final Logger log = LoggerFactory.getLogger(CmsApplicationSpringTest.class);
	
	private IRepositoryAdminService repositoryAdminService;
    private IEmailSender emailSender;
    private IDataService dataService;
    private BuildInformation buildInformation;
    private RuntimeConfigurationType configType;

	public CmsApplicationSpringTest() {
		super();
		setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
    @Override
	protected String[] getConfigLocations() {
    	List<String> configLocations = new ArrayList<String>();
		configLocations.add("classpath:org/emalan/cms/service/ocm/applicationContext-cms.xml");
        configLocations.add("classpath:com/madalla/webapp/applicationContext-test.xml");
        return configLocations.toArray(new String[configLocations.size()]);
    }
	
	public void testSpringConfig() {
		
	}

}
