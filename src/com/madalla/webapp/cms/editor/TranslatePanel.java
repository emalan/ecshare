package com.madalla.webapp.cms.editor;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
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

	private Log log = LogFactory.getLog(this.getClass());

	
	public TranslatePanel(String name, final String nodeName, final String contentId) {
		super(name);
		
		add(JavascriptPackageResource.getHeaderContribution(TinyMce.class, "tiny_mce.js"));
				
		
		//setup Javascript template
		Map<String, Object> vars = EditorSetup.setupTemplateVariables((CmsSession) getSession());
		add(TextTemplateHeaderContributor.forJavaScript(EditorSetup.class,"EditorSetup.js", Model.ofMap(vars)));
		
        PageData page = getRepositoryService().getPage(nodeName);
        final ContentData content = getRepositoryService().getContent(page, contentId);
        log.debug("init - content" + content);
        
        //Base Language display
        String baseContent = getRepositoryService().getContentText(content, Locale.ENGLISH);
        Label baseContentLabel = new Label("baseContent", new Model<String>(baseContent));
        baseContentLabel.setEscapeModelStrings(false);
        add(baseContentLabel);
                
        //Dynamic destination Language editor
        final ContentEntryData contentEntry = getRepositoryService().getContentEntry(content, Locale.ENGLISH);
        final IModel<ContentEntryData> destModel = new Model<ContentEntryData>(contentEntry);
        final Panel destPanel = new ContentFormPanel("contentEditor", destModel );
        destPanel.setOutputMarkupId(true);
		add(destPanel);

		// Supported Languages
		List<SiteLanguage> locales = SiteLanguage.getLanguages();
		
		//Language Selector
		SiteLanguage selectedLanguage = null;
		final DropDownChoice<SiteLanguage> select = new DropDownChoice<SiteLanguage>("langSelect", 
				new Model<SiteLanguage>(selectedLanguage), locales);

		select.add(new AjaxFormComponentUpdatingBehavior("onchange"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				SiteLanguage language = select.getModelObject();
				log.debug("language select changed - " + language);
				ContentEntryData contentEntry = getRepositoryService().getContentEntry(content, language.locale);
				destModel.setObject(contentEntry);
				target.appendJavascript("tinyMCE.activeEditor.setContent('"+contentEntry.getText()+"')");
			}
			
		});
		add(select);

	}


}

