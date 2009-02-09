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
			doBlogConversion(node);
		}
	}
	
	private static final String CLASS_NAME = "ocm:classname";
	private static final String oldBlog = "";
	private static final String newBlog = "com.madalla.cms.bo.impl.ocm.blog.Blog";
	private static final String oldBlogEntry = "";
	private static final String newBlogEntry = "com.madalla.cms.bo.impl.ocm.blog.BlogEntry";
	private static final String oldPage = "";
	private static final String newPage = " com.madalla.cms.bo.impl.ocm.page.Page";
	private static final String oldContent = "";
	private static final String newContent = "com.madalla.cms.bo.impl.ocm.page.Content";
	private static final String oldAlbum = "";
	private static final String newAlbum = "com.madalla.cms.bo.impl.ocm.image.Album";
	private static final String oldImage = "";
	private static final String newImage = "";
	
	private static void doBlogConversion(Node parent) throws RepositoryException{
		String oldBlog = "";
		String newBlog = "com.madalla.cms.bo.impl.ocm.blog.Blog";
		String oldBlogEntry = "";
		String newBlogEntry = "com.madalla.cms.bo.impl.ocm.blog.BlogEntry";
		NodeIterator iter =(NodeIterator) parent.getNodes() ;
		while (iter.hasNext()){
			Node node = iter.nextNode();
			doClassNameConversion(node,oldBlog, oldBlog );
			NodeIterator iter2 = node.getNodes();
			while (iter2.hasNext()){
				Node child = iter2.nextNode();
				doClassNameConversion(node,oldBlogEntry, newBlogEntry );
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
