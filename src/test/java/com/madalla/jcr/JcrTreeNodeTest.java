package com.madalla.jcr;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.springmodules.jcr.JcrTemplate;

@Ignore
public class JcrTreeNodeTest extends AbstractJcrTester {

	Log log = LogFactory.getLog(this.getClass());
	public JcrTemplate template;
	private String site = "test" ;
	final String CONTENT = "Testcontent";

//	@Override
//	public void onSetUp() throws Exception{
//		super.onSetUp();
//
//		//remove all children
//		template.execute(new JcrCallback(){
//
//			public Object doInJcr(Session session) throws IOException,
//					RepositoryException {
//				{
//					Node siteNode = getCreateNode(site, session.getRootNode());
//					NodeIterator nodeIterator = siteNode.getNodes();
//					for (NodeIterator iterator = siteNode.getNodes();iterator.hasNext();){
//						Node node = iterator.nextNode();
//						node.remove();
//					}
//					session.save();
//				}
//
//				//add some data for test
//				{
//					Node siteNode = session.getRootNode().getNode(site);
//					Node one = siteNode.addNode("PageOne");
//					one.addNode("para1").setProperty(CONTENT, "Some text");
//					one.addNode("para2").setProperty(CONTENT, "Other text");
//
//					Node two = siteNode.addNode("PageTwo");
//					two.addNode("para1");
//
//					Node three = siteNode.addNode("PageThree");
//
//					session.save();
//				}
//
//				return null;
//			}
//
//		});
//	}

	public void testJcrTree() {
		System.out.println("TODO");
	}
//	public void testJcrTreeNode(){
//		template.execute(new JcrCallback(){
//
//			public Object doInJcr(Session session) throws IOException,
//					RepositoryException {
//				Node root = session.getRootNode();
//				Node siteNode = root.getNode(site);
//				int count = (int) siteNode.getNodes().getSize();
//				assertNotNull(siteNode);
//                JcrNodeModel nodeModel = new JcrNodeModel(root);
//                JcrTreeNode treeNode = new JcrTreeNode(nodeModel);
//                JcrTreeModel jcrTreeModel = new JcrTreeModel(treeNode);
//                assertNotNull(treeNode);
//
//                boolean leaf = treeNode.isLeaf();
//                int testCount = treeNode.getChildCount();
//                assertEquals(count, testCount);
//
//                Enumeration e = treeNode.children();
//                assertNotNull(e);
//
//                e.nextElement();
//                AbstractTreeNode abstractTreeNode = (AbstractTreeNode)e.nextElement();
//                int i = treeNode.getIndex(abstractTreeNode);
//                assertEquals(1, i);
//
//                abstractTreeNode.renderNode();
//                treeNode.renderNode();
//			    return null;
//			}
//
//		});
//	}

	public JcrTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JcrTemplate template) {
		this.template = template;
	}

}
