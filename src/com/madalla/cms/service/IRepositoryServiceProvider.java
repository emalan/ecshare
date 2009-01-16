package com.madalla.cms.service;

/**
 * This should be implemented by the class supplying the implentation
 * of the IRepositoryService
 * <p>
 * @author Eugene Malan
 * @see IRepositoryService
 */
public interface IRepositoryServiceProvider {

	IRepositoryService getRepositoryService();
	
}
