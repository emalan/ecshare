package com.madalla.webapp.site;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;

import com.madalla.bo.SiteLanguage;
import com.madalla.bo.page.PageData;
import com.madalla.webapp.CmsPage;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.panel.CmsPanel;

public class PageAdminPanel extends CmsPanel {
	
	private static final long serialVersionUID = 1L;
	
    public PageAdminPanel(String id){
		super(id);
		add(Css.CSS_FORM);
		final PageData pageData = getRepositoryService().getPage(getHomePageName());
		
		List<SiteLanguage> locales = SiteLanguage.getLanguages();
		final Locale defaultLocale = getDefaultLocale(locales);
		
		add(new PageMetaPanel("metaPanel", pageData, locales, defaultLocale));
		
	}
	
	private String getHomePageName(){
		Class<? extends Page> page = getApplication().getHomePage();
		if (!CmsPage.class.isAssignableFrom(page)){
			throw new WicketRuntimeException("Home page does not extends CmsPage class. Home Page is " + page);
		}
		return page.getSimpleName();
	}
	
	private Locale getDefaultLocale(List<SiteLanguage> locales){
		List<SiteLanguage> configuredLangs = getRepositoryService().getSiteData().getLocaleList();
		if (configuredLangs.isEmpty()){
			return locales.get(0).locale;
		} else {
			return configuredLangs.get(0).locale;
		}
	}
}
