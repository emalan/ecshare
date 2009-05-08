package com.madalla.webapp;

import static com.madalla.webapp.PageParams.RETURN_PAGE;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;

import com.madalla.bo.SiteData;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.util.security.SecureCredentials;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.login.LoginPanel;
import com.madalla.webapp.pages.UserLoginPage;
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
		if (!isMetadataOveridden()){
		    SiteData siteData = ((IRepositoryServiceProvider)getApplication()).getRepositoryService().getSiteData();
		    if (StringUtils.isNotEmpty(siteData.getMetaDescription())){
		        add(new StringHeaderContributor("<meta name=\"description\" content=\"" + siteData.getMetaDescription() + "\"/>" ));
		    }
		    if (StringUtils.isNotEmpty(siteData.getMetaKeywords())){
		        add(new KeywordHeaderContributor(siteData.getMetaKeywords()));
		    }
		}
		
        if (isPopupLoginEnabled()) {

            //Login feedback
            FeedbackPanel feedback = new FeedbackPanel("feedback");
            feedback.setOutputMarkupId(true);
            add(feedback);

            //Logon link
            add(new AjaxFallbackLink<Object>("logon") {
                private static final long serialVersionUID = 1L;
                CmsSession session = (CmsSession) getSession();
                
                public void onClick(AjaxRequestTarget target) {
                    if (session.isLoggedIn()){
                        session.logout();
                        setResponsePage(getPage());
                    } else {
                        if (target != null){
                            target.appendJavascript("wicketShow('loginPopup')");
                        } else {
                            error(getString("label.javascript.error"));
                        }
                    } 
                }
                protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
                    if(session.isLoggedIn()){
                        replaceComponentTagBody(markupStream, openTag, getString("label.logout"));
                    } else {
                        replaceComponentTagBody(markupStream, openTag, getString("label.login"));
                    }
                }
            });

            add(new LoginPanel("signInPanel", new SecureCredentials()){
                private static final long serialVersionUID = 1L;
                public boolean signIn(String username, String password) {
                    CmsSession session = (CmsSession) getSession();
                    if (session.login(username, password)) {
                        return true;
                    } else {
                        return false;
                    }
                }

                protected void onSignInSucceeded() {
                    super.onSignInSucceeded();
                }
            });

        } else {
            //dummy label that will end up hidden on page
            add(new Label("signInPanel"));

            //Logon link
            add(new AjaxFallbackLink<Object>("logon") {
                private static final long serialVersionUID = 1L;
                CmsSession session = (CmsSession) getSession();
                
                public void onClick(AjaxRequestTarget target) {
                    if (session.isLoggedIn()){
                        session.logout();
                        setResponsePage(getPage());
                    } else {
                        if (target != null){
                            setResponsePage(new UserLoginPage(new PageParameters(RETURN_PAGE + "=" + getPage().getClass().getName())));
                        } else {
                            error(getString("label.javascript.error"));
                        }
                    } 
                }
                protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
                    if(session.isLoggedIn()){
                        replaceComponentTagBody(markupStream, openTag, getString("label.logout"));
                    } else {
                        replaceComponentTagBody(markupStream, openTag, getString("label.login"));
                    }
                }
            });

        }

	}
	
	
	/**
	 * @return true if you want to set your own metadata, otherwise the metadata will be set from the CMS values
	 */
	protected boolean isMetadataOveridden(){
	    return false;
	}
	
	/**
	 * @return true if you want to use the built in popup login
	 */
	protected boolean isPopupLoginEnabled(){
	    return false;
	}

    protected void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    protected String getPageTitle() {
        return pageTitle;
    }
}
