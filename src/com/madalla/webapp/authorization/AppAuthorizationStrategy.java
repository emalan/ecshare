/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madalla.webapp.authorization;

import java.util.Collection;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authorization.IUnauthorizedComponentInstantiationListener;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.authorization.strategies.page.AbstractPageAuthorizationStrategy;

/**
 * Authorization strategy for Admin and logged in pages.
 * 
 * Copied and amended SimplePageAuthorizationStrategy
 * 
 * @author Eugene Malan
 *
 */
public class AppAuthorizationStrategy extends AbstractPageAuthorizationStrategy{

	Collection <PageAuthorization> pageAuthorizations;
	/**
	 * Construct.
	 * 
	 * @param securePageSuperType
	 *            The class or interface supertype that indicates that a given Page requires
	 *            authorization
	 * @param signInPageClass
	 *            The sign in page class
	 */
	public AppAuthorizationStrategy(final Class signInPageClass, Collection <PageAuthorization> pageAuthorizations)
	{
		
		this.pageAuthorizations = pageAuthorizations;

		// Handle unauthorized access to pages
		Application.get().getSecuritySettings().setUnauthorizedComponentInstantiationListener(
				new IUnauthorizedComponentInstantiationListener()
				{
					public void onUnauthorizedInstantiation(final Component component)
					{
						// If there is a sign in page class declared, and the
						// unauthorized component is a page, but it's not the
						// sign in page
						if (component instanceof Page)
						{
							// Redirect to page to let the user sign in
							throw new RestartResponseAtInterceptPageException(signInPageClass);
						}
						else
						{
							// The component was not a page, so throw exception
							throw new UnauthorizedInstantiationException(component.getClass());
						}
					}
				});
	}

	/**
	 * @see org.apache.wicket.authorization.strategies.page.AbstractPageAuthorizationStrategy#isPageAuthorized(java.lang.Class)
	 */
	protected boolean isPageAuthorized(final Class pageClass)
	{
		for (PageAuthorization pageAuthorization: pageAuthorizations){
			if (instanceOf(pageClass, (Class)pageAuthorization.getPageSuperTypeRef()))
			{
				if(!pageAuthorization.isAuthorized()){
					return false;
				}
			}
		}
		
		return true;

	}

}
