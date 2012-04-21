package com.madalla.webapp.authorization;

import org.apache.wicket.authroles.authorization.strategies.role.IRoleCheckingStrategy;

public interface ICmsRoleAuthorizedCheck extends IRoleCheckingStrategy {

	boolean isMemberAuthorized();

}
