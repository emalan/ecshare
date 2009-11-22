package com.madalla.webapp.site;

import java.util.Locale;

import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.madalla.bo.page.PageData;
import com.madalla.bo.page.PageMetaLangData;
import com.madalla.webapp.CmsPage;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.panel.CmsPanel;
import com.madalla.wicket.form.AjaxValidationStyleRequiredTextField;
import com.madalla.wicket.form.AjaxValidationStyleSubmitButton;

public class PageAdminPanel extends CmsPanel {
	
	private static final long serialVersionUID = 1L;
    
    public class MetaDataForm extends Form<PageMetaLangData> {
    	
    	private static final long serialVersionUID = 1L;
        
    	public MetaDataForm(String id, IModel<PageMetaLangData> model) {
            super(id, model);
            
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
		
		//Home Page
		PageData pageData = getRepositoryService().getPage(getHomePageName());
		PageMetaLangData pageMetaLang = getRepositoryService().getPageMetaLang(Locale.ENGLISH, pageData);
		final Form<PageMetaLangData> homeBaseForm = new MetaDataForm("homeBaseForm", new CompoundPropertyModel<PageMetaLangData>(pageMetaLang));
	    
		final FeedbackPanel homeBaseFeedback = new ComponentFeedbackPanel("siteFeedback", homeBaseForm);
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
	                saveData((PageMetaLangData)form.getModelObject());
	                form.info(getString("message.success"));
	            }
	            
	        };
	        submitLink.setOutputMarkupId(true);
	        homeBaseForm.add(submitLink);
		
	        //Loop through other pages
		
	}
	
	private String getHomePageName(){
		Class<? extends Page> page = getApplication().getHomePage();
		if (!page.isAssignableFrom(CmsPage.class)){
			throw new WicketRuntimeException("The home page for this application does not extend the base CmsPage class");
		}
		return page.getSimpleName();
	}
}
