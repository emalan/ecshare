package com.madalla.webapp.cms.editor;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.template.TextTemplateHeaderContributor;

import tiny_mce.TinyMce;

import com.madalla.bo.SiteLanguage;
import com.madalla.bo.page.ContentData;
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
				
		// Supported Languages
		List<SiteLanguage> locales = getRepositoryService().getSiteData().getLocaleList();
		Locale currentLocale = getSession().getLocale();
		
		//setup Javascript template
		Map<String, Object> vars = EditorSetup.setupTemplateVariables((CmsSession) getSession(), locales, currentLocale);
		add(TextTemplateHeaderContributor.forJavaScript(EditorSetup.class,"EditorSetup.js", Model.ofMap(vars)));
		
        PageData page = getRepositoryService().getPage(nodeName);
        final ContentData content = getRepositoryService().getContent(page, contentId);
        log.debug("init - content" + content);
        
        String baseContent = getRepositoryService().getContentText(content, Locale.ENGLISH);
        Label baseContentLabel = new Label("baseContent", new Model<String>(baseContent));
        baseContentLabel.setEscapeModelStrings(false);
        add(baseContentLabel);
                
		add(new ContentFormPanel("contentEditor", content, Locale.ENGLISH));

	}


}

