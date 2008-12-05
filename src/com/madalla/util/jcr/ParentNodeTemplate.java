package com.madalla.util.jcr;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.service.cms.ocm.RepositoryInfo;
import com.madalla.service.cms.ocm.RepositoryInfo.RepositoryType;

public class ParentNodeTemplate {

	private JcrTemplate template;
	private ObjectContentManager ocm;
	private String site;
	
	public ParentNodeTemplate(JcrTemplate template, ObjectContentManager ocm, String site){
		this.template = template;
		this.ocm = ocm;
		this.site = site;
	}
	public Object execute(final RepositoryType type, final ParentNodeCallback callback){
		return execute(type,"", callback);
	}
	public Object execute(final RepositoryType type, final String name, final ParentNodeCallback callback){
		String parentPath = (String)template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node node = RepositoryInfo.getGroupNode(session, site, type);
				session.save();
				return node.getPath();
				
			}
		});
		String nodePath = parentPath + (StringUtils.isEmpty(name)?type.typeName : "/" + name);
		if (!ocm.objectExists(nodePath)){
			Object obj = callback.createNew(parentPath, name);
			ocm.insert(obj);
			ocm.save();
		}
		return ocm.getObject(type.typeClass, nodePath);
	}
	
	
	
}
