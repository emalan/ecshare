package com.madalla.webapp.images.admin;

import static com.madalla.webapp.images.admin.AlbumParams.ALBUM;
import static com.madalla.webapp.images.admin.AlbumParams.RETURN_PAGE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.string.JavascriptUtils;

import com.madalla.service.cms.AbstractImageData;
import com.madalla.service.cms.IRepositoryAdminService;
import com.madalla.service.cms.IRepositoryAdminServiceProvider;
import com.madalla.service.cms.IRepositoryService;
import com.madalla.service.cms.IRepositoryServiceProvider;
import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;

public class AlbumAdminPanel extends Panel{
	private static Bytes MAX_FILE_SIZE = Bytes.kilobytes(2000);
	
	private class FileUploadForm extends Form{
		private static final long serialVersionUID = 1L;
		
		private final Collection<FileUpload> uploads = new ArrayList<FileUpload>();
		
		public FileUploadForm(String name) {
            super(name);

            setMultiPart(true);
            add(new MultiFileUploadField("fileInput", new PropertyModel(this, "uploads"), 5));
            setMaxSize(MAX_FILE_SIZE);
        }
		
		@Override
		protected void onSubmit() {
            Iterator<FileUpload> it = uploads.iterator();
            while (it.hasNext()) {
                final FileUpload upload = it.next();
                try {
                	log.info("uploading file "+ upload.getClientFileName());
                	String imageName = StringUtils.deleteWhitespace(upload.getClientFileName());
                	imageName = StringUtils.substringBefore(imageName, ".");
                	getRepositoryAdminService().createOriginalImage(imageName, upload.getInputStream());
					log.info("finished processing upload "+ imageName);
				} catch (IOException e) {
					log.error("onSubmit - failed to upload File."+e.getLocalizedMessage());
					AlbumAdminPanel.this.error("Failed to upload images.");
				}
            }
        }
	}
	
	private class ImageListView extends ListView{
		private static final long serialVersionUID = 1L;

		public ImageListView(String id, final IModel files) {
			super(id, files);
		}

		@Override
		protected void populateItem(final ListItem listItem) {
			final AbstractImageData imageData = (AbstractImageData)listItem.getModelObject();
            listItem.add(new Label("file", imageData.getName()));
            Image image = new Image("thumb",imageData.getThumbnail()){

				@Override
				protected void onRender(MarkupStream markupStream) {
					super.onRender(markupStream);
					String s = "var e = $('"+getMarkupId()+"'); new Draggable(e,{ghosting: true, revert: true});"+
					"e.setStyle({ cursor: 'move'}); e.addClassName('draggable')";
					JavascriptUtils.writeJavascript(getResponse(), s);
				}
            	
            };
            image.setOutputMarkupId(true);
            
            listItem.add(image);
            listItem.add(new IndicatingAjaxFallbackLink("delete") {
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
	
	
	public AlbumAdminPanel(String id, final PageParameters params) {
		super(id);
		
		add(HeaderContributor.forJavaScript(Scriptaculous.PROTOTYPE));
		add(HeaderContributor.forJavaScript(Scriptaculous.EFFECTS));
		add(HeaderContributor.forJavaScript(Scriptaculous.DRAGDROP));
		//add(HeaderContributor.forJavaScript(ScriptUtils.DRAG_DROP));
		
		String album = params.getString(ALBUM); //use this to default the dropdown
		Class<? extends Page> returnPage = null;
		try {
			String pageString = params.getString(RETURN_PAGE);
            returnPage = (Class<? extends Page>) Class.forName(pageString);
        } catch (ClassNotFoundException e) {
            log.error("constructor - Exception while getting return Class.", e);
        } catch (ClassCastException e) {
        	log.error("constructor - Exception while casting return Class.", e);
        }
		add(new PageLink("returnLink", returnPage));
		
		
        final FileUploadForm simpleUploadForm = new FileUploadForm("simpleUpload");
        final FeedbackPanel uploadFeedback = new FeedbackPanel("uploadFeedback");
        simpleUploadForm.add(uploadFeedback);
        add(simpleUploadForm);
        add(new AlbumDisplayPanel("albumDisplay", album));
        
        // Add folder view
        imageListView = new ImageListView("imageListView", new LoadableDetachableModel() {
			private static final long serialVersionUID = 1L;

			protected Object load() {
                return getRepositoryService().getAlbumOriginalImages();
            }
			
			
        });
        WebMarkupContainer listContainer = new WebMarkupContainer("listContainer");
        listContainer.setOutputMarkupId(true);
        listContainer.add(imageListView);
        add(listContainer);
		
	}
	
	private IRepositoryService getRepositoryService(){
		IRepositoryServiceProvider provider = (IRepositoryServiceProvider)getApplication();
		return provider.getRepositoryService();
	}
	
	private IRepositoryAdminService getRepositoryAdminService(){
		IRepositoryAdminServiceProvider provider = (IRepositoryAdminServiceProvider)getApplication();
		return provider.getRepositoryAdminService();
	}
	
}
