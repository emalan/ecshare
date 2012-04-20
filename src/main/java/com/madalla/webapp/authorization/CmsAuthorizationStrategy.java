package com.madalla.webapp.authorization;

import org.apache.wicket.authroles.authorization.strategies.role.RoleAuthorizationStrategy;

public class CmsAuthorizationStrategy extends RoleAuthorizationStrategy {

	public CmsAuthorizationStrategy(ICmsRoleAuthorizedCheck roleCheckingStrategy) {
		super(roleCheckingStrategy);
		add(new MemberPageAuthorizationStrategy(roleCheckingStrategy));
	}

}
