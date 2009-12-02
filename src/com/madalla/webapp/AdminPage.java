package com.madalla.webapp;

import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;

import com.madalla.webapp.css.Css;
import com.madalla.webapp.login.aware.AdminLink;
import com.madalla.webapp.pages.ContentAdminPage;
import com.madalla.webapp.pages.SiteAdminPage;
import com.madalla.webapp.pages.SiteDataPage;
import com.madalla.webapp.pages.UserProfilePage;

public abstract class AdminPage extends WebPage {
	
	private static final long serialVersionUID = -2837757448336709448L;
	
	private String pageTitle = "(no title)";
	private Class<? extends Page> returnPage;
	
	public AdminPage(){
	    setPageTitle(getString("page.title"));
        add(new Label("title", new PropertyModel<String>(this,"pageTitle")));

        add(Css.YUI_CORE);
        add(Css.BASE);
        add(CSSPackageResource.getHeaderContribution(AdminPage.class,"AdminPage.css"));
        add(Css.CSS_BUTTONS);
        add(Css.CSS_FORM);
        setupMenu();
	}
	
	protected void setupMenu(){
		add(new AdminLink("UserProfile", UserProfilePage.class, false).setAutoEnable(true));
		add(new AdminLink("SiteAdmin", SiteAdminPage.class, true).setAutoEnable(true));
		add(new AdminLink("Data", SiteDataPage.class, true).setAutoEnable(true));
		add(new AdminLink("Content", ContentAdminPage.class, true).setAutoEnable(true));
		
		add(new Link<Object>("returnLink"){

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				if (returnPage == null) {
					throw new WicketRuntimeException("Return Page has not been set.");
				}
				setResponsePage(getSession().getPageFactory().newPage(returnPage));
			}
			
		});
	}
	
	public CmsSession getAppSession(){
        return (CmsSession)getSession();
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageTitle() {
        return pageTitle;
    }

	public void setReturnPage(Class<? extends Page> returnPage) {
		this.returnPage = returnPage;
	}
	
	public Class<? extends Page> getReturnPage(){
		return returnPage;
	}

}
