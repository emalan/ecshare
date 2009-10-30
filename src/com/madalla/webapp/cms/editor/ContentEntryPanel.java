package com.madalla.webapp.cms.editor;

import static com.madalla.webapp.PageParams.RETURN_PAGE;
import static com.madalla.webapp.cms.ContentParameters.CONTENT_NODE;
import static com.madalla.webapp.cms.ContentParameters.CONTENT_ID;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.template.TextTemplateHeaderContributor;

import tiny_mce.TinyMce;

import com.madalla.bo.SiteLanguage;
import com.madalla.bo.page.ContentData;
import com.madalla.bo.page.ContentEntryData;
import com.madalla.bo.page.PageData;
import com.madalla.cms.jcr.model.ContentNode;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.login.aware.LoggedinBookmarkablePageLink;
import com.madalla.webapp.pages.TranslatePage;
import com.madalla.webapp.pages.UserAdminPage;
import com.madalla.webapp.panel.CmsPanel;

/**
 * Content Entry Panel - Edit User Content using a WYSWYG HTML editor.
 * <p>
 * Panel uses the TinyMCE (Javascript WYSIWYG Editor). Note: The Wicket
 * application must implement the IContentServiceProvider interface.
 * </p>
 * 
 * @author Eugene Malan
 * 
 */
public class ContentEntryPanel extends CmsPanel {
	private static final long serialVersionUID = 1L;

	private Log log = LogFactory.getLog(this.getClass());

	final class ContentForm extends Form<ContentEntryData> {
		private static final long serialVersionUID = -3526743712542402160L;

		public ContentForm(final String name, IModel<ContentEntryData> model) {
			super(name, model);
			add(new TextArea<String>("text").setOutputMarkupId(true));
		}
	}

	
	/**
	 * @param name
	 *            - wicket id
	 * @param nodeName
	 * @param contentId
	 * @param returnPage
	 *            - used to create return Link
	 * 
	 */
	public ContentEntryPanel(String name, final String nodeName, final String contentId, final String returnPage) {
		super(name);
		add(JavascriptPackageResource.getHeaderContribution(TinyMce.class, "tiny_mce.js"));

		//tabs style sheet
		add(CSSPackageResource.getHeaderContribution(ContentEntryPanel.class, "tabs.css"));
		
		// Supported Languages
		List<SiteLanguage> locales = getRepositoryService().getSiteData().getLocaleList();
		Locale currentLocale = getSession().getLocale();
		
		//setup Javascript template
		Map<String, Object> vars = EditorSetup.setupTemplateVariables((CmsSession) getSession(), locales, currentLocale);
		add(TextTemplateHeaderContributor.forJavaScript(this.getClass(),"ContentEntryPanel.js", Model.ofMap(vars)));
		
        PageData page = getRepositoryService().getPage(nodeName);
        final ContentData content = getRepositoryService().getContent(page, contentId);
        log.debug("init - content" + content);
        
//        //Temporary link to test AJAX unload of editor
//        WebMarkupContainer testLink = new AjaxFallbackLink("testLink")
//		{
//
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void onClick(AjaxRequestTarget target)
//			{
//				if (target != null)
//				{
//					target.appendJavascript("removeEditors();");
//				}
//			}
//
//		};
//		add(testLink);
        
		//setup tabs
		int selectedTab = 0;
		List<ITab> tabs = new ArrayList<ITab>();

		// Default English tab
		tabs.add(new AbstractTab(new Model<String>(Locale.ENGLISH.getDisplayLanguage())) {
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId) {
				return new ContentFormPanel(panelId, content, Locale.ENGLISH);
			}

		});

		for (final SiteLanguage siteLanguage : locales) {
			if (siteLanguage.locale.equals(currentLocale)) {
				selectedTab = tabs.size();
			}
			tabs.add(new AbstractTab(new Model<String>(siteLanguage.getDisplayName())) {
				private static final long serialVersionUID = 1L;

				@Override
				public Panel getPanel(String panelId) {
					return new ContentFormPanel(panelId, content, siteLanguage.locale);
				}

			});

		}
		TabbedPanel tabPanel = new TabbedPanel("contentEditor", tabs);
		
//		TabbedPanel tabPanel = new AjaxTabbedPanel("contentEditor", tabs){
//
//			@Override
//			protected void onAjaxUpdate(AjaxRequestTarget target) {
//				target.appendJavascript("addEditors();");
//				
//			}
//			
//		};
		tabPanel.setSelectedTab(selectedTab);
		add(tabPanel);
		
		//User admin link
		PageParameters params = new PageParameters(RETURN_PAGE + "=" + returnPage + "," + CONTENT_ID + "=" + contentId + "," + CONTENT_NODE + "=" + nodeName);
		add(new LoggedinBookmarkablePageLink("translateLink", TranslatePage.class, params, false, false, false).setAutoEnable(true));
	}

}
