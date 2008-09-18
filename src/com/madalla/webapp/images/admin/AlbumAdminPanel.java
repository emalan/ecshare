package com.madalla.webapp.images.admin;

import static com.madalla.webapp.images.admin.AlbumParams.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.lang.Bytes;

import com.madalla.service.cms.IRepositoryService;
import com.madalla.service.cms.IRepositoryServiceProvider;
import com.madalla.service.cms.ImageData;

public class AlbumAdminPanel extends Panel{

	private class FileUploadForm extends Form{
		private static final long serialVersionUID = 1L;
		private final Collection uploads = new ArrayList();
		
		public FileUploadForm(String name) {
            super(name);

            setMultiPart(true);
            add(new MultiFileUploadField("fileInput", new PropertyModel(this, "uploads"), 5));
            setMaxSize(Bytes.kilobytes(800));
        }
		
		@Override
		protected void onSubmit() {
            Iterator it = uploads.iterator();
            while (it.hasNext()) {
                final FileUpload upload = (FileUpload)it.next();
                try {
                	String imageName = StringUtils.deleteWhitespace(upload.getClientFileName());
                	imageName = StringUtils.substringBefore(imageName, ".");
					ImageData imageData = new ImageData(album,imageName, upload.getInputStream());
					imageData.save();
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
		protected void populateItem(ListItem listItem) {
			final ImageData imageData = (ImageData)listItem.getModelObject();
            listItem.add(new Label("file", imageData.getName()));
            listItem.add(new Link("delete")
            {
                public void onClick()
                {
                    //Files.remove(imageData);
                    AlbumAdminPanel.this.info("Deleted ");
                }
            });
			
		}
		
	}

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(AlbumAdminPanel.class);
	
	private ImageListView imageListView;
	private final String album;
	
	public AlbumAdminPanel(String id, final PageParameters params) {
		super(id);
		this.album = params.getString(ALBUM);
		
        final FeedbackPanel uploadFeedback = new FeedbackPanel("uploadFeedback");
        add(uploadFeedback);
        final FileUploadForm simpleUploadForm = new FileUploadForm("simpleUpload");
        add(simpleUploadForm);

        // Add folder view
        imageListView = new ImageListView("imageListView", new LoadableDetachableModel() {
			private static final long serialVersionUID = 1L;

			protected Object load() {
                return getRepositoryService().getAlbumImages(album);
            }
        });
        add(imageListView);
		
	}
	
	public IRepositoryService getRepositoryService(){
		IRepositoryServiceProvider provider = (IRepositoryServiceProvider)getApplication();
		return provider.getRepositoryService();
	}
	
	
	
}
