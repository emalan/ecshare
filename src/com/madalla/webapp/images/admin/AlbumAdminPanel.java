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
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.madalla.bo.image.IAlbumData;
import com.madalla.bo.image.ImageData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.images.ImageDefaults;
import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;
import com.madalla.wicket.DraggableAjaxBehaviour;

public class AlbumAdminPanel extends Panel{
	
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

		public ImageListView(String id, final IModel<List<ImageData>> files) {
			super(id, files);
		}

		@Override
		protected void populateItem(final ListItem<ImageData> listItem) {
			final ImageData imageData = listItem.getModelObject();
			log.info("ImageListView - populateItem - "+imageData );
			listItem.add(new Label("file", imageData.getName()));
            Image image = new Image("thumb",imageData.getImageThumb());
            image.setOutputMarkupId(true);
            image.add(new DraggableAjaxBehaviour(imageData.getName()));
            
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

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(AlbumAdminPanel.class);
	private ImageListView imageListView;
	
	
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
        add(new AlbumDisplayPanel("albumDisplay", albumName));
        
        // Add folder view
        imageListView = new ImageListView("imageListView", new LoadableDetachableModel<List<ImageData>>() {
			private static final long serialVersionUID = 1L;

			protected List<ImageData> load() {
			    log.info("ImageListView - load images.");
                return getAlbumImages();
            }
			
			
        });
        WebMarkupContainer listContainer = new WebMarkupContainer("listContainer");
        listContainer.setOutputMarkupId(true);
        listContainer.add(imageListView);
        add(listContainer);
		
	}
	
	private List<ImageData> getAlbumImages() {
        List<ImageData> images;
        try {
            images = getRepositoryService().getAlbumOriginalImages();
        } catch (Exception e) {
            images = Collections.emptyList();
            error("Images corrupt");
        }
        return images;
    }
	
	private IRepositoryService getRepositoryService(){
		IRepositoryServiceProvider provider = (IRepositoryServiceProvider)getApplication();
		return provider.getRepositoryService();
	}
	
}
