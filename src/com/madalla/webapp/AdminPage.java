package com.madalla.webapp;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.border.Border;

import com.madalla.webapp.css.Css;

public abstract class AdminPage extends WebPage implements ISecureWebPage{
	
	private static final long serialVersionUID = -2837757448336709448L;
	private Border border;
	
	public AdminPage(){
	    border = new AdminBorder("border");
        border.setTransparentResolver(true);
        super.add(border);
        add(HeaderContributor.forCss(AdminPage.class,"AdminPage.css"));
        add(Css.CSS_BUTTONS);
	}
	
	public CmsSession getAppSession(){
        return (CmsSession)getSession();
    }

}
