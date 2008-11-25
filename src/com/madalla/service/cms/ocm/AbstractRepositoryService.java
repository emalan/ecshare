package com.madalla.service.cms.ocm;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.apache.jackrabbit.ocm.mapper.Mapper;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.AnnotationMapperImpl;
import org.apache.wicket.WicketRuntimeException;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.service.cms.jcr.BlogEntry;
import com.madalla.service.cms.ocm.blog.Blog;
import com.madalla.util.jcr.JcrUtils;

abstract class AbstractRepositoryService {
	
	private static final Log log = LogFactory.getLog(AbstractRepositoryService.class);
	
    private String site ;
	protected JcrTemplate template;
    protected List<Locale> locales;
    protected ObjectContentManager ocm;

    //Repository Node Names
    static final String NS = "ec:";
    static final String EC_NODE_APP = NS + "apps";
    
    AbstractRepositoryService() {
    	List<Class> classes = new ArrayList<Class>();	
		classes.add(Blog.class);
		classes.add(BlogEntry.class);
				
		Mapper mapper = new AnnotationMapperImpl(classes);
		Session session;
		try {
			session = template.getSessionFactory().getSession();
			JcrUtils.setupOcmNodeTypes(session);
		} catch (RepositoryException e) {
			log.error("Error initializing - getting session from JcrTemplate", e);
			throw new WicketRuntimeException("Error getting Session from JcrTemplate.",e);
		}
		ocm =  new ObjectContentManagerImpl(session, mapper);
    }

    public Node getApplicationNode(Session session) throws RepositoryException{
    	return getCreateNode(EC_NODE_APP, session.getRootNode());
    }

	public Node getSiteNode(Session session) throws RepositoryException {
    	Node appNode = getApplicationNode(session);
    	return getCreateNode(NS+site, appNode);
	}
	
    
    /**
     *  returns the class name node -- creates it if its not there
     */
    public Node getCreateNode(String nodeName, Node parent) throws RepositoryException{
    	if (null == nodeName || null == parent){
    		log.error("getCreateNode - all parameters must be supplied");
    		return null;
    	}
        Node node = null;
        try {
            node = parent.getNode(nodeName);
        } catch (PathNotFoundException e){
            log.debug("Node not found in repository, now adding. new node="+nodeName);
            node = parent.addNode(nodeName);
        }
        return node;
        
    }
    
    public void setTemplate(JcrTemplate template) {
        this.template = template;
    }

	public void setLocales(List<Locale> locales) {
		this.locales = locales;
	}
	
	public void setSite(String site) {
		this.site = site;
	}
	
	public String getSite(){
		return site;
	}



}
