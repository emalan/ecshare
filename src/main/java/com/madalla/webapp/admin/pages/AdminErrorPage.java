package com.madalla.webapp.admin.pages;

import javax.servlet.http.HttpServletResponse;

import com.madalla.webapp.admin.AbstractAdminPage;

public class AdminErrorPage extends AbstractAdminPage {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 */
	public AdminErrorPage() {
		add(homePageLink("homePageLink"));
	}

	/**
	 * @see org.apache.wicket.markup.html.WebPage#configureResponse()
	 */
	@Override
	protected void configureResponse() {
		super.configureResponse();
		getWebRequestCycle().getWebResponse().getHttpServletResponse().setStatus(
				HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}

	/**
	 * @see org.apache.wicket.Component#isVersioned()
	 */
	@Override
	public boolean isVersioned() {
		return false;
	}

	/**
	 * @see org.apache.wicket.Page#isErrorPage()
	 */
	@Override
	public boolean isErrorPage() {
		return true;
	}

}
