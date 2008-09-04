package com.madalla.service.cms;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.jcr.JcrTemplate;

/**
 * Content Service Implementation for JCR Content Repository
 * <p>
 * This class is aware of the structure of the data in the repository 
 * and will create the structure if it does not exist. The schema is
 * open and not enforced by the repository. 
 * <p>
 * <pre>
 *            ec:apps 
 *         -----|------------------------------                 
 *        |                                    |
 *     [ec:site1]                          [ec:site2]                               
 *        |                                    |
 * 
 * </pre>
 * 
 * @author Eugene Malan
 *
 */
public class RepositoryService implements IRepositoryService , Serializable{

	private static final long serialVersionUID = 795763276139305054L;
	private static final Log log = LogFactory.getLog(RepositoryService.class);

	private JcrTemplate template;
    private List<Locale> locales;
    private String site ;
    
    //Repository Node Names
    static final String NS = "ec:";
    static final String EC_NODE_APP = NS + "apps";

    
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

    public String getLocaleId(String id, Locale locale) {
        Locale found = null;
        for (Iterator<Locale> iter = locales.iterator(); iter.hasNext();) {
            Locale current = iter.next();
            if (current.getLanguage().equals(locale.getLanguage())){
                found = current;
                break;
            }
        }
        if (null == found){
            return id;
        } else {
            return id + "_"+ found.getLanguage();
        }
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
