package com.madalla.service.cms.ocm.blog;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.service.cms.ocm.RepositoryInfo;
import com.madalla.util.jcr.JcrUtils;

public class BlogInfo extends RepositoryInfo{

	private static final String NS_BLOG = NS + "blogs";
	
	public static String getPath(final String blog, JcrTemplate template, final String site){
		
		return (String )template.execute(new JcrCallback(){

			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node test = getSiteNode(session, site);
				Node blogs = JcrUtils.getCreateNode(NS_BLOG, test);
				session.save();
				return blogs.getPath()+"/"+ NS +blog;
			}
			
		});
		
	}
			
}
