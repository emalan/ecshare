package com.madalla.webapp;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.WebPage;

import com.madalla.bo.SiteData;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.webapp.css.Css;
import com.madalla.wicket.KeywordHeaderContributor;

public class CmsPage extends WebPage {

	public CmsPage(){
		super();
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
}
