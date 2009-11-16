package com.madalla.db.dao;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.madalla.bo.email.EmailEntryData;

public class DaoTester extends AbstractDependencyInjectionSpringContextTests{
	
	private EmailEntryDao emailEntryDao;
	
	public DaoTester(){
		super();
		setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	@Override
	protected String[] getConfigLocations() {
		List<String> configLocations = new ArrayList<String>();
		configLocations.add("classpath:com/madalla/cms/service/ocm/applicationContext-cms.xml");
        configLocations.add("classpath:com/madalla/cms/jcr/applicationContext-jcr.xml");
        configLocations.add("classpath:com/madalla/db/dao/applicationContext-dao.xml");
        configLocations.add("classpath:com/madalla/db/dao/applicationContext-test.xml");
		return  (String[])configLocations.toArray(new String[configLocations.size()]);
	}

	
	public void testEmailDao(){
		DateTime key = new DateTime();
		EmailEntry email = new EmailEntry();
		email.setDateTime(key);
		email.setSenderName("Eugene Malan");
		email.setSenderEmailAddress("ee@emalan.com");
		email.setSenderComment("Cooment goes here");
		
		emailEntryDao.create(email);
		
		List<EmailEntryData> list = emailEntryDao.fetch();
		for (EmailEntryData item: list){
			System.out.println(item);
			emailEntryDao.find(item.getId());
			emailEntryDao.delete(item);
		}
		List<EmailEntryData> check = emailEntryDao.fetch();
		assertTrue(check.isEmpty());
	}

	public void setEmailEntryDao(EmailEntryDao emailEntryDao) {
		this.emailEntryDao = emailEntryDao;
	}

}
