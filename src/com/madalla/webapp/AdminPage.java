package com.madalla.webapp;

import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;

import com.madalla.webapp.css.Css;

public abstract class AdminPage extends WebPage {
	
	private static final long serialVersionUID = -2837757448336709448L;
	
	private String pageTitle = "(no title)";
	
	public AdminPage(){
	    super();
	    
	    setPageTitle(getString("page.title"));
        add(new Label("title", new PropertyModel<String>(this,"pageTitle")));
        add(new StringHeaderContributor("<meta name=\"AUTHOR\" content=\"Eugene Malan\" />"));

        add(Css.YUI_CORE);
        add(Css.BASE);
        add(CSSPackageResource.getHeaderContribution(AdminPage.class,"AdminPage.css"));
        add(Css.CSS_BUTTONS);
        add(Css.CSS_FORM);
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

}
