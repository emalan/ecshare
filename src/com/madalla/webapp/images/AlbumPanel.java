package com.madalla.webapp.images;

import static com.madalla.webapp.css.Css.CSS_IMAGE;
import static com.madalla.webapp.images.admin.AlbumParams.ALBUM;
import static com.madalla.webapp.images.admin.AlbumParams.RETURN_PAGE;
import static com.madalla.webapp.scripts.scriptaculous.Scriptaculous.EFFECTS;
import static com.madalla.webapp.scripts.scriptaculous.Scriptaculous.PROTOTYPE;
import static com.madalla.webapp.scripts.utility.ScriptUtils.BANNER;
import static com.madalla.webapp.scripts.utility.ScriptUtils.CROSSFADE;
import static com.madalla.webapp.scripts.utility.ScriptUtils.FAST_INIT;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;

import com.madalla.bo.image.AlbumData;
import com.madalla.bo.image.ImageData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.webapp.login.aware.LoggedinBookmarkablePageLink;
import com.madalla.webapp.pages.AlbumAdminPage;

public class AlbumPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
    private static final Log log = LogFactory.getLog(AlbumPanel.class);

	public AlbumPanel(String id, String albumName, Class<? extends Page> returnPage) {
		super(id);

		add(JavascriptPackageResource.getHeaderContribution(PROTOTYPE));
        add(JavascriptPackageResource.getHeaderContribution(EFFECTS));
        add(JavascriptPackageResource.getHeaderContribution(FAST_INIT));
        add(JavascriptPackageResource.getHeaderContribution(CROSSFADE));
        add(JavascriptPackageResource.getHeaderContribution(BANNER));
        add(CSS_IMAGE);
        
        AlbumData album = getRepositoryService().getAlbum(albumName);
        
        add(new LoggedinBookmarkablePageLink("adminLink", AlbumAdminPage.class, 
                new PageParameters(ALBUM +"="+albumName+","+RETURN_PAGE+"="+returnPage.getName()), true));
        
        List<ImageData> images = getRepositoryService().getAlbumImages(album);
        
        add(new ListView<ImageData>("image-list", images){
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ImageData> item) {
				final com.madalla.cms.bo.impl.ocm.image.Image imageData = (com.madalla.cms.bo.impl.ocm.image.Image) item.getModelObject();
				if (imageData.getImageFull() != null){
					Image image = new Image("id",imageData.getImageFull());
					if(StringUtils.isEmpty(imageData.getUrl())){
						//TODO create a link to URL
						image.add(new AjaxEventBehavior("onclick"){
							private static final long serialVersionUID = 1L;
							@Override
							protected void onEvent(AjaxRequestTarget target) {
								getRequestCycle().setRequestTarget(new RedirectRequestTarget(imageData.getUrl()));
							}
						});
					}
					item.add(image);
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
