package com.madalla.webapp.site;

import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.madalla.bo.SiteLanguage;
import com.madalla.bo.page.PageData;
import com.madalla.bo.page.PageMetaLangData;
import com.madalla.webapp.CmsPage;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.panel.CmsPanel;
import com.madalla.wicket.form.AjaxValidationStyleRequiredTextField;
import com.madalla.wicket.form.AjaxValidationStyleSubmitButton;

public class PageAdminPanel extends CmsPanel {
	
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(this.getClass());
    
    public class MetaDataForm extends Form<PageMetaLangData> {
    	
    	private static final long serialVersionUID = 1L;
        
    	public MetaDataForm(String id, IModel<PageMetaLangData> model) {
            super(id, model);
            
            FeedbackPanel nameFeedback = new FeedbackPanel("nameFeedback");
            add(nameFeedback);
            add(new AjaxValidationStyleRequiredTextField("displayName",nameFeedback));
            
            FeedbackPanel titleFeedback = new FeedbackPanel("titleFeedback");
            add(titleFeedback);
            add(new AjaxValidationStyleRequiredTextField("title",titleFeedback));
           
            add(new TextField<String>("author"));
            
            add(new TextField<String>("description"));
            
            add(new TextField<String>("keywords")); 
            
        }

        
        
    }
	public PageAdminPanel(String id){
		super(id);
		add(Css.CSS_FORM);
		
		//********************
		//Home Page Base Lang
		//********************
		final PageData pageData = getRepositoryService().getPage(getHomePageName());
		PageMetaLangData pageMetaLang = getRepositoryService().getPageMetaLang(Locale.ENGLISH, pageData);
		final Form<PageMetaLangData> homeBaseForm = new MetaDataForm("homeBaseForm", new CompoundPropertyModel<PageMetaLangData>(pageMetaLang));
	    add(homeBaseForm);
	    
		final FeedbackPanel homeBaseFeedback = new ComponentFeedbackPanel("formFeedback", homeBaseForm);
		homeBaseFeedback.setOutputMarkupId(true);
		homeBaseForm.add(homeBaseFeedback);
	        
	    AjaxButton submitLink = new AjaxValidationStyleSubmitButton("submitLink", homeBaseForm) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				saveData((PageMetaLangData) form.getModelObject());
				form.info(getString("message.success"));
			}

		};
		submitLink.setOutputMarkupId(true);
		homeBaseForm.add(submitLink);
		
	    //*************************
		//Home Page - other Langs
		//*************************
        List<SiteLanguage> locales = SiteLanguage.getLanguages();
        final Locale defaultLocale = getDefaultLocale(locales);
        
		final CompoundPropertyModel<PageMetaLangData> homeOtherModel = new CompoundPropertyModel<PageMetaLangData>(getRepositoryService().getPageMetaLang(defaultLocale, pageData));
		
	    final Form<PageMetaLangData> homeOtherForm = new MetaDataForm("homeOtherForm", homeOtherModel);
		add(homeOtherForm);
		    
		final FeedbackPanel homeOtherFeedback = new ComponentFeedbackPanel("formFeedback", homeOtherForm);
		homeOtherFeedback.setOutputMarkupId(true);
		homeOtherForm.add(homeOtherFeedback);
		        
		AjaxButton submitOtherLink = new AjaxValidationStyleSubmitButton("submitLink", homeOtherForm) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				saveData((PageMetaLangData) form.getModelObject());
				form.info(getString("message.success"));
			}

		};
		submitLink.setOutputMarkupId(true);
		homeOtherForm.add(submitOtherLink);
	        

		SiteLanguage selectedLanguage = SiteLanguage.getLanguage(defaultLocale.getLanguage());
		final DropDownChoice<SiteLanguage> select = new DropDownChoice<SiteLanguage>("langSelect",
				new Model<SiteLanguage>(selectedLanguage), locales, new ChoiceRenderer<SiteLanguage>(
						"locale.displayLanguage"));
		select.setNullValid(false);
		select.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				SiteLanguage language = select.getModelObject();
				log.debug("language select changed - " + language);
				PageMetaLangData newMetaLang = getRepositoryService().getPageMetaLang(language.locale, pageData);
				log.debug("new Meta Lang. " + newMetaLang);
				homeOtherModel.setObject(newMetaLang);
				target.addComponent(homeOtherForm);
			}

		});
		add(select);
			
	        
	        //Loop through other pages
		
	}
	
	private String getHomePageName(){
		Class<? extends Page> page = getApplication().getHomePage();
		if (!CmsPage.class.isAssignableFrom(page)){
			throw new WicketRuntimeException("Home page does not extends CmsPage class. Home Page is " + page);
		}
		return page.getSimpleName();
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
