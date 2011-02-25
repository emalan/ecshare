package com.madalla.webapp.admin.site;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.UrlValidator;
import org.joda.time.DateTimeZone;

import com.madalla.bo.SiteData;
import com.madalla.bo.SiteLanguage;
import com.madalla.webapp.CmsPanel;
import com.madalla.webapp.css.Css;
import com.madalla.wicket.form.AjaxValidationBehaviour;
import com.madalla.wicket.form.AjaxValidationForm;
import com.madalla.wicket.form.AjaxValidationRequiredTextField;
import com.madalla.wicket.form.ValidationStyleBehaviour;

@AuthorizeInstantiation("USER")
public class SiteAdminPanel extends CmsPanel{

    private static final long serialVersionUID = 1L;
    
    public class SiteForm extends AjaxValidationForm<SiteData> {

        private static final long serialVersionUID = 1L;

		public SiteForm(String id, IModel<SiteData> model) {
            super(id, model);
            
            FeedbackPanel emailFeedback = new FeedbackPanel("emailFeedback");
            add(emailFeedback);
            TextField<String> email = new AjaxValidationRequiredTextField("adminEmail",emailFeedback);
            email.add(EmailAddressValidator.getInstance());
            add(email);
            
            add(new TextField<String>("siteName"));
            
            FeedbackPanel urlFeedback = new FeedbackPanel("urlFeedback");
            add(urlFeedback);
            TextField<String> url = new TextField<String>("url");
            url.add(new AjaxValidationBehaviour(urlFeedback));
            url.add(new ValidationStyleBehaviour());
            url.add(new UrlValidator(UrlValidator.NO_FRAGMENTS));
            add(url);
            
            add(new TextField<String>("metaDescription"));
            
            add(new CheckBox("securityCertificate"));
            
            add(new CheckBoxMultipleChoice<SiteLanguage>("localeList", Model.ofList(SiteData.getAvailableLocales()), 
            		new ChoiceRenderer<SiteLanguage>("displayName")));
            
           	add(new DropDownChoice<String>("timeZone",getAvailableTimeZones()));
            
        }

		@Override
		protected void onSubmit(AjaxRequestTarget target) {
			saveData(getModelObject());
            info(getString("message.success"));
		}

    }
    
    public SiteAdminPanel(String id) {
        super(id);
        add(Css.CSS_FORM);
        
        final Form<SiteData> form = new SiteForm("siteForm", new CompoundPropertyModel<SiteData>(getSiteData()));
        add(form);
         
    }
    
    private SiteData getSiteData(){
    	return getRepositoryService().getSiteData();
    }
    
    @SuppressWarnings("unchecked")
	private List<String> getAvailableTimeZones(){
    	return new ArrayList<String>(DateTimeZone.getAvailableIDs());
    }

    
}
