package com.madalla.util.jcr.ocm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.wicket.WicketRuntimeException;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.service.cms.AbstractBlog;
import com.madalla.service.cms.AbstractBlogEntry;
import com.madalla.service.cms.IRepositoryService;
import com.madalla.service.cms.ocm.RepositoryInfo;
import com.madalla.service.cms.ocm.blog.BlogEntry;

public class JcrOcmConversion {
	
	private IRepositoryService repositoryService;
	private com.madalla.service.cms.jcr.RepositoryService oldRepositoryService;
	private ObjectContentManager ocm;
	private JcrTemplate template;
	private String site;
	
	public void init(JcrTemplate template, 
			com.madalla.service.cms.jcr.RepositoryService oldRepositoryService, 
			IRepositoryService repositoryService, String site){
		this.oldRepositoryService = oldRepositoryService;
		this.repositoryService = repositoryService;
		this.template = template;
		this.site = site;
		Session session;
		try {
			session = template.getSessionFactory().getSession();
		} catch (RepositoryException e) {
			throw new WicketRuntimeException("Exception getting Session from JcrTemplate", e);
		}
		ocm =  JcrOcmUtils.getObjectContentManager(session);
	}

	public void convertNodesToOcm(){
		//Do Blogs
		
		
		template.execute(new JcrCallback(){

			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				final List<Node> list = new ArrayList<Node>();
				Node blogsNode = RepositoryInfo.getBlogsNode(session, site);
				for (Iterator iter = blogsNode.getNodes(); iter.hasNext();){
					Node node = (Node) iter.next();
					if (node.getName().startsWith("ec:")){
						list.add(node);
					}
				}
				for (Node blogNode: list){
					
					String blogName = StringUtils.substringAfter(blogNode.getName(), "ec:");
					List<AbstractBlogEntry> blogEntries = oldRepositoryService.getBlogEntries(blogName);
					AbstractBlog blog = repositoryService.getBlog(blogName);
					for(AbstractBlogEntry old: blogEntries){
						BlogEntry newEntry = repositoryService.getNewBlogEntry(blog, old.getTitle(), old.getDate());
						newEntry.setCategory(old.getCategory());
						newEntry.setDescription(old.getDescription());
						newEntry.setKeywords(old.getKeywords());
						newEntry.setText(old.getText());
						repositoryService.saveBlogEntry(newEntry);
					}
					blogNode.remove();
					session.save();
				}
				
				return null;
			}
			
		});
		
		
	}
	
	private static void convertBlogEntry(){
		
	}
	
	private static void convertBlog(){
		
	}
}
