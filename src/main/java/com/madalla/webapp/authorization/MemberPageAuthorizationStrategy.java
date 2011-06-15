package com.madalla.webapp.authorization;

import org.apache.wicket.Page;
import org.apache.wicket.authorization.strategies.page.AbstractPageAuthorizationStrategy;

import com.madalla.webapp.admin.member.IMemberAuthPage;

public class MemberPageAuthorizationStrategy extends AbstractPageAuthorizationStrategy {

	private static Class<?> MEMBER_AUTH_PAGE = IMemberAuthPage.class;

	private ICmsRoleAuthorizedCheck authorizedCheck;

	public MemberPageAuthorizationStrategy(ICmsRoleAuthorizedCheck memberAuthorizedCheck) {
		this.authorizedCheck = memberAuthorizedCheck;
	}

	@Override
	protected <T extends Page> boolean isPageAuthorized(final Class<T> pageClass)
	{
		if (instanceOf(pageClass, MEMBER_AUTH_PAGE))
		{
			return authorizedCheck.isMemberAuthorized();
		}

		// Allow construction by default
		return true;
	}





}
