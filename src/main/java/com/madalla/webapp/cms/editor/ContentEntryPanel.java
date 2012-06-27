package com.madalla.webapp.cms.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.emalan.cms.bo.SiteLanguage;
import org.emalan.cms.bo.page.ContentData;
import org.emalan.cms.bo.page.ContentEntryData;
import org.emalan.cms.bo.page.PageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madalla.webapp.CmsPanel;
import com.madalla.webapp.admin.pages.AdminPanelLink;

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

	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * @param id - wicket id
	 * @param nodeName - Node/Group/Page Name
	 * @param contentId - Actual Content Id
	 *
	 */
	public ContentEntryPanel(String id, final String nodeName, final String contentId) {
		super(id);

		// Supported Languages
		List<SiteLanguage> locales = getRepositoryService().getSiteData().getLocaleList();
		Locale currentLocale = getSession().getLocale();

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
				log.debug(contentEntry.toString());
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

		// Link to translate panel
        add(new AdminPanelLink("translateLink", TranslatePanel.class){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				getPage().replace(new TranslatePanel(ID, nodeName, contentId));
			}

        });
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		
		//tabs style sheet
		response.render(CssHeaderItem.forReference(new PackageResourceReference(ContentEntryPanel.class, "tabs.css")));

	}

}
