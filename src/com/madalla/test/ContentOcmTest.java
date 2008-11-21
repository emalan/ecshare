package com.madalla.test;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.apache.jackrabbit.ocm.mapper.Mapper;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.AnnotationMapperImpl;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.service.cms.jcr.BlogEntry;
import com.madalla.service.cms.ocm.blog.Blog;

public class ContentOcmTest extends AbstractSpringWicketTester {

	Log log = LogFactory.getLog(this.getClass());
	private JcrTemplate template;
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		List<Class> classes = new ArrayList<Class>();	
		classes.add(Blog.class);
		classes.add(BlogEntry.class);
				
		Mapper mapper = new AnnotationMapperImpl(classes);
		Session session = template.getSessionFactory().getSession();
		ObjectContentManager ocm =  new ObjectContentManagerImpl(session, mapper);
	}

	@Override
	protected List<String> getTestConfigLocations() {
		List<String> configLocations = new ArrayList<String>();
		configLocations
				.add("classpath:com/madalla/service/cms/jcr/applicationContext-cms.xml");
		configLocations
				.add("classpath:com/madalla/util/jcr/applicationContext-jcr-local.xml");

		return configLocations;
	}
	
	
	
	public void testOcmBlogInsert(){
		//Fetch or create Blogs node
		
		
		Blog blog = new Blog();
		
	}
	
	public JcrTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JcrTemplate template) {
		this.template = template;
	}


}
