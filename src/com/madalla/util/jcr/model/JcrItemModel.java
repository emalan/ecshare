package com.madalla.util.jcr.model;

import javax.jcr.Item;
import javax.jcr.RepositoryException;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.model.LoadableDetachableModel;

public class JcrItemModel extends LoadableDetachableModel {
	private static final long serialVersionUID = 1L;
    static final Log log = LogFactory.getLog(JcrItemModel.class);

    protected String path;

    // constructors

    public JcrItemModel(Item item) {
        super(item);
        try {
            this.path = item.getPath();
        } catch (RepositoryException e) {
            log.error(e.getMessage());
        }
    }

    public JcrItemModel(String path) {
        super();
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public boolean exists() {
        boolean result = false;

//        try {
//            //UserSession sessionProvider = (UserSession) Session.get();
//            //result = sessionProvider.getJcrSession().itemExists(path);
//        } catch (RepositoryException e) {
//            log.error(e.getMessage());
//        }
        return result;
    }

    public JcrItemModel getParentModel() {
        int idx = path.lastIndexOf('/');
        if (idx > 0) {
            String parent = path.substring(0, path.lastIndexOf('/'));
            return new JcrItemModel(parent);
        } else if (idx == 0) {
            if (path.equals("/")) {
                return null;
            }
            return new JcrItemModel("/");
        } else {
            log.error("Unrecognised path " + path);
            return null;
        }
    }

    // LoadableDetachableModel

    protected Object load() {
    	log.debug("load - loading item from repository with path="+path);
//    	Item result = (Item) template.execute(new JcrCallback(){
//
//			public Object doInJcr(Session session) throws IOException,
//					RepositoryException {
//				return session.getItem(path);
//			}
//        	
//        });
//        return result;
    	return null;
    }

    // override Object

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("path", path).toString();
    }

    public boolean equals(Object object) {
        if (object instanceof JcrItemModel == false) {
            return false;
        }
        if (this == object) {
            return true;
        }
        JcrItemModel itemModel = (JcrItemModel) object;
        return new EqualsBuilder().append(normalizePath(path), normalizePath(itemModel.path)).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(177, 3).append(path).toHashCode();
    }

    private static String normalizePath(String path) {
        if (path.length() > 0) {
            if (path.charAt(path.length() - 1) == ']') {
                return path;
            }
            return path + "[1]";
        }
        return path;
    }

}
