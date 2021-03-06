package com.madalla.db.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.RandomStringUtils;
import org.emalan.cms.bo.email.EmailEntryData;
import org.emalan.cms.bo.log.LogData;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.madalla.bo.member.MemberData;
import com.madalla.db.dao.springjdbc.EmailEntrySpringDao;
import com.madalla.db.dao.springjdbc.JdbcDatabaseSetup;
import com.madalla.db.dao.springjdbc.MemberSpringDao;
import com.madalla.db.dao.springjdbc.TransactionLogSpringDao;

public class DaoSpringTest {

    private EmailEntrySpringDao emailEntryDao;
    private TransactionLogSpringDao transactionLogDao;
    private MemberSpringDao memberDao;

    private FileSystemXmlApplicationContext context;

    @Before
    public void setUp() throws Exception {
        final List<String> configLocations = new ArrayList<String>();
        configLocations.add("classpath:com/madalla/db/dao/applicationContext-dao.xml");
        configLocations.add("classpath:com/madalla/db/dao/applicationContext-test.xml");

        context = new FileSystemXmlApplicationContext(configLocations.toArray(new String[configLocations.size()]));
        
        emailEntryDao = context.getBean(EmailEntrySpringDao.class);
        transactionLogDao = context.getBean(TransactionLogSpringDao.class);
        memberDao = context.getBean(MemberSpringDao.class);
        
        DataSource dataSource = context.getBean(DataSource.class);
        JdbcDatabaseSetup setup = new JdbcDatabaseSetup();
        setup.setDataSource(dataSource);
        setup.init();
    }

    @Test
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

    @Test
    public void testSaveLogDao() {

        transactionLogDao.create("testUser", "TEST", "ID");

        List<? extends LogData> list = transactionLogDao.fetch();
        assertTrue(list.size() >= 1);
        for (LogData item : list) {
            System.out.println(item);

            transactionLogDao.find(item.getId());
            transactionLogDao.delete(item);
        }
        List<? extends LogData> check = transactionLogDao.fetch();
        assertTrue(check.isEmpty());

    }

    @Test
    public void testEmailDao() {

        emailEntryDao.fetch();

        emailEntryDao.create("Eugene Malan", "ee@emalan.com", "Cooment goes here");

        List<EmailEntryData> list = emailEntryDao.fetch();
        assertTrue(list.size() >= 1);
        for (EmailEntryData item : list) {
            System.out.println(item);
            System.out.println("DATE DISPLAY UTC : " + item.getDateTime().toString());
            System.out.println("DATE DISPLAY US/Central : "
                    + item.getDateTime().toDateTime(DateTimeZone.forID("US/Central")));

            emailEntryDao.find(item.getId());
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

    public void setMemberDao(MemberSpringDao memberDao) {
        this.memberDao = memberDao;
    }

}
