package com.madalla.webapp.admin.site;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.emalan.cms.bo.SiteLanguage;
import org.emalan.cms.bo.page.PageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madalla.webapp.CmsApplication;
import com.madalla.webapp.CmsPage;
import com.madalla.webapp.CmsPanel;
import com.madalla.webapp.css.Css;

public class PageAdminPanel extends CmsPanel {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(PageAdminPanel.class);

    public PageAdminPanel(String id){
		super(id);

		final List<SiteLanguage> locales = SiteLanguage.getLanguages();
		final Locale defaultLocale = getDefaultLocale(locales);

		add(new ListView<PageData>("pageDiv", getPages()){
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<PageData> item) {
				item.add(new PageMetaPanel("metaPanel", item.getModelObject(), locales, defaultLocale){
					private static final long serialVersionUID = 1L;

					@Override
					void preSaveProcessing(String existingMount, String newMount) {
						log.debug("changing mount. existing:" + existingMount + " new:" + newMount);
						if (StringUtils.isNotEmpty(existingMount)){
							getCmsApplication().unmount(existingMount);
						}
						Class <? extends Page> page = getPageClass(item.getModelObject().getName());
						if (page != null){
							try {
								getCmsApplication().mountPage(newMount, page);
							} catch (WicketRuntimeException e){
								log.error("Error while mounting Application Page.", e);
							}
						}
					}

				});

			}

		});

	}
    
    @Override
    public void renderHead(IHeaderResponse response) {
    	response.renderCSSReference(Css.CSS_FORM);
    }

    private Class<? extends Page> getPageClass(String name){
    	Collection<Class<? extends Page>> pages = ((CmsApplication)getApplication()).getAppPages();
		for (Class<? extends Page> page : pages){
			if (getPageName(page).equals(name)){
				return page;
			}
		}
		return null;
    }

	private String getPageName(Class<? extends Page> page){
		if (!CmsPage.class.isAssignableFrom(page)){
			throw new WicketRuntimeException("Home page does not extends CmsPage class. Home Page is " + page);
		}
		return page.getSimpleName();
	}

	private List<PageData> getPages(){
		Collection<Class<? extends Page>> pages = ((CmsApplication)getApplication()).getAppPages();
		List<PageData> rt = new ArrayList<PageData>();
		for (Class<? extends Page> page : pages){
			PageData pageData = getRepositoryService().getPage(getPageName(page));
			rt.add(pageData);
		}
		return rt;
	}

	//get the first configured language
	private Locale getDefaultLocale(List<SiteLanguage> locales){
		List<SiteLanguage> configuredLangs = getRepositoryService().getSiteData().getLocaleList();
		if (configuredLangs.isEmpty()){
			return locales.get(0).locale;
		} else {
			return configuredLangs.get(0).locale;
		}
	}
}
