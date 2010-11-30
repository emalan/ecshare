package com.madalla.webapp.components.member;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import com.madalla.bo.member.MemberData;
import com.madalla.util.security.ICredentialHolder;
import com.madalla.util.security.SecureCredentials;
import com.madalla.webapp.CmsPanel;
import com.madalla.webapp.admin.member.MemberSession;
import com.madalla.webapp.login.LoginPanel;

public class MemberLoginPanel extends CmsPanel{
	private static final long serialVersionUID = 1L;
	
	public MemberLoginPanel(String id){
		this(id, new SecureCredentials(), null);
	}

	public MemberLoginPanel(String id, final ICredentialHolder credentials, final Class<? extends Page> destination) {
		super(id);
		
		final MemberSession session = getAppSession().getMemberSession();
		
		final Component loginInfo = new Label("loginInfo", new StringResourceModel("login.info",new Model<MemberData>(session.getMember()))){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onBeforeRender() {
				setOutputMarkupId(true);
				setVisibilityAllowed(true);
				if (!session.isSignedIn()){
					setVisible(false);
				}
				super.onBeforeRender();
			}

		};
		add(loginInfo);
		
		final Component panel = new LoginPanel("signInPanel", credentials, true, destination){
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean signIn(String username, String password) {
            	return session.signIn(username, password);
            }
            
			@Override
			protected void onBeforeRender() {
				setOutputMarkupId(true);
				if (session.isSignedIn()){
					setEnabled(false);
				} else {
					setEnabled(true);
				}
				super.onBeforeRender();
			}
            
            
        };
        add(panel);
        
		add(new AjaxFallbackLink<Object>("logout"){

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				session.signOut();
				target.addComponent(this);
				target.addComponent(loginInfo);
				target.addComponent(panel);
			}

			@Override
			protected void onBeforeRender() {
				setOutputMarkupId(true);
				setVisibilityAllowed(true);
				if (!session.isSignedIn()){
					setVisible(false);
				}
				super.onBeforeRender();
			}
			
		});
	}

}
