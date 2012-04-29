package com.madalla.cms.service.ocm;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang.StringUtils;
import org.emalan.cms.bo.AbstractData;
import org.emalan.cms.bo.log.LogData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.cms.jcr.JcrUtils;
import com.madalla.cms.service.ocm.template.RepositoryTemplate;
import com.madalla.db.dao.TransactionLogDao;

abstract class AbstractRepositoryService{

	private final static Logger log = LoggerFactory.getLogger(AbstractRepositoryService.class);

    protected String site ;
	protected JcrTemplate template;
    //protected List<SiteLanguage> locales;
    //protected ObjectContentManager ocm;
    protected RepositoryTemplate repositoryTemplate;
    protected TransactionLogDao transactionLogDao;

    protected Node getCreateNode(String nodeName, Node parent) throws RepositoryException{
    	return JcrUtils.getCreateNode(nodeName, parent);
    }

    protected void createTransactionLog(String user, AbstractData data){
    	final LogData logData = new LogData();
		logData.setUser(user);
		logData.setType(data.getClass().getSimpleName());
		logData.setCmsId(data.getId());
		log.debug("creating log entry. " + logData);
		try {
			transactionLogDao.create(logData);
		} catch (Exception e){
			log.error("Exception while logging transaction.", e);
		}
    }

    public LogData getTransactionLog(String id){
    	if (StringUtils.isEmpty(id)) {
			log.error("getTransactionLog - id is required.");
			return null;
		}
    	return transactionLogDao.find(id);
    }



    public void deleteNode(final String path) {
    	JcrUtils.deleteNode(template, path);
    }

    public void saveDataObject(AbstractData data, String user){
    	createTransactionLog(user, data);
    	repositoryTemplate.saveDataObject(data);
    }


}
