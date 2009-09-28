package com.madalla.webapp;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.PropertyModel;

import com.madalla.webapp.css.Css;
import com.madalla.webapp.login.aware.LoggedinBookmarkablePageLink;
import com.madalla.webapp.pages.ContentAdminPage;
import com.madalla.webapp.pages.SiteAdminPage;
import com.madalla.webapp.pages.SiteDataPage;
import com.madalla.webapp.pages.UserProfilePage;
import com.madalla.webapp.panel.Panels;

public abstract class AdminPage extends WebPage {
	
	private static final long serialVersionUID = -2837757448336709448L;
	
	private String pageTitle = "(no title)";
	
	public AdminPage(final PageParameters params){
	    super();
	    
	    setPageTitle(getString("page.title"));
        add(new Label("title", new PropertyModel<String>(this,"pageTitle")));
        add(new StringHeaderContributor("<meta name=\"AUTHOR\" content=\"Eugene Malan\" />"));

        add(Css.YUI_CORE);
        add(Css.BASE);
        add(CSSPackageResource.getHeaderContribution(AdminPage.class,"AdminPage.css"));
        add(Css.CSS_BUTTONS);
        add(Css.CSS_FORM);
        setupMenu(params);
	}
	
	protected void setupMenu(final PageParameters params){
		add(new LoggedinBookmarkablePageLink("UserProfile", UserProfilePage.class, params, false).setAutoEnable(true));
		add(new LoggedinBookmarkablePageLink("SiteAdmin", SiteAdminPage.class, params, true).setAutoEnable(true));
		add(new LoggedinBookmarkablePageLink("Data", SiteDataPage.class, params, true).setAutoEnable(true));
		add(new LoggedinBookmarkablePageLink("Content", ContentAdminPage.class, params, true).setAutoEnable(true));
		
		add(new BookmarkablePageLink<Page>("returnLink", Panels.getReturnPage(params, "AdminPage") ));
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

}
