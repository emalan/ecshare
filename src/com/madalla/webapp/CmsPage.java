package com.madalla.webapp;

import static com.madalla.webapp.PageParams.RETURN_PAGE;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Application;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.madalla.bo.SiteData;
import com.madalla.bo.SiteLanguage;
import com.madalla.bo.page.PageData;
import com.madalla.bo.page.PageMetaLangData;
import com.madalla.cms.bo.impl.ocm.Site;
import com.madalla.service.IDataService;
import com.madalla.service.IDataServiceProvider;
import com.madalla.util.security.SecureCredentials;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.login.LoginPanel;
import com.madalla.webapp.pages.SecureLoginPage;
import com.madalla.webapp.pages.UserLoginPage;
import com.madalla.webapp.security.IAuthenticator;

/**
 * Base class for Application Pages that supplies Content and other functionality.
 * <p>
 * The Content Admin will supply and allow editing of meta information for this page. So make
 * sure you do not duplicate the following (title, author, keywords, description).
 * </p>
 * <p>
 * Layout functionality is supplied using YUI, so make sure to leverage that for your page layout and
 * in your style sheets. Popup login functionality and a Language switcher can be activated by overiding
 * the relevant methods.
 * </p>
 * @author Eugene Malan
 *
 */
public abstract class CmsPage extends WebPage {

	private static final String PAGE_TITLE = "page.title";
	private static final String META_NAME = "<meta name=\"{0}\" content=\"{1}\"/>";
	private static final String META_HTTP = "<meta http-equiv=\"{0}\" content=\"{1}\"/>";

	private String pageTitle = "(no title)";
	
	public abstract class LoginLink extends AjaxFallbackLink<Object> {
		
		private static final long serialVersionUID = 1L;

		final CmsSession session ;
		public LoginLink(String name, CmsSession session){
			super(name);
			this.session = session;
		}
		
		public void onClick(AjaxRequestTarget target) {
			if (session.isLoggedIn()) {
				session.logout();
				setResponsePage(getPage());
			} else {
				if (target != null) {
					onClickAction(target);
				} else {
					error(getString("label.javascript.error"));
				}
			}
		}

		protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
			if (session.isLoggedIn()) {
				replaceComponentTagBody(markupStream, openTag, getString("label.logout"));
			} else {
				replaceComponentTagBody(markupStream, openTag, getString("label.login"));
			}
		}
		
