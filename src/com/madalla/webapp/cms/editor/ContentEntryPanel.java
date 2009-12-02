package com.madalla.webapp.cms.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.template.TextTemplateHeaderContributor;

import tiny_mce.TinyMce;

import com.madalla.bo.SiteLanguage;
import com.madalla.bo.page.ContentData;
import com.madalla.bo.page.ContentEntryData;
import com.madalla.bo.page.PageData;
import com.madalla.webapp.AdminPage;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.login.aware.LoginAwareAdminLink;
import com.madalla.webapp.pages.TranslatePage;
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
	
	/**
	 * @param id - wicket id
	 * @param nodeName - Node/Group/Page Name
	 * @param contentId - Actual Content Id
	 * 
	 */
	public ContentEntryPanel(String id, final String nodeName, final String contentId) {
		super(id);
		add(JavascriptPackageResource.getHeaderContribution(TinyMce.class, "tiny_mce.js"));

		//tabs style sheet
		add(CSSPackageResource.getHeaderContribution(ContentEntryPanel.class, "tabs.css"));
		
		// Supported Languages
		List<SiteLanguage> locales = getRepositoryService().getSiteData().getLocaleList();
		Locale currentLocale = getSession().getLocale();
		
		//setup Javascript template
		Map<String, Object> vars = EditorSetup.setupTemplateVariables((CmsSession) getSession());
		add(TextTemplateHeaderContributor.forJavaScript(EditorSetup.class,"EditorSetup.js", Model.ofMap(vars)));
		
        PageData page = getRepositoryService().getPage(nodeName);
        final ContentData content = getRepositoryService().getContent(page, contentId);
        log.debug("init - content" + content);
        
		//setup tabs
		int selectedTab = 0;
		List<ITab> tabs = new ArrayList<ITab>();

		// Default English tab
		tabs.add(new AbstractTab(new Model<String>(Locale.ENGLISH.getDisplayLanguage())) {
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId) {
				ContentEntryData contentEntry = getRepositoryService().getContentEntry(content, Locale.ENGLISH);
				log.debug(contentEntry);
				return new ContentFormPanel(panelId, contentEntry);
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
					ContentEntryData contentEntry = getRepositoryService().getContentEntry(content, siteLanguage.locale);
					return new ContentFormPanel(panelId, contentEntry);
				}

			});

		}
		TabbedPanel tabPanel = new TabbedPanel("contentEditor", tabs);

		tabPanel.setSelectedTab(selectedTab);
		add(tabPanel);
		
		//User admin link
		// add link to edit it
        add(new LoginAwareAdminLink("translateLink", TranslatePage.class, false){
			private static final long serialVersionUID = 1L;

			@Override
			protected AdminPage constructAdminPage(Class<? extends AdminPage> clazz) {
				return new TranslatePage(contentId, nodeName);
			}

        });
	}

}
