package com.madalla.webapp.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import com.madalla.bo.SiteLanguage;
import com.madalla.bo.page.PageData;
import com.madalla.webapp.CmsApplication;
import com.madalla.webapp.CmsPage;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.panel.CmsPanel;

public class PageAdminPanel extends CmsPanel {
	
	private static final long serialVersionUID = 1L;
	
    public PageAdminPanel(String id){
		super(id);
		add(Css.CSS_FORM);
		
		final List<SiteLanguage> locales = SiteLanguage.getLanguages();
		final Locale defaultLocale = getDefaultLocale(locales);
		
		add(new ListView<PageData>("pageDiv", getPages()){
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<PageData> item) {
				item.add(new PageMetaPanel("metaPanel", item.getModelObject(), locales, defaultLocale));
				
			}
			
		});
		
	}
	
	private String getPageName(Class<? extends Page> page){
		if (!CmsPage.class.isAssignableFrom(page)){
			throw new WicketRuntimeException("Home page does not extends CmsPage class. Home Page is " + page);
		}
		return page.getSimpleName();
	}
	
	private List<PageData> getPages(){
		List<Class<? extends Page>> pages = ((CmsApplication)getApplication()).getPageMenuList();
		List<PageData> rt = new ArrayList<PageData>();
		for (Class<? extends Page> page : pages){
			PageData pageData = getRepositoryService().getPage(getPageName(page));
			rt.add(pageData);
		}
		return rt;
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
