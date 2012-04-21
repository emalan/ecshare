package com.madalla.webapp.admin.image;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madalla.bo.image.IAlbumData;
import com.madalla.bo.image.ImageData;
import com.madalla.service.IDataService;
import com.madalla.webapp.CmsPanel;
import com.madalla.webapp.components.image.album.ImageDefaults;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;
import com.madalla.webapp.upload.FileUploadGroup;
import com.madalla.webapp.upload.FileUploadThread;
import com.madalla.webapp.upload.IFileUploadInfo;
import com.madalla.webapp.upload.IFileUploadProcess;

public class ImageAdminPanel extends CmsPanel{

	private abstract class FileUploadForm extends Form<Object>{
		private static final long serialVersionUID = 1L;

		private final Collection<FileUpload> uploads = new ArrayList<FileUpload>();

		public FileUploadForm(String name) {
            super(name);

            add(new MultiFileUploadField("fileInput", new PropertyModel<Collection<FileUpload>>(this, "uploads"), 5){
				private static final long serialVersionUID = 1L;

				//Hack to prevent fileUploads being trashed, we will close them ourselves....
            	@Override
            	protected void onDetach()
            	{
            		setConvertedInput(null);

            		super.onDetach();
            	}
            });
            setMaxSize(ImageDefaults.MAX_UPLOAD_SIZE);

            final Component uploadFeedback = new ComponentFeedbackPanel("uploadFeedback", this);
            uploadFeedback.setOutputMarkupId(true);
            add(uploadFeedback);

            add(new SubmitLink("formSubmit"));

        }

		@Override
		protected void onSubmit() {
            Iterator<FileUpload> it = uploads.iterator();
            while (it.hasNext()) {
                final FileUpload fileUpload = it.next();
                try {
                	log.info("Submit file for uploading: "+ fileUpload.getClientFileName());

                	String contentType = fileUpload.getContentType();
                	log.info("file upload - Content type="+contentType);
                	if (contentType == null || !(contentType.equalsIgnoreCase("image/png") || contentType.equalsIgnoreCase("image/jpeg"))){
                		log.warn("file upload - Input type not supported. Type="+contentType);
                		warn(getString("error.type", new Model<FileUpload>(fileUpload)));
                		continue;
                	}

                	//TODO check for existing upload, so we can display message

                	//Prepare Thread for uploading
                	String imageName = StringUtils.deleteWhitespace(fileUpload.getClientFileName());
                	IFileUploadInfo uploadInfo = (IFileUploadInfo)getSession();
                	IFileUploadProcess process = new ImageUploadProcess(getRepositoryService());

                	final Thread submit = new FileUploadThread(uploadInfo, fileUpload, process,imageName, GROUP);
                	submit.start();

                	log.info("finished submitting file for uploading: "+ imageName);
                	info(getString("message.success", new Model<FileUpload>(fileUpload)));

				} catch (Exception e) {
					log.error("onSubmit - failed to upload File."+e.getLocalizedMessage());
					error(getString("message.fail"));
				}
            }
        }

		protected abstract void refreshImageList(AjaxRequestTarget target);
	}

	private class ImageUploadProcess implements IFileUploadProcess {

		final private IDataService service;

		public ImageUploadProcess(IDataService service){
			this.service = service;
		}
		public void execute(InputStream inputStream, String fileName) {

        	String imageName = StringUtils.deleteWhitespace(fileName);
        	imageName = StringUtils.substringBefore(imageName, ".");
        	IAlbumData album = service.getOriginalsAlbum();
        	service.createImage(album, imageName, inputStream);
		}

	}

	private class FileUploadListView extends ListView<String>{
		private static final long serialVersionUID = 1L;
		List<String> files;

		public FileUploadListView(String id) {
			super(id);

		}

		@Override
		protected void populateItem(ListItem<String> item) {
			item.add(new AjaxIndicatingStatusLabel("file", item.getModelObject()));
		}

		@Override
		protected void onBeforeRender() {
			setList(((IFileUploadInfo)getSession()).getFileUploadStatus(GROUP));
			super.onBeforeRender();
		}

	}

