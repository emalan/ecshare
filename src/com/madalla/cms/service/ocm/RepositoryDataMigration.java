package com.madalla.cms.service.ocm;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.cms.service.ocm.RepositoryInfo.RepositoryType;

public class RepositoryDataMigration {
	
	private static final Log log = LogFactory.getLog(RepositoryDataMigration.class);
	
	

	public static void transformData(JcrTemplate template, final String site){
		
		template.execute(new JcrCallback(){

			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				
				for(RepositoryType type: RepositoryType.values()){
					Node groupNode = RepositoryInfo.getGroupNode(session, site, type);
					
					convertDataGroup(groupNode, type);
					session.save();
				}
				
				return null;
			}
			
		});
		
	}
	
	private static void convertDataGroup(Node node, RepositoryType type) throws RepositoryException{
		log.debug("Checking Data Migration for " + type.name());
		if (type.equals(RepositoryType.BLOG)){
			log.debug("Processing Data Migration for " + type.name());
			doConversion(node,oldBlog, newBlog, oldBlogEntry, newBlogEntry);
		} else if (type.equals(RepositoryType.PAGE)){
		    log.debug("Processing Data Migration for " + type.name());
		    doConversion(node, oldPage, newPage, oldContent, newContent);
		} else if (type.equals(RepositoryType.ALBUM)){
		    log.debug("Processing Data Migration for " + type.name());
		    doConversion(node, oldAlbum, newAlbum, oldImage, newImage);
		}
	}
	
	private static final String CLASS_NAME = "ocm:classname";
	private static final String oldBlog = "com.madalla.service.cms.ocm.blog.Blog";
	private static final String newBlog = "com.madalla.cms.bo.impl.ocm.blog.Blog";
	private static final String oldBlogEntry = "com.madalla.service.cms.ocm.blog.BlogEntry";
	private static final String newBlogEntry = "com.madalla.cms.bo.impl.ocm.blog.BlogEntry";
	private static final String oldPage = "com.madalla.service.cms.ocm.page.Page";
	private static final String newPage = " com.madalla.cms.bo.impl.ocm.page.Page";
	private static final String oldContent = "com.madalla.service.cms.ocm.page.Content";
	private static final String newContent = "com.madalla.cms.bo.impl.ocm.page.Content";
	private static final String oldAlbum = "com.madalla.service.cms.ocm.image.Album";
	private static final String newAlbum = "com.madalla.cms.bo.impl.ocm.image.Album";
	private static final String oldImage = "com.madalla.service.cms.ocm.image.Image";
	private static final String newImage = "com.madalla.cms.bo.impl.ocm.image.Image";
	
	private static void doConversion(Node parent, String oldParent, String newParent, String oldChild, String newChild) throws RepositoryException{
		NodeIterator iter =(NodeIterator) parent.getNodes() ;
		while (iter.hasNext()){
			Node node = iter.nextNode();
			doClassNameConversion(node,oldParent, newParent );
			NodeIterator iter2 = node.getNodes();
			while (iter2.hasNext()){
				Node child = iter2.nextNode();
				doClassNameConversion(child,oldChild, newChild );
			}
		}
	}
	
	private static void doClassNameConversion(Node node, String oldClass, String newClass) throws PathNotFoundException, RepositoryException{
		Property property = node.getProperty(CLASS_NAME);
		if (oldClass.equals(property.getValue().getString())){
			node.setProperty(CLASS_NAME, newClass);
			log.debug("Changing class name from '" + oldClass + "' to '" + newClass);
		}
	}
	
	 
}
