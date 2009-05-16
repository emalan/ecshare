package com.madalla.webapp.site;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import com.madalla.bo.SiteData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.webapp.css.Css;
import com.madalla.wicket.form.AjaxValidationStyleRequiredTextField;
import com.madalla.wicket.form.AjaxValidationStyleSubmitLink;

public class SiteAdminPanel extends Panel{

    private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(SiteAdminPanel.class);
    
    public class SiteForm extends Form<SiteData> {

        public SiteForm(String id, IModel<SiteData> model) {
            super(id, model);
            
            FeedbackPanel emailFeedback = new FeedbackPanel("emailFeedback");
            add(emailFeedback);
            TextField<String> email = new AjaxValidationStyleRequiredTextField("adminEmail",emailFeedback);
            email.add(EmailAddressValidator.getInstance());
            add(email);

            add(new TextField<String>("metaDescription"));
           
            add(new TextField<String>("metaKeywords"));
        }

        private static final long serialVersionUID = 1L;
        
    }
    
    public SiteAdminPanel(String id, Class<? extends Page> returnPage) {
        super(id);
        add(Css.CSS_FORM);
        
        add(new PageLink<Page>("returnLink", returnPage));
        
        final Form<SiteData> form = new SiteForm("siteForm", new CompoundPropertyModel<SiteData>(getSiteData()));
        add(form);
        
        final FeedbackPanel siteFeedback = new ComponentFeedbackPanel("siteFeedback", form);
        siteFeedback.setOutputMarkupId(true);
        form.add(siteFeedback);
        
        AjaxSubmitLink submitLink = new AjaxValidationStyleSubmitLink("submitLink", form) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                saveSiteData((SiteData)form.getModelObject());
                form.info(getString("message.success"));
            }
            
            
            
        };
        submitLink.setOutputMarkupId(true);
        form.add(submitLink);
        
    }
    
    private SiteData getSiteData(){
        return getRepositoryService().getSiteData();
    }
    
    private void saveSiteData(SiteData data){
        log.debug("saveSiteData - + " + data);
        getRepositoryService().saveSite(data);
    }
    
    private IRepositoryService getRepositoryService(){
        return ((IRepositoryServiceProvider) getApplication()).getRepositoryService();
    }
    
}