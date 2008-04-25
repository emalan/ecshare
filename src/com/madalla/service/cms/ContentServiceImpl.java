package com.madalla.service.cms;

import java.io.IOException;
import java.io.Serializable;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.webapp.cms.Content;

public class ContentServiceImpl implements IContentService, Serializable {
    
    private static final long serialVersionUID = 1L;
    private JcrTemplate template;
    private final Log log = LogFactory.getLog(this.getClass());
    private String site ;

    public void loadContent(String className) {
        // TODO load all data for class

    }

    public String getContentData(final String pageName, final String id) {
        
        return (String) template.execute(new JcrCallback(){

            public Object doInJcr(Session session) throws IOException, RepositoryException {
                String content = null;
                try {
                	Node siteNode = getCreateNode(site, session.getRootNode());
                    Node page = getCreateNode(pageName, siteNode);
                    content = getContent(page, id);
                } catch (RepositoryException e) {
                    log.error("Exception getting content.", e);
                    content = "There has been a technical difficulty accessing the Content Repository. " + e.getMessage();
                }
                session.save();
                return content;
            }
            
        });
    }

    public void setContent(final Content content) throws RepositoryException {
        template.execute(new JcrCallback(){

            public Object doInJcr(Session session) throws IOException, RepositoryException {
            	Node siteNode = getCreateNode(site, session.getRootNode());
                Node classNode = getCreateNode(content.getClassName(),siteNode);
                Node node;
                try {
                    node = classNode.getNode(content.getContentId());
                } catch (PathNotFoundException e){
                	log.debug("Path Content Not found, now adding it. contentId="+content.getContentId());
                	node = classNode.addNode(content.getContentId());
                }
                node.setProperty(CONTENT, content.getText());
                session.save();
                return null;
            }
            
        });
    }
    
    /**
     *  returns the class name node -- creates it if its not there
     */
    private Node getCreateNode(String nodeName, Node root) throws RepositoryException{
        Node node = null;
        try {
            node = root.getNode(nodeName);
        } catch (PathNotFoundException e){
            log.debug("Node not found in repository, now adding. new node="+nodeName);
            node = root.addNode(nodeName);
        }
        return node;
        
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

    public void setTemplate(JcrTemplate template) {
        this.template = template;
    }

    public JcrTemplate getTemplate() {
        return template;
    }

	public void setSite(String site) {
		this.site = site;
	}

}
