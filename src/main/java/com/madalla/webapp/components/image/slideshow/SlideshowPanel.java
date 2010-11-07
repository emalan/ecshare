package com.madalla.webapp.components.image.slideshow;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import com.madalla.bo.image.ImageData;
import com.madalla.service.IDataService;
import com.madalla.service.IDataServiceProvider;
import com.madalla.webapp.scripts.utility.ScriptUtils;

public class SlideshowPanel extends Panel implements IHeaderContributor {

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
            Image image = new Image("thumb",imageData.getImageThumb());
            listItem.add(image);
            image.add(new AttributeModifier("alt", true, new Model<String>(imageData.getTitle())));
            DynamicImageResource fullsize = imageData.getImageFull();
            //TODO resource references
            listItem.add(new Label("fullsize", new Model<String>("resource" + "/original/full/" + imageData.getName())));
        }
        
    }

    public SlideshowPanel(String id) {
        super(id);
        add(ScriptUtils.SLIDESHOW_CSS);
        add(JavascriptPackageResource.getHeaderContribution(ScriptUtils.SLIDESHOW));
        
        // Add folder view
        ImageListView imageListView = new ImageListView("slideshowImage", new LoadableDetachableModel<List<ImageData>>() {
            private static final long serialVersionUID = 1L;

            protected List<ImageData> load() {
                return getAlbumImages();
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
    
    private IDataService getRepositoryService(){
        IDataServiceProvider provider = (IDataServiceProvider)getApplication();
        return provider.getRepositoryService();
    }

	public void renderHead(IHeaderResponse response) {
		//response.renderOnLoadJavascript("var slideshow = new TINY.slideshow('slideshow'); TINY.load();");
		
	}

}
