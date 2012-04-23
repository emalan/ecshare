package com.madalla.wicket.mount;

import java.util.Locale;

import org.apache.wicket.Session;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.BookmarkableMapper;
import org.apache.wicket.request.mapper.parameter.IPageParametersEncoder;
import org.apache.wicket.request.mapper.parameter.PageParametersEncoder;
import org.apache.wicket.util.lang.Args;

public class I18NBookmarkablePageMapper extends BookmarkableMapper{
	
	private final Locale locale;

	public I18NBookmarkablePageMapper(Locale locale, IPageParametersEncoder pageParametersEncoder)
	{
		super(pageParametersEncoder);

		this.locale = locale;
	}

	/**
	 * Construct.
	 */
	public I18NBookmarkablePageMapper(Locale locale)
	{
		this(locale, new PageParametersEncoder());
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
	
	public boolean matches(Url requestTarget) {
        boolean matches = 
                    locale == null ||
                       locale.equals(Session.get().getLocale()) ||
                       //also match en and en_US
                       locale.getLanguage().equals(Session.get().getLocale().getLanguage());
            
            return matches;
    }

}
