package com.madalla.webapp.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;

import com.madalla.webapp.CmsApplication;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.admin.site.SiteAdminPanel;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.panelmenu.PanelListView;
import com.madalla.webapp.panelmenu.PanelMenuItem;
import com.madalla.webapp.user.UserProfilePanel;

/**
 * @author Eugene Malan
 *
 *	Class deprecated use AbstractAdminPage
 */
@Deprecated 
public abstract class AdminPage extends WebPage {

	private static final long serialVersionUID = -2837757448336709448L;
	protected static final String ID = "adminPanel";

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

        setupMenu();
	}

	protected void setupMenu(){
		List<PanelMenuItem> menuItems = getAdminMenu();
		add(new PanelListView("menuList",ID,"menuLink",menuItems ));
		
//TODO fix this
//		IPageMapEntry backPage = getAppSession().getLastSitePage();
//		if (backPage == null){
//			add(new BookmarkablePageLink<Void>("returnLink", getApplication().getHomePage()));
//		} else {
//			add(new BookmarkablePageLink<Void>("returnLink", backPage.getPageClass(), backPage.getPage().getPageParameters()));
//		}

	}

	public List<PanelMenuItem> getAdminMenu() {
		List<PanelMenuItem> menuList = new ArrayList<PanelMenuItem>();
		menuList.add(new PanelMenuItem(UserProfilePanel.class, 
				new StringResourceModel("label.profile", this, null),
				new StringResourceModel("info.profile", this, null)));
		menuList.add(new PanelMenuItem(SiteAdminPanel.class, 
				new StringResourceModel("label.site", this, null),
				new StringResourceModel("info.site", this, null)));
		//menuList.add(new PanelMenuItem(MemberAdminPanel.class, "label.member", "info.member"));
		//menuList.add(new PanelMenuItem(ImageAdminPanel.class, "label.image","info.image"));
		//menuList.add(new PanelMenuItem(ContentAdminPanel.class, "label.content","info.content"));

		return Collections.unmodifiableList(menuList);
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
	        response.renderCSSReference(Css.YUI_CORE);
	        response.renderCSSReference(Css.BASE);
	        response.renderCSSReference(new PackageResourceReference(AdminPage.class,"AdminPage.css"));
	        response.renderCSSReference(Css.CSS_BUTTONS);
	        response.renderCSSReference(Css.CSS_FORM);
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
