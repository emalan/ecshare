package com.madalla.webapp;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class AppPageLink extends BookmarkablePageLink {

	private static final long serialVersionUID = -607174917270219309L;

	public AppPageLink(final String id, final Class c){
        super(id,c);
        setAfterDisabledLink(id);
        setAutoEnable(true);
    }
}
