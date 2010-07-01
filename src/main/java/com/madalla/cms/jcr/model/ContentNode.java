package com.madalla.cms.jcr.model;

import java.io.Serializable;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class ContentNode extends AbstractReadOnlyModel<String>  implements Serializable, IContentNode{
	private static final long serialVersionUID = 6274872365809010643L;
	private static Log log = LogFactory.getLog(ContentNode.class);

	private String path;
	private String name;

	public ContentNode(Node node){
		try {
			this.path = node.getPath();
			this.name = node.getName();
		} catch (RepositoryException e) {
			log.error("Exception creating Content Node", e);
		}
	}

	public String getPath() {
		return path;
	}

	/* (non-Javadoc)
     * @see com.madalla.util.jcr.model.IContentNode#getObject()
     */
	public String getObject() {
		return path;
	}

	public String getName() {
		return name;
	}
    
    /* (non-Javadoc)
     * @see com.madalla.util.jcr.model.IContentNode#render()
     */
    public String render(){
        return name;
    }
	
    public String toString() {
        return new ToStringBuilder(this).append("path",path).append("name", name).toString();
    }
}
