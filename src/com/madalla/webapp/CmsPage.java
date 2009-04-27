package com.madalla.webapp;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;

import com.madalla.bo.SiteData;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.util.security.SecureCredentials;
import com.madalla.webapp.cms.IContentAdmin;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.login.LoginPanel;
import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;
import com.madalla.webapp.scripts.utility.ScriptUtils;
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

            //popup login TODO remove dependancy on libraries
            add(JavascriptPackageResource.getHeaderContribution(Scriptaculous.PROTOTYPE));
            add(JavascriptPackageResource.getHeaderContribution(Scriptaculous.EFFECTS));
            add(JavascriptPackageResource.getHeaderContribution(ScriptUtils.FADE_POPUP));
            
            //Login feedback
            FeedbackPanel feedback = new FeedbackPanel("feedback");
            feedback.setOutputMarkupId(true);
            add(feedback);

            //Logon link
        add(new AjaxFallbackLink<Object>("logon") {
            private static final long serialVersionUID = 1L;
            
            public void onClick(AjaxRequestTarget target) {
                if (target != null){
                    target.appendJavascript("$('loginPopup').show()");
                } else {
                    error(getString("label.javascript.error"));
                }
            }
            protected final void onBeforeRender(){
                if (((IContentAdmin)getSession()).isLoggedIn()){
                    setEnabled(false);
                } else {
                    setEnabled(true);
                }
                super.onBeforeRender();
            }
        });
        //Logout link
        add(new Link<Object>("logout") {
            private static final long serialVersionUID = 1L;
            CmsSession session = (CmsSession) getSession();
            public void onClick() {
                session.logout();
            }
            protected final void onBeforeRender(){
                if (session.isLoggedIn()){
                    setEnabled(true);
                } else {
                    setEnabled(false);
                }
                super.onBeforeRender();
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
