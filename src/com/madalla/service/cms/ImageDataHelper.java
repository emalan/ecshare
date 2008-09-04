package com.madalla.service.cms;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.jcr.JcrTemplate;

public class ImageDataHelper extends AbstractContentHelper {
	private static final Log log = LogFactory.getLog(ImageDataHelper.class);
	
	// Repository Values
    static final String EC_NODE_IMAGES = NS + "images";

	private static ImageDataHelper instance;
	public static ImageDataHelper getInstance(){
		return instance;
	}

	public ImageDataHelper(String site, JcrTemplate template ){
		this.site = site;
		this.template = template;
		instance = this;
	}
	
	public String save(final ImageData imageData) {
		return genericSave(imageData);
    }
	
    
    Node getParentNode(Node node) throws RepositoryException{
    	return getCreateNode(EC_NODE_IMAGES, node);
    }

	@Override
	void setPropertyValues(Node node, IRepositoryData data)
			throws RepositoryException {
		// TODO Auto-generated method stub
		
	}

}
