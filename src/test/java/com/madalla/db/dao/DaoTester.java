package com.madalla.db.dao;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.madalla.bo.email.EmailEntryData;
import com.madalla.bo.log.LogData;

public class DaoTester extends AbstractDependencyInjectionSpringContextTests{
	
	private EmailEntryDao emailEntryDao;
	private TransactionLogDao transactionLogDao;
	
	public DaoTester(){
		super();
		setAutowireMode(AUTOWIRE_BY_NAME);
	}
	
	@Override
	protected String[] getConfigLocations() {
		List<String> configLocations = new ArrayList<String>();
        configLocations.add("classpath:com/madalla/db/dao/applicationContext-dao.xml");
        configLocations.add("classpath:com/madalla/db/dao/applicationContext-test.xml");
		return  (String[])configLocations.toArray(new String[configLocations.size()]);
	}
	
	public void testSaveLogDao(){
		DateTime key = new DateTime();
		TransactionLog log = new TransactionLog();
		log.setDateTime(key);
		log.setUser("testUser");
		log.setType("TEST");
		log.setCmsId("ID");
		
		transactionLogDao.create(log);
		
		List<LogData> list = transactionLogDao.fetch();
		for (LogData item: list){
			System.out.println(item);
			
			transactionLogDao.find(item.getId());
			transactionLogDao.delete(item);
		}
		List<LogData> check = transactionLogDao.fetch();
		assertTrue(check.isEmpty());
		
	}
	
	
	
	public void testEmailDao(){
		DateTime key = new DateTime();
		EmailEntry email = new EmailEntry();
		email.setDateTime(key);
		email.setSenderName("Eugene Malan");
		email.setSenderEmailAddress("ee@emalan.com");
		email.setSenderComment("Cooment goes here");
		
		emailEntryDao.fetch();
		
		emailEntryDao.create(email);
		
		List<EmailEntryData> list = emailEntryDao.fetch();
		for (EmailEntryData item: list){
			System.out.println(item);
			System.out.println("DATE DISPLAY UTC : " + item.getDateTime().toString());
			System.out.println("DATE DISPLAY US/Central : " + item.getDateTime().toDateTime(DateTimeZone.forID("US/Central")));
			
			emailEntryDao.find(item.getId());
			emailEntryDao.delete(item);
		}
		List<EmailEntryData> check = emailEntryDao.fetch();
		assertTrue(check.isEmpty());
	}

	public void setEmailEntryDao(EmailEntryDao emailEntryDao) {
		this.emailEntryDao = emailEntryDao;
	}
	
	public void setTransactionLogDao(TransactionLogDao transactionLogDao) {
		this.transactionLogDao = transactionLogDao;
	}

	private void listTimeZones(){
		for (Object id : DateTimeZone.getAvailableIDs()){
			System.out.println(id);
		}
			
	}

}
