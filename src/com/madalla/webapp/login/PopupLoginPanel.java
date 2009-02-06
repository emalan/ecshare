package com.madalla.webapp.login;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.transformer.AbstractTransformerBehavior;

import com.madalla.util.security.SecureCredentials;
import com.madalla.webapp.CmsSession;
import com.madalla.wicket.javascript.JavascriptBuilder;

public abstract class PopupLoginPanel extends Panel{

	private static final long serialVersionUID = 1L;
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	public PopupLoginPanel(final String id) {
		super(id);
		
        final MarkupContainer popup = new WebMarkupContainer("loginPopup");
        popup.setOutputMarkupId(true);
        popup.add(new AbstractTransformerBehavior(){

			private static final long serialVersionUID = 1L;

			@Override
			public CharSequence transform(Component component,
					CharSequence output) throws Exception {
				CmsSession session = (CmsSession) getSession();
				boolean loggedIn = session.isLoggedIn();
				return output + getJavascriptTemplate(component.getMarkupId(), loggedIn);
			}
			
			private String getJavascriptTemplate(String id, boolean loggedIn){
				JavascriptBuilder builder = new JavascriptBuilder();
				builder.addLine("var elem = document.getElementById('"+id+"');");
				if (loggedIn){ //hide
					builder.addLine("elem.style.display = 'none';");
				} else { //show
					builder.addLine("elem.style.display = '';");
				}
				return builder.buildScriptTagString();
			}
        	
        });
        add(popup);
		
        //Sign in Panel
        final Panel signinPanel = new LoginPanel("signInPanel", new SecureCredentials()){
            private static final long serialVersionUID = 1L;

            public boolean signIn(String username, String password) {
                CmsSession session = (CmsSession) getSession();
                if (session.login(username, password)) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            protected void onSignInSucceeded(AjaxRequestTarget target) {
            	super.onSignInSucceeded(target);
                log.debug("onSignInSucceeded");
                target.addComponent(popup);
                afterLogonSuccess(target);
                afterLogonSuccess();
            }

        };
        signinPanel.setOutputMarkupId(true);
        popup.add(signinPanel);
	}
	
	/**
	 * Overide this to do own after Login stuff
	 */
	public void afterLogonSuccess(){
		
	}
	
	/**
	 * Ajax after login stuff
	 * @param target
	 */
	public void afterLogonSuccess(AjaxRequestTarget target){
		
	}

}
