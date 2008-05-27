package com.madalla.util.jcr.test;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import com.madalla.test.AbstractSpringWicketTester;

public abstract class AbstractJcrTester extends AbstractSpringWicketTester{

	
	
    protected Node getCreateNode(String nodeName, Node root) throws RepositoryException{
    	if (null == nodeName){
    		log.error("getCreateNode - Parameter nodeName cannot be null");
    		return null;
    	}
        Node node = null;
        try {
            node = root.getNode(nodeName);
        } catch (PathNotFoundException e){
            log.debug("Node not found in repository, now adding. new node="+nodeName);
            node = root.addNode(nodeName);
        }
        return node;
        
    }
}
