package com.madalla.webapp.images;

import static com.madalla.webapp.scripts.scriptaculous.Scriptaculous.PROTOTYPE;
import static com.madalla.webapp.scripts.utility.ScriptUtils.CROSSFADE;
import static com.madalla.webapp.scripts.utility.ScriptUtils.FAST_INIT;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Page;
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
import org.apache.wicket.request.target.basic.RedirectRequestTarget;

import com.madalla.bo.image.AlbumData;
import com.madalla.bo.image.ImageData;
import com.madalla.webapp.AdminPage;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.login.aware.LoginAwareAdminLink;
import com.madalla.webapp.pages.AlbumAdminPage;
import com.madalla.webapp.panel.CmsPanel;
import com.madalla.webapp.scripts.JavascriptResources;
import com.madalla.webapp.scripts.utility.ScriptUtils;

public class AlbumPanel extends CmsPanel implements IHeaderContributor {
	private static final long serialVersionUID = 1L;
	
    private static final Log log = LogFactory.getLog(AlbumPanel.class);
    
    private WebMarkupContainer container;
    private AlbumData album;
    private boolean navigation = false;
    
    public AlbumPanel(String id, String albumName, Class<? extends Page> returnPage) {
    	this(id, albumName, returnPage, false);
    }

	public AlbumPanel(String id, final String albumName, Class<? extends Page> returnPage, boolean navigation) {
		super(id);

		this.navigation = navigation;
		
		add(JavascriptPackageResource.getHeaderContribution(PROTOTYPE));
		add(JavascriptResources.ANIMATOR);
        add(JavascriptPackageResource.getHeaderContribution(FAST_INIT));
        add(Css.CSS_IMAGE);

        if (navigation) {
        	add(JavascriptPackageResource.getHeaderContribution(CROSSFADE));
        	add(JavascriptPackageResource.getHeaderContribution(ScriptUtils.BANNER));
        	add(ScriptUtils.BANNER_CSS);
        } else {
        	add(JavascriptPackageResource.getHeaderContribution(CROSSFADE));
        	add(ScriptUtils.CROSSFADE_CSS);
        }
        
        album = getRepositoryService().getAlbum(albumName);
        
        add(new LoginAwareAdminLink("adminLink", AlbumAdminPage.class, true, true){
			private static final long serialVersionUID = 1L;

			@Override
			protected AdminPage constructAdminPage(Class<? extends AdminPage> clazz) {
				return new AlbumAdminPage(albumName);
			}
        	
        });
        
        List<ImageData> images = getAlbumImages(album);
        
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
                            tag.put("height", album.getHeight() );
                            tag.put("width", album.getWidth() );
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
	
	private List<ImageData> getAlbumImages(AlbumData albumData){
	    List<ImageData> images;
	    try {
	        images = getRepositoryService().getAlbumImages(albumData);
	    }catch (Exception e){
	        images = Collections.emptyList();
	        error("Images corrupt");
	    }
	    return images;
	}
	

    public void renderHead(IHeaderResponse response) {
        int interval = album.getInterval() ;
        int width = album.getWidth() ;
        int height = album.getHeight() ;
        String params = "{interval:"+interval+", height:"+height+", width: "+ width+"}";
        
        if (navigation) {
        	response.renderOnLoadJavascript("new Banner($('"+ container.getMarkupId() +"')," + params + ")");
        } else {
        	response.renderOnLoadJavascript("new Crossfade($('"+ container.getMarkupId() +"')," + params + ")");
        }
        
        
    }

}
