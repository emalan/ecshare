package com.madalla.service.cms;

import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.service.blog.BlogEntry;
import com.madalla.webapp.cms.Content;

/**
 * @author emalan
 *
 */
public class ContentServiceImpl implements IContentData, IContentService, Serializable {
    
    private static final long serialVersionUID = 1L;
    private JcrTemplate template;
    private final Log log = LogFactory.getLog(this.getClass());
    private String site ;
    private List locales;
    
    public String getContentData(final String nodeName, final String id, Locale locale) {
        String localeId = getLocaleId(id, locale);
        return getContentData(nodeName, localeId);
    }
    
    public String getContentData(final String page, final String contentId) {
        return processContentEntry(page, contentId, CONTENT_DEFAULT, false);
    }
    
    public void setContent(final Content content, Locale locale) throws RepositoryException {
        String localeId = getLocaleId(content.getContentId(), locale);
        processContentEntry(content.getPageName(), localeId, content.getText());
    }

    public void setContent(final Content content)  {
        processContentEntry(content.getPageName(), content.getContentId(), content.getText());
    }
    
	public void setBlogEntry(BlogEntry blogEntry) {
		processBlogEntry(blogEntry, true, false);
	}
	
	public BlogEntry getBlogEntryData(final String blog, final long date){
		BlogEntry blogEntry = new BlogEntry();
		blogEntry.setDate(new Date(date));
		processBlogEntry(blogEntry, false, false);
		return blogEntry;
	}
	
	public void deleteBlogEntry(final String blog, final long Date){
		processBlogEntry(null, false, false);
	}
    
    /**
     * Create or get Content Entry from repository 
     * @param page
     * @param contentId
     * @param text
     * @return Content value
     */
    private String processContentEntry(final String page, final String contentId, final String text){
        return processContentEntry(page, contentId, text, true);
    }
    
    /**
     * @param page
     * @param contentId
     * @param text
     * @param overwrite - if true then replace value in repository even if it exists
     * @return
     */
    private String processContentEntry(final String page, final String contentId, final String defaultValue, final boolean overwrite){
        return (String) template.execute(new JcrCallback(){

            public Object doInJcr(Session session) throws IOException, RepositoryException {
                Node rootContentNode = getCreateAppNode(session.getRootNode());
                Node siteNode = getCreateSiteNode(rootContentNode, site);
                Node pageNode = getCreatePageNode(siteNode, page);
                
                Node node = getCreateContentEntry(pageNode, contentId, defaultValue, overwrite);
                log.debug("setContent - Saving Content. page="+page+" contentId="+contentId);
                session.save();
                return node.getProperty(EC_PROP_CONTENT).getString();
            }
            
        });
    }
    
    /**
     * @param page
     * @param contentId
     * @param text
     * @param overwrite - if true then replace value in repository even if it exists
     * @return
     */
    private BlogEntry processBlogEntry(final BlogEntry blogEntry, final boolean overwrite, final boolean delete){
        return (BlogEntry) template.execute(new JcrCallback(){

            public Object doInJcr(Session session) throws IOException, RepositoryException {
                Node rootContentNode = getCreateAppNode(session.getRootNode());
                Node siteNode = getCreateSiteNode(rootContentNode, site);
                Node blogNode = getCreateBlogNode(siteNode, blogEntry.getBlog());
                
                Node node = getCreateBlogEntry(blogNode, blogEntry, overwrite);
                log.debug("processBlogEntry - Saving Blog. " + blogEntry);
                session.save();
                return node.getProperty(EC_PROP_CONTENT).getString();
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
    
    private Node getCreateAppNode(Node root) throws RepositoryException{
        return getCreateNode(EC_NODE_APP, root);
    }
    
    private Node getCreateSiteNode(Node parent, String title) throws RepositoryException{
        return getCreateNodeTitle(EC_NODE_SITE, title, parent);
    }
    
    private Node getCreatePageNode(Node parent, String title) throws RepositoryException{
        return getCreateNodeTitle(EC_NODE_PAGE, title, parent);
    }
    
    private Node getCreateBlogNode(Node parent, String title) throws RepositoryException{
        return getCreateNodeTitle(EC_NODE_BLOG, title, parent);
    }
    
    private Node getCreateContentEntry(Node parent, String title, String text, boolean overwrite) throws RepositoryException{
        Node node = getCreateNodeTitle(EC_NODE_CONTENTENTRY, title, parent);
        if (node.isNew() || overwrite){
            node.setProperty(EC_PROP_CONTENT, text);
        }
        log.debug("setContent - Saving Content. title="+title);
        return node;
    }
    
    private Node getCreateBlogEntry(Node parent, BlogEntry blogEntry, boolean overwrite) throws RepositoryException{
        Node node = getCreateNodeDate(EC_NODE_BLOGENTRY, blogEntry.getDate(), parent);
        if (node.isNew() || overwrite){
            node.setProperty(EC_PROP_CONTENT, blogEntry.getText());
            //TODO keywords, description etc...
        }
        log.debug("setContent - Saving Blog. date="+blogEntry);
        return node;
    }

    /**
     *  returns the class name node -- creates it if its not there
     */
    private Node getCreateNode(String nodeName, Node parent) throws RepositoryException{
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
    
    private Node getCreateNodeTitle(String nodeName, String title, Node parent) throws RepositoryException{
        if (null == nodeName || null == title || null == parent){
            log.error("getCreateNode - all parameters must be provided");
            return null;
        }
        
        // check for existing node with title
        Node node = null;
        for (NodeIterator iterator = parent.getNodes(nodeName);iterator.hasNext();){
            Node nextNode = iterator.nextNode();
            if (nextNode.hasProperties() && nextNode.hasProperty(EC_PROP_TITLE)){
                String testTitle = nextNode.getProperty(EC_PROP_TITLE).getString();
                if (testTitle.equals(title)){
                    node = nextNode;
                    break;
                }
            }
        }
        
        // if node not found then create a new one
        if (null == node){
            log.debug("getCreateNodeTitle - Node not found. Now creating. nodeName="+nodeName+" title="+title);
            node = parent.addNode(nodeName);
            node.setProperty(EC_PROP_TITLE, title);
        }

        return node;
    }
    
    private Node getCreateNodeDate(String nodeName, Date date, Node parent) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException{
        if (null == nodeName || null == date || null == parent){
            log.error("getCreateNode - all parameters must be provided");
            return null;
        }
        
        // check for existing node with date
        Node node = null;
        for (NodeIterator iterator = parent.getNodes(nodeName);iterator.hasNext();){
            Node nextNode = iterator.nextNode();
            if (nextNode.hasProperties() && nextNode.hasProperty(EC_PROP_ENTRYDATE)){
                Calendar testDate = nextNode.getProperty(EC_PROP_ENTRYDATE).getDate();
                if (testDate.getTime().equals(date)){
                    node = nextNode;
                    break;
                }
            }
        }
        
        // if node not found then create a new one
        if (null == node){
            log.debug("getCreateNodeDate - Node not found. Now creating. nodeName="+nodeName+" date="+date);
            node = parent.addNode(nodeName);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            node.setProperty(EC_PROP_ENTRYDATE, calendar);
        }
        
        return node;
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
