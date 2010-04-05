package com.madalla.cms.service.ocm;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.bo.SiteLanguage;
import com.madalla.cms.jcr.JcrUtils;

abstract class AbstractRepositoryService{
	
    protected String site ;
	protected JcrTemplate template;
    protected List<SiteLanguage> locales;
    protected ObjectContentManager ocm;
    
    protected Node getCreateNode(String nodeName, Node parent) throws RepositoryException{
    	return JcrUtils.getCreateNode(nodeName, parent);
    }

    public void setTemplate(JcrTemplate template) {
        this.template = template;
    }

	public void setSite(String site) {
		this.site = site;
	}

}
