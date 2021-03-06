package com.madalla.webapp;

import org.apache.wicket.Page;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class AppPageLink extends BookmarkablePageLink<String> {

    private static final long serialVersionUID = -607174917270219309L;

    private final String body;

    public AppPageLink(final String id, final Class<? extends Page> c, final String body) {
        super(id, c);
        this.body = body;
        setAfterDisabledLink(id);
        setAutoEnable(true);
    }

    @Override
    public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
        replaceComponentTagBody(markupStream, openTag, body);
    }

    @Override
    protected CharSequence getURL() {
        PageParameters parameters = getPageParameters();

        return urlFor(getPageClass(), parameters);
    }
}
