package com.madalla.webapp.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.session.pagemap.IPageMapEntry;

import com.madalla.webapp.CmsApplication;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.admin.site.SiteAdminPanel;
import com.madalla.webapp.admin.site.SiteDataPanel;
import com.madalla.webapp.cms.admin.ContentAdminPanel;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.images.admin.ImageAdminPanel;
import com.madalla.webapp.pages.AdminPanelLink;
import com.madalla.webapp.user.UserProfilePanel;

public abstract class AdminPage extends WebPage {
	
	private static final long serialVersionUID = -2837757448336709448L;
	
	private class MenuListView extends ListView<PanelMenuItem>{
		private static final long serialVersionUID = -8310833021413122278L;
		
		private String menuLinkId;
		
		public MenuListView(final String id, final String menuLinkId, List<PanelMenuItem> items) {
			super(id, items);
			this.menuLinkId = menuLinkId;
		}

		@Override
		protected void populateItem(ListItem<PanelMenuItem> item) {
			final PanelMenuItem menu = item.getModelObject();
			item.add(new AdminPanelLink(menuLinkId, menu.c, menu.key, menu.titleKey));
		}
		
	}
	
	private String pageTitle = "(no title)";
	
	public AdminPage(PageParameters parameters){
		super(parameters);
		commonInit();
	}
	
	public AdminPage(){
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
        add(CSSPackageResource.getHeaderContribution(AdminPage.class,"AdminPage.css"));
        add(Css.CSS_BUTTONS);
        add(Css.CSS_FORM);
        setupMenu();
	}
	
	protected void setupMenu(){
		List<PanelMenuItem> menuItems = getAdminMenu();
		add(new MenuListView("menuList","menuLink",menuItems ));

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
		menuList.add(new PanelMenuItem(SiteAdminPanel.class, "label.site","info.site"));
		menuList.add(new PanelMenuItem(SiteDataPanel.class, "label.data","info.data"));
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
