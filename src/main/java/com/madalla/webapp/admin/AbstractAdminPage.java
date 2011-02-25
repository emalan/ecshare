package com.madalla.webapp.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.session.pagemap.IPageMapEntry;

import com.madalla.webapp.CmsApplication;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.admin.content.ContentAdminPanel;
import com.madalla.webapp.admin.image.ImageAdminPanel;
import com.madalla.webapp.admin.member.MemberAdminPanel;
import com.madalla.webapp.admin.site.PageAdminPanel;
import com.madalla.webapp.admin.site.SiteAdminPanel;
import com.madalla.webapp.admin.site.SiteDataPanel;
import com.madalla.webapp.admin.site.SiteEmailPanel;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.panelmenu.PanelListView;
import com.madalla.webapp.panelmenu.PanelMenuItem;
import com.madalla.webapp.user.UserAdminPanel;
import com.madalla.webapp.user.UserProfilePanel;

public abstract class AbstractAdminPage extends WebPage{

	protected static final String ID = "adminPanel";
	
	private String pageTitle = "(no title)";
	
	public AbstractAdminPage(PageParameters parameters){
		super(parameters);
		commonInit();
	}
	
	public AbstractAdminPage(){
		super();
	    commonInit();
	}
	
	private void commonInit(){
	    setPageTitle(getString("page.title"));
        add(new Label("title", new PropertyModel<String>(this,"pageTitle")));
        add(new Label("version", getCmsApplication().getBuildInformation().getVersion()));
        add(new Label("webapp", getCmsApplication().getBuildInformation().getWebappVersion()));
        
        add(Css.YUI_CORE);
        add(Css.BASE);
        add(CSSPackageResource.getHeaderContribution(AbstractAdminPage.class,"AdminPage.css"));
        add(Css.CSS_BUTTONS);
        add(Css.CSS_FORM);
        setupMenu();
	}
	
	protected void setupMenu(){
		List<PanelMenuItem> menuItems = getAdminMenu();
		add(new PanelListView("menuList",ID,"menuLink",menuItems ));

		IPageMapEntry backPage = getAppSession().getLastSitePage();
		if (backPage == null){
			add(new BookmarkablePageLink<Void>("returnLink", getApplication().getHomePage()));
		} else {
			add(new BookmarkablePageLink<Void>("returnLink", backPage.getPageClass(), backPage.getPage().getPageParameters()));
		}
		
	}
	
	public List<PanelMenuItem> getAdminMenu() {
		List<PanelMenuItem> menuList = new ArrayList<PanelMenuItem>();
		menuList.add(new PanelMenuItem(UserProfilePanel.class, "label.profile","info.profile"));
		menuList.add(new PanelMenuItem(UserAdminPanel.class, "label.user","info.user"));
		menuList.add(new PanelMenuItem(SiteAdminPanel.class, "label.site","info.site"));
		menuList.add(new PanelMenuItem(PageAdminPanel.class, "label.page","info.page"));
		menuList.add(new PanelMenuItem(SiteEmailPanel.class, "label.email","info.email"));
		menuList.add(new PanelMenuItem(SiteDataPanel.class, "label.siteData","info.siteData"));
		menuList.add(new PanelMenuItem(MemberAdminPanel.class, "label.member", "info.member"));
		menuList.add(new PanelMenuItem(ImageAdminPanel.class, "label.image","info.image"));
		menuList.add(new PanelMenuItem(ContentAdminPanel.class, "label.content","info.content"));
		
		return Collections.unmodifiableList(menuList);
	}
	
	public CmsSession getAppSession(){
        return (CmsSession)getSession();
    }
	
	public CmsApplication getCmsApplication(){
        return (CmsApplication) getApplication();
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageTitle() {
        return pageTitle;
    }
	
}
