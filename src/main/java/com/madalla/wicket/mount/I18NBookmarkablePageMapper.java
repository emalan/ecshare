package com.madalla.wicket.mount;

import java.util.Locale;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.core.request.handler.BookmarkablePageRequestHandler;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler.RedirectPolicy;
import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.info.PageComponentInfo;
import org.apache.wicket.request.mapper.info.PageInfo;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.ClassProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class I18NBookmarkablePageMapper extends MountedMapper {

    private static final Logger log = LoggerFactory.getLogger(I18NBookmarkablePageMapper.class);
    
    private final ClassProvider<? extends IRequestablePage> pageClassProvider;
    private final Locale locale;

    public I18NBookmarkablePageMapper(final String mountName, Locale locale, Class<? extends Page> pageClass) {
    	super(mountName, pageClass);
        this.pageClassProvider = ClassProvider.of(pageClass);
        this.locale = locale;
    }

    public IRequestHandler mapRequest(Request request) {
        UrlInfo urlInfo = parseRequest(request);
        if (urlInfo != null) {
            PageParameters pageParameters = urlInfo.getPageParameters();
            PageProvider provider = new PageProvider(pageClassProvider.get(), pageParameters);
            provider.setPageSource(getContext());
            return new RenderPageRequestHandler(provider, RedirectPolicy.NEVER_REDIRECT);
        }
        return null;
    }

    public Url mapHandler(IRequestHandler requestHandler) {
        if (requestHandler instanceof BookmarkablePageRequestHandler) {
            BookmarkablePageRequestHandler handler = (BookmarkablePageRequestHandler) requestHandler;
            //log.trace("mapHandler - locale=" + locale + " page:"+ pageClassProvider.get().getSimpleName());
            if (!checkPageClass(handler.getPageClass())) {
                return null;
            }
            //log.trace("mapHandler - page matches:" + handler.getPageClass().getSimpleName());
            if (!matches()) {
                return null;
            }
            log.debug("mapHandler - matched. locale=" + locale + " page:"+ pageClassProvider.get().getSimpleName());
            PageInfo info = new PageInfo();
            UrlInfo urlInfo = new UrlInfo(new PageComponentInfo(info, null), handler.getPageClass(),
                    handler.getPageParameters());
            //log.trace("getCompatibilityScore - locale=" + locale + " page:"+ pageClassProvider.get().getSimpleName() + " url=" + requestHandler.);
            return buildUrl(urlInfo);
        } else {
            return null;
        }
    }

    public boolean matches() {
        return locale == null || locale.equals(Session.get().getLocale()) ||
        // also match en and en_US
                locale.getLanguage().equals(Session.get().getLocale().getLanguage());

    }

}
