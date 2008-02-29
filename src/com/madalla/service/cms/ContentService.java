package com.madalla.service.cms;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.core.config.RepositoryConfig;

import com.madalla.webapp.cms.Content;


public class ContentService implements IContentService  {

    private String repositoryHome;
    private String repositoryConfig;
    private String section;
    private Session session;
    private final Log log = LogFactory.getLog(this.getClass());
    
    public void init(){
        Repository repository = null;
        // TODO when we move to singleton, then just throw initializing
        // exception
            try {
                RepositoryConfig config = RepositoryConfig.create(repositoryConfig,repositoryHome);
                repository = new TransientRepository(config);
                session = repository.login(new SimpleCredentials("username",
                        "password".toCharArray()));
                String user = session.getUserID();
                String name = repository.getDescriptor(Repository.REP_NAME_DESC);
                log.debug("Logged in as " + user + " to a " + name
                                + " repository.");
            } catch (Exception e) {
                log.error("Exception while setting up Jackrabbit repository.",e);
                throw new RuntimeException(e);
            } 
    }

    public void loadContent(String node){
    	//TODO load all Node data
        section = node;    
    }
    
    public void setContent(Content content) throws RepositoryException{
        setContentData(content.getClassName(), content.getContentId(), content.getText());
    }

    public String getContentData(String className, String id){
    	String content = null;
		try {
			Node classNode = getClassNode(className);
			content = getContent(classNode, id);
		} catch (RepositoryException e) {
			log.error("Exception getting content.", e);
			content = "There has been a technical difficulty accessing the Content Repository. " + e.getMessage();
		}
        
        return content;
    }
    
    /**
     *  returns the class name node -- creates it if its not there
     */
    private Node getClassNode(String className) throws RepositoryException{
    	Node root = session.getRootNode();
		Node classNode = null;
		try {
			classNode = root.getNode(className);
		} catch (PathNotFoundException e){
			log.debug("Class node not found in repository, now adding. class="+className);
			classNode = root.addNode(className);
		}
		return classNode;
    	
    }
    
    private String getContent(Node mainNode, String id) {
        String content = "Content placeholder : there is no content in the repository at present.";
        Node node;
        try {
            try {
                node = mainNode.getNode(id);
            } catch (PathNotFoundException e) {
                log.debug("Content ID not found in repository, now adding. id="+id);
                node = mainNode.addNode(id);
            }
            try {
                content = node.getProperty(CONTENT).getString();
            } catch (PathNotFoundException e) {
                log.debug("No Content found in repository, now adding default value. id="+id);
                Property property = node.setProperty(CONTENT,CONTENT_DEFAULT);
                content = property.getString();
            }
        } catch (Exception e) {
            log.error("Exception while getting content from repository.", e);
            throw new RuntimeException(e);
        }
        
        
        return content;
    }

    private void setContentData(String className, String id, String value) throws RepositoryException{
        Node classNode = getClassNode(className);
        Node node = classNode.getNode(id);
        node.setProperty(CONTENT, value);
        
    }
    
    public void destroy() {
        if (session != null){
            log.info("Logging out from Content repository.");
            session.logout();
        }
        
    }

    public void setRepositoryHome(String repositoryHome) {
        this.repositoryHome = repositoryHome;
    }

    public String getRepositoryHome() {
        return repositoryHome;
    }

	public String getRepositoryConfig() {
		return repositoryConfig;
	}

	public void setRepositoryConfig(String repositoryConfig) {
		this.repositoryConfig = repositoryConfig;
	}

}
