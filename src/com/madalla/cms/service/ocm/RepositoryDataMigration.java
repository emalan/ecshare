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
	
	private static final String CLASS_NODE_OLD = "ocm:classname";
	private static final String CLASS_NODE_NEW = "ocm_classname";

	public static void transformData(JcrTemplate template, final String site){
		
		template.execute(new JcrCallback(){

			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				
				Node appNode = RepositoryInfo.getApplicationNode(session);

				//convert node to OCM 
				try {
                    Node siteNode = appNode.getNode("ec:"+site);
                    siteNode.setProperty(CLASS_NAME, "com.madalla.cms.bo.impl.ocm.Site");
                    session.move(siteNode.getPath(), appNode.getPath()+"/"+site);
                    session.save();
                    doConversionByType(session, site);
                } catch (PathNotFoundException e){
                    log.warn("No OCM conversion needed for " + site);
                }

				
				try {
				    Node siteNode = appNode.getNode(site);
				    Property property = siteNode.getProperty(CLASS_NODE_OLD);
                    doOcmClassNodeChangeConversion(siteNode);
				    session.save();
				    doConversionByType(session, site);
				} catch (PathNotFoundException e){
				    log.warn("Site conversion not needed for site=" + site);
				}
				return null;
			}
			
		});
		
	}
	
	private static void doConversionByType(Session session, String site) throws RepositoryException{
        for(RepositoryType type: RepositoryType.values()){
            Node groupNode = RepositoryInfo.getGroupNode(session, site, type);
            convertDataGroup(groupNode, type);
            session.save();
        }
	}
	
	//filter for Repository Types
	private static void convertDataGroup(Node node, RepositoryType type) throws RepositoryException{
		log.warn("Checking Data Migration for " + type.name());
		if (type.equals(RepositoryType.BLOG)){
			log.warn("Processing Data Migration for " + type.name());
			convertGroup(node);
		} else if (type.equals(RepositoryType.PAGE)){
		    log.warn("Processing Data Migration for " + type.name());
		    convertGroup(node);
		} else if (type.equals(RepositoryType.ALBUM)){
		    log.warn("Processing Data Migration for " + type.name());
		    convertGroup(node);
		} else if (type.equals(RepositoryType.USER)) {
            log.warn("Processing Data Migration for " + type.name());
            convertGroup(node);
		}
	}
	
    private static void convertGroup(Node parent) throws RepositoryException{
        NodeIterator iter =(NodeIterator) parent.getNodes() ;
        while (iter.hasNext()){
            Node node = iter.nextNode();
            doOcmClassNodeChangeConversion(node);
            NodeIterator iter2 = node.getNodes();
            while (iter2.hasNext()){
                Node child = iter2.nextNode();
                doOcmClassNodeChangeConversion(child);
            }
        }
    }
    
    private static void doOcmClassNodeChangeConversion(Node node) throws PathNotFoundException, RepositoryException{
        try {
            Property property = node.getProperty(CLASS_NODE_OLD);
            log.warn("Converting class node from '" + CLASS_NODE_OLD + "' to '" + CLASS_NODE_NEW + ". value=" + property.getString());
                node.setProperty(CLASS_NODE_NEW, property.getString());
                property.remove();
        } catch (PathNotFoundException e) {
            log.warn("Mmmmm... WTF! Maybe a previous incomplete conversion");
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
	
	
	/*
	private static void doClassNameConversion(Node node, String oldClass, String newClass) throws PathNotFoundException, RepositoryException{
		Property property = node.getProperty(CLASS_NAME);
		if (oldClass.equals(property.getValue().getString())){
			node.setProperty(CLASS_NAME, newClass);
			log.warn("Changing class name from '" + oldClass + "' to '" + newClass);
		}
	}
	*/
	

    
	
	
	 
}
