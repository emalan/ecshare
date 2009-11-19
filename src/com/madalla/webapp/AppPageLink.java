package com.madalla.webapp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class AppPageLink extends BookmarkablePageLink<Page> {
	
	Log log = LogFactory.getLog(this.getClass());

	@Override
	protected CharSequence getURL() {
		CharSequence url =  super.getURL();
		log.debug(" URL : " + url);
		return url;
	}

	private static final long serialVersionUID = -607174917270219309L;

	public AppPageLink(final String id, final Class<? extends Page> c){
        super(id,c);
        setAfterDisabledLink(id);
        setAutoEnable(true);
    }
}
