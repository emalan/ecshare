package com.madalla.jcr;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public abstract class AbstractJcrTester extends AbstractDependencyInjectionSpringContextTests{

	private static final Logger log = LoggerFactory.getLogger(AbstractJcrTester.class);
	
	public AbstractJcrTester() {
		super();
		setAutowireMode(AUTOWIRE_BY_NAME);
	}

    @Override
	protected String[] getConfigLocations() {
        final List<String> configLocations = new ArrayList<String>();
        configLocations.add("classpath:com/madalla/applicationContext-test.xml");
        return configLocations.toArray(new String[configLocations.size()]);
    }
    
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
