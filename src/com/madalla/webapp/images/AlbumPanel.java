package com.madalla.webapp.images;

import static com.madalla.webapp.scripts.scriptaculous.Scriptaculous.PROTOTYPE;
import static com.madalla.webapp.scripts.utility.ScriptUtils.CROSSFADE;
import static com.madalla.webapp.scripts.utility.ScriptUtils.FAST_INIT;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
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

public class AlbumPanel extends CmsPanel {
	private static final long serialVersionUID = 1L;
	
    private static final Log log = LogFactory.getLog(AlbumPanel.class);
    
    public AlbumPanel(String id, String albumName) {
    	this(id, albumName, false);
    }

	public AlbumPanel(String id, final String albumName, final boolean navigation) {
		super(id);

        add(Css.CSS_IMAGE);

        //link to album configure page
        add(new LoginAwareAdminLink("adminLink", AlbumAdminPage.class, true, true){
			private static final long serialVersionUID = 1L;

			@Override
			protected AdminPage constructAdminPage(Class<? extends AdminPage> clazz) {
				return new AlbumAdminPage(albumName);
			}
        	
        });
        
        final AlbumData album = getRepositoryService().getAlbum(albumName);
        final List<ImageData> images = getAlbumImages(album);

        final WebMarkupContainer imageListContainer  = new WebMarkupContainer("images");
        imageListContainer.setOutputMarkupId(true);
        add(imageListContainer);

        if (images.size() <= 1) {
        	//one or less images scenario
        	
        	WebMarkupContainer list = new WebMarkupContainer("image-list");
        	imageListContainer.add(list);
        	
        	if (images.isEmpty()){
        		ResourceReference emptyImage = new ResourceReference(this.getClass(), "images.png");
        		list.add(createImageWrapper(album).add(new Image("id", emptyImage )));
        		list.add(new Label("imageLabel",getString("label.empty")));
        	} else {
        		final ImageData imageData = images.get(0);
        		list.add(createImage(album, imageData));
        		list.add(new Label("imageLabel", imageData.getTitle()));
        	}
        	
        } else {
    		add(JavascriptPackageResource.getHeaderContribution(PROTOTYPE));
    		add(JavascriptPackageResource.getHeaderContribution(JavascriptResources.ANIMATOR));
            add(JavascriptPackageResource.getHeaderContribution(FAST_INIT));
            
        	if (navigation) {
        		add(JavascriptPackageResource.getHeaderContribution(CROSSFADE));
        		add(JavascriptPackageResource.getHeaderContribution(ScriptUtils.BANNER));
        		add(ScriptUtils.BANNER_CSS);
        	} else {
        		add(JavascriptPackageResource.getHeaderContribution(CROSSFADE));
        		add(ScriptUtils.CROSSFADE_CSS);
        	}
        
        	imageListContainer.add(new ListView<ImageData>("image-list", images) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<ImageData> item) {
					final ImageData imageData = item.getModelObject();
					if (imageData.getImageFull() != null) {
						item.add(createImage(album, imageData));
					} else {
						log.warn("Could not get Image from ImageData." + imageData);
					}
					item.add(new Label("imageLabel", imageData.getTitle()));

				}

				@Override
				protected void onComponentTag(ComponentTag tag) {
					super.onComponentTag(tag);
				}
				
				@Override
				public void renderHead(HtmlHeaderContainer container) {
					int interval = album.getInterval() ;
			        int width = album.getWidth() ;
			        int height = album.getHeight() ;
			        String params = "{interval:"+interval+", height:"+height+", width: "+ width+"}";
			        
			        if (navigation) {
			        	container.getHeaderResponse().renderOnLoadJavascript("new Banner($('"+ imageListContainer.getMarkupId() +"')," + params + ")");
			        } else {
			        	container.getHeaderResponse().renderOnLoadJavascript("new Crossfade($('"+ imageListContainer.getMarkupId() +"')," + params + ")");
			        }
					super.renderHead(container);
				}


			});
        
        }
	}
	
	private WebMarkupContainer createImageWrapper(final AlbumData album){
		return new WebMarkupContainer("imageWrapper"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("style", "height: "+ (album.getHeight()+ 15)+"px; width: "+ (album.getWidth()+15)+"px;");
			}
    	};
	}

	private Component createImage(final AlbumData album, final ImageData imageData){
		WebMarkupContainer imageWrapper = createImageWrapper(album);
		
    	Image image = new Image("id", imageData.getImageFull()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				tag.put("height", album.getHeight());
				tag.put("width", album.getWidth());
			}

		};

		if (StringUtils.isEmpty(imageData.getUrl())) {
			// TODO create a link to URL
			image.add(new AjaxEventBehavior("onclick") {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onEvent(AjaxRequestTarget target) {
					getRequestCycle().setRequestTarget(new RedirectRequestTarget(imageData.getUrl()));
				}
			});
		}
		imageWrapper.add(image);
		return imageWrapper;
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

}
