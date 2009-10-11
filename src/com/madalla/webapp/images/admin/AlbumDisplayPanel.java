package com.madalla.webapp.images.admin;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
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
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.validation.validator.RangeValidator;

import com.madalla.bo.image.AlbumData;
import com.madalla.bo.image.IAlbumData;
import com.madalla.bo.image.ImageData;
import com.madalla.webapp.panel.CmsPanel;
import com.madalla.wicket.DraggableAjaxBehaviour;
import com.madalla.wicket.DroppableAjaxBehaviour;

class AlbumDisplayPanel extends CmsPanel {
	
	private static final long serialVersionUID = 1L;
	private final static Log log = LogFactory.getLog(AlbumDisplayPanel.class);
	
	private class AlbumForm extends Form<AlbumData>{
		public AlbumForm(String id, IModel<AlbumData> model) {
			super(id);
			
			add(new TextField<String>("title"));
			
			FeedbackPanel intervalFeedback = new FeedbackPanel("intervalFeedback");
			add(intervalFeedback);
			add(new TextField<Integer>("interval").add(new RangeValidator<Integer>(1,30)));
			
			FeedbackPanel heightFeedback = new FeedbackPanel("heightFeedback");
			add(heightFeedback);
			add(new TextField<Integer>("height").add(new RangeValidator<Integer>(50,700)));
			
			FeedbackPanel widthFeedback = new FeedbackPanel("widthFeedback");
			add(widthFeedback);
			add(new TextField<Integer>("width").add(new RangeValidator<Integer>(100,950)));
			
		}

		private static final long serialVersionUID = 1L;
		
	}
	
	public AlbumDisplayPanel(String id, final String albumName) {
		super(id);
		
		final Form<AlbumData> albumForm = new AlbumForm("albumForm", new CompoundPropertyModel<AlbumData>(getAlbum(albumName)));
		add(albumForm);
		
		final Form<Object> form = new Form<Object>("imagesForm");
		
		form.add(new ComponentFeedbackPanel("albumFeedback", form));
		
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
		
		

		IModel<TreeModel> treeModel = new LoadableDetachableModel<TreeModel>(){
			private static final long serialVersionUID = 1L;

			@Override
			protected TreeModel load() {
			    AlbumData album = getRepositoryService().getAlbum(albumName);
				return getAlbumImagesTree(album);
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
				IAlbumData album = getAlbum(albumName);
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
	
	private AlbumData getAlbum(String name){
		return getRepositoryService().getAlbum(name);
	}
	
	private TreeModel getAlbumImagesTree(AlbumData albumData){
	        TreeModel tree;
	        try {
	            tree = getRepositoryService().getAlbumImagesAsTree(albumData);
	        }catch (Exception e){
	            tree = new DefaultTreeModel(new DefaultMutableTreeNode(""));
	            error("Images corrupt");
	        }
	        return tree;
	}

}
