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
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;

import com.madalla.service.cms.AbstractImageData;
import com.madalla.service.cms.IRepositoryService;
import com.madalla.service.cms.IRepositoryServiceProvider;
import com.madalla.service.cms.ocm.image.Album;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.pages.AlbumAdminPage;

public class AlbumPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
    private static final Log log = LogFactory.getLog(AlbumPanel.class);

	public AlbumPanel(String id, String albumName, Class<? extends Page> returnPage) {
		super(id);

		add(HeaderContributor.forJavaScript(PROTOTYPE));
        add(HeaderContributor.forJavaScript(EFFECTS));
        add(HeaderContributor.forJavaScript(FAST_INIT));
        add(HeaderContributor.forJavaScript(CROSSFADE));
        add(HeaderContributor.forJavaScript(BANNER));
        add(CSS_IMAGE);
        
        Album album = getRepositoryService().getAlbum(albumName);
        
        Link link = new BookmarkablePageLink("adminLink", AlbumAdminPage.class, new PageParameters(ALBUM +"="+albumName+","+RETURN_PAGE+"="+returnPage.getName())) {
            private static final long serialVersionUID = 1801145612969874170L;

            protected final void onBeforeRender() {
                if (((CmsSession)getSession()).isCmsAdminMode()) {
                    setEnabled(true);
                } else {
                    setEnabled(false);
                }
                super.onBeforeRender();
            }

        };
        link.setAfterDisabledLink("");
        link.setBeforeDisabledLink("");
        add(link);
        
        List<com.madalla.service.cms.ocm.image.Image> images = getRepositoryService().getAlbumImages(album);
        
        add(new ListView("image-list", images){
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				final AbstractImageData imageData = (AbstractImageData) item.getModelObject();
				if (imageData.getFullImageAsResource() != null){
					Image image = new Image("id",imageData.getFullImageAsResource());
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
