package com.madalla.service.cms;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AbstractContentService implements IContentData {

	private final Log log = LogFactory.getLog(this.getClass());
	
    protected String site ;
    
    protected Node getPagesParent(Node root) throws RepositoryException{
    	return getCreateNode(EC_NODE_PAGES, getCreateSiteNode(root));
    }
    
    protected Node getBlogsParent(Node root) throws RepositoryException{
    	return getCreateNode(EC_NODE_PAGES, getCreateSiteNode(root));
    }
    
    protected Node getCreateSiteNode(Node root) throws RepositoryException{
    	Node appNode = getCreateNode(EC_NODE_APP, root);
    	return getCreateNode(NS+site, appNode);
    }

    /**
     *  returns the class name node -- creates it if its not there
     */
    protected Node getCreateNode(String nodeName, Node parent) throws RepositoryException{
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
    
	public void setSite(String site) {
		this.site = site;
	}


}