	private class AjaxIndicatingStatusLabel extends Label{
		private static final long serialVersionUID = 1L;

		private final AjaxIndicatorAppender indicatorAppender = new AjaxIndicatorAppender();
		private final String uploadId;

		public AjaxIndicatingStatusLabel(final String id, final String text){
			super(id, text);
			this.uploadId = text;
			add(indicatorAppender);
			add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(3)){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onPostProcessTarget(AjaxRequestTarget target) {
					if (getStatus(text)){
						target.appendJavaScript("wicketShow('" + indicatorAppender.getMarkupId() +"');");
					} else {
						target.appendJavaScript("wicketHide('" + indicatorAppender.getMarkupId() +"');");
					}

				}
			});
		}

		@Override
		public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
			String text = getDefaultModelObjectAsString();
			String status = getStatusText(uploadId);
			replaceComponentTagBody(markupStream, openTag, text + "<em>" + status + "</em>");
		}

		private String getStatusText(String id){
			if (getStatus(id)){
	        	return getString("message.uploading");
	        } else {
	        	return getString("message.uploadcomplete");
	        }
		}

		private boolean getStatus(String id){
			return ((IFileUploadInfo)getSession()).getFileUploadStatus(id).isUploading();
		}
	}

	private class ImageListView extends ListView<ImageData>{
		private static final long serialVersionUID = 1L;

		public ImageListView(String id, final IModel<List<ImageData>> files) {
			super(id, files);
		}

		@Override
		protected void populateItem(final ListItem<ImageData> listItem) {
			final ImageData imageData = listItem.getModelObject();
			log.info("ImageListView - populateItem - "+imageData );
			listItem.add(new Label("file", imageData.getName()));
            Image image = new NonCachingImage("thumb",imageData.getImageThumb());
            image.setOutputMarkupId(true);

            listItem.add(image);

            listItem.add(new IndicatingAjaxFallbackLink<Object>("delete") {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {
                    getRepositoryService().deleteNode(imageData.getId());
                    //repaint the container that contains the list
                    target.addComponent(listItem.getParent().getParent());
				}
            });
			listItem.setOutputMarkupId(true);
		}



	}

	private static final long serialVersionUID = 981631179962049451L;
	private static final Logger log = LoggerFactory.getLogger(ImageAdminPanel.class);
	private static final String IMAGE_FILE_UPLOAD_GROUP = "ImageAdmin";
	private static final FileUploadGroup GROUP = new FileUploadGroup(IMAGE_FILE_UPLOAD_GROUP);

	public ImageAdminPanel(String id) {
		super(id);

	    //Images Display
        final WebMarkupContainer availableContainer = new WebMarkupContainer("availableContainer");
        availableContainer.setOutputMarkupId(true);
        add(availableContainer);

        add(new ComponentFeedbackPanel("availableFeedback",availableContainer));

        final ImageListView availableDisplay = new ImageListView("availableDisplay",
				new LoadableDetachableModel<List<ImageData>>() {
					private static final long serialVersionUID = 1L;

					@Override
					protected List<ImageData> load() {
						log.info("ImageListView - load images.");
						List<ImageData> images;
						try {
							images = getRepositoryService().getAlbumOriginalImages();
						} catch (Exception e) {
							log.error("Exception while Loading Original Images.", e);
							images = Collections.emptyList();
							availableContainer.error(getString("message.display.fail"));
						}
						return images;

					}

				});
        availableDisplay.setOutputMarkupId(true);
        availableContainer.add(availableDisplay);

        add(new AjaxLink<String>("refreshList"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				target.addComponent(availableContainer);
			}

        });

        // File upload
        final FileUploadForm simpleUploadForm = new FileUploadForm("simpleUpload"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void refreshImageList(AjaxRequestTarget target) {
				target.addComponent(availableContainer);
			}

        };

        add(simpleUploadForm);

        add(new FileUploadListView("results"));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		response.renderCSSReference(Css.CSS_FORM);
		response.renderJavaScriptReference(Scriptaculous.PROTOTYPE);
		response.renderJavaScriptReference(Scriptaculous.EFFECTS);
		response.renderJavaScriptReference(Scriptaculous.DRAGDROP);
		
	}


}
