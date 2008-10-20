package com.madalla.service.cms.jcr;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;
import com.madalla.service.cms.IRepositoryData;

abstract class AbstractContentHelper {

	private static final Log log = LogFactory.getLog(AbstractContentHelper.class);
	static final String NS = "ec:";
    static final String EC_NODE_APP = NS + "apps";
    
    //shared repository properties
	static final String EC_PROP_CONTENT = NS + "content";

	//Spring configured
    protected String site ;
	protected JcrTemplate template;

	public void setSite(String site) {
		this.site = site;
	}
	public void setTemplate(JcrTemplate template) {
		this.template = template;
	}
	

	//Utility methods

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
    
    protected String genericSave(final IRepositoryData data) {
    	return (String) template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
		    	log.debug("processEntry - " + this);
		        Node node ;
		        if (StringUtils.isEmpty(data.getId())){
		        	Node siteNode = getSiteNode(session);
		        	Node parent = getParentNode(siteNode);
		        	Node groupNode = getCreateNode(NS+data.getGroup(), parent);
		            node = getCreateNode(NS+ data.getName(), groupNode);
		        } else {
		            log.debug("processEntry - retrieving node by path. path="+ data.getId());
		            node = (Node) session.getItem(data.getId());
		        }
		        setPropertyValues(node, data);
		        session.save();
		        return node.getPath();

			}
    	});
    }

    abstract Node getParentNode(Node node)throws RepositoryException;

    abstract void setPropertyValues(Node node, IRepositoryData data)throws  RepositoryException;
}
