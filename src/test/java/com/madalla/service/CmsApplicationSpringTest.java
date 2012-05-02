package com.madalla.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.emalan.cms.bo.email.EmailEntryData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.madalla.BuildInformation;

/**
 * Spring Integration Test to test Spring start up when using the eccms package.
 *
 * @author Eugene Malan
 *
 */
public class CmsApplicationSpringTest extends AbstractDependencyInjectionSpringContextTests{

	private static final Logger log = LoggerFactory.getLogger(CmsApplicationSpringTest.class);
	
	private ApplicationService applicationService;

	public CmsApplicationSpringTest() {
		super();
		setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
    @Override
	protected String[] getConfigLocations() {
    	List<String> configLocations = new ArrayList<String>();
		configLocations.add("classpath:com/madalla/webapp/applicationContext-app.xml");
        configLocations.add("classpath:com/madalla/service/applicationContext-test.xml");
        return configLocations.toArray(new String[configLocations.size()]);
    }
	
	public void testSpringConfig() {
		assertNotNull(applicationService);
	}
	
	public void testApplicationService() {
		//build information
		BuildInformation buildInformation = applicationService.getBuildInformation();
		log.info("Buildinformation version = " + buildInformation.getVersion());
		log.info("BuildInformation webappversion = " + buildInformation.getWebappVersion());
		
		//TODO Member
		
		// email sender
		final String subject = RandomStringUtils.random(12);
		final String body = "body of email.";
		final String email = "test@test.com";
		final String name = "testName";
		applicationService.sendEmail();
		applicationService.sendEmail(subject, body);
		applicationService.sendUserEmail(subject, body, email, name);
		applicationService.sendUserEmail(subject, body, email, name, true);
		applicationService.sendUserHtmlEmail(subject, body, email, name);
		
		applicationService.createEmailEntry(name, email, body);
		List<EmailEntryData> emails = applicationService.getEmailEntries();
		assertTrue(emails.size() > 0);
		for (EmailEntryData emailEntryData : emails) {
			log.info("Email entry : " + emailEntryData);
		}
		
		
		
	}

	public ApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}
	


}
