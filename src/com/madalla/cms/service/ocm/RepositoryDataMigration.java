package com.madalla.cms.service.ocm;

import java.io.IOException;
import java.util.Iterator;

import javax.jcr.Node;
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
				
				Node appNode = RepositoryInfo.getApplicationNode(session);

				//convert node to OCM 
				try {
                    doConversionByType(session, site);
                } catch (PathNotFoundException e){
                    log.warn("No OCM conversion needed for " + site);
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
		if (type.equals(RepositoryType.CONTENT)){
			log.warn("Processing Data Migration for " + type.name());
			removeOldText(node);
		} 
	}
	
    private static void removeOldText(Node node) throws RepositoryException{
    	log.warn( node);
    	for (Iterator<Node> iterator = node.getNodes(); iterator.hasNext();) {
			Node page = iterator.next();
			log.warn(page);
			for(Iterator<Node> iter1 = page.getNodes(); iter1.hasNext();){
				Node content = iter1.next();
				log.warn(content);
				if (content.hasProperty("text")){
					content.getProperty("text").setValue("");
					log.warn("Clearing text value...");
				}
			}
			
		}

    }
	 
}
