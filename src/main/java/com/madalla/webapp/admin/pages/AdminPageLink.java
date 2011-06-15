package com.madalla.webapp.admin.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import com.madalla.webapp.admin.AbstractAdminPage;

@AuthorizeAction(action = "RENDER", roles = { "ADMIN", "USER" })
public class AdminPageLink extends BookmarkablePageLink<Object>{

	private static final long serialVersionUID = 1L;

	public AdminPageLink(String id, final Class<? extends AbstractAdminPage> adminPageClass) {
		super(id, adminPageClass);
	}

	public AdminPageLink(final String id, final Class<? extends AbstractAdminPage> adminPageClass,
			final PageParameters parameters){
		super(id, adminPageClass, parameters);
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		tag.put("rel", "nofollow");
		super.onComponentTag(tag);
	}
}
