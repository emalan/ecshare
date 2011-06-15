package com.madalla.cms.jcr.model;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


public class JcrNodeModel implements IPluginModel {
    private static final long serialVersionUID = 1L;
    //private static final Log log = LogFactory.getLog(JcrNodeModel.class);

    private ContentNode contentNode;

    public JcrNodeModel(Node node) {
    	contentNode = new ContentNode(node);
    }

    public Map<String, String> getMapRepresentation() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("node", contentNode.getPath());
        return map;
    }

    // override Object
    @Override
	public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("contentNode", contentNode.toString())
                .toString();
    }

    @Override
	public boolean equals(Object object) {
        if (object instanceof JcrNodeModel == false) {
            return false;
        }
        if (this == object) {
            return true;
        }
        JcrNodeModel nodeModel = (JcrNodeModel) object;
        return new EqualsBuilder().append(contentNode, nodeModel.contentNode).isEquals();
    }

    @Override
	public int hashCode() {
        return new HashCodeBuilder(57, 433).append(contentNode).toHashCode();
    }

	public ContentNode getObject() {
		return contentNode;
	}

	public void setObject(ContentNode obj) {
		contentNode = obj;
	}

	public void detach() {
		//contentNode = null;
	}

	public IContentNode getContentNode() {
		return contentNode;
	}

    public String render() {
        return contentNode.render();
    }
}
