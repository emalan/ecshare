package com.madalla.service.cms;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class AbstractContentService  {

//	final static String TYPE_IMAGE = "image";
//	final static String TYPE_BLOG = "blog";
//	final static String TYPE_TEXT = "text";
	
	static final String NS = "ec:";
	
    static final String EC_NODE_BACKUP = NS + "backup";

    
    //TODO move these to Convertor class

	private final Log log = LogFactory.getLog(this.getClass());
	
    protected String site ;
    
    
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
    
	public void setSite(String site) {
		this.site = site;
	}
	
	public String getSite(){
		return site;
	}


}
