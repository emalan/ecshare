package com.madalla.service.cms;

import java.io.IOException;
import java.io.OutputStream;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.swing.tree.TreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.util.jcr.model.JcrNodeModel;
import com.madalla.util.jcr.model.tree.JcrTreeModel;
import com.madalla.util.jcr.model.tree.JcrTreeNode;

public class ContentAdminService implements IContentData, IContentAdminService {
	
    private static final long serialVersionUID = 1L;
    private JcrTemplate template;
    private final Log log = LogFactory.getLog(this.getClass());

    public TreeModel getSiteContent(){
        return (TreeModel) template.execute(new JcrCallback(){
            
            public Object doInJcr(Session session) throws IOException, RepositoryException {
                Node rootContentNode = session.getRootNode().getNode(EC_NODE_APP);
                Node siteNode = rootContentNode.getNode(EC_NODE_SITE);

                //TODO change to using site Node. Site Nood must act like root.
                JcrNodeModel nodeModel = new JcrNodeModel(rootContentNode);
                JcrTreeNode treeNode = new JcrTreeNode(nodeModel);
                JcrTreeModel jcrTreeModel = new JcrTreeModel(treeNode);
                treeNode.init(rootContentNode);
                return jcrTreeModel;
            }
        });
    }
    
    public void backupContent(){
    	template.execute(new JcrCallback(){

			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
                Node rootContentNode = session.getRootNode().getNode(EC_NODE_APP);
                Node siteNode = rootContentNode.getNode(EC_NODE_SITE);
				
                OutputStream out = null;
                session.exportDocumentView(rootContentNode.getPath(), out, true, false);
				
				
				//session.exportSystemView(rootContentNode.getPath(), out, true, false);
				return null;
			}
    		
    	});
    }
}
