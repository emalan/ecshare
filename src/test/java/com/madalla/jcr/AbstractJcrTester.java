package com.madalla.jcr;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.madalla.jcr.explorer.ExplorerService;

public abstract class AbstractJcrTester extends TestCase {

	private static final Logger log = LoggerFactory.getLogger(AbstractJcrTester.class);

	
	private FileSystemXmlApplicationContext context;
	private ExplorerService explorerService;
	
	@Override
	protected void setUp() throws Exception {
        final List<String> configLocations = new ArrayList<String>();
        configLocations.add("classpath:com/madalla/webapp/applicationContext-app.xml");
        configLocations.add("classpath:com/madalla/service/applicationContext-test.xml");

        context = new FileSystemXmlApplicationContext(
                configLocations.toArray(new String[configLocations.size()]));
        
        explorerService = (ExplorerService) context.getBean("explorerService");

	}

	protected Node getCreateNode(String nodeName, Node root) throws RepositoryException {
		if (null == nodeName) {
			log.error("getCreateNode - Parameter nodeName cannot be null");
			return null;
		}
		Node node = null;
		try {
			node = root.getNode(nodeName);
		} catch (PathNotFoundException e) {
			log.debug("Node not found in repository, now adding. new node=" + nodeName);
			node = root.addNode(nodeName);
		}
		return node;

	}
}
