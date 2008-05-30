package com.madalla.webapp.cms;

import java.text.DateFormat;

import javax.swing.tree.TreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.madalla.service.cms.IContentData;
import com.madalla.service.cms.IContentService;
import com.madalla.service.cms.IContentServiceProvider;
import com.madalla.util.jcr.model.ContentNode;
import com.madalla.util.jcr.model.tree.JcrTreeNode;

public class ContentExplorerPanel extends Panel implements IContentData{
	private static final long serialVersionUID = 1L;

	private static DateFormat df = DateFormat.getDateInstance();

	private Log log = LogFactory.getLog(this.getClass());

	public ContentExplorerPanel(String name) {
		super(name);
		// List existing Blogs
		log.debug("construtor - retrieving blog entries from service.");
		IContentService service = getContentService();
		TreeModel treeModel = service.getSiteContent();
		log.debug("construtor - retrieved content entries. root="
				+ treeModel.getRoot());

		BaseTree tree = new LinkTree("ContentTree", treeModel) {
			private static final long serialVersionUID = 1L;

			protected IModel getNodeTextModel(IModel model) {
				JcrTreeNode jcrTreeNode = (JcrTreeNode) model.getObject();
				ContentNode contentNode = (ContentNode)jcrTreeNode.getObject();
				return new Model(contentNode.getName());
			}

		};
		tree.getTreeState().expandAll();
		add(tree);

	}

	protected IContentService getContentService() {
		return ((IContentServiceProvider) getApplication()).getContentService();
	}

}
