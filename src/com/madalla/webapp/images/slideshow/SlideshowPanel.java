package com.madalla.webapp.images.slideshow;

import java.util.List;

import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.madalla.bo.image.ImageData;
import com.madalla.service.IDataService;
import com.madalla.service.IDataServiceProvider;
import com.madalla.webapp.scripts.JavascriptResources;
import com.madalla.webapp.scripts.utility.ScriptUtils;

public class SlideshowPanel extends Panel {

    private static final long serialVersionUID = 1L;
    
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
        }
        
    }

    public SlideshowPanel(String id) {
        super(id);
        add(CSSPackageResource.getHeaderContribution(this.getClass(),"slideshow.css"));
        add(JavascriptResources.PROTOTYPE);
        add(JavascriptPackageResource.getHeaderContribution(ScriptUtils.SLIDESHOW));
        
        // Add folder view
        ImageListView imageListView = new ImageListView("slideshowImage", new LoadableDetachableModel<List<ImageData>>() {
            private static final long serialVersionUID = 1L;

            protected List<ImageData> load() {
                return getRepositoryService().getAlbumOriginalImages();
            }
            
            
        });
        
        WebMarkupContainer listContainer = new WebMarkupContainer("slideshowList");
        listContainer.setOutputMarkupId(true);
        listContainer.add(imageListView);
        add(listContainer);


    }
    
    private IDataService getRepositoryService(){
        IDataServiceProvider provider = (IDataServiceProvider)getApplication();
        return provider.getRepositoryService();
    }

}
