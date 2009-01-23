package com.madalla.webapp.user;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import com.madalla.bo.security.UserData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;
import com.madalla.wicket.form.ValidationStyleRequiredTextField;

public class UserProfilePanel extends Panel{

	private static final long serialVersionUID = 9027719184960390850L;
	private static final Log log = LogFactory.getLog(UserProfilePanel.class);
	
	private UserData user;
	
    public class ProfileForm extends Form {
        private static final long serialVersionUID = -2684823497770522924L;
        
        //private final CaptchaImageResource captchaImageResource;
        
        public ProfileForm(String id) {
            super(id);
            
            FeedbackPanel emailFeedback = new FeedbackPanel("emailFeedback");
            add(emailFeedback);
            TextField email = new ValidationStyleRequiredTextField("email",new PropertyModel(user,"email"), emailFeedback);
            email.add(EmailAddressValidator.getInstance());
            add(email);
            
        }
    }

	public UserProfilePanel(String id, Class<? extends Page> returnPage){
		
		super(id);
		
		add(HeaderContributor.forJavaScript(Scriptaculous.PROTOTYPE));
		
		add(new PageLink("returnLink", returnPage));
		
		//Heading
		String username = ((CmsSession)getSession()).getUsername();
		user = getRepositoryService().getUser(username);
		add(new Label("heading",getString("heading.profile", new Model(user) )));
		
		//Form
		Form form = new ProfileForm("profileForm");
		form.setOutputMarkupId(true);
		add(form);
		
		final FeedbackPanel feedbackPanel = new ComponentFeedbackPanel("feedback",form);
		feedbackPanel.setOutputMarkupId(true);
		form.add(feedbackPanel);
		
        AjaxButton submitButton = new IndicatingAjaxButton("submit", form){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				log.debug("Ajax onsubmit called.");
				target.addComponent(feedbackPanel);
				form.info(getString("message.success"));
				//setResponsePage(this.findPage());
			}
			
			@Override
			protected void onError(final AjaxRequestTarget target, Form form) {
				log.debug("Ajax onerror called");
				target.addComponent(feedbackPanel);
	           	form.visitChildren(new Component.IVisitor() {
					public Object component(Component component) {
	           			log.debug("formVisitor="+component);
	           			if (component instanceof FormComponent) {
	           				FormComponent formComponent = (FormComponent) component;
	           				if (!formComponent.isValid()){
	           					target.addComponent(formComponent);
	           					log.debug("Component is invalid. Component MarkupId="+formComponent.getMarkupId()+". Message is " +formComponent.getFeedbackMessage().getMessage());
	           				}
	           			} else if (component instanceof ComponentFeedbackPanel){
	           				log.debug("Ajax onerror - adding feedback to target.");
	           				ComponentFeedbackPanel feedback = (ComponentFeedbackPanel) component;
	           				target.addComponent(feedback);
	           			}
	           			return null;
					}
	           	});

			}
        };
        form.add(submitButton);
		
		
	}
	
    private IRepositoryService getRepositoryService(){
    	return ((IRepositoryServiceProvider)getApplication()).getRepositoryService();
    }
}
