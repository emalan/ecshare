package com.madalla.webapp.images.admin;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tree.table.AbstractTreeColumn;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation;
import org.apache.wicket.extensions.markup.html.tree.table.IColumn;
import org.apache.wicket.extensions.markup.html.tree.table.TreeTable;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Alignment;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Unit;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import com.madalla.service.cms.IRepositoryService;
import com.madalla.service.cms.IRepositoryServiceProvider;
import com.madalla.service.cms.ImageData;

public class AlbumDisplayPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private final static Log log = LogFactory.getLog(AlbumDisplayPanel.class);
	
	private TreeTable tree;

	public AlbumDisplayPanel(String id, String album) {
		super(id);
		IColumn column = new AbstractTreeColumn(new ColumnLocation(Alignment.LEFT,20, Unit.EM),"Images"){

			@Override
			public String renderNode(TreeNode node) {
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
				Object userObject = treeNode.getUserObject();
				if (userObject instanceof String){
					return (String)userObject;
				} else if (userObject instanceof ImageData){
					return ((ImageData)userObject).getName();
				} else {
					return null;
				}
				
			}
			
		};

		IColumn columns[] = new IColumn[]{
				column
		};
		

		Form form = new Form("albumForm");
		form.setOutputMarkupId(true);
		form.add(new AjaxEventBehavior("imagedropped"){

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				log.info("albumForm - something dropped!");
				
			}
			
		});
        add(form);
		TreeModel treeModel = getRepositoryService().getAlbumImagesAsTree(album);
		tree = new TreeTable("albumTreeTable", treeModel, columns);
		form.add(tree);
		tree.getTreeState().expandAll();
	}
	
	private IRepositoryService getRepositoryService(){
		IRepositoryServiceProvider provider = (IRepositoryServiceProvider)getApplication();
		return provider.getRepositoryService();
	}
}
