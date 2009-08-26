package com.madalla.webapp.cms.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import tiny_mce.TinyMce;

import com.madalla.bo.SiteLanguage;
import com.madalla.bo.page.ContentData;
import com.madalla.bo.page.ContentEntryData;
import com.madalla.bo.page.PageData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.webapp.CmsSession;
import com.madalla.wicket.form.AjaxValidationStyleSubmitButton;

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
public class ContentEntryPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private static final CompressedResourceReference JAVASCRIPT_ADM = new CompressedResourceReference(
			ContentEntryPanel.class, "ContentEntryPanel_admin.js");
	private static final CompressedResourceReference JAVASCRIPT = new CompressedResourceReference(
			ContentEntryPanel.class, "ContentEntryPanel.js");
	private static final CompressedResourceReference JAVASCRIPT_SPR = new CompressedResourceReference(
			ContentEntryPanel.class, "ContentEntryPanel_super.js");

	private Log log = LogFactory.getLog(this.getClass());

	final class ContentForm extends Form<ContentEntryData> {
		private static final long serialVersionUID = -3526743712542402160L;

		public ContentForm(final String name, IModel<ContentEntryData> model) {
			super(name, model);
			add(new TextArea<String>("text"));
		}
	}

	final class ContentFormFragment extends Fragment {

		private static final long serialVersionUID = 1L;

		private ContentEntryData contentEntry;

		public ContentFormFragment(String name, String markupId, MarkupContainer markupProvider, final String nodeName,
				final String contentId, final Locale locale) {
			super(name, markupId, markupProvider);

			PageData page = getRepositoryservice().getPage(nodeName);
			ContentData content = getRepositoryservice().getContent(page, contentId);
			contentEntry = getRepositoryservice().getContentEntry(content, locale);

			Form<ContentEntryData> form = new ContentForm("contentForm", new CompoundPropertyModel<ContentEntryData>(
					contentEntry));

			final FeedbackPanel feedback = new ComponentFeedbackPanel("feedback", form);
			feedback.setOutputMarkupId(true);
			form.add(feedback);

			AjaxButton submitLink = new AjaxValidationStyleSubmitButton("submitButton", form) {

				private static final long serialVersionUID = 1L;

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					super.onError(target, form);
				}

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					super.onSubmit(target, form);
					log.debug("Submiting populated Content object to Content service.");
					getRepositoryservice().saveContentEntry((ContentEntryData) form.getModelObject());
					log.debug("Content successfully saved to repository. content=" + contentEntry);
					form.info(getString("message.success"));
				}

				@Override
				protected String getOnClickScript() {
					return "tinyMCE.get('text').save();";
				}

			};
			submitLink.setOutputMarkupId(true);
			form.add(submitLink);
			add(form);
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
	public ContentEntryPanel(String name, final String nodeName, final String contentId) {
		super(name);
		add(JavascriptPackageResource.getHeaderContribution(TinyMce.class, "tiny_mce.js"));
		if (((CmsSession) getSession()).isSuperAdmin()) {
			add(JavascriptPackageResource.getHeaderContribution(JAVASCRIPT_SPR));
		} else if (((CmsSession) getSession()).isCmsAdminMode()) {
			add(JavascriptPackageResource.getHeaderContribution(JAVASCRIPT_ADM));
		} else {
			add(JavascriptPackageResource.getHeaderContribution(JAVASCRIPT));
		}

		Locale currentLocale = getSession().getLocale();
		add(CSSPackageResource.getHeaderContribution(ContentEntryPanel.class, "tabs.css"));
		int selectedTab = 0;
		List<ITab> tabs = new ArrayList<ITab>();

		// Default English tab
		tabs.add(new AbstractTab(new Model<String>(Locale.ENGLISH.getDisplayLanguage())) {
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId) {
				return new ContentFormPanel(panelId, nodeName, contentId, Locale.ENGLISH);
			}

		});

		// Supported Languages
		List<SiteLanguage> locales = getRepositoryservice().getSiteData().getLocaleList();
		for (final SiteLanguage siteLanguage : locales) {
			if (siteLanguage.locale.equals(currentLocale)) {
				selectedTab = tabs.size();
			}
			tabs.add(new AbstractTab(new Model<String>(siteLanguage.getDisplayName())) {
				private static final long serialVersionUID = 1L;

				@Override
				public Panel getPanel(String panelId) {
					return new ContentFormPanel(panelId, nodeName, contentId, siteLanguage.locale);
				}

			});

		}
		TabbedPanel tabPanel = new TabbedPanel("contentEditor", tabs);
		tabPanel.setSelectedTab(selectedTab);
		add(tabPanel);
	}

	private IRepositoryService getRepositoryservice() {
		return ((IRepositoryServiceProvider) getApplication()).getRepositoryService();
	}

}
