package com.madalla.db.dao.hbm;

import java.util.List;

import org.emalan.cms.bo.log.LogData;
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
import com.madalla.db.dao.TransactionLog;
import com.madalla.db.dao.TransactionLogDao;

@Component("TransactionLogHbmDao")
@Qualifier("Hibernate")
public class TransactionLogHbmDao extends AbstractDao implements TransactionLogDao {

    private Logger log = LoggerFactory.getLogger(TransactionLogHbmDao.class);
    
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public void create(String user, String type, String id) {
        log.trace("create - " + id);
        TransactionLog data = new TransactionLog();
        data.setSite(site);
        data.setUser(user);
        data.setType(type);
        data.setCmsId(id);
        data.setDateTime(new DateTime(DateTimeZone.UTC));
        Long key = (Long) getSession().save(data);
        log.trace("create - key=" + key);
    }
    
    @Override
    @Transactional(readOnly = true)
    public LogData find(final String id) {
        log.trace("find - " + id);
        TransactionLog data = (TransactionLog) getSession().load(TransactionLog.class, Long.valueOf(id));
        return data;
    }

    @Override
    @Transactional(readOnly = true)
    public List<? extends LogData> fetch() {
        log.trace("fetch - " + site);
        return getSession().createCriteria(TransactionLog.class)
            .add(Restrictions.eq("site", site)).list();
    }
    
    @Override
    @Transactional
    public int delete(LogData data) {
        log.trace("delete - " + data);
        getSession().delete(data);
        return 0;
    }
    
    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    
    

}
