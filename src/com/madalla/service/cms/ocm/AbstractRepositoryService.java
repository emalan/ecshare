package com.madalla.service.cms.ocm;

import java.util.List;
import java.util.Locale;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.wicket.WicketRuntimeException;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.util.jcr.JcrUtils;
import com.madalla.util.jcr.ocm.JcrOcmUtils;

abstract class AbstractRepositoryService {
	
	private static final Log log = LogFactory.getLog(AbstractRepositoryService.class);
	
    private String site ;
	protected JcrTemplate template;
    protected List<Locale> locales;
    protected ObjectContentManager ocm;

    //Repository Node Names
    static final String NS = "ec:";
    static final String EC_NODE_APP = NS + "apps";
    
    public void init(){
		Session session;
		try {
			session = template.getSessionFactory().getSession();
		} catch (RepositoryException e) {
			log.error("Error initializing - getting session from JcrTemplate", e);
			throw new WicketRuntimeException("Error getting Session from JcrTemplate.",e);
		}
		ocm =  JcrOcmUtils.getObjectContentManager(session);
    }

    public Node getApplicationNode(Session session) throws RepositoryException{
    	return getCreateNode(EC_NODE_APP, session.getRootNode());
    }

	public Node getSiteNode(Session session) throws RepositoryException {
    	Node appNode = getApplicationNode(session);
    	return getCreateNode(NS+site, appNode);
	}
	
    protected Node getCreateNode(String nodeName, Node parent) throws RepositoryException{
    	return JcrUtils.getCreateNode(nodeName, parent);
    }

    public void setTemplate(JcrTemplate template) {
        this.template = template;
    }

	public void setLocales(List<Locale> locales) {
		this.locales = locales;
	}
	
	public void setSite(String site) {
		this.site = site;
	}
	
	public String getSite(){
		return site;
	}



}
