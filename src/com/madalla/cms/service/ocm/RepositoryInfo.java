package com.madalla.cms.service.ocm;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.bo.AbstractData;
import com.madalla.cms.bo.impl.ocm.Site;
import com.madalla.cms.bo.impl.ocm.blog.Blog;
import com.madalla.cms.bo.impl.ocm.blog.BlogEntry;
import com.madalla.cms.bo.impl.ocm.image.Album;
import com.madalla.cms.bo.impl.ocm.image.Image;
import com.madalla.cms.bo.impl.ocm.page.Content;
import com.madalla.cms.bo.impl.ocm.page.Page;
import com.madalla.cms.bo.impl.ocm.page.Resource;
import com.madalla.cms.bo.impl.ocm.security.User;
import com.madalla.cms.bo.impl.ocm.security.UserSite;
import com.madalla.cms.bo.impl.ocm.video.VideoPlayer;
import com.madalla.cms.jcr.JcrUtils;
/**
 * Information regarding the Schema in the Content respository System
 * <p>
 * The schema is depicted below. The data regarding the parent nodes and 
 * the classes that are children is all stored here. The RepositoryType
 * Enum holds all the required data.
 * <p>
 * <pre>
 *            ec:apps 
 *         -----|------------------------------                 
 *        |                                    |
 *     [ec:site1]                          [ec:site2]                               
 *        |                                    |
 *                       -------------------------------------------------------
 *                      |                         |                             |
 *                    ec:pages                 ec:blogs                      ec:images
 *                      |                         |                             |
 *               Pages and Content         Blogs and Blog Entries       Albums and Images   
 *               
 *           ec:users
 *              |
 *            Users
 *              |
 *         UserRole | UserSite | questions | UserBlog | UserPage | UserAlbum
 *            
 * </pre>
 * 
 * @author Eugene Malan
 *
 */
public class RepositoryInfo {

    //Repository Node Names
    private static final String NS = "ec:";
    private static final String EC_NODE_APP = NS + "apps";
    private static final String EC_NODE_BLOGS = NS + "blogs";
    private static final String EC_NODE_PAGES = NS + "pages";
    private static final String EC_NODE_IMAGES = NS + "images";
    private static final String EC_NODE_USERS = NS + "users";
    private static final String OCM_CLASS = "ocm:classname";

	/**
	 * Holds Repository specific information about the classes that are to be registered
	 * for persistence in the repository as well as the general structure of the data tree.
	 * For Parent classes there is information about where they are positioned in the
	 * tree. Other classes are aware of who there parent are by way of their constructors.
	 * 
	 * @author Eugene Malan
	 *
	 */
	public enum RepositoryType {
		SITE(Site.class, false, true, EC_NODE_APP),
		BLOG(Blog.class, true, true, EC_NODE_BLOGS),
		BLOGENTRY(BlogEntry.class, true, false,EC_NODE_BLOGS),
		ALBUM(Album.class, true, true, EC_NODE_IMAGES),
		IMAGE(Image.class, true, false, EC_NODE_IMAGES),
		PAGE(Page.class, true, true, EC_NODE_PAGES),
		CONTENT(Content.class, true, false, EC_NODE_PAGES),
		RESOURCE(Resource.class, true, false, EC_NODE_PAGES),
		VIDEO(VideoPlayer.class, true, false, EC_NODE_PAGES),
		USER(User.class, false, true, EC_NODE_USERS),
		USERSITE(UserSite.class, false, false, EC_NODE_USERS);
		
		/**
		 * This is the class that is to be registered for persistence
		 */
		public final Class<? extends AbstractData> typeClass;

		/**
		 * Set to true if the class and group is positioned under the site node.
		 */
		public final boolean site;  

		/**
		 * If true, then this Node is positioned as a child to the group Node.
		 */
		public final boolean parent;  
		
		/**
		 * This is the group or category Node for this class
		 */
		public String groupName;
		
		RepositoryType(Class<? extends AbstractData> typeClass, boolean site, boolean parent, String groupName){
			this.typeClass = typeClass;
			this.site = site;
			this.parent = parent;
			this.groupName = groupName;
		}
	}

 
    public static Node getApplicationNode(Session session) throws RepositoryException{
    	return JcrUtils.getCreateNode(EC_NODE_APP, session.getRootNode());
    }
    
    public static Node getGroupNode(Session session, String site, RepositoryType type) throws RepositoryException{
  		return getCreateParentNode(session, site, type);
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
	
	public static boolean isContentPasteNode(JcrTemplate template, final String path){
	    Boolean rt = (Boolean) template.execute(new JcrCallback(){

			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node node = (Node) session.getItem(path);
				if (node.hasProperty(OCM_CLASS)){
					String className = node.getProperty(OCM_CLASS).getString();
					if ("com.madalla.service.cms.ocm.page.Page".equals(className)){
						return Boolean.TRUE;
					} else {
						return Boolean.FALSE;
					}
				}else {
					return Boolean.FALSE;
				}
			}
	    	
	    });
	    return rt;
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
	
	private static Node getCreateParentNode(Session session, String site, RepositoryType type) throws RepositoryException {
    	Node appNode = getApplicationNode(session);
    	Node parentNode;
    	if (type.site){
    		parentNode =  JcrUtils.getCreateNode(site, appNode);
    	} else {
    		parentNode = appNode;
    	}
    	
    	Node ret;
    	if (type.groupName.equals(EC_NODE_APP)){
    		ret = appNode;
    	} else {
    		ret =  JcrUtils.getCreateNode(type.groupName, parentNode);
    	}
    	session.save();
    	return ret;
	}
	


}
