package com.madalla.webapp.admin.site;

import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.madalla.bo.SiteLanguage;
import com.madalla.bo.page.PageData;
import com.madalla.bo.page.PageMetaLangData;
import com.madalla.webapp.CmsPanel;
import com.madalla.wicket.form.AjaxValidationForm;
import com.madalla.wicket.form.AjaxValidationRequiredTextField;

public abstract class PageMetaPanel extends CmsPanel{
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(this.getClass());

    public class MetaDataForm extends AjaxValidationForm<PageMetaLangData> {
    	
    	private static final long serialVersionUID = 1L;
    	
    	private String existingMount;
        
    	public MetaDataForm(String id, IModel<PageMetaLangData> model) {
            super(id, model);
            
            this.existingMount = model.getObject().getMountName();
            
            add(new AjaxValidationRequiredTextField("displayName"));
            
            add(new TextField<String>("title"));
           
            add(new TextField<String>("author"));
            
            add(new TextArea<String>("description"));
            
            add(new TextArea<String>("keywords")); 
            
        }

		@Override
		protected void onSubmit(AjaxRequestTarget target) {
			PageMetaLangData data = getModelObject();
			
			preSaveProcessing(existingMount, data.getMountName());
			
			saveData(data);
			info(getString("message.success"));
		}
		
    }

	public PageMetaPanel(String id, final PageData pageData, List<SiteLanguage> siteLanguages, Locale defaultLocale) {
		super(id);

		add(new Label("pageName", pageData.getName()));
		
		// Base Lang
		PageMetaLangData pageMetaLang = getRepositoryService().getPageMetaLang(SiteLanguage.BASE_LOCALE, pageData);
		log.debug("Constructing base lang Form.  " + pageMetaLang );
		final Form<PageMetaLangData> homeBaseForm = new MetaDataForm("homeBaseForm", new CompoundPropertyModel<PageMetaLangData>(pageMetaLang));
	    add(homeBaseForm);
	    
		// Other Langs
	    log.debug("Constructing other langs. default=" + defaultLocale );
		final CompoundPropertyModel<PageMetaLangData> homeOtherModel = new CompoundPropertyModel<PageMetaLangData>(getRepositoryService().getPageMetaLang(defaultLocale, pageData, false));
		log.debug("Constructing other lang Form. Model: " + homeOtherModel );
	    final Form<PageMetaLangData> homeOtherForm = new MetaDataForm("homeOtherForm", homeOtherModel);
		add(homeOtherForm);
		    
		SiteLanguage selectedLanguage = SiteLanguage.getLanguage(defaultLocale.getLanguage());
		final DropDownChoice<SiteLanguage> select = new DropDownChoice<SiteLanguage>("langSelect",
				new Model<SiteLanguage>(selectedLanguage), siteLanguages, new ChoiceRenderer<SiteLanguage>(
						"locale.displayLanguage"));
		select.setNullValid(false);
		select.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				SiteLanguage language = select.getModelObject();
				log.debug("language select changed - " + language);
				PageMetaLangData newMetaLang = getRepositoryService().getPageMetaLang(language.locale, pageData, false);
				log.debug("new Meta Lang. " + newMetaLang);
				homeOtherModel.setObject(newMetaLang);
				target.addComponent(homeOtherForm);
			}

		});
		add(select);

	}
	
	abstract void preSaveProcessing(String existingMount, String newMount);
	
}
