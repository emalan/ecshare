package com.madalla.webapp.images.admin;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;

import com.madalla.bo.image.AlbumData;
import com.madalla.bo.image.IAlbumData;
import com.madalla.bo.image.ImageData;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.images.ImageDefaults;
import com.madalla.webapp.panel.CmsPanel;
import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;
import com.madalla.wicket.DraggableAjaxBehaviour;
import com.madalla.wicket.DroppableAjaxBehaviour;
import com.madalla.wicket.form.AjaxValidationSubmitButton;

public class AlbumAdminPanel extends CmsPanel{
	
	private class FileUploadForm extends Form<Object>{
		private static final long serialVersionUID = 1L;
		
		private final Collection<FileUpload> uploads = new ArrayList<FileUpload>();
		
		public FileUploadForm(String name) {
            super(name);

            setMultiPart(true);
            add(new MultiFileUploadField("fileInput", new PropertyModel<Collection<FileUpload>>(this, "uploads"), 5));
            setMaxSize(ImageDefaults.MAX_UPLOAD_SIZE);
        }
		
		@Override
		protected void onSubmit() {
            Iterator<FileUpload> it = uploads.iterator();
            while (it.hasNext()) {
                final FileUpload upload = it.next();
                try {
                	log.info("file upload - uploading file "+ upload.getClientFileName());
                	
                	String contentType = upload.getContentType();
                	log.info("file upload - Content type="+contentType);
                	if (contentType == null || !(contentType.equalsIgnoreCase("image/png") || contentType.equalsIgnoreCase("image/jpeg"))){
                		log.warn("file upload - Input type not supported. Type="+contentType);
                		warn(getString("error.type", new Model<FileUpload>(upload)));
                		continue;
                	}
                	InputStream inputStream = upload.getInputStream();
                	if (inputStream == null){
                		log.warn("file upload - Input resource invalid.");
                		warn(getString("error.resource", new Model<FileUpload>(upload)));
                		continue;
                	} 
                	String imageName = StringUtils.deleteWhitespace(upload.getClientFileName());
                	imageName = StringUtils.substringBefore(imageName, ".");
                	IAlbumData album = getRepositoryService().getOriginalsAlbum();
                	getRepositoryService().createImage(album, imageName, inputStream);
                	log.info("finished processing upload "+ imageName);
                	info(getString("info.success", new Model<FileUpload>(upload)));
                	
				} catch (Exception e) {
					log.error("onSubmit - failed to upload File."+e.getLocalizedMessage());
					error(getString("error.fail"));
				}
            }
        }
	}
	
	private class ImageListView extends ListView<ImageData>{
		private static final long serialVersionUID = 1L;
		
		private boolean draggable;
		
		public ImageListView(String id, final IModel<List<ImageData>> files, boolean draggable) {
			super(id, files);
			this.draggable = draggable;
		}

		@Override
		protected void populateItem(final ListItem<ImageData> listItem) {
			final ImageData imageData = listItem.getModelObject();
			log.info("ImageListView - populateItem - "+imageData );
			listItem.add(new Label("file", imageData.getName()));
            Image image = new Image("thumb",imageData.getImageThumb());
            image.setOutputMarkupId(true);
            
            if(draggable){
            	image.add(new DraggableAjaxBehaviour(imageData.getName()));
            }
            
            listItem.add(image);
            
            listItem.add(new IndicatingAjaxFallbackLink<Object>("delete") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {
                    AlbumAdminPanel.this.info("Deleting original image. "+imageData);
                    getRepositoryService().deleteNode(imageData.getId());
                    //repaint the container that contains the list
                    target.addComponent(listItem.getParent().getParent());
				}
            });
			listItem.setOutputMarkupId(true);
		}
	}
	
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
	private static final Log log = LogFactory.getLog(AlbumAdminPanel.class);
	
	public AlbumAdminPanel(String id, String albumName) {
		super(id);
		
		add(Css.CSS_FORM);
		add(JavascriptPackageResource.getHeaderContribution(Scriptaculous.PROTOTYPE));
		add(JavascriptPackageResource.getHeaderContribution(Scriptaculous.EFFECTS));
		add(JavascriptPackageResource.getHeaderContribution(Scriptaculous.DRAGDROP));
		
        final FileUploadForm simpleUploadForm = new FileUploadForm("simpleUpload");
        final FeedbackPanel uploadFeedback = new ComponentFeedbackPanel("uploadFeedback",simpleUploadForm);
        simpleUploadForm.add(uploadFeedback);
        simpleUploadForm.add(new SubmitLink("submitLink"));
        add(simpleUploadForm);
        
        final AlbumData album = getRepositoryService().getAlbum(albumName);
        
        // Available Images Display
        WebMarkupContainer availableContainer = new WebMarkupContainer("availableContainer");
        availableContainer.setOutputMarkupId(true);
        add(availableContainer);
        
        ImageListView availableDisplay = new ImageListView("availableDisplay", new LoadableDetachableModel<List<ImageData>>() {
			private static final long serialVersionUID = 1L;

			protected List<ImageData> load() {
			    log.info("ImageListView - load images.");
                return getAvailableImages();
            }
			
			
        }, true);
        availableContainer.add(availableDisplay);
        
        // Album Display
        final WebMarkupContainer albumContainer = new WebMarkupContainer("albumContainer");
        albumContainer.setOutputMarkupId(true);
        add(albumContainer);

        final Form<Object> displayForm = new Form<Object>("displayForm");
        albumContainer.add(displayForm);
        final Component displayFeedback = new ComponentFeedbackPanel("displayFeedback", displayForm).setOutputMarkupId(true);
        displayForm.add(displayFeedback);
        
        
        final ImageListView albumDisplay = new ImageListView("albumDisplay", new LoadableDetachableModel<List<ImageData>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<ImageData> load() {
				return getAlbumImages(album);
			}
			
		}, false);
        displayForm.add(albumDisplay);
        
		// Drop functionality
		final AbstractDefaultAjaxBehavior dropCallback = new AbstractDefaultAjaxBehavior() {
			private static final long serialVersionUID = 1L;

			protected void respond(final AjaxRequestTarget target) {
				String dragId = DraggableAjaxBehaviour.getDraggablesId(getRequest());
				log.debug("something dropped. arg="+dragId);
		    	getRepositoryService().addImageToAlbum(album, dragId);
		    	displayForm.info(getString("message.success"));
		    	target.addComponent(albumContainer);
		    	target.addComponent(displayFeedback);
		    }
		};
		albumContainer.add(new DroppableAjaxBehaviour(dropCallback));
		albumContainer.add(dropCallback);
		
		// Album Form
		final Form<AlbumData> albumForm = new AlbumForm("albumForm", new CompoundPropertyModel<AlbumData>(album));
		albumContainer.add(albumForm);
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
	}
	
	private List<ImageData> getAvailableImages() {
        List<ImageData> images;
        try {
            images = getRepositoryService().getAlbumOriginalImages();
        } catch (Exception e) {
            images = Collections.emptyList();
            error("Images corrupt");
        }
        return images;
    }
	
	private List<ImageData> getAlbumImages(AlbumData album) {
        List<ImageData> images;
        try {
            images = getRepositoryService().getAlbumImages(album);
        } catch (Exception e) {
            images = Collections.emptyList();
            error(getString("message.image.error"));
        }
        return images;
    }
	
}
