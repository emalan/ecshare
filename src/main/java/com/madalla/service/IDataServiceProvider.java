package com.madalla.service;

/**
 * This should be implemented by the class supplying the implementation
 * of the IDataService
 * <p>
 * @author Eugene Malan
 * @see IDataService
 */
public interface IDataServiceProvider {

	IDataService getRepositoryService();

}
