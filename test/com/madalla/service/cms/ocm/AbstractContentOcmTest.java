package com.madalla.service.cms.ocm;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.manager.atomictypeconverter.AtomicTypeConverterProvider;
import org.apache.jackrabbit.ocm.manager.atomictypeconverter.impl.AtomicTypeConverterProviderImpl;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.apache.jackrabbit.ocm.manager.objectconverter.ObjectConverter;
import org.apache.jackrabbit.ocm.manager.objectconverter.impl.ObjectConverterImpl;
import org.apache.jackrabbit.ocm.mapper.Mapper;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.AnnotationMapperImpl;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.AbstractSpringWicketTester;
import com.madalla.service.cms.ocm.blog.Blog;
import com.madalla.service.cms.ocm.blog.BlogEntry;
import com.madalla.util.jcr.JcrOcmUtils;
import com.madalla.util.jcr.JcrUtils;

public abstract class AbstractContentOcmTest extends AbstractSpringWicketTester {

	/////ec:apps/ec:test/ec:blogs/ec:testBlog
	protected static final String NS = "ec:";
	private static final String NS_APP = NS+"apps";
	private static final String NS_TEST = NS+"test";
	
	Log log = LogFactory.getLog(this.getClass());
	protected JcrTemplate template;
	protected ObjectContentManagerImpl ocm;
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		
		//Setup all OCM annotated classes
		List<Class> classes = new ArrayList<Class>();	
		classes.add(Blog.class);
		classes.add(BlogEntry.class);
		Mapper mapper = new AnnotationMapperImpl(classes);
		Session session = template.getSessionFactory().getSession();
		JcrOcmUtils.setupOcmNodeTypes(session);

		ocm =  new ObjectContentManagerImpl(session, mapper);
		
		//Add new Convertors
		AtomicTypeConverterProvider atomicTypeConverterProvider = new AtomicTypeConverterProviderImpl();
		//atomicTypeConverterProvider.getAtomicTypeConverters().put(key, value);
		ObjectConverterImpl converterImpl = new ObjectConverterImpl(mapper, atomicTypeConverterProvider);

		ocm.setObjectConverter(converterImpl);
	}
	
	protected Node getTestNode(Session session) throws RepositoryException{
		Node root = session.getRootNode();
		Node app = JcrUtils.getCreateNode(NS_APP, root);
		return JcrUtils.getCreateNode(NS_TEST, app);
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
	
	public JcrTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JcrTemplate template) {
		this.template = template;
	}


}
