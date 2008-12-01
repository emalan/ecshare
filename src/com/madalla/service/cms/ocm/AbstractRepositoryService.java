package com.madalla.service.cms.ocm;

import static com.madalla.service.cms.ocm.RepositoryInfo.EC_NODE_APP;
import static com.madalla.service.cms.ocm.RepositoryInfo.NS;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.util.jcr.JcrUtils;
import com.madalla.util.jcr.ocm.JcrOcmUtils;

abstract class AbstractRepositoryService{
	
	private static final Log log = LogFactory.getLog(AbstractRepositoryService.class);
	
    protected String site ;
	protected JcrTemplate template;
    protected List<Locale> locales;
    protected ObjectContentManager ocm;

    public void init(){
    	template.execute(new JcrCallback(){

			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				ocm =  JcrOcmUtils.getObjectContentManager(session);
				//do conversion if neccessary
				return null;
			}
    		
    	});
		
    }

    public Node getApplicationNode(Session session) throws RepositoryException{
    	return RepositoryInfo.getApplicationNode(session);
    }

	public Node getSiteNode(Session session) throws RepositoryException {
		return RepositoryInfo.getSiteNode(session, site);
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
