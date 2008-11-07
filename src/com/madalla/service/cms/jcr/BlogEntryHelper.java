package com.madalla.service.cms.jcr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.joda.time.DateTime;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.service.cms.AbstractBlogEntry;
import com.madalla.service.cms.IRepositoryData;

class BlogEntryHelper extends AbstractContentHelper {
	
	//Repository data names
	private static final String EC_NODE_BLOGS = NS + "blogs";
    private static final String EC_PROP_TITLE = NS + "title";
    private static final String EC_PROP_KEYWORDS = NS + "keywords";
    private static final String EC_PROP_DESCRIPTION = NS + "description";
    private static final String EC_PROP_ENTRYDATE = NS + "entryDate";
    private static final String EC_PROP_CATEGORY = NS + "category";


	private static BlogEntryHelper instance;
	static BlogEntryHelper getInstance(){
		return instance;
	}

	public BlogEntryHelper(String site, JcrTemplate template ){
		this.site = site;
		this.template = template;
		instance = this;
	}
	
    public static boolean isNodeType(final String path){
    	return isNodeType(path, EC_NODE_BLOGS);
    }
    
    private static BlogEntry create(Node node) throws RepositoryException{
    	String blog = node.getParent().getName().replaceFirst(NS,"");
    	String title = node.getProperty(EC_PROP_TITLE).getString();
    	String description = node.getProperty(EC_PROP_DESCRIPTION).getString();
    	String category = node.getProperty(EC_PROP_CATEGORY).getString();
    	String keywords = node.getProperty(EC_PROP_KEYWORDS).getString();
    	Calendar calendar = node.getProperty(EC_PROP_ENTRYDATE).getDate();
    	String text = node.getProperty(EC_PROP_CONTENT).getString();
    	
    	return new BlogEntry.Builder(node.getPath(), blog, title, new DateTime(calendar)).
    	category(category).desription(description).keywords(keywords).text(text).build();
    }

	String save(final BlogEntry blogEntry) {
		return genericSave(blogEntry);
    }
	
	BlogEntry get(final String path){
		return (BlogEntry) template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,	RepositoryException {
				Node node = (Node) session.getItem(path);
				return create(node);
			}
			
		});
	}
	
	List<AbstractBlogEntry> getBlogEntries(final String blog){
		return (List<AbstractBlogEntry>) template.execute(new JcrCallback(){
            List<AbstractBlogEntry> list = new ArrayList<AbstractBlogEntry>();
            public List<AbstractBlogEntry> doInJcr(Session session) throws IOException, RepositoryException {
            	Node siteNode = getSiteNode(session);
            	Node blogParent = getCreateNode(EC_NODE_BLOGS, siteNode);
            	Node blogNode = getCreateNode(NS+blog, blogParent);
                
                for (NodeIterator iterator = blogNode.getNodes(); iterator.hasNext();){
                    Node nextNode = iterator.nextNode();
                    list.add(BlogEntryHelper.create(nextNode));
                }
                return list;
            }
        });
	}
	

    @Override
	Node getParentNode(Node node) throws RepositoryException {
		return getCreateNode(EC_NODE_BLOGS, node);
	}

	@Override
	void setPropertyValues(Node node, IRepositoryData data) throws  RepositoryException {
		BlogEntry blogEntry = (BlogEntry) data;
		node.setProperty(EC_PROP_TITLE,blogEntry.getTitle() );
	    node.setProperty(EC_PROP_DESCRIPTION,blogEntry.getDescription() );
	    node.setProperty(EC_PROP_CATEGORY, blogEntry.getCategory());
	    node.setProperty(EC_PROP_KEYWORDS,blogEntry.getKeywords() );
	    node.setProperty(EC_PROP_ENTRYDATE, blogEntry.getDate().toGregorianCalendar());
	    node.setProperty(EC_PROP_CONTENT, blogEntry.getText());
	}

}
