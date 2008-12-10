package com.madalla.service.cms.ocm;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.service.cms.ocm.blog.Blog;
import com.madalla.service.cms.ocm.blog.BlogEntry;
import com.madalla.service.cms.ocm.image.Album;
import com.madalla.service.cms.ocm.image.Image;
import com.madalla.service.cms.ocm.page.Content;
import com.madalla.service.cms.ocm.page.Page;
import com.madalla.util.jcr.JcrUtils;

public class RepositoryInfo {

    //Repository Node Names
    private static final String NS = "ec:";
    private static final String EC_NODE_APP = NS + "apps";
    private static final String EC_NODE_BLOGS = NS + "blogs";
    private static final String EC_NODE_PAGES = NS + "pages";
    private static final String EC_NODE_IMAGES = NS + "images";

	public enum RepositoryType {
		BLOG(Blog.class, true, EC_NODE_BLOGS),
		BLOGENTRY(BlogEntry.class, false,EC_NODE_BLOGS),
		ALBUM(Album.class, true, EC_NODE_IMAGES),
		IMAGE(Image.class, false, EC_NODE_IMAGES),
		PAGE(Page.class, true, EC_NODE_PAGES),
		CONTENT(Content.class, false, EC_NODE_PAGES);
		
		public final Class typeClass;
		public final boolean parent;
		public String groupName;
		RepositoryType(Class typeClass, boolean parent, String groupName){
			this.typeClass = typeClass;
			this.parent = parent;
			this.groupName = groupName;
		}
	}

 
    public static Node getApplicationNode(Session session) throws RepositoryException{
    	return JcrUtils.getCreateNode(EC_NODE_APP, session.getRootNode());
    }
    
    public static Node getGroupNode(Session session, String site, RepositoryType type) throws RepositoryException{
  		return getCreateParentNode(session, site, type.groupName);
    }
    
	public static boolean isBlogNodeType(final String path){
    	return isNodeType(path, EC_NODE_BLOGS);
    }
	
	public static boolean isImagesNodeType(final String path){
    	return isNodeType(path, EC_NODE_IMAGES);
    }
	
	public static boolean isContentNodeType(final String path){
        return isNodeType(path, EC_NODE_PAGES);
    }
	
	public static boolean isContentPasteNode(final String path){
	    //TODO
	    return false;
	}
	
	public static boolean isDeletableNode(JcrTemplate template, final String path){
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

	private static boolean isNodeType(final String path, String type){
    	String[] pathArray = path.split("/");
    	return type.equals(pathArray[pathArray.length - 3]);
    }
	
	private static Node getCreateParentNode(Session session, String site, String type) throws RepositoryException {
    	Node appNode = getApplicationNode(session);
    	Node siteNode =  JcrUtils.getCreateNode(NS+site, appNode);
    	Node ret =  JcrUtils.getCreateNode(type, siteNode);
    	session.save();
    	return ret;
	}
	


}
