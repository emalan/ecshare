package com.madalla.service.cms.jcr;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.swing.tree.TreeModel;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.springmodules.jcr.JcrCallback;

import com.madalla.service.cms.AbstractBlog;
import com.madalla.service.cms.AbstractBlogEntry;
import com.madalla.service.cms.AbstractImageData;
import com.madalla.service.cms.IRepositoryService;
import com.madalla.service.cms.ocm.blog.Blog;

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
public class RepositoryService extends AbstractRepositoryService implements IRepositoryService, Serializable{

	private static final long serialVersionUID = 795763276139305054L;
	private static final Log log = LogFactory.getLog(RepositoryService.class);

    

    //Delete this and move out to Data classes
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
    	return ContentHelper.isNodeType(path);
    }
    
    public boolean isBlogNode(final String path){
    	return BlogEntryHelper.isNodeType(path);
    }
    
    public boolean isImageNode(final String path){
    	return ImageDataHelper.isNodeType(path);
    }
    
    public boolean isContentPasteNode(final String path){
    	return ContentHelper.isContentPasteNode(path);
    }
    
    public AbstractImageData getImageData(final String path) {
        if (StringUtils.isEmpty(path)){
            log.error("getBlogEntry - path is required.");
            return null;
        }
        return ImageDataHelper.getInstance().get(path);
    }
    
    public List<AbstractImageData> getAlbumImages(final String album){
    	return ImageDataHelper.getInstance().getAlbumEntries(album);
    }
    
    public TreeModel getAlbumImagesAsTree(final String album) {
    	return ImageDataHelper.getInstance().getAlbumEntriesAsTree(album);
    }
    
	public List<AbstractImageData> getAlbumOriginalImages() {
		return ImageDataHelper.getInstance().getOriginalEntries();
	}

    public AbstractBlogEntry getBlogEntry(final String path) {
        if (StringUtils.isEmpty(path)){
            log.error("getBlogEntry - path is required.");
            return null;
        }
        return BlogEntryHelper.getInstance().get(path);
    }
    
    public AbstractBlogEntry getNewBlogEntry(String blog, String title, DateTime date){
    	return new BlogEntry.Builder(blog, title, date ).build();
    }
    
    public String getContentData(final String nodeName, final String id, Locale locale) {
        String localeId = getLocaleId(id, locale);
        return getContentData(nodeName, localeId);
    }
    
    public String getContentData(final String page, final String contentId) {
    	return ContentHelper.getInstance().getData(page, contentId);
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
    
    public List<AbstractBlogEntry> getBlogEntries(final String blog){
    	return BlogEntryHelper.getInstance().getBlogEntries(blog);
    }

	public Content getContent(final String path) {
        return (Content) template.execute(new JcrCallback(){
            public Content doInJcr(Session session) throws IOException, RepositoryException {
                Node node = (Node) session.getItem(path);
                String pageName = node.getParent().getName().replaceFirst(AbstractRepositoryService.NS,"");
                Content content = new Content(node.getPath(), pageName, node.getName());
                content.setText(node.getProperty(EC_PROP_CONTENT).getString());
                return content;
            }
        });
	}
    
    public void pasteContent(final String path, final Content content){
        log.debug("pasteContent - path="+path);
    	template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node parent = (Node) session.getItem(path);
                Node newNode = getCreateNode(content.getName(), parent);
                newNode.setProperty(EC_PROP_CONTENT, content.getText());
                session.save();
                log.debug("pasteContent - Done pasting. path="+path);
                return null;
			}
    	});
    }

	public AbstractBlog getBlog(String blogName) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<AbstractBlogEntry> getBlogEntries(AbstractBlog blog) {
		// TODO Auto-generated method stub
		return null;
	}
}