		protected abstract void onClickAction(AjaxRequestTarget target);
			
		
		
	}

	public CmsPage() {
		super();
		setPageTitle(getString(PAGE_TITLE));
		add(new Label("title", new PropertyModel<String>(this, "pageTitle")));

		add(Css.YUI_CORE);
		add(Css.BASE);
		
		if (isHomePage()) {
			setLocaleFromUrl(getRequest().getURL());
		}
		PageData page = getRepositoryService().getPage(getPageName());
		PageMetaLangData pageInfo = getRepositoryService().getPageMetaLang(getLocale(), page);
		processPageMetaInformation(pageInfo);

		if (hasPopupLogin()) {
			setupPopupLogin();
		} else {
			// dummy label that will end up hidden on page
			add(new Label("signInPanel"));
		}

		if (hasLoginLink()) {
			setupLoginLink();
		}

		if (hasLangDropDown()) {
			setupLangDropDown();
		}

	}
	
	private void processPageMetaInformation(PageMetaLangData pageInfo){
		//TODO remove the following block
		if (isHomePage()) {
			SiteData siteData = getRepositoryService().getSiteData();
			if (StringUtils.isNotEmpty(siteData.getMetaDescription())){
				if (StringUtils.isEmpty(pageInfo.getDescription())){
					pageInfo.setDescription(siteData.getMetaDescription());
					getRepositoryService().saveDataObject(pageInfo);
				}
				((Site) siteData).setMetaDescription("");
			}
			if(StringUtils.isNotEmpty(siteData.getMetaKeywords())){
				if (StringUtils.isEmpty(pageInfo.getKeywords())){
					pageInfo.setKeywords(siteData.getMetaKeywords());
					getRepositoryService().saveDataObject(pageInfo);
				}
				((Site) siteData).setMetaKeywords("");
			}
		}
		//remove to here
		
		add(new StringHeaderContributor(MessageFormat.format(META_HTTP, "lang", pageInfo.getLang())));
		
		if (!isMetadataOveridden()) {
			if (StringUtils.isNotEmpty(pageInfo.getTitle())){
				add(new StringHeaderContributor("<title>" + pageInfo.getTitle() + "</title>"));
			}
			if (StringUtils.isNotEmpty(pageInfo.getAuthor())){
				add(new StringHeaderContributor(MessageFormat.format(META_NAME, "author", pageInfo.getAuthor())));
			}
			if (StringUtils.isNotEmpty(pageInfo.getDescription())) {
				add(new StringHeaderContributor(MessageFormat.format(META_NAME, "description", pageInfo.getDescription())));
			}
			if (StringUtils.isNotEmpty(pageInfo.getKeywords())) {
				add(new StringHeaderContributor(MessageFormat.format(META_NAME, "keywords", pageInfo.getKeywords())));
			}
		}
	}
	
	private void setupPopupLogin(){
		CmsSession session = (CmsSession) getSession();
		add(new LoginLink("logon", session){

			private static final long serialVersionUID = 1L;

			@Override
			protected void onClickAction(AjaxRequestTarget target) {
				target.appendJavascript("var elm = wicketGet('loginPopup'); wicketShow(elm); elm.focus();");
			}
			
		});

		add(new LoginPanel("signInPanel", new SecureCredentials(), false) {
			private static final long serialVersionUID = 1L;
			private static final int loginMax = 4;
			private int count = 0;

			@Override
			protected void preSignIn(String username) {
				if (getApplication().getConfigurationType().equals(Application.DEVELOPMENT)) {
					return;
				}
				IAuthenticator authenticator = getRepositoryService().getUserAuthenticator();
				if (authenticator.requiresSecureAuthentication(username)) {
					redirectToInterceptPage(new SecureLoginPage(new PageParameters(RETURN_PAGE + "="
							+ getPage().getClass().getName()), username));
				}
			}

			@Override
			public boolean signIn(String username, String password) {
				CmsSession session = (CmsSession) getSession();
				if (session.login(username, password)) {
					return true;
				} else {
					return false;
				}
			}

			@Override
			protected void onSignInSucceeded() {
				// If login has been called because the user was not yet
				// logged in, then continue to the original destination,
				// otherwise to the Home page
				if (!continueToOriginalDestination()) {
					setResponsePage(getPage());
				}
			}

			@Override
			protected void onSignInFailed(String username) {
				super.onSignInFailed(username);
				count++;
				if (count >= loginMax) {
					redirectToInterceptPage(new UserLoginPage(new PageParameters(RETURN_PAGE + "="
							+ getPage().getClass().getName()), username));
				}
			}

		});

	}
	
	private void setupLoginLink(){
		CmsSession session = (CmsSession) getSession();
		add(new LoginLink("logon", session){

			private static final long serialVersionUID = 1L;

			@Override
			protected void onClickAction(AjaxRequestTarget target) {
				setResponsePage(new UserLoginPage(new PageParameters(RETURN_PAGE + "="
						+ getPage().getClass().getName())));
			}
			
		});
	}
	
	private void setupLangDropDown(){
		List<SiteLanguage> langs = getRepositoryService().getSiteData().getLocaleList();
		langs.add(SiteLanguage.ENGLISH);

		final IModel<SiteLanguage> langModel = new Model<SiteLanguage>();
		langModel.setObject(SiteLanguage.getLanguage(getSession().getLocale().getLanguage()));
		DropDownChoice<SiteLanguage> choice = new DropDownChoice<SiteLanguage>("langSelect", langModel, langs,
				new ChoiceRenderer<SiteLanguage>("locale.displayLanguage"));

		choice.setNullValid(false);
		choice.add(new OnChangeAjaxBehavior() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				getSession().setLocale(langModel.getObject().locale);
				setResponsePage(getPage());
			}

		});
		add(choice);
	}
	
	private void setLocaleFromUrl(String url){
		String[] urlfrags = url.split("/");
		String s = urlfrags[urlfrags.length - 1];
		if (s.length() == 2){
			getSession().setLocale(SiteLanguage.getLanguage(s).locale);
		}
	}

	/**
	 * @return true if you want to set your own metadata, otherwise the metadata
	 *         will be set from the CMS values
	 */
	protected boolean isMetadataOveridden() {
		return false;
	}

	/**
	 * @return true if you want to use the built in popup login
	 */
	protected boolean hasPopupLogin() {
		return false;
	}

	/**
	 * @return true if you want to use the built in popup login
	 */
	protected boolean hasLoginLink() {
		return false;
	}

	/**
	 * 
	 * @return true if you want a lang dropdown for dynamic languae switching
	 */
	protected boolean hasLangDropDown() {
		return false;
	}

	protected void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	protected String getPageTitle() {
		return pageTitle;
	}

	protected IDataService getRepositoryService() {
		return ((IDataServiceProvider) getApplication()).getRepositoryService();
	}

	protected boolean isHomePage() {
		return getApplication().getHomePage().equals(this.getPageClass());
	}
	
	abstract protected String getPageName();
}
