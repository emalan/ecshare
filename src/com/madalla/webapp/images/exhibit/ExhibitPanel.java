package com.madalla.webapp.images.exhibit;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.panel.Panel;

import com.madalla.webapp.cms.ContentPanel;
import com.madalla.webapp.panel.Panels;

public class ExhibitPanel extends Panel {

    private static final long serialVersionUID = 1L;
    private static final String CONTENT = "content";
    private static final String NODE = "Exhibit";

    public ExhibitPanel(String id, Class<? extends Page> returnPage) {
        super(id);
        add(new ContentPanel(CONTENT + "1", id+"first", NODE, returnPage));
        
        Panel album = Panels.albumPanel("nested", id, returnPage);
        Panel para2 = new ContentPanel(CONTENT + "2", id+"next", NODE, returnPage, album);
        add(para2);
    }


    
}
