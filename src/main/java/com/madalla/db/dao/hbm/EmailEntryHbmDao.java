package com.madalla.db.dao.hbm;

import java.util.List;

import org.emalan.cms.bo.email.EmailEntryData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.madalla.db.dao.AbstractDao;
import com.madalla.db.dao.EmailEntryDao;

@Component("EmailEntryHbmDao")
@Qualifier("Hibernate")
public class EmailEntryHbmDao extends AbstractDao implements EmailEntryDao {

    private Logger log = LoggerFactory.getLogger(EmailEntryHbmDao.class);
    
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public int create(String name, String email, String comment) {
        log.trace("create - " + name);
        EmailEntry data = new EmailEntry();
        data.setSenderName(name);
        data.setSenderEmailAddress(email);
        data.setSenderComment(comment);
        data.setDateTime(new DateTime(DateTimeZone.UTC));
        data.setSite(site);
        Long id = (Long) getSession().save(data);
        log.trace("create - id=" + id);
        return 1;
    }
    
    @Override
    @Transactional(readOnly = true)
    public EmailEntryData find(final String id) {
        log.trace("find - " + id);
        EmailEntry data = (EmailEntry) getSession().load(EmailEntry.class, Long.valueOf(id));
        return data;
    }

    @Override
    @Transactional
    public int delete(EmailEntryData email) {
        log.trace("delete - " + email);
        getSession().delete(email);
        return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<EmailEntryData> fetch() {
        log.trace("fetch - " + site);
        return getSession().createCriteria(EmailEntry.class)
            .add(Restrictions.eq("site", site)).list();
    }
    
    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    // @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    

}
