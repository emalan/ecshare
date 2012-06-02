package com.madalla.jcr.explorer;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.swing.tree.DefaultTreeModel;

import org.emalan.cms.IRepositoryAdminService;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.jcr.model.JcrNodeModel;
import com.madalla.jcr.model.tree.JcrTreeModel;
import com.madalla.jcr.model.tree.JcrTreeNode;

public class ExplorerService {
	
	private JcrTemplate template;
	private IRepositoryAdminService repositoryAdminService;

    public DefaultTreeModel getRepositoryContent(){
        return (DefaultTreeModel) template.execute(new JcrCallback(){

            public Object doInJcr(Session session) throws IOException, RepositoryException {
                Node rootNode = repositoryAdminService.getApplicationNode(session);
                session.save();
                JcrNodeModel nodeModel = new JcrNodeModel(rootNode);
                JcrTreeNode treeNode = new JcrTreeNode(nodeModel);
                JcrTreeModel jcrTreeModel = new JcrTreeModel(treeNode);
                treeNode.init(rootNode);
                return jcrTreeModel;
            }
        });
    }

    public DefaultTreeModel getSiteContent(){
        return (DefaultTreeModel) template.execute(new JcrCallback(){

            public Object doInJcr(Session session) throws IOException, RepositoryException {
                Node siteNode = repositoryAdminService.getSiteNode(session);
                session.save();
                JcrNodeModel nodeModel = new JcrNodeModel(siteNode);
                JcrTreeNode treeNode = new JcrTreeNode(nodeModel);
                JcrTreeModel jcrTreeModel = new JcrTreeModel(treeNode);
                treeNode.init(siteNode);
                return jcrTreeModel;
            }
        });
    }

	public void setTemplate(JcrTemplate template) {
		this.template = template;
	}

	public void setRepositoryAdminService(IRepositoryAdminService repositoryAdminService) {
		this.repositoryAdminService = repositoryAdminService;
	}
    



}
