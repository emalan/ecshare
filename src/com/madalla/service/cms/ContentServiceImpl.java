package com.madalla.service.cms;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.service.blog.BlogEntry;
import com.madalla.webapp.cms.Content;

/**
 * Content Service Implementation for JCR Content Reository
 * 
 * This class is aware of the structure of the data in the repository 
 * and will create the structure if it does not exist. The schema is
 * open and not enforced by the repository. This class is reponsible
 * for creating the structure and fetching the data. 
 * 
 *            ec:apps 
 *         -----|---------------------------------------                 
 *        |             |                               |
 *     <site 1>      <site 2>                        <site 3>
 *                      |-------------------------
 *                      |                         |
 *                    ec:pages                 ec:blogs
 *              --------|----------               |----------------  
 *             |        |          |              |                |
 *         <page 1>  <page 2>   <page 3>        <main Blog>    <other Blog>
 *                      |                           |---------------
 *                   ec:content                     |               |
 *       ---------------|-----------           <blog entry 1>  <blog entry 2>
 *      |               |           |
 * <paragraph1>   <paragraph2>   <block1>
 * 
 * @author exmalan
 *
 */
public class ContentServiceImpl extends AbstractContentService implements IContentService, Serializable {
    
    private static final long serialVersionUID = 1L;
    private JcrTemplate template;
    private final Log log = LogFactory.getLog(this.getClass());
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
        processContentEntry(content.getPageName(), localeId, content.getText(), true);
    }

    public void setContent(final Content content)  {
        processContentEntry(content.getPageName(), content.getContentId(), content.getText(), true);
    }
    
	public String insertBlogEntry(BlogEntry blogEntry) {
		return processBlogEntry(blogEntry);
	}
    
    public void updateBlogEntry(BlogEntry blogEntry){
        processBlogEntry(blogEntry);
    }
    
    public BlogEntry getBlogEntry(final String path) {
        if (StringUtils.isEmpty(path)){
            log.error("getBlogEntry - path is required.");
            return null;
        }
        return (BlogEntry) template.execute(new JcrCallback(){
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                Node node = (Node) session.getItem(path);
                BlogEntry blogEntry = new BlogEntry();
                BlogEntryConvertor.populateBlogEntry(node, blogEntry);
                return blogEntry;
            }
        });
    }
    
    public void deleteBlogEntry(final String path) {
        if (StringUtils.isEmpty(path)) {
            log.error("deleteBlogEntry - path is required.");
        } else {
            template.execute(new JcrCallback() {
                public Object doInJcr(Session session) throws IOException, RepositoryException {
                    Node node = (Node) session.getItem(path);
                    node.remove();
                    session.save();
                    return null;
                }
            });
        }
    }
    
    public List getBlogEntries(final String blog){
        return (List) template.execute(new JcrCallback(){
            List list = new ArrayList();
            public Object doInJcr(Session session) throws IOException, RepositoryException {
            	Node blogParent = getBlogsParent(session.getRootNode());
            	Node blogNode = getCreateNode(NS+blog, blogParent);
                
                for (NodeIterator iterator = blogNode.getNodes(); iterator.hasNext();){
                    Node nextNode = iterator.nextNode();
                    BlogEntry blogEntry = new BlogEntry();
                    blogEntry.setBlog(blog);
                    BlogEntryConvertor.populateBlogEntry(nextNode, blogEntry);
                    list.add(blogEntry);
                }
                return list;
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
    private String processBlogEntry(final BlogEntry blogEntry){
        return (String) template.execute(new JcrCallback(){
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                log.debug("processBlogEntry - Processing Blog. " + blogEntry);
                Node node ;
                if (StringUtils.isEmpty(blogEntry.getId())){
                	Node blogParent = getBlogsParent(session.getRootNode());
                	Node blogNode = getCreateNode(NS+blogEntry.getBlog(), blogParent);
                    node = getCreateNode(NS+blogEntry.getName(), blogNode);
                } else {
                    log.debug("processBlogEntry - retrieving node by path. path="+blogEntry.getId());
                    node = (Node) session.getItem(blogEntry.getId());
                }
               
                BlogEntryConvertor.populateNode(node, blogEntry);
                
                session.save();
                return node.getPath();
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
    private String processContentEntry(final String page, final String contentId, final String defaultValue, final boolean overwrite){
        return (String) template.execute(new JcrCallback(){
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                Node pageParent = getPagesParent(session.getRootNode());
                Node pageNode = getCreateNode(NS+page, pageParent);
                Node contentNode = getCreateNode(EC_NODE_CONTENT, pageNode);
                
                Node node = getCreateContentEntry(contentNode, contentId, defaultValue, overwrite);
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
    
    private Node getCreateContentEntry(Node parent, String contentID, String text, boolean overwrite) throws RepositoryException{
        Node node = getCreateNode(NS+contentID, parent);
        if (node.isNew() || overwrite){
            node.setProperty(EC_PROP_CONTENT, text);
        }
        log.debug("getCreateContentEntry - Saving Content. title="+contentID);
        return node;
    }
    
    
    public void setTemplate(JcrTemplate template) {
        this.template = template;
    }

    public JcrTemplate getTemplate() {
        return new JcrTemplate();
    }

	public void setLocales(List locales) {
		this.locales = locales;
	}
}
