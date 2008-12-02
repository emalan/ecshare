package com.madalla.service.cms.ocm;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.util.jcr.JcrUtils;

public class RepositoryInfo {

    //Repository Node Names
    public static final String NS = "ec:";
    public static final String EC_NODE_APP = NS + "apps";
    public static final String EC_NODE_BLOGS = NS + "blogs";
    public static final String EC_NODE_PAGES = NS + "pages";
    public static final String EC_NODE_IMAGES = NS + "images";
 
    
    public static Node getApplicationNode(Session session) throws RepositoryException{
    	return JcrUtils.getCreateNode(EC_NODE_APP, session.getRootNode());
    }
    
	public static Node getSiteNode(Session session, String site) throws RepositoryException {
    	Node appNode = getApplicationNode(session);
    	return JcrUtils.getCreateNode(NS+site, appNode);
	}
	
	public static Node getBlogsNode(Session session, String site) throws RepositoryException{
		Node siteNode = getSiteNode(session, site);
		return JcrUtils.getCreateNode(EC_NODE_BLOGS, siteNode);
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
}
