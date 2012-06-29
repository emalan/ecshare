package com.madalla.db.dao;

import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.emalan.cms.bo.email.EmailEntryData;
import org.emalan.cms.bo.log.LogData;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.madalla.db.dao.springjdbc.MemberSpringDao;

public class DaoHibernateTest {
    
    private static final Logger log = LoggerFactory.getLogger(DaoHibernateTest.class);

    private EmailEntryDao emailEntryDao;
    private TransactionLogDao transactionLogDao;
    
    //TODO
    private MemberSpringDao memberDao;

    private FileSystemXmlApplicationContext context;

    @Before
    public void setUp() throws Exception {
        final List<String> configLocations = new ArrayList<String>();
        configLocations.add("classpath:com/madalla/db/dao/applicationContext-dao.xml");
        configLocations.add("classpath:com/madalla/db/dao/applicationContext-test.xml");

        context = new FileSystemXmlApplicationContext(configLocations.toArray(new String[configLocations.size()]));
        
        emailEntryDao = context.getBean("EmailEntryHbmDao", EmailEntryDao.class);
        transactionLogDao = context.getBean("TransactionLogHbmDao", TransactionLogDao.class);
    }

    @Test
    public void testEmailDao() {

        emailEntryDao.fetch();

        emailEntryDao.create("Eugene Malan", "ee@emalan.com", "Comment");

        List<EmailEntryData> list = emailEntryDao.fetch();
        assertTrue(list.size() >= 1);
        for (EmailEntryData item : list) {
            log.debug(item.toString());
            log.debug("DATE DISPLAY UTC : " + item.getDateTime().toString());
            log.debug("DATE DISPLAY US/Central : "
                    + item.getDateTime().toDateTime(DateTimeZone.forID("US/Central")));

            emailEntryDao.find(item.getId());
            emailEntryDao.delete(item);
        }
        List<EmailEntryData> check = emailEntryDao.fetch();
        assertTrue(check.isEmpty());
    }
    
    @Test
    public void testTransactionLogDao() {
        transactionLogDao.fetch();
        
        transactionLogDao.create("test user", "testType", "test/page/item/id");
        
        List<? extends LogData> list = transactionLogDao.fetch();
        assertTrue(list.size() >= 1);
        for (LogData logData : list) {
            log.debug(logData.toString());
            transactionLogDao.find(logData.getId());
            transactionLogDao.delete(logData);
        }
        
        List<? extends LogData> check = transactionLogDao.fetch();
        assertTrue(check.isEmpty());
        
        
    }

    public void listTimeZones() {
        for (Object id : DateTimeZone.getAvailableIDs()) {
            System.out.println(id);
        }

    }


}
