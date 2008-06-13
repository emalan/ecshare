package com.madalla.service.cms;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AbstractContentService implements IContentData {

	private final Log log = LogFactory.getLog(this.getClass());
	
    protected String site ;
    
    protected Node getPagesParent(Session session) throws RepositoryException{
    	return getCreateNode(EC_NODE_PAGES, getCreateSiteNode(session));
    }
    
    protected Node getBlogsParent(Session session) throws RepositoryException{
    	return getCreateNode(EC_NODE_BLOGS, getCreateSiteNode(session));
    }
    
    protected Node getCreateSiteNode(Session session) throws RepositoryException{
    	Node appNode = getCreateNode(EC_NODE_APP, session.getRootNode());
    	return getCreateNode(NS+site, appNode);
    }
    
    protected Node getCreateBackupNode(Session session) throws RepositoryException{
    	Node appNode = getCreateNode(EC_NODE_APP, session.getRootNode());
    	return getCreateNode(EC_NODE_BACKUP, appNode);
    }
    
    protected Node getCreateVersionableNode(String nodeName, Node parent) throws RepositoryException{
        Node node = getCreateNode(nodeName, parent);
        if (node.isNew() ||  !node.isNodeType("mix:versionable")){
        	node.addMixin("mix:versionable");
        }
        return node;
    }
    
    protected Node getCreateReferenceableNode(String nodeName, Node parent) throws RepositoryException{
        Node node = getCreateNode(nodeName, parent);
        if (node.isNew() ||  !node.isNodeType("mix:referenceable")){
        	node.addMixin("mix:referenceable");
        }
        return node;
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
