package com.madalla.util.jcr.model;

import java.io.Serializable;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class ContentNode extends AbstractReadOnlyModel  implements Serializable{
	private static final long serialVersionUID = 6274872365809010643L;

	private String path;
	private String name;

	public ContentNode(Node node){
		try {
			this.path = node.getPath();
			this.name = node.getName();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getPath() {
		return path;
	}

	public Object getObject() {
		return path;
	}

	public String getName() {
		return name;
	}
	
    public String toString() {
        return new ToStringBuilder(this).append("path",path).append("name", name).toString();
    }
}
