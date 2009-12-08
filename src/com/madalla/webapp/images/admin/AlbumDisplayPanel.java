package com.madalla.webapp.images.admin;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.validation.validator.RangeValidator;

import com.madalla.bo.image.AlbumData;
import com.madalla.bo.image.ImageData;
import com.madalla.webapp.panel.CmsPanel;
import com.madalla.wicket.DraggableAjaxBehaviour;
import com.madalla.wicket.DroppableAjaxBehaviour;
import com.madalla.wicket.form.AjaxValidationSubmitButton;

class AlbumDisplayPanel extends CmsPanel {
	
	private class AlbumForm extends Form<AlbumData>{
		private static final long serialVersionUID = 1L;

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
		
	}
	private static final long serialVersionUID = 1L;
	
	private final static Log log = LogFactory.getLog(AlbumDisplayPanel.class);
	
	public AlbumDisplayPanel(String id, final String albumName) {
		super(id);
		
		final AlbumData album = getRepositoryService().getAlbum(albumName);
		
		// Album Form
		final Form<AlbumData> albumForm = new AlbumForm("albumForm", new CompoundPropertyModel<AlbumData>(album));
		add(albumForm);
		AjaxButton submitLink = new AjaxValidationSubmitButton("submitLink", albumForm) {

			private static final long serialVersionUID = 1L;

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
		
		// Images Form
		final Form<Object> imagesForm = new Form<Object>("imagesForm");
		imagesForm.setOutputMarkupId(true);
		add(imagesForm);
		imagesForm.add(new ComponentFeedbackPanel("formFeedback", imagesForm).setOutputMarkupId(true));
		
		//Album Image Table
		IModel<List<ImageData>> viewModel = new LoadableDetachableModel<List<ImageData>>(){
			private static final long serialVersionUID = 1L;

			@Override
			protected List<ImageData> load() {
				return getRepositoryService().getAlbumImages(album);
			}

		};
		final ListView<ImageData> dataView = new ListView<ImageData>("dataview",viewModel ){
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ImageData> item) {
				ImageData data = item.getModelObject();
				item.add(new Label("name", data.getName()));
				item.add(new Label("title", data.getTitle()));
				item.add(new Label("url", data.getUrl()));				
			}
			
		};
		imagesForm.add(dataView);
		
		// Drop functionality
		final AbstractDefaultAjaxBehavior dropCallback = new AbstractDefaultAjaxBehavior() {
			private static final long serialVersionUID = 1L;

			protected void respond(final AjaxRequestTarget target) {
				String dragId = DraggableAjaxBehaviour.getDraggablesId(getRequest());
				log.debug("something dropped. arg="+dragId);
		    	getRepositoryService().addImageToAlbum(album, dragId);
		    	imagesForm.info("Success! Image saved to album.");
		    	target.addComponent(dataView);
		    }
		};
		add(new DroppableAjaxBehaviour(dropCallback));
		add(dropCallback);
		
	}
	
}
