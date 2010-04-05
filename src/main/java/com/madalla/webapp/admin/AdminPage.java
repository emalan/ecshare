package com.madalla.webapp.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.madalla.webapp.CmsApplication;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.cms.admin.ContentAdminPanel;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.images.admin.ImageAdminPanel;
import com.madalla.webapp.pages.AdminPanelLink;
import com.madalla.webapp.site.SiteAdminPanel;
import com.madalla.webapp.site.SiteDataPanel;
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
			item.add(new AdminPanelLink(menuLinkId, menu.getKey(), menu.getTitleKey(), false){
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					getPage().replace(menu.getPanel(ID));
				}
				
			});
		}
		
	}
	
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
		List<PanelMenuItem> menuItems = getAdminMenu();
		add(new MenuListView("menuList","menuLink",menuItems ));

		add(new Link<Object>("returnLink"){

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				if (returnPage == null) {
					returnPage = getApplication().getHomePage();
				}
				setResponsePage(returnPage);
				//setResponsePage(getSession().getPageFactory().newPage(returnPage));
			}
			
		});
	}
	
	//TODO make Serializable and add to Application
	public List<PanelMenuItem> getAdminMenu() {
		List<PanelMenuItem> menuList = new ArrayList<PanelMenuItem>();
		menuList.add(new PanelMenuItem("label.profile","info.profile"){
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String id) {
				return new UserProfilePanel(id);
			}

		});
		menuList.add(new PanelMenuItem("label.site","info.site"){
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String id) {
				return new SiteAdminPanel(id);
			}
			
		});
		menuList.add(new PanelMenuItem("label.data","info.data"){
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String id) {
				return new SiteDataPanel(id);
			}
			
		});
		menuList.add(new PanelMenuItem("label.image","info.image"){
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String id) {
				return new ImageAdminPanel(id);
			}
			
		});
		menuList.add(new PanelMenuItem("label.content","info.content"){
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String id) {
				
				if (((CmsApplication)getApplication()).getRepositoryService().isAdminApp()){
					return ContentAdminPanel.newAdminInstance(id);
		    	} else {
		    		return ContentAdminPanel.newInstance(id);
		    	}
			}
			
		});
		
		return Collections.unmodifiableList(menuList);

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
