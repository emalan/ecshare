package com.madalla.webapp.images.exhibit;

import org.apache.wicket.markup.html.panel.Panel;

import com.madalla.webapp.cms.ContentPanel;
import com.madalla.webapp.images.AlbumPanel;

public class ExhibitPanel extends Panel {

    private static final long serialVersionUID = 1L;
    private static final String CONTENT = "content";
    private static final String NODE = "Exhibit";

    public ExhibitPanel(String id) {
        super(id);
        add(new ContentPanel(CONTENT + "1", id+"first", NODE));
        
        
        Panel album =  new AlbumPanel("nested", id);
        Panel para2 = new ContentPanel(CONTENT + "2", id+"next", NODE, album);
        add(para2);
    }


    
}