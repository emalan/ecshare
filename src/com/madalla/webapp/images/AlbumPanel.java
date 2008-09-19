package com.madalla.webapp.images;

import static com.madalla.webapp.images.admin.AlbumParams.ALBUM;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

import com.madalla.service.cms.IRepositoryService;
import com.madalla.service.cms.IRepositoryServiceProvider;
import com.madalla.service.cms.ImageData;
import com.madalla.webapp.pages.AlbumAdminPage;
import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;

public class AlbumPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
    private static final CompressedResourceReference JS_PROTOTYPE = new CompressedResourceReference(Scriptaculous.class, "prototype.js");
    private static final CompressedResourceReference JS_EFFECTS = new CompressedResourceReference(Scriptaculous.class, "effects.js");
    
    private static final CompressedResourceReference JS_FASTINIT = new CompressedResourceReference(AlbumPanel.class, "fastinit.js");
    private static final CompressedResourceReference JS_CROSSFADE = new CompressedResourceReference(AlbumPanel.class, "crossfade.js");
    private static final CompressedResourceReference JS_BANNER = new CompressedResourceReference(AlbumPanel.class, "banner.js");

    private static final Log log = LogFactory.getLog(AlbumPanel.class);

	public AlbumPanel(String id, String album) {
		super(id);

		add(HeaderContributor.forJavaScript(JS_PROTOTYPE));
        add(HeaderContributor.forJavaScript(JS_EFFECTS));
        add(HeaderContributor.forJavaScript(JS_FASTINIT));
        add(HeaderContributor.forJavaScript(JS_CROSSFADE));
        add(HeaderContributor.forJavaScript(JS_BANNER));
        
        add(new BookmarkablePageLink("adminLink", AlbumAdminPage.class, new PageParameters(ALBUM +"="+album)));
        
        List<ImageData> images = getRepositoryService().getAlbumImages(album);
        
        add(new ListView("image-list", images){
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				final ImageData imageData = (ImageData) item.getModelObject();
				if (imageData.getFullImageAsResource() != null){
					item.add(new Image("id",imageData.getFullImageAsResource()));
				} else {
					log.info("Could not get Image from ImageData."+imageData);
				}

			}
        });
	}
	
	public IRepositoryService getRepositoryService(){
		IRepositoryServiceProvider provider = (IRepositoryServiceProvider)getApplication();
		return provider.getRepositoryService();
	}

}