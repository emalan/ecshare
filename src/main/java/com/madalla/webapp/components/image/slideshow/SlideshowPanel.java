package com.madalla.webapp.components.image.slideshow;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.emalan.cms.bo.image.AlbumData;
import org.emalan.cms.bo.image.ImageData;

import com.madalla.webapp.CmsPanel;
import com.madalla.webapp.admin.pages.AdminPageLink;
import com.madalla.webapp.admin.pages.AlbumAdminPage;
import com.madalla.webapp.scripts.utility.ScriptUtils;

public class SlideshowPanel extends CmsPanel implements IHeaderContributor {

    private static final long serialVersionUID = 1L;
    //private WebMarkupContainer listContainer;

    private class ImageListView extends ListView<ImageData> {

        private static final long serialVersionUID = 1L;

        public ImageListView(String id, IModel<List<ImageData>> model) {
            super(id, model);
        }

        @Override
        protected void populateItem(final ListItem<ImageData> listItem) {
            ImageData imageData = listItem.getModelObject();
            BufferedImage bufferedImage = imageData.getImageThumb();
            BufferedDynamicImageResource imageResource = new BufferedDynamicImageResource();
            imageResource.setImage(bufferedImage);
            Image image = new Image("thumb",imageResource);
            listItem.add(image);
            image.add(new AttributeModifier("alt", new Model<String>(imageData.getTitle())));
            //DynamicImageResource fullsize = imageData.getImageFull();
            listItem.add(new Label("fullsize", new Model<String>(imageData.getMountUrl())));
        }

    }

    public SlideshowPanel(String id, final String albumName) {
        super(id);

        //link to album configure page
        Component adminPageLink;
        PageParameters params = new PageParameters();
        params.set(0, albumName);
        add(adminPageLink = new AdminPageLink("adminLink", AlbumAdminPage.class, params));
        MetaDataRoleAuthorizationStrategy.authorize(adminPageLink, ENABLE, "ADMIN");

        final AlbumData album = getRepositoryService().getAlbum(albumName);

        // Add folder view
        ImageListView imageListView = new ImageListView("slideshowImage", new LoadableDetachableModel<List<ImageData>>() {
            private static final long serialVersionUID = 1L;

            @Override
			protected List<ImageData> load() {
                return getAlbumImages(album);
            }


        });

        //listContainer = new WebMarkupContainer("slideshow");
        //listContainer.setOutputMarkupId(true);
        //add(listContainer);

        add(imageListView);


        //listContainer.add(new AttributeAppender("style", true, new Model<String>("display:none"), ";"));
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
		response.render(CssHeaderItem.forReference(ScriptUtils.SLIDESHOW_CSS));
		response.render(JavaScriptHeaderItem.forReference(ScriptUtils.SLIDESHOW));
        
		//response.renderOnLoadJavascript("var slideshow = new TINY.slideshow('slideshow'); TINY.load();");

	}

}
