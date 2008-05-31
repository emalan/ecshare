package com.madalla.service.cms;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.swing.tree.TreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.util.jcr.model.JcrNodeModel;
import com.madalla.util.jcr.model.tree.JcrTreeModel;
import com.madalla.util.jcr.model.tree.JcrTreeNode;
import com.madalla.webapp.cms.Content;

public class ContentServiceImpl implements IContentData, IContentService, Serializable {
    
    private static final long serialVersionUID = 1L;
    private JcrTemplate template;
    private final Log log = LogFactory.getLog(this.getClass());
    private String site ;
    private List locales;
    
    public ContentServiceImpl(){

    }

    public String getContentData(final String nodeName, final String id, Locale locale) {
        String localeId = getLocaleId(id, locale);
        return getContentData(nodeName, localeId);
    }
    
    public String getContentData(final String nodeName, final String id) {
        
        return (String) template.execute(new JcrCallback(){

            public Object doInJcr(Session session) throws IOException, RepositoryException {
                String content = null;
                try {
                	Node siteNode = getCreateNode(site, session.getRootNode());
                    Node page = getCreateNode(nodeName, siteNode);
                    content = getContent(page, id, CONTENT_DEFAULT);
                } catch (RepositoryException e) {
                    log.error("Exception getting content.", e);
                    content = "There has been a technical difficulty accessing the Content Repository. " + e.getMessage();
                }
                session.save();
                return content;
            }
            
        });
    }
    
    public void setContent(final Content content, Locale locale) throws RepositoryException {
        String localeId = getLocaleId(content.getContentId(), locale);
        setContent(content.getClassName(), localeId, content.getText());
    }

    public void setContent(final Content content)  {
        setContent(content.getClassName(), content.getContentId(), content.getText());
    }
    
    public TreeModel getSiteContent(){
    	return (TreeModel) template.execute(new JcrCallback(){
            
            public Object doInJcr(Session session) throws IOException, RepositoryException {
            	Node rootNode = session.getRootNode();
            	Node siteNode = getCreateNode(site, rootNode);
                //TODO change to using site Node. Site Nood must act like root.
                JcrNodeModel nodeModel = new JcrNodeModel(rootNode);
                JcrTreeNode treeNode = new JcrTreeNode(nodeModel);
                JcrTreeModel jcrTreeModel = new JcrTreeModel(treeNode);
                treeNode.init(rootNode);
                return jcrTreeModel;
            }
        });
    }
    
    private void setContent(final String nodeName, final String contentId, final String text){
        template.execute(new JcrCallback(){

            public Object doInJcr(Session session) throws IOException, RepositoryException {
                Node siteNode = getCreateNode(site, session.getRootNode());
                Node classNode = getCreateNode(nodeName,siteNode);
                Node node;
                try {
                    node = classNode.getNode(contentId);
                } catch (PathNotFoundException e){
                    log.debug("Path Content Not found, now adding it. contentId="+contentId);
                    node = classNode.addNode(contentId);
                }
                node.setProperty(CONTENT, text);
                log.debug("setContent - Saving Content. parentNode="+nodeName+" node="+contentId);
                session.save();
                return null;
            }
            
        });
    }
    
    private String getLocaleId(String id, Locale locale) {
        
        Locale found = null;
        for (Iterator iter = locales.iterator(); iter.hasNext();) {
            Locale current = (Locale) iter.next();
            if (current.getLanguage().equals(locale.getLanguage())){
                found = current;
                break;
            }
        }
        if (null == found){
            return id;
        } else {
            return id + "_"+ found.getLanguage();
        }
    }

    /**
     *  returns the class name node -- creates it if its not there
     */
    private Node getCreateNode(String nodeName, Node root) throws RepositoryException{
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
    
    private String getContent(Node mainNode, String id, String defaultValue) {
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
                Property property = node.setProperty(CONTENT,defaultValue);
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
        return new JcrTemplate();
    }

	public void setSite(String site) {
		this.site = site;
	}

	public void setLocales(List locales) {
		this.locales = locales;
	}



}
