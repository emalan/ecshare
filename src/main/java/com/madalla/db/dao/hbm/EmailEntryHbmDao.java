package com.madalla.db.dao.hbm;

import java.util.List;

import org.emalan.cms.bo.email.EmailEntryData;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.madalla.db.dao.AbstractDao;
import com.madalla.db.dao.EmailEntryDao;

@Component()
@Qualifier("Hibernate")
public class EmailEntryHbmDao extends AbstractDao implements EmailEntryDao {

    private Logger log = LoggerFactory.getLogger(EmailEntryHbmDao.class);
    
    private SessionFactory sessionFactory;

    @Override
    public int create(String name, String email, String comment) {
        log.trace("create - " + name);
        return 0;
    }
    
    @Override
    public EmailEntryData find(String id) {
        log.trace("find - " + id);
        return null;
    }

    @Override
    public int delete(EmailEntryData email) {
        log.trace("delete - " + email);
        return 0;
    }

    @Override
    public List<EmailEntryData> fetch() {
        log.trace("fetch - " + site);
        sessionFactory.getCurrentSession().createQuery("from EmailEntry where EmailEntry.site = " + site);
        return null;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
