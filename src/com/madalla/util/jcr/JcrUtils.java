package com.madalla.util.jcr;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JcrUtils {

	private static Log log = LogFactory.getLog(JcrUtils.class);
	
    /**
     *  returns the class name node -- creates it if its not there
     */
    public static Node getCreateNode(String nodeName, Node parent) throws RepositoryException{
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
