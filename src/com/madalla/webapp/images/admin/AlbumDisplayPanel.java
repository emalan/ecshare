package com.madalla.webapp.images.admin;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tree.table.AbstractTreeColumn;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation;
import org.apache.wicket.extensions.markup.html.tree.table.IColumn;
import org.apache.wicket.extensions.markup.html.tree.table.TreeTable;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Alignment;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Unit;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.madalla.bo.AlbumData;
import com.madalla.bo.IAlbumData;
import com.madalla.bo.ImageData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.wicket.DraggableAjaxBehaviour;
import com.madalla.wicket.DroppableAjaxBehaviour;

class AlbumDisplayPanel extends Panel {
	
	private static final long serialVersionUID = 1L;
	private final static Log log = LogFactory.getLog(AlbumDisplayPanel.class);
	
	public AlbumDisplayPanel(String id, final String albumName) {
		super(id);
		
		final Form form = new Form("albumForm");
		
		form.add(new FeedbackPanel("albumFeedback"));
		
		//Setting up Tree Table
		IColumn column = new AbstractTreeColumn(new ColumnLocation(Alignment.LEFT,20, Unit.EM),"Images"){
			private static final long serialVersionUID = 1L;

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
		
		

		IModel treeModel = new LoadableDetachableModel(){
			private static final long serialVersionUID = 1L;

			@Override
			protected Object load() {
			    AlbumData album = getRepositoryService().getAlbum(albumName);
				return getRepositoryService().getAlbumImagesAsTree(album);
			}
		};
		final TreeTable tree = new TreeTable("albumTreeTable", treeModel, columns);
		tree.setOutputMarkupId(true);
		form.add(tree);
		
		final AbstractDefaultAjaxBehavior onDrop = new AbstractDefaultAjaxBehavior() {
			private static final long serialVersionUID = 1L;

			protected void respond(final AjaxRequestTarget target) {
				String dragId = DraggableAjaxBehaviour.getDraggablesId(getRequest());
				log.debug("something dropped. arg="+dragId);
				IAlbumData album = getRepositoryService().getAlbum(albumName);
		    	getRepositoryService().addImageToAlbum(album, dragId);
		    	form.info("Success! Image saved to album.");
		    	target.addComponent(tree);
		    	target.addComponent(form);
		    }
		};
		add(new DroppableAjaxBehaviour(onDrop));
		add(onDrop);
		form.setOutputMarkupId(true);
		
		tree.getTreeState().expandAll();
		
		add(form);
	}
	
	private IRepositoryService getRepositoryService(){
		IRepositoryServiceProvider provider = (IRepositoryServiceProvider)getApplication();
		return provider.getRepositoryService();
	}

}
