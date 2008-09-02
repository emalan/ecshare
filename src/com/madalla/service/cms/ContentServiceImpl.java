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


/**
 * Content Service Implementation for JCR Content Repository
 * <p>
 * This class is aware of the structure of the data in the repository 
 * and will create the structure if it does not exist. The schema is
 * open and not enforced by the repository. This class is reponsible
 * for creating the structure and fetching the data. 
 * <p>
 * <pre>
 *            ec:apps 
 *         -----|------------------------------                 
 *        |                                    |
 *     [ec:site1]                          [ec:site2]                               
 *        |                                    |
 *                       ------------------------------------------------------------
 *                      |                         |                                  |
 *                    ec:pages                 ec:blogs                           ec:images
 *              --------|----------               |----------------                  |---------
 *             |        |          |              |                |                 |         |
 *        [ec:page1] [ec:page2] [ec:page3]    [ec:mainBlog]    [ec:otherBlog]    [ec:album1] [ec:group2]
 *             |        |          |                |---------------
 *                   ec:content                     |               |
 *       ---------------|-----------           [ec:blogEntry1]  [ec:blogEntry2]
 *      |               |           |
 * [ec:para1]   [ec:para2]    [ec:block1]
 * 
 * </pre>
 * 
 * @author Eugene Malan
 *
 */
public class ContentServiceImpl extends AbstractContentService implements IContentService, Serializable {
    
    private static final long serialVersionUID = 1L;
    private JcrTemplate template;
    private final Log log = LogFactory.getLog(this.getClass());
    private List<Locale> locales;
    
    /*
     * (non-Javadoc)
     *  Boolean checks - TODO examine these methods for performance
     */
    
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
    	String[] pathArray = path.split("/");
    	if (EC_NODE_CONTENT.equals(pathArray[pathArray.length-2])){
    		return true;
    	}
    	return false;
    }
    
    public boolean isBlogNode(final String path){
    	String[] pathArray = path.split("/");
    	if (EC_NODE_BLOGS.equals(pathArray[pathArray.length-2])){
    		return true;
    	}
    	return false;
    }
    
    public boolean isContentPasteNode(final String path){
    	String[] pathArray = path.split("/");
    	if (EC_NODE_CONTENT.equals(pathArray[pathArray.length-1])){
    		return true;
    	}
    	return false;
    }

    public BlogEntry getBlogEntry(final String path) {
        if (StringUtils.isEmpty(path)){
            log.error("getBlogEntry - path is required.");
            return null;
        }
        return (BlogEntry) template.execute(new JcrCallback(){
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                Node node = (Node) session.getItem(path);
                return BlogEntryConvertor.createBlogEntry(node);
            }
        });
    }
    
    public String insertBlogEntry(BlogEntry blogEntry) {
		return processEntry(blogEntry, TYPE_BLOG, new BlogEntryConvertor());
	}
    
    public void updateBlogEntry(BlogEntry blogEntry){
        processEntry(blogEntry, TYPE_BLOG, new BlogEntryConvertor());
    }
    
    public void setImageData(final ImageData data){
    	processEntry(data, TYPE_IMAGE, new IEntryProcessor(){
			public void processNode(Node node, IContentData content) throws RepositoryException {
				ImageData imageData = (ImageData) content;
				//TODO
				//node.setProperty("",imageData.get );
			}
    	});
    }

    public String getContentData(final String nodeName, final String id, Locale locale) {
        String localeId = getLocaleId(id, locale);
        return getContentData(nodeName, localeId);
    }
    
    public String getContentData(final String page, final String contentId) {
    	Content content = new Content(page, contentId);
        processEntry(content, TYPE_TEXT, new IEntryProcessor(){
			public void processNode(Node node, IContentData content) throws RepositoryException {
				((Content)content).setText(node.getProperty(EC_PROP_CONTENT).getString()); 
			}
        });
        return content.getText();
    }
    
    public void setContent(final Content content)  {
        processEntry(content , TYPE_TEXT, new IEntryProcessor(){
			public void processNode(Node node, IContentData content) throws RepositoryException {
				node.setProperty(EC_PROP_CONTENT, ((Content)content).getText());
			}
        });
    }
    
    private String processEntry(final IContentData entry, final String type, final IEntryProcessor convertor){
    	if (entry == null ){
            log.error("processEntry - Entry cannot be null.");
            return null;
        }
        return (String) template.execute(new JcrCallback(){
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                log.debug("processEntry - " + entry);
                Node node ;
                if (StringUtils.isEmpty(entry.getId())){
                	Node parent = getParentNode(type, session);
                	Node groupNode = getCreateNode(NS+entry.getGroup(), parent);
                	if (type.equals(TYPE_TEXT)){ //TODO change data structure to get rid of this
                		groupNode = getCreateNode(EC_NODE_CONTENT, groupNode);
        			}
                    node = getCreateNode(NS+entry.getName(), groupNode);
                } else {
                    log.debug("processEntry - retrieving node by path. path="+entry.getId());
                    node = (Node) session.getItem(entry.getId());
                }
                convertor.processNode(node, entry);
                session.save();
                return node.getPath();
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
            	Node blogParent = getBlogsParent(session);
            	Node blogNode = getCreateNode(NS+blog, blogParent);
                
                for (NodeIterator iterator = blogNode.getNodes(); iterator.hasNext();){
                    Node nextNode = iterator.nextNode();
                    list.add(BlogEntryConvertor.createBlogEntry(nextNode));
                }
                return list;
            }
        });
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
    
    public void setTemplate(JcrTemplate template) {
        this.template = template;
    }

	public void setLocales(List<Locale> locales) {
		this.locales = locales;
	}

}
