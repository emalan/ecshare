package com.madalla.util.jcr.model;

import java.io.Serializable;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.wicket.model.AbstractReadOnlyModel;

//TODO Move this to service package cause it is aware of Schema - might involve some refactoring
public class ContentNode extends AbstractReadOnlyModel  implements Serializable, IContentNode{
	private static final long serialVersionUID = 6274872365809010643L;

	private String path;
	private String name;
    private String title;

	public ContentNode(Node node){
		try {
			this.path = node.getPath();
			this.name = node.getName();
            this.title = node.getProperty("ec:title").getString();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getPath() {
		return path;
	}

	/* (non-Javadoc)
     * @see com.madalla.util.jcr.model.IContentNode#getObject()
     */
	public Object getObject() {
		return path;
	}

	public String getName() {
		return name;
	}
    
    /* (non-Javadoc)
     * @see com.madalla.util.jcr.model.IContentNode#render()
     */
    public String render(){
        return name + (null ==title?"":" - "+title);
    }
	
    public String toString() {
        return new ToStringBuilder(this).append("path",path).append("name", name).toString();
    }
}
