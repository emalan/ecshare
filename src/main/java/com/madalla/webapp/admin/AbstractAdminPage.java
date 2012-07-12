package com.madalla.webapp.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.PackageResourceReference;

import com.madalla.service.ApplicationService;
import com.madalla.webapp.CmsApplication;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.admin.content.ContentAdminPanel;
import com.madalla.webapp.admin.image.ImageAdminPanel;
import com.madalla.webapp.admin.member.MemberAdminPanel;
import com.madalla.webapp.admin.site.PageAdminPanel;
import com.madalla.webapp.admin.site.SiteAdminPanel;
import com.madalla.webapp.admin.site.SiteEmailPanel;
import com.madalla.webapp.admin.sitedata.SiteDataPanel;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.panelmenu.PanelListView;
import com.madalla.webapp.panelmenu.PanelMenuItem;
import com.madalla.webapp.user.UserAdminPanel;
import com.madalla.webapp.user.UserProfilePanel;

public abstract class AbstractAdminPage extends WebPage {
    private static final long serialVersionUID = 1L;

    protected static final String ID = "adminPanel";

    private String pageTitle = "(no title)";

    public AbstractAdminPage(PageParameters parameters) {
        super(parameters);
        commonInit();
    }

    public AbstractAdminPage() {
        super();
        commonInit();
    }

    private void commonInit() {
        setPageTitle(getString("page.title"));
        add(new Label("title", new PropertyModel<String>(this, "pageTitle")));
        add(new Label("version", getApplicationService().getBuildInformation().getVersion()));
        add(new Label("webapp", getApplicationService().getBuildInformation().getWebappVersion()));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        setupMenu();
    }

    protected void setupMenu() {
        List<PanelMenuItem> menuItems = getAdminMenu();

        final Component menu;
        add(menu = new PanelListView("menuList", ID, "menuLink", menuItems));
        menu.setVisible(!isErrorPage());

        PageReference backPage = getAppSession().getLastSitePage();
        if (backPage == null) {
            add(new BookmarkablePageLink<Void>("returnLink", getApplication().getHomePage()));
        } else {
            add(new BookmarkablePageLink<Void>("returnLink", backPage.getPage().getClass(), backPage.getPage()
                    .getPageParameters()));
        }

    }

    /**
     * Builds menu list. Each item has all the info needed to switch panels.
     * 
     * @return
     */
    public List<PanelMenuItem> getAdminMenu() {
        List<PanelMenuItem> menuList = new ArrayList<PanelMenuItem>();
        menuList.add(new PanelMenuItem(UserProfilePanel.class, new StringResourceModel("label.profile", this, null),
                new StringResourceModel("info.profile", this, null)));
        menuList.add(new PanelMenuItem(UserAdminPanel.class, new StringResourceModel("label.user", this, null),
                new StringResourceModel("info.user", this, null)));
        menuList.add(new PanelMenuItem(SiteAdminPanel.class, new StringResourceModel("label.site", this, null),
                new StringResourceModel("info.site", this, null)));
        menuList.add(new PanelMenuItem(PageAdminPanel.class, new StringResourceModel("label.page", this, null),
                new StringResourceModel("info.page", this, null)));
        menuList.add(new PanelMenuItem(SiteEmailPanel.class, new StringResourceModel("label.email", this, null),
                new StringResourceModel("info.email", this, null)));
        menuList.add(new PanelMenuItem(SiteDataPanel.class, new StringResourceModel("label.siteData", this, null),
                new StringResourceModel("info.siteData", this, null)));
        if (getCmsApplication().hasMemberService()) {
            menuList.add(new PanelMenuItem(MemberAdminPanel.class, new StringResourceModel("label.member", this, null),
                    new StringResourceModel("info.member", this, null)));
        }
        menuList.add(new PanelMenuItem(ImageAdminPanel.class, new StringResourceModel("label.image", this, null),
                new StringResourceModel("info.image", this, null)));
        menuList.add(new PanelMenuItem(ContentAdminPanel.class, new StringResourceModel("label.content", this, null),
                new StringResourceModel("info.content", this, null)));

        return Collections.unmodifiableList(menuList);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(Css.YUI_CORE));
        response.render(CssHeaderItem.forReference(Css.BASE));
        response.render(CssHeaderItem.forReference(new PackageResourceReference(AbstractAdminPage.class,
                "AdminPage.css")));
        response.render(CssHeaderItem.forReference(Css.CSS_BUTTONS));
        response.render(CssHeaderItem.forReference(Css.CSS_FORM));
    }

    public CmsSession getAppSession() {
        return (CmsSession) getSession();
    }

    public CmsApplication getCmsApplication() {
        return (CmsApplication) getApplication();
    }

    public ApplicationService getApplicationService() {
        return getCmsApplication().getApplicationService();
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageTitle() {
        return pageTitle;
    }

}
