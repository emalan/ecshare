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

/**
 * Content Service Implementation for JCR Content Repository
 * <p>
 * This class is aware of the structure of the data in the repository 
 * and will create the structure if it does not exist. The schema is
 * open and not enforced by the repository. 
 * <p>
 * <pre>
 *            ec:apps 
 *         -----|------------------------------                 
 *        |                                    |
 *     [ec:site1]                          [ec:site2]                               
 *        |                                    |
 * 
 * </pre>
 * 
 * @author Eugene Malan
 *
 */
public class RepositoryService extends AbstractRepositoryService implements IRepositoryService , Serializable{

	private static final long serialVersionUID = 795763276139305054L;
	private static final Log log = LogFactory.getLog(RepositoryService.class);

    

    //Delete this and move out to Data classes
    static final String EC_NODE_BLOGS = NS + "blogs";
    static final String EC_PROP_CONTENT = NS + "content";

    public boolean isDeletableNode(final String path){
    	Node result = (Node) template.execute(new JcrCallback(){

			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node node = (Node) session.getItem(path);
				Node root = session.getRootNode();
				Node parent = node.getParent();
				while (!root.isSame(parent)) {
					if (parent.getName().equals(EC_NODE_APP)){
						return parent;
					}
					parent = parent.getParent();
				}

				return null;
			}
    		
    	});
    	if (result == null){
    		return false;
    	} else {
    		return true;
    	}
    	
    }
    
    public boolean isContentNode(final String path){
    	return Content.isContentNode(path);
    }
    
    public boolean isBlogNode(final String path){
    	return BlogEntry.isBlogNode(path);
    }
    
    public boolean isContentPasteNode(final String path){
    	return Content.isContentPasteNode(path);
    }

    public BlogEntry getBlogEntry(final String path) {
        if (StringUtils.isEmpty(path)){
            log.error("getBlogEntry - path is required.");
            return null;
        }
        return (BlogEntry) template.execute(new JcrCallback(){
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                Node node = (Node) session.getItem(path);
                return BlogEntry.createBlogEntry(node);
            }
        });
    }
    
    public String insertBlogEntry(BlogEntry blogEntry) {
		return processEntry(blogEntry);
	}
    
    public void updateBlogEntry(BlogEntry blogEntry){
        processEntry(blogEntry);
    }
    
    public void setImageData(final ImageData data){
    	processEntry(data);
    }

    public String getContentData(final String nodeName, final String id, Locale locale) {
        String localeId = getLocaleId(id, locale);
        return getContentData(nodeName, localeId);
    }
    
    public String getContentData(final String page, final String contentId) {
    	Content content = new Content(page, contentId);
        processEntry(content);
        return content.getText();
    }
    
    public void setContent(final Content content)  {
        processEntry(content);
    }



    public String getLocaleId(String id, Locale locale) {
        Locale found = null;
        for (Iterator<Locale> iter = locales.iterator(); iter.hasNext();) {
            Locale current = iter.next();
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


    public void deleteNode(final String path) {
        if (StringUtils.isEmpty(path)) {
            log.error("deleteNode - path is required.");
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
    
    public List<BlogEntry> getBlogEntries(final String blog){
        return (List<BlogEntry>) template.execute(new JcrCallback(){
            List<BlogEntry> list = new ArrayList<BlogEntry>();
            public List<BlogEntry> doInJcr(Session session) throws IOException, RepositoryException {
            	Node siteNode = getSiteNode(session);
            	Node blogParent = getCreateNode(EC_NODE_BLOGS, siteNode);
            	Node blogNode = getCreateNode(NS+blog, blogParent);
                
                for (NodeIterator iterator = blogNode.getNodes(); iterator.hasNext();){
                    Node nextNode = iterator.nextNode();
                    list.add(BlogEntry.createBlogEntry(nextNode));
                }
                return list;
            }
        });
    }

	public Content getContent(final String path) {
        return (Content) template.execute(new JcrCallback(){
            public Content doInJcr(Session session) throws IOException, RepositoryException {
                Node node = (Node) session.getItem(path);
                String pageName = node.getParent().getName().replaceFirst(NS,"");
                Content content = new Content(node.getPath(), pageName, node.getName());
                content.setText(node.getProperty(EC_PROP_CONTENT).getString());
                return content;
            }
        });
	}
    
    public void pasteContent(final String path, final Content content){
        log.debug("pasteContent - path="+path+content);
    	template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node parent = (Node) session.getItem(path);
                Node newNode = getCreateNode(content.getId(), parent);
                newNode.setProperty(EC_PROP_CONTENT, content.getText());
                session.save();
                log.debug("pasteContent - Done pasting. path="+path);
                return null;
			}
    	});
    }
    
    private String processEntry(final IRepositoryData entry){
    	if (entry == null ){
            log.error("processEntry - Entry cannot be null.");
            return null;
        }
    	
        return (String) template.execute(new JcrCallback(){
            public Object doInJcr(Session session) throws IOException, RepositoryException {
            	return entry.processEntry(session, getService());
            }
        });
    }
    
    private IRepositoryService getService(){
    	return this;
    }


}
