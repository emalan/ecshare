package com.madalla.service.cms;

import java.io.Serializable;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ImageData extends AbstractContentData implements Serializable {
	private static final long serialVersionUID = -3173685117852794066L;
    static final String EC_NODE_IMAGES = NS + "images";
    private static final Log log = LogFactory.getLog(ImageData.class);

	private final String name;
	private final String album;
	private final String id;
	
	public ImageData(final String album, final String name){
		id = "";
		this.album = album;
		this.name = name;
	}

	public ImageData(final String album, final String name, String id){
		this.id = id;
		this.album = album;
		this.name = name;
	}
	
    public String processEntry(Session session, IRepositoryService service) throws RepositoryException{
    	log.debug("processEntry - " + this);
        Node node ;
        if (StringUtils.isEmpty(id)){
        	Node siteNode = service.getSiteNode(session);
        	Node parent = service.getCreateNode(EC_NODE_IMAGES, siteNode);
        	Node groupNode = service.getCreateNode(NS+getGroup(), parent);
            node = service.getCreateNode(NS+ getName(), groupNode);
        } else {
            log.debug("processEntry - retrieving node by path. path="+ getId());
            node = (Node) session.getItem(getId());
        }
		//node.setProperty("",imageData.get );
        return node.getPath();
    }


	public String getAlbum() {
		return album;
	}

    public String toString() {
        return ReflectionToStringBuilder.toString(this).toString();
    }

	public String getGroup() {
		return album;
	}

	public String getName() {
		return name;
	}

	public String getId(){
		return id;
	}
	
	
	
}
