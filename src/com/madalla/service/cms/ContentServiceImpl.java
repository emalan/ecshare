package com.madalla.service.cms;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
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
    
    public TreeModel getSiteContent(){
        return (TreeModel) template.execute(new JcrCallback(){
            
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                Node rootContentNode = getCreateAppNode(session.getRootNode());
                Node siteNode = getCreateSiteNode(rootContentNode, site);

                //TODO change to using site Node. Site Nood must act like root.
                JcrNodeModel nodeModel = new JcrNodeModel(rootContentNode);
                JcrTreeNode treeNode = new JcrTreeNode(nodeModel);
                JcrTreeModel jcrTreeModel = new JcrTreeModel(treeNode);
                treeNode.init(rootContentNode);
                return jcrTreeModel;
            }
        });
    }

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
        return getCreateNode(EC_NODE_SITE, title, parent);
    }
    
    private Node getCreatePageNode(Node parent, String title) throws RepositoryException{
        return getCreateNode(EC_NODE_PAGE, title, parent);
    }
    
    private Node getCreateBlogNode(Node parent, String title) throws RepositoryException{
        return getCreateNode(EC_NODE_BLOG, title, parent);
    }
    
    private Node getCreateContentEntry(Node parent, String title, String text, boolean overwrite) throws RepositoryException{
        Node node = getCreateNode(EC_NODE_CONTENTENTRY, title, parent);
        if (node.isNew() || overwrite){
            node.setProperty(EC_PROP_CONTENT, text);
        }
        log.debug("setContent - Saving Content. title="+title);
        return node;
    }
    
    private Node getCreateBlogEntry(Node parent, String title, String text) throws RepositoryException{
        Node node = getCreateNode(EC_NODE_BLOGENTRY, title, parent);
        //node.setProperty(EC_PROP_, text);
        log.debug("setContent - Saving Blog. title="+title);
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
    
    private Node getCreateNode(String nodeName, String title, Node parent) throws RepositoryException{
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
            log.debug("getCreateNode - Node not found. Now creating. nodeName="+nodeName+" title="+title);
            node = parent.addNode(nodeName);
            node.setProperty(EC_PROP_TITLE, title);
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
