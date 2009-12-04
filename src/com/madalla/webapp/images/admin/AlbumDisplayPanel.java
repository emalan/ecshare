package com.madalla.webapp.images.admin;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.tree.table.AbstractTreeColumn;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation;
import org.apache.wicket.extensions.markup.html.tree.table.IColumn;
import org.apache.wicket.extensions.markup.html.tree.table.TreeTable;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Alignment;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Unit;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.EmptyDataProvider;
import org.apache.wicket.markup.repeater.data.IDataProvider;
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
import com.madalla.wicket.form.AjaxValidationSubmitButton;

class AlbumDisplayPanel extends CmsPanel {
	
	private static final long serialVersionUID = 1L;
	private final static Log log = LogFactory.getLog(AlbumDisplayPanel.class);
	
	private class AlbumForm extends Form<AlbumData>{
		public AlbumForm(String id, IModel<AlbumData> model) {
			super(id, model);
			
			add(new TextField<String>("title").setOutputMarkupId(true));
			
			Component interval = new RequiredTextField<Integer>("interval").add(new RangeValidator<Integer>(1,30));
			interval.setOutputMarkupId(true);
			add(new ComponentFeedbackPanel("intervalFeedback",interval).setOutputMarkupId(true));
			add(interval);
			
			Component height = new RequiredTextField<Integer>("height").add(new RangeValidator<Integer>(50,700));
			height.setOutputMarkupId(true);
			add(new ComponentFeedbackPanel("heightFeedback", height).setOutputMarkupId(true));
			add(height);

			Component width = new RequiredTextField<Integer>("width").add(new RangeValidator<Integer>(100,950));
			width.setOutputMarkupId(true);
			add(new ComponentFeedbackPanel("widthFeedback", width).setOutputMarkupId(true));
			add(width);
			
		}

		private static final long serialVersionUID = 1L;
		
	}
	
	public AlbumDisplayPanel(String id, final String albumName) {
		super(id);
		
		final Form<AlbumData> albumForm = new AlbumForm("albumForm", new CompoundPropertyModel<AlbumData>(getAlbum(albumName)));
		add(albumForm);
		AjaxButton submitLink = new AjaxValidationSubmitButton("submitLink", albumForm) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				saveData((AlbumData) form.getModelObject());
				form.info(getString("message.success"));
			}

		};
		submitLink.setOutputMarkupId(true);
		albumForm.add(submitLink);
		albumForm.add(new ComponentFeedbackPanel("albumFeedback", albumForm).setOutputMarkupId(true));
		
		final Form<Object> form = new Form<Object>("imagesForm");
		
		form.add(new ComponentFeedbackPanel("formFeedback", form).setOutputMarkupId(true));
		
		//Album Image Table
		IDataProvider<ImageData> provider = new EmptyDataProvider<ImageData>();
		final DataView<ImageData> dataView = new DataView<ImageData>("dataview",provider ){
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<ImageData> item) {
				ImageData data = item.getModelObject();
				item.add(new Label("name", data.getName()));
				item.add(new Label("title", data.getTitle()));
				item.add(new Label("url", data.getUrl()));
			}
			
		};
		
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
