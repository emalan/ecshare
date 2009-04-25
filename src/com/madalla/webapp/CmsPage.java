package com.madalla.webapp;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;

import com.madalla.bo.SiteData;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.webapp.css.Css;
import com.madalla.wicket.KeywordHeaderContributor;

public class CmsPage extends WebPage {

    private static final String PAGE_TITLE = "page.title";

    private String pageTitle = "(no title)";
    
	public CmsPage(){
	    super();
	    setPageTitle(getString(PAGE_TITLE));
        add(new Label("title", new PropertyModel<String>(this,"pageTitle")));

		add(Css.YUI_CORE);
		add(Css.BASE);
		
		add(new StringHeaderContributor("<meta name=\"AUTHOR\" content=\"Eugene Malan\" />"));
		if (!overrideMetaData()){
		    SiteData siteData = ((IRepositoryServiceProvider)getApplication()).getRepositoryService().getSiteData();
		    if (StringUtils.isNotEmpty(siteData.getMetaDescription())){
		        add(new StringHeaderContributor("<meta name=\"description\" content=\"" + siteData.getMetaDescription() + "\"/>" ));
		    }
		    if (StringUtils.isNotEmpty(siteData.getMetaKeywords())){
		        add(new KeywordHeaderContributor(siteData.getMetaKeywords()));
		    }
		}
	}
	
	protected boolean overrideMetaData(){
	    return false;
	}

    protected void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    protected String getPageTitle() {
        return pageTitle;
    }
}
