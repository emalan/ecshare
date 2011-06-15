package com.madalla.wicket;

import java.util.Locale;

import org.apache.wicket.IRequestTarget;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.request.target.coding.BookmarkablePageRequestTargetUrlCodingStrategy;

/**
 * Allows retrieving of mounts based on locale in session.
 *
 * Thanks to : Ilja Pavkovic
 *
 */
public class I18NBookmarkablePageRequestTargetUrlCodingStrategy extends BookmarkablePageRequestTargetUrlCodingStrategy{
	private Locale locale;

    public <C extends Page> I18NBookmarkablePageRequestTargetUrlCodingStrategy(Locale locale,
    		String mountPath, Class<C> bookmarkablePageClass) {

            this(locale, mountPath, bookmarkablePageClass, null);
    }

    private <C extends Page> I18NBookmarkablePageRequestTargetUrlCodingStrategy(Locale locale,
    		String mountPath, Class<C> bookmarkablePageClass, String pageMapName) {

            super(mountPath, bookmarkablePageClass, pageMapName);
            this.locale = locale;
    }

    //public IRequestTarget decode(RequestParameters requestParameters)
    @Override
	public boolean matches(IRequestTarget requestTarget) {
            boolean matches = super.matches(requestTarget);
            if(matches) {
                    matches = locale == null ||
                       locale.equals(Session.get().getLocale()) ||
                       //also match en and en_US
                       locale.getLanguage().equals(Session.get().getLocale().getLanguage());
            }
            return matches;
    }
}
