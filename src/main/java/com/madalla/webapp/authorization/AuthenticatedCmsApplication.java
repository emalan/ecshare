package com.madalla.webapp.authorization;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

import com.madalla.webapp.CmsSession;
import com.madalla.webapp.admin.member.IMemberAuthPage;

public abstract class AuthenticatedCmsApplication extends WebApplication
	implements
		IUnauthorizedComponentInstantiationListener,
		ICmsRoleAuthorizedCheck{

	private static Class<?> MEMBER_AUTH_PAGE = IMemberAuthPage.class;

	@Override
	protected void init()
	{
		super.init();

		// Set authorization strategy and unauthorized instantiation listener
		getSecuritySettings().setAuthorizationStrategy(new CmsAuthorizationStrategy(this));
		getSecuritySettings().setUnauthorizedComponentInstantiationListener(this);
	}

	public abstract boolean isMemberAuthorized();

	public final boolean hasAnyRole(final Roles roles)
	{
		final Roles sessionRoles = AuthenticatedWebSession.get().getRoles();
		return sessionRoles != null && sessionRoles.hasAnyRole(roles);
	}

	public final void onUnauthorizedInstantiation(final Component component){

		if (component instanceof Page) {

			// Member Security
			if (MEMBER_AUTH_PAGE.isAssignableFrom(component.getClass())) {

				if (!CmsSession.get().getMemberSession().isSignedIn()){
					throw new RestartResponseAtInterceptPageException(getMemberRegistrationPage());
				} else {
					onUnauthorizedPage((Page)component);
				}

			// If there is a sign in page class declared, and the unauthorized
			// component is a page, but it's not the sign in page
			} else {
				if (!AuthenticatedWebSession.get().isSignedIn()) {
					// Redirect to intercept page to let the user sign in
					throw new RestartResponseAtInterceptPageException(getSignInPageClass());
				} else {
					onUnauthorizedPage((Page)component);
				}
			}
		} else {
			// The component was not a page, so throw an exception
			throw new UnauthorizedInstantiationException(component.getClass());
		}
	}

	protected abstract Class<? extends AuthenticatedWebSession> getWebSessionClass();

	protected abstract Class<? extends WebPage> getSignInPageClass();

	protected abstract Class<? extends WebPage> getMemberRegistrationPage();

	protected void onUnauthorizedPage(final Page page)
	{
		// The component was not a page, so throw an exception
		throw new UnauthorizedInstantiationException(page.getClass());
	}

}
