package com.madalla.webapp.authorization;

import java.lang.ref.WeakReference;

public abstract class PageAuthorization {

	/**
	 * The supertype (class or interface) of Pages that require authorization to be instantiated.
	 */
	private final WeakReference/* <Class> */securePageSuperTypeRef;
	
	public PageAuthorization(final Class securePageSuperType){
		if (securePageSuperType == null)
		{
			throw new IllegalArgumentException("Secure page super type must not be null");
		}

		this.securePageSuperTypeRef = new WeakReference(securePageSuperType);

	}
	
	public Object getPageSuperTypeRef(){
		return securePageSuperTypeRef.get();
	}
	
	/**
	 * Gets whether the current user/session is authorized to instantiate a page class which extends
	 * or implements the supertype (base class or tagging interface) passed to the constructor.
	 * 
	 * @return True if the instantiation should be allowed to proceed. False, if the user should be
	 *         directed to the application's sign-in page.
	 */
	protected abstract boolean isAuthorized();
}
