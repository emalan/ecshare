package com.madalla.db.dao;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.lang.RandomStringUtils;
import org.emalan.cms.bo.email.EmailEntryData;
import org.emalan.cms.bo.log.LogData;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.madalla.bo.member.MemberData;
import com.madalla.db.dao.springjdbc.EmailEntrySpringDao;
import com.madalla.db.dao.springjdbc.MemberSpringDao;
import com.madalla.db.dao.springjdbc.TransactionLogSpringDao;

public class DaoSpringTest extends TestCase {

    private EmailEntrySpringDao emailEntryDao;
    private TransactionLogSpringDao transactionLogDao;
    private MemberSpringDao memberDao;

    private FileSystemXmlApplicationContext context;

    @Override
    protected void setUp() throws Exception {
        final List<String> configLocations = new ArrayList<String>();
        configLocations.add("classpath:com/madalla/db/dao/applicationContext-dao.xml");
        configLocations.add("classpath:com/madalla/db/dao/applicationContext-test.xml");

        context = new FileSystemXmlApplicationContext(configLocations.toArray(new String[configLocations.size()]));
        
        emailEntryDao = context.getBean(EmailEntrySpringDao.class);
        transactionLogDao = context.getBean(TransactionLogSpringDao.class);
        memberDao = context.getBean(MemberSpringDao.class);
    }

    public void testMemberDao() {
        String firstName = RandomStringUtils.randomAlphabetic(10);
        String lastName = RandomStringUtils.randomAlphabetic(10);
        String memberId = RandomStringUtils.randomAlphabetic(12);

        List<? extends MemberData> list = memberDao.fetch();
        for (MemberData item : list) {
            System.out.println(item);
        }

        Member data = new Member();
        data.setFirstName(firstName);
        data.setLastName(lastName);
        data.setMemberId(memberId);
        data.setEmail("testEmail");
        memberDao.save(data);

        String password = "test";
        memberDao.savePassword(data.getMemberId(), password);
        memberDao.save(data);

        assertEquals(password, memberDao.getPassword(data.getMemberId()));

        MemberData test = memberDao.findbyMemberId(memberId);
        assertNotNull(test);
        assertEquals(firstName, test.getFirstName());

        memberDao.delete(test);

    }

    public void testSaveLogDao() {
        DateTime key = new DateTime();
        LogData log = new LogData();
        log.setDateTime(key);
        log.setUser("testUser");
        log.setType("TEST");
        log.setCmsId("ID");

        transactionLogDao.create(log);

        List<LogData> list = transactionLogDao.fetch();
        for (LogData item : list) {
            System.out.println(item);

            transactionLogDao.find(item.getId());
            transactionLogDao.delete(item);
        }
        List<LogData> check = transactionLogDao.fetch();
        assertTrue(check.isEmpty());

    }

    public void testEmailDao() {

        emailEntryDao.fetch();

        emailEntryDao.create("Eugene Malan", "ee@emalan.com", "Cooment goes here");

        List<EmailEntryData> list = emailEntryDao.fetch();
        for (EmailEntryData item : list) {
            System.out.println(item);
            System.out.println("DATE DISPLAY UTC : " + item.getDateTime().toString());
            System.out.println("DATE DISPLAY US/Central : "
                    + item.getDateTime().toDateTime(DateTimeZone.forID("US/Central")));

            emailEntryDao.find(item.getIdAsString());
            emailEntryDao.delete(item);
        }
        List<EmailEntryData> check = emailEntryDao.fetch();
        assertTrue(check.isEmpty());
    }

    public void setEmailEntryDao(EmailEntrySpringDao emailEntryDao) {
        this.emailEntryDao = emailEntryDao;
    }

    public void setTransactionLogDao(TransactionLogSpringDao transactionLogDao) {
        this.transactionLogDao = transactionLogDao;
    }

    private void listTimeZones() {
        for (Object id : DateTimeZone.getAvailableIDs()) {
            System.out.println(id);
        }

    }

    public void setMemberDao(MemberSpringDao memberDao) {
        this.memberDao = memberDao;
    }

}
