package com.madalla.webapp.images;

import java.util.List;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;

public class ImageAlbumPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
    private static final CompressedResourceReference JS_PROTOTYPE = new CompressedResourceReference(Scriptaculous.class, "prototype.js");
    private static final CompressedResourceReference JS_EFFECTS = new CompressedResourceReference(Scriptaculous.class, "effects.js");
    
    private static final CompressedResourceReference JS_FASTINIT = new CompressedResourceReference(ImageAlbumPanel.class, "fastinit.js");
    private static final CompressedResourceReference JS_CROSSFADE = new CompressedResourceReference(ImageAlbumPanel.class, "crossfade.js");
    private static final CompressedResourceReference JS_BANNER = new CompressedResourceReference(ImageAlbumPanel.class, "banner.js");


	public ImageAlbumPanel(String id, List<String> list) {
		super(id);

		add(HeaderContributor.forJavaScript(JS_PROTOTYPE));
        add(HeaderContributor.forJavaScript(JS_EFFECTS));
        add(HeaderContributor.forJavaScript(JS_FASTINIT));
        add(HeaderContributor.forJavaScript(JS_CROSSFADE));
        add(HeaderContributor.forJavaScript(JS_BANNER));
        
        
        
        add(new ListView("image-list", list){
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem item) {
				final String src = (String) item.getModelObject();
				item.add(new Image("id",src));
				
			}
        	
        });

	}

}
