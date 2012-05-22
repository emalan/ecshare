package com.madalla.wicket.mount;

import java.util.Locale;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.handler.BookmarkablePageRequestHandler;
import org.apache.wicket.request.handler.PageProvider;
import org.apache.wicket.request.handler.RenderPageRequestHandler;
import org.apache.wicket.request.handler.RenderPageRequestHandler.RedirectPolicy;
import org.apache.wicket.request.mapper.MountedMapper;
import org.apache.wicket.request.mapper.info.PageComponentInfo;
import org.apache.wicket.request.mapper.info.PageInfo;
import org.apache.wicket.util.ClassProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class I18NBookmarkablePageMapper extends MountedMapper {

    private static final Logger log = LoggerFactory.getLogger(I18NBookmarkablePageMapper.class);
    
    private final Locale locale;
    private final ClassProvider<? extends IRequestablePage> pageClassProvider;

    public I18NBookmarkablePageMapper(final Locale locale, final String langCode, Class<? extends Page> pageClass) {
        super(langCode, pageClass);

        this.locale = locale;
        this.pageClassProvider = ClassProvider.of(pageClass);
    }

    public IRequestHandler mapRequest(Request request) {
        log.trace("mapRequest - " + locale);
        PageProvider provider = new PageProvider(pageClassProvider.get());
        provider.setPageSource(getContext());
        return new RenderPageRequestHandler(provider, RedirectPolicy.NEVER_REDIRECT);
    }

    public int getCompatibilityScore(Request request) {
        int score = super.getCompatibilityScore(request);
        log.trace("getCompatibilityScore - " + locale + " url=" + request.getUrl() + " score=" + score);
        return score;
    }

    public Url mapHandler(IRequestHandler requestHandler) {
        log.trace("mapHandler - " + locale);

        if (requestHandler instanceof BookmarkablePageRequestHandler) {
            BookmarkablePageRequestHandler handler = (BookmarkablePageRequestHandler) requestHandler;

            if (!checkPageClass(handler.getPageClass())) {
                return null;
            }

            if (!matches()) {
                return null;
            }
            
            PageInfo info = new PageInfo();
            UrlInfo urlInfo = new UrlInfo(new PageComponentInfo(info, null), handler.getPageClass(),
                    handler.getPageParameters());

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
