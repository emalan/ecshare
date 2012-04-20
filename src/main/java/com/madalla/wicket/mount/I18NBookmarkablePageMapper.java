package com.madalla.wicket.mount;

import java.util.Locale;

import org.apache.wicket.Page;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.BookmarkableMapper;

public class I18NBookmarkablePageMapper extends BookmarkableMapper{

	//TODO implement this
	public I18NBookmarkablePageMapper(Locale locale, String string, Class<? extends Page> class1) {
		
	}
	public IRequestHandler mapRequest(Request request) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getCompatibilityScore(Request request) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Url mapHandler(IRequestHandler requestHandler) {
		// TODO Auto-generated method stub
		return null;
	}

}
