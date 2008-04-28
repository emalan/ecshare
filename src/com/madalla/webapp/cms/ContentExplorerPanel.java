package com.madalla.webapp.cms;

import java.text.DateFormat;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.madalla.service.cms.IContentService;
import com.madalla.service.cms.IContentServiceProvider;
import com.madalla.util.ui.ITreeInput;

public class ContentExplorerPanel extends Panel {
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
//				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) model
//						.getObject();
//				if (treeNode.getUserObject() instanceof ITreeInput) {
//					ITreeInput treeInput = (ITreeInput) treeNode.getUserObject();
//					String title = treeInput.getTitle();
//					return new Model(df.format(treeInput.getDate())
//							+ (null == title ? "" : " - " + title));
//				} else {
//					return model;
//				}
				return model;
			}

		};
		tree.getTreeState().expandAll();
		add(tree);

	}

	protected IContentService getContentService() {
		return ((IContentServiceProvider) getApplication()).getContentService();
	}

}
