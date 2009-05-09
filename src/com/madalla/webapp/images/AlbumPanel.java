package com.madalla.webapp.images;

import static com.madalla.webapp.images.admin.AlbumParams.ALBUM;
import static com.madalla.webapp.images.admin.AlbumParams.RETURN_PAGE;
import static com.madalla.webapp.scripts.scriptaculous.Scriptaculous.PROTOTYPE;
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
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;

import com.madalla.bo.image.AlbumData;
import com.madalla.bo.image.ImageData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.login.aware.LoggedinBookmarkablePageLink;
import com.madalla.webapp.pages.AlbumAdminPage;
import com.madalla.webapp.scripts.JavascriptResources;
import com.madalla.webapp.scripts.utility.ScriptUtils;

public class AlbumPanel extends Panel implements IHeaderContributor {
	private static final long serialVersionUID = 1L;
	
    private static final Log log = LogFactory.getLog(AlbumPanel.class);
    private static final int INTERVAL = 5;
    private static final int WIDTH = 450;
    private static final int HEIGHT = 325;
    
    private WebMarkupContainer container;
    private AlbumData album;

	public AlbumPanel(String id, String albumName, Class<? extends Page> returnPage) {
		super(id);

		add(JavascriptPackageResource.getHeaderContribution(PROTOTYPE));
		add(JavascriptResources.ANIMATOR);
        add(JavascriptPackageResource.getHeaderContribution(FAST_INIT));
        add(JavascriptPackageResource.getHeaderContribution(CROSSFADE));
        add(JavascriptPackageResource.getHeaderContribution(ScriptUtils.BANNER));
        add(ScriptUtils.BANNER_CSS);
        add(Css.CSS_IMAGE);
        
        album = getRepositoryService().getAlbum(albumName);
        
        add(new LoggedinBookmarkablePageLink("adminLink", AlbumAdminPage.class, 
                new PageParameters(ALBUM +"="+albumName+","+RETURN_PAGE+"="+returnPage.getName()), false, true));
        
        List<ImageData> images = getRepositoryService().getAlbumImages(album);
        
        container  = new WebMarkupContainer("images");
        container.setOutputMarkupId(true);
        add(container);
        container.add(new ListView<ImageData>("image-list", images){
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ImageData> item) {
				final ImageData imageData =  item.getModelObject();
				if (imageData.getImageFull() != null){
					Image image = new Image("id",imageData.getImageFull()){
                        private static final long serialVersionUID = 1L;

                        @Override
                        protected void onComponentTag(ComponentTag tag) {
                            super.onComponentTag(tag);
                            album.getHeight();
                            tag.put("height", album.getHeight() == null ? HEIGHT : album.getHeight());
                            tag.put("width", album.getWidth() == null ? WIDTH : album.getWidth());
                        }
					    
					};

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
					log.warn("Could not get Image from ImageData."+imageData);
				}

			}

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
             
            }
			
			
        });
	}
	
	public IRepositoryService getRepositoryService(){
		IRepositoryServiceProvider provider = (IRepositoryServiceProvider)getApplication();
		return provider.getRepositoryService();
	}

    public void renderHead(IHeaderResponse response) {
        int interval = album.getInterval() == null ? INTERVAL : album.getInterval();
        int width = album.getWidth() == null ? WIDTH : album.getWidth();
        int height = album.getHeight() == null ? HEIGHT : album.getHeight();
        String params = "{interval:"+interval+", height:"+height+", width: "+ width+"}";
        
        response.renderOnLoadJavascript("new Banner($('"+ container.getMarkupId() +"')," + params + ");");
        
    }

}
