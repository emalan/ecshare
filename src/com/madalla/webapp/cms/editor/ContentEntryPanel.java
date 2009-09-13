package com.madalla.webapp.cms.editor;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.madalla.webapp.CmsSession;
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
			add(new TextArea<String>("text"));
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

		//tabs style sheet
		add(CSSPackageResource.getHeaderContribution(ContentEntryPanel.class, "tabs.css"));
		
		
		List<SiteLanguage> locales = getRepositoryService().getSiteData().getLocaleList();
		Locale currentLocale = getSession().getLocale();
		
		//setup Javascript template
		Map<String, Object> vars = setupTemplateVariables((CmsSession) getSession(), locales, currentLocale);
		add(TextTemplateHeaderContributor.forJavaScript(this.getClass(),"ContentEntryPanel.js", Model.ofMap(vars)));
		
		
		log.debug("init - surrentLocale="+currentLocale);
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
				return new ContentFormPanel(panelId, content, Locale.ENGLISH);
			}

		});

		// Supported Languages
		
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
		tabPanel.setSelectedTab(selectedTab);
		add(tabPanel);
	}
	
	private Map<String, Object> setupTemplateVariables(CmsSession session, List<SiteLanguage> locales, Locale currentLocale){
		Map<String, Object> map = new HashMap<String, Object>();
		if (session.isSuperAdmin()) {
			map.put("button1", "newdocument,fullscreen,cleanup,removeformat,code,help,|,undo,redo,|,paste,pastetext,pasteword,|,link,unlink,anchor,image,|,styleprops,|,cite,abbr,acronym,del,ins,attribs,");
			map.put("button2", "formatselect,fontselect,fontsizeselect|,forecolor,backcolor,|,tablecontrols");
			map.put("button3", "bold,italic,underline,strikethrough,|,undo,redo,|,cleanup,|,bullist,numlist,|,sub,sup,|,charmap,|,removeformat");
		} else if (session.isCmsAdminMode()) {
			map.put("button1", "bold,italic,underline,strikethrough,|,sub,sup,|,justifyleft,justifycenter,justifyright,justifyfull,|,styleselect,formatselect");
			map.put("button2", "bullist,numlist,|,hr,charmap,removeformat,|,outdent,indent,|,undo,redo,|,link,unlink,anchor,cleanup,code");
			map.put("button3", "bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,bullist,numlist,|,outdent,indent,blockquote,|,hr,advhr,charmap,emotions,|,insertdate,inserttime");
		} else {
			map.put("button1", "bold,italic,underline,strikethrough,|,undo,redo,|,cleanup,|,bullist,numlist,|,sub,sup,|,charmap,|,removeformat");
			map.put("button2", "");
			map.put("button3", "");
		}
		
		//Translate source langs
		StringBuffer sb = new StringBuffer();
		for (SiteLanguage lang : locales) {
			sb.append("if (google.language.isTranslatable('"+ lang.getLanguageCode() +"')) {dst.options.add(new Option('" +lang.getDisplayName() + "','" + lang.getLanguageCode() + "'))};");
		}
		map.put("dstlangs", sb.toString());
		
		//map.put("srclang", "src.options.add(new Option('" + currentLocale.getDisplayLanguage() + "','" + currentLocale.getLanguage()+ "'));");
		
		return map;
	}

}
