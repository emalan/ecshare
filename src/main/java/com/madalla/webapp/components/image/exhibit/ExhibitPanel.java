package com.madalla.webapp.components.image.exhibit;

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.emalan.cms.bo.image.AlbumData;
import org.emalan.cms.bo.image.ImageData;

import com.madalla.webapp.cms.ContentPanel;
import com.madalla.webapp.components.image.album.AlbumPanel;

public class ExhibitPanel extends Panel {

    private static final long serialVersionUID = 1L;
    private static final String CONTENT = "content";
    private static final String NODE = "Exhibit";

    public ExhibitPanel(String id, AlbumData album, IModel<List<ImageData>> imagesModel) {
        super(id);
        add(new ContentPanel(CONTENT + "1", id+"first", NODE));


        Panel albumPanel =  new AlbumPanel("nested", album, imagesModel);
        Panel para2 = new ContentPanel(CONTENT + "2", id+"next", NODE, albumPanel);
        add(para2);
    }



}
