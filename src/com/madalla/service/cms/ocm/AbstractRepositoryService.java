package com.madalla.service.cms.ocm;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.jcr.Node;
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
import com.madalla.util.jcr.ocm.JcrOcmUtils;

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
			JcrOcmUtils.setupOcmNodeTypes(session);
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
	
    protected Node getCreateNode(String nodeName, Node parent) throws RepositoryException{
    	return JcrUtils.getCreateNode(nodeName, parent);
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
