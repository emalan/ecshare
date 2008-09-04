package com.madalla.service.cms;

import java.util.List;
import java.util.Locale;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.jcr.JcrTemplate;

abstract class AbstractRepositoryData implements IRepositoryData {

	private static final Log log = LogFactory.getLog(AbstractRepositoryData.class);
	static final String NS = "ec:";
    static final String EC_NODE_APP = NS + "apps";

    //TODO remove this Node to make data structure more consistent
    static final String EC_NODE_CONTENT = NS + "content";
    
    //shared repository properties
	static final String EC_PROP_CONTENT = NS + "content";

	//Spring configured
    protected String site ;
	protected JcrTemplate template;
    protected List<Locale> locales;

	public void setSite(String site) {
		this.site = site;
	}
	public void setLocales(List<Locale> locales) {
		this.locales = locales;
	}
	public void setTemplate(JcrTemplate template) {
		this.template = template;
	}
	

    public Node getApplicationNode(Session session) throws RepositoryException{
    	return getCreateNode(EC_NODE_APP, session.getRootNode());
    }

	public Node getSiteNode(Session session) throws RepositoryException {
    	Node appNode = getApplicationNode(session);
    	return getCreateNode(NS+site, appNode);
	}
	
    
    /**
     *  returns the class name node -- creates it if its not there
     */
    public Node getCreateNode(String nodeName, Node parent) throws RepositoryException{
    	if (null == nodeName || null == parent){
    		log.error("getCreateNode - all parameters must be supplied");
    		return null;
    	}
        Node node = null;
        try {
            node = parent.getNode(nodeName);
        } catch (PathNotFoundException e){
            log.debug("Node not found in repository, now adding. new node="+nodeName);
            node = parent.addNode(nodeName);
        }
        return node;
        
    }



    


	
}
