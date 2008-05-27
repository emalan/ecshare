package com.madalla.util.jcr.model.tree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.util.jcr.model.JcrItemModel;
import com.madalla.util.jcr.model.JcrNodeModel;
import com.madalla.util.jcr.test.AbstractJcrTester;

public class TestJcrTreeNode extends AbstractJcrTester {

	Log log = LogFactory.getLog(this.getClass());
	private JcrTemplate template;
	private String site = "test" ;
	final String CONTENT = "Testcontent";

	protected List getTestConfigLocations() {
		List configLocations = new ArrayList();
		configLocations.add("classpath:com/madalla/service/cms/applicationContext-cms.xml");
		return configLocations;
	}
	
	public void onSetUp() throws Exception{
		super.onSetUp();
		
		//remove all children
		template.execute(new JcrCallback(){

			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				{
					Node siteNode = getCreateNode(site, session.getRootNode());
					NodeIterator nodeIterator = siteNode.getNodes();
					for (NodeIterator iterator = siteNode.getNodes();iterator.hasNext();){
						Node node = iterator.nextNode();
						node.remove();
					}
					session.save();
				}
				
				//add some data for test
				{
					Node siteNode = session.getRootNode().getNode(site);
					Node one = siteNode.addNode("PageOne");
					one.addNode("para1").setProperty(CONTENT, "Some text");
					one.addNode("para2").setProperty(CONTENT, "Other text");
					
					Node two = siteNode.addNode("PageTwo");
					two.addNode("para1");
					
					Node three = siteNode.addNode("PageThree");
					
					session.save();
				}

				return null;
			}
			
		});
	}
	
	public void testJcrTreeNode(){
		template.execute(new JcrCallback(){

			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node root = session.getRootNode();
				Node siteNode = root.getNode(site);
				int count = (int) siteNode.getNodes().getSize();
				assertNotNull(siteNode);
                JcrItemModel itemModel = new JcrItemModel(siteNode);
                JcrNodeModel nodeModel = new JcrNodeModel(itemModel);
                JcrTreeNode treeNode = new JcrTreeNode(nodeModel);
                JcrTreeModel jcrTreeModel = new JcrTreeModel(treeNode);
                assertNotNull(treeNode);
                
                boolean leaf = treeNode.isLeaf();
                int testCount = treeNode.getChildCount();
                assertEquals(count, testCount);
                
                Enumeration e = treeNode.children();
                assertNotNull(e);
                
                e.nextElement();
                AbstractTreeNode abstractTreeNode = (AbstractTreeNode)e.nextElement();
                int i = treeNode.getIndex(abstractTreeNode);
                assertEquals(1, i);

                abstractTreeNode.renderNode();
                treeNode.renderNode();
			    return null;
			}
			
		});
	}

	public JcrTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JcrTemplate template) {
		this.template = template;
	}

}
