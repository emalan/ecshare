package com.madalla.webapp.cms.editor;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.resource.TextTemplateResourceReference;
import org.emalan.cms.bo.SiteLanguage;
import org.emalan.cms.bo.page.ContentData;
import org.emalan.cms.bo.page.ContentEntryData;
import org.emalan.cms.bo.page.PageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madalla.webapp.CmsPanel;

/**
 * Translate Panel - View translate from text and edit the translate text. Also translate functionality.
 *
 * <p>
 * Panel uses the TinyMCE WYSIWYG Editor. Translate functionality is supplied
 * using Googles translate service.
 * </p>
 *
 * @author Eugene Malan
 *
 */
public class TranslatePanel extends CmsPanel {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(TranslatePanel.class);

	final Map<String, Object> vars ;

	public TranslatePanel(String name, final String nodeName, final String contentId) {
		super(name);

        PageData page = getRepositoryService().getPage(nodeName);
        final ContentData content = getRepositoryService().getContent(page, contentId);
        log.debug("init - content" + content);

        //Base Language display
        String baseContent = getRepositoryService().getContentText(content, SiteLanguage.BASE_LOCALE);
        Label baseContentLabel = new Label("baseContent", new Model<String>(baseContent));
        baseContentLabel.setOutputMarkupId(true);
        baseContentLabel.setEscapeModelStrings(false);
        add(baseContentLabel);

		// Supported Languages
		List<SiteLanguage> locales = SiteLanguage.getLanguages();

        //Dynamic destination Language editor
		final Locale selectedLang = getDefaultLocale(locales);
		log.debug("start lang :" + selectedLang);
        final ContentEntryData contentEntry = getRepositoryService().getContentEntry(content, selectedLang.getDisplayName(), "");
        log.debug("start content entry. " + contentEntry);
        final ContentFormPanel destPanel = new ContentFormPanel("contentEditor", contentEntry );
        destPanel.setOutputMarkupId(true);
		add(destPanel);
		
		//translate values
		vars = new HashMap<String, Object>();
		vars.put("sourceDiv", baseContentLabel.getMarkupId());
		vars.put("destLang", selectedLang);

		//Language Selector
		SiteLanguage selectedLanguage = SiteLanguage.getLanguage(selectedLang.getLanguage());

		final DropDownChoice<SiteLanguage> select = new DropDownChoice<SiteLanguage>("langSelect",
				new Model<SiteLanguage>(selectedLanguage), locales, new ChoiceRenderer<SiteLanguage>("locale.displayLanguage"));
		select.setNullValid(false);
		select.add(new AjaxFormComponentUpdatingBehavior("onchange"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				SiteLanguage language = select.getModelObject();
				log.debug("language select changed - " + language);
				ContentEntryData newContentEntry = getRepositoryService().getContentEntry(content, language.locale.getDisplayName(), "");
				log.debug("new Content Entry." + newContentEntry);
				destPanel.changeContentEntry(newContentEntry);
				final String changeLang = "changeLanguage('"+ language.getLanguageCode()+"', '"+
						StringEscapeUtils.escapeJavaScript(newContentEntry.getText()) + "');";
				log.trace("script run --> " + changeLang);		
				target.appendJavaScript(changeLang);
			}

		});
		add(select);

	}
	
	@Override
	public void renderHead(IHeaderResponse response) {

		final ResourceReference translateJs = new TextTemplateResourceReference(TranslatePanel.class, "TranslatePanel.js", Model.ofMap(vars));
		response.render(JavaScriptHeaderItem.forReference(translateJs));
	}

	private Locale getDefaultLocale(List<SiteLanguage> locales){
		List<SiteLanguage> configuredLangs = getRepositoryService().getSiteData().getLocaleList();
		if (configuredLangs.isEmpty()){
			return locales.get(0).locale;
		} else {
			return configuredLangs.get(0).locale;
		}
	}


}

