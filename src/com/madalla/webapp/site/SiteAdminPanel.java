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
import org.apache.wicket.model.PropertyModel;
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
    
    private SiteData data;
    
    public class SiteForm extends Form {

        public SiteForm(String id) {
            super(id);
            
            FeedbackPanel emailFeedback = new FeedbackPanel("emailFeedback");
            add(emailFeedback);
            TextField email = new AjaxValidationStyleRequiredTextField("Email",
                    new PropertyModel(data, "adminEmail"), emailFeedback);
            email.add(EmailAddressValidator.getInstance());
            add(email);

            add(new TextField("description", new PropertyModel(data, "metaDescription")));
            add(new TextField("keywords", new PropertyModel(data, "metaKeywords")));
        }

        private static final long serialVersionUID = 1L;
        
    }
    
    public SiteAdminPanel(String id, Class<? extends Page> returnPage) {
        super(id);
        add(Css.CSS_FORM);
        
        add(new PageLink("returnLink", returnPage));
        
        data = getSiteData();
        
        final Form form = new SiteForm("siteForm");
        add(form);
        
        final FeedbackPanel siteFeedback = new ComponentFeedbackPanel("siteFeedback", form);
        siteFeedback.setOutputMarkupId(true);
        form.add(siteFeedback);
        
        AjaxSubmitLink submitLink = new AjaxValidationStyleSubmitLink("submitLink", form) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onError(AjaxRequestTarget target, Form form) {
                super.onError(target, form);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                super.onSubmit(target, form);
                saveSiteData();
                form.info(getString("message.success"));
            }
            
            
            
        };
        submitLink.setOutputMarkupId(true);
        form.add(submitLink);
        
    }
    
    private SiteData getSiteData(){
        return getRepositoryService().getSiteData();
    }
    
    private void saveSiteData(){
        log.debug("saveSiteData - + " + data);
        getRepositoryService().saveSite(data);
    }
    
    private IRepositoryService getRepositoryService(){
        return ((IRepositoryServiceProvider) getApplication()).getRepositoryService();
    }
    
}
